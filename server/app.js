var config = require("./lib/config.js"),
	express = require("express"),
	app = express(),
	mysql = require("mysql"),
    async = require("async");

var auth = require("./lib/routes/auth.js");

var sql;

// Routes middlewares
var products = require("./lib/routes/products.js");
var users = require("./lib/routes/users.js");
var categories = require("./lib/routes/categories.js");
var creditcards = require("./lib/routes/creditcards.js");
var addresses = require("./lib/routes/addresses.js");
var cart = require("./lib/routes/cart.js")
var orders = require("./lib/routes/orders.js");
var charts = require("./lib/routes/charts.js");

app.configure(function() {
	app.set("name", config.appName);
});

/* Request Logger */
app.use(function (req, res, next) {
    console.log("Request: %s %s", req.method, req.url);
    next();
});

app.use(function (req, res, next) {
    req.db = sql;
    next();
});

// Static content
app.use(express.static(__dirname + "/public"));

//Auth Middleware
app.use(auth)

// Routes use
app.use(products);
app.use(users);
app.use(categories);
app.use(creditcards);
app.use(addresses);
app.use(cart);
app.use(orders);
app.use(charts);

//Error handler middleware
app.use(function (err, req, res, next) {
    console.log(err);
    res.send(500, {"error": err});
});

//DB Connection & port app listening
sql = dbClient = mysql.createConnection(config.dbAddress);
dbClient.connect(function (err) {
    if(err) {
        return console.error("Could not connect to mysql", err);
    }

    console.log("Database connection successful");

    app.listen(config.appPort);
    console.log("App started, listening at port %s", config.appPort);

    processEndedBids();
});

function setTimeoutProcessEndedBids() {
    /* We need to start this process in each new minute */
    var upTime = new Date();
    var nextMinuteInMillis = (61 - upTime.getSeconds()) * 1000; /* 61 because we want to start this one second after the new minute has started */
    setTimeout(processEndedBids, nextMinuteInMillis);

    console.log("Next processEndedBids in milliseconds: " + nextMinuteInMillis);
}

function processEndedBids() {
    setTimeoutProcessEndedBids();

    var temp = new Date();
    console.log("[" + temp + "] Processing Ended Bids!");

    var query = "SELECT * FROM product P INNER JOIN (SELECT product_id, MAX(bid_amount) as bid_amount FROM bid GROUP BY product_id) B ON B.product_id = P.product_id INNER JOIN (SELECT bid_id, user_id as buyer_user_id, product_id, bid_amount FROM bid) B2 ON B2.product_id = P.product_id AND B2.bid_amount = B.bid_amount WHERE auction_end_ts < NOW() AND P.product_id IN (SELECT product_id FROM bid) AND P.product_id NOT IN (SELECT product_id FROM order_detail)";
    console.log("MySQL Query: " + query);

    dbClient.query(query, function (err, results) {
        if (err) { 
            throw err;
        }

        var i = 0;
        var product = results[i];

        for (i = 0; i < results.length; i++) {
            console.log("Found Sold Product by Auction: " + results[i].product_id);

            // Get default credit card and address (if exists)
            async.series({
                "cc": function (done) {
                    var query = "SELECT * FROM credit_card WHERE user_id = " + product.buyer_user_id + " AND is_primary = 1 AND expiration_date > NOW();";
                    console.log("MySQL Query: " + query);
                    dbClient.query(query, done);
                },
                "addr": function (done) {
                    var query = "SELECT * FROM address WHERE user_id = " + product.buyer_user_id + " AND is_primary = 1;";
                    console.log("MySQL Query: " + query);
                    dbClient.query(query, done);
                },
            }, function (err, resultsTemp) {
                if (err)
                    throw err;

                var cc = (resultsTemp.cc[0].length > 0) ? resultsTemp.cc[0][0] : null;
                var addr = (resultsTemp.addr[0].length > 0) ? resultsTemp.addr[0][0] : null;

                if (cc == null || addr == null) {
                    console.log("User " + product.buyer_user_id + " doesnt have a default Credit Card or Address. Order cannot be processed.");
                } else {
                    //console.log(cc.creditcard_id + " - " + cc.address_id);

                    dbClient.beginTransaction(function(err) {
                        if (err) 
                            throw err;

                        var query = "INSERT INTO `order` SET user_id = " + product.buyer_user_id + ", credit_card_id = " + cc.creditcard_id + ", address_id = " + addr.address_id;
                        console.log("MySQL Query: " + query);

                        dbClient.query(query, function(err, result) {
                            if (err) {
                                dbClient.rollback(function() {
                                    throw err;
                                });
                                throw err;
                            }

                            var orderId = result.insertId;

                            console.log("Inserted Order ID: " + orderId);

                            var query = "INSERT INTO order_detail SET order_id = " + orderId + ", product_id = " + product.product_id + ", quantity = 1, final_price = " + product.bid_amount;
                            console.log("MySQL Query: " + query);

                            dbClient.query(query, function(err, result) {
                                if (err) {
                                    dbClient.rollback(function() {
                                        throw err;
                                    });
                                    throw err;
                                }

                                var orderDetailId = result.insertId;

                                console.log("Inserted Order Detail ID: " + orderDetailId);

                                var query = "INSERT INTO order_detail_winning_bid SET order_detail_id = " + orderDetailId + ", bid_id = " + product.bid_id;
                                console.log("MySQL Query: " + query);

                                dbClient.query(query, function(err, result) {
                                    if (err) {
                                        dbClient.rollback(function() {
                                            throw err;
                                        });
                                        throw err;
                                    }

                                    var orderDetailWinningBidId = result.insertId;

                                    console.log("Created Order Detail Winning Bid");

                                    var query = "INSERT INTO seller_transaction SET order_detail_id = " + orderDetailId + ", user_id = " + product.user_id + ", earning = " + product.bid_amount;
                                    console.log("MySQL Query: " + query);

                                    dbClient.query(query, function(err, result) {
                                        if (err) {
                                            dbClient.rollback(function() {
                                                throw err;
                                            });
                                            throw err;
                                        }

                                        var sellerTransactionId = result.insertId;

                                        console.log("Inserted Seller Transaction ID: " + sellerTransactionId);
                                        
                                        dbClient.commit(function(err) {
                                            if (err) { 
                                                dbClient.rollback(function() {
                                                    throw err;
                                                });
                                                throw err;
                                            }

                                            console.log("New Order created without problems!");
                                        });
                                    });
                                });
                            });
                        });
                    });
                }
            });
        }
    });

    /*dbClient.beginTransaction(function(err) {
        if (err) 
            throw err;

        var query = "SELECT * FROM product P INNER JOIN (SELECT product_id, user_id as buyer_seller_id, MAX(bid_amount) as bid_amount FROM bid GROUP BY product_id) B ON B.product_id = P.product_id WHERE auction_end_ts < NOW() AND P.product_id IN (SELECT product_id FROM bid) AND P.product_id NOT IN (SELECT product_id FROM order_detail)";

        dbClient.query(query, function (err, results) {
            if (err) { 
                connection.rollback(function() {
                    throw err;
                });
            }
        });
    );*/
}