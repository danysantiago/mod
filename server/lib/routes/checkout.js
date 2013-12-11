var config = require("../config.js"), express = require("express"), async = require("async");

var routes = express();

routes.post("/checkout/now", express.bodyParser(), function (req, res, next) {
	var newOrder = req.body;

	console.log(newOrder);

    var query = "SELECT *, (P.quantity - IFNULL((SELECT SUM(quantity) FROM order_detail OD WHERE OD.product_id = P.product_id GROUP BY OD.product_id),0)) AS stock FROM product P WHERE P.product_id = " + req.db.escape(newOrder.product_id);
    console.log("MySQL Query: " + query);

    req.db.query(query, function (err, result) {
        if (err) { 
            throw err;
        }

        if (result.length == 0) {
	        res.send(404, {"status": "INVALID_PRODUCT"});
	        return;
        }

        var product = result[0];

        if (product.stock <= 0) {
	        res.send(200, {"status": "OUT_OF_STOCK"});
        	return;
        }

        if (newOrder.creditcard_id == undefined) {
	        res.send(404, {"status": "INVALID_CC"});
    		return;
        } else if (newOrder.address_id == undefined) {
	        res.send(404, {"status": "INVALID_ADDR"});
    		return;
    	}

        // Get info about address and credit card
        async.series({
            "cc": function (done) {
                var query = "SELECT *, (expiration_date > NOW()) as valid FROM credit_card WHERE creditcard_id = " + newOrder.creditcard_id;
                console.log("MySQL Query: " + query);
                req.db.query(query, done);
            },
            "addr": function (done) {
                var query = "SELECT * FROM address WHERE address_id = " + newOrder.address_id;
                console.log("MySQL Query: " + query);
                req.db.query(query, done);
            },
        }, function (err, resultsTemp) {
            if (err)
                throw err;

            var cc = (resultsTemp.cc[0].length > 0) ? resultsTemp.cc[0][0] : null;
            var addr = (resultsTemp.addr[0].length > 0) ? resultsTemp.addr[0][0] : null;

            if (product.user_id == newOrder.user_id) {
		        res.send(200, {"status": "PRODUCT_FROM_BUYER"});
        		return;
            } if (cc == null) {
		        res.send(404, {"status": "INVALID_CC"});
        		return;
            } else if (addr == null) {
		        res.send(404, {"status": "INVALID_ADDR"});
        		return;
        	} else if (cc.valid != 1) {
		        res.send(200, {"status": "EXPIRED_CC"});
        		return;
        	} else if (cc.user_id != newOrder.user_id) {
		        res.send(404, {"status": "NOT_USER_CC"});
        		return;
        	} else if (addr.user_id != newOrder.user_id) {
		        res.send(404, {"status": "NOT_USER_ADDR"});
        		return;
            } else {
                //console.log(cc.creditcard_id + " - " + cc.address_id);

                req.db.beginTransaction(function(err) {
                    if (err) 
                        throw err;

                    var query = "INSERT INTO `order` SET user_id = " + newOrder.user_id + ", credit_card_id = " + cc.creditcard_id + ", address_id = " + addr.address_id;
                    console.log("MySQL Query: " + query);

                    req.db.query(query, function(err, result) {
                        if (err) {
                            req.db.rollback(function() {
                                throw err;
                            });

            		        res.send(404, {"status": "CHECKOUT_ERR"});
    						return;
                        }

                        var orderId = result.insertId;

                        console.log("Inserted Order ID: " + orderId);

                        var query = "INSERT INTO order_detail SET order_id = " + orderId + ", product_id = " + product.product_id + ", quantity = 1, final_price = " + product.buy_price;
                        console.log("MySQL Query: " + query);

                        req.db.query(query, function(err, result) {
                            if (err) {
                                req.db.rollback(function() {
                                    throw err;
                                });
                                res.send(404, {"status": "CHECKOUT_ERR"});
    							return;
                            }

                            var orderDetailId = result.insertId;

                            console.log("Inserted Order Detail ID: " + orderDetailId);


                            var query = "INSERT INTO seller_transaction SET order_detail_id = " + orderDetailId + ", user_id = " + product.user_id + ", earning = " + product.buy_price;
                            console.log("MySQL Query: " + query);

                            req.db.query(query, function(err, result) {
                                if (err) {
                                    req.db.rollback(function() {
                                        throw err;
                                    });
                                    res.send(404, {"status": "CHECKOUT_ERR"});
    								return;
                                }

                                var sellerTransactionId = result.insertId;

                                console.log("Inserted Seller Transaction ID: " + sellerTransactionId);
                                
                                req.db.commit(function(err) {
                                    if (err) { 
                                        req.db.rollback(function() {
                                            throw err;
                                        });
                                        res.send(404, {"status": "CHECKOUT_ERR"});
    									return;
                                    }

                                    console.log("New Order created without problems!");
                                    res.send(200, {"status": "OK", "order_id": orderId});
                                });
                            });
                        });
                    });
                });
            }
        });
    });
});

routes.post("/checkout/cart", express.bodyParser(), function (req, res, next) {
    var newOrder = req.body;

    if (newOrder.user_id == undefined) {
        res.send(404, {"status": "INVALID_USER"});
        return;
    }

    var query = "SELECT P.user_id, P.product_id, (P.quantity - IFNULL((SELECT SUM(quantity) FROM order_detail OD WHERE OD.product_id = P.product_id GROUP BY OD.product_id),0)) AS stock, C.quantity as cart_quantity, P.buy_price FROM product P INNER JOIN cart C ON P.product_id = C.product_id WHERE C.user_id = " + req.db.escape(newOrder.user_id);
    console.log("MySQL Query: " + query);

    req.db.query(query, function (err, results) {
        if (err) { 
            throw err;
        }

        var products = results;

        if (results.length == 0) {
            res.send(200, {"status": "NOTHING_ON_CART"});
            return;
        }

        if (newOrder.creditcard_id == undefined) {
            res.send(404, {"status": "INVALID_CC"});
            return;
        } else if (newOrder.address_id == undefined) {
            res.send(404, {"status": "INVALID_ADDR"});
            return;
        }

        // Get info about address and credit card
        async.series({
            "cc": function (done) {
                var query = "SELECT *, (expiration_date > NOW()) as valid FROM credit_card WHERE creditcard_id = " + newOrder.creditcard_id;
                console.log("MySQL Query: " + query);
                req.db.query(query, done);
            },
            "addr": function (done) {
                var query = "SELECT * FROM address WHERE address_id = " + newOrder.address_id;
                console.log("MySQL Query: " + query);
                req.db.query(query, done);
            },
        }, function (err, resultsTemp) {
            if (err)
                throw err;

            var cc = (resultsTemp.cc[0].length > 0) ? resultsTemp.cc[0][0] : null;
            var addr = (resultsTemp.addr[0].length > 0) ? resultsTemp.addr[0][0] : null;

            if (cc == null) {
                res.send(404, {"status": "INVALID_CC"});
                return;
            } else if (addr == null) {
                res.send(404, {"status": "INVALID_ADDR"});
                return;
            } else if (cc.valid != 1) {
                res.send(200, {"status": "EXPIRED_CC"});
                return;
            } else if (cc.user_id != newOrder.user_id) {
                res.send(404, {"status": "NOT_USER_CC"});
                return;
            } else if (addr.user_id != newOrder.user_id) {
                res.send(404, {"status": "NOT_USER_ADDR"});
                return;
            } else {
                req.db.beginTransaction(function(err) {
                    if (err) 
                        throw err;

                    var query = "INSERT INTO `order` SET user_id = " + newOrder.user_id + ", credit_card_id = " + cc.creditcard_id + ", address_id = " + addr.address_id;
                    console.log("MySQL Query: " + query);

                    req.db.query(query, function(err, result) {
                        if (err) {
                            req.db.rollback(function() {
                                throw err;
                            });

                            res.send(404, {"status": "CHECKOUT_ERR"});
                            return;
                        }

                        var orderId = result.insertId;

                        console.log("Inserted Order ID: " + orderId);

                        var i = 0;

                        var pids = [];
                        for(i = 0; i < products.length; i++) {
                            pids.push(i);
                        }

                        async.eachSeries(pids, function (pId, done) {
                                addOrderDetail(products[pId], orderId, res, req, newOrder, done)
                            }, function(err, results) {
                            if (err) {
                                req.db.rollback(function() {
                                    throw err;
                                });
                                res.send(404, {"status": "CHECKOUT_ERR"});
                                return;
                            }

                            var query = "DELETE FROM cart WHERE user_id = " + req.db.escape(newOrder.user_id);
                            console.log("MySQL Query: " + query);

                            req.db.query(query, function(err, result) {
                                if (err) {
                                    req.db.rollback(function() {
                                        throw err;
                                    });
                                    res.send(404, {"status": "CHECKOUT_ERR"});
                                    return;
                                }

                                if(result.affectedRows <= 0){
                                    res.send(404, {"status": "CANT_DELETE_CART"});
                                    return;
                                }

                                console.log("Deleted Cart Elements: " + result.affectedRows);

                                req.db.commit(function(err) {
                                    if (err) { 
                                        req.db.rollback(function() {
                                            throw err;
                                        });
                                        res.send(404, {"status": "CHECKOUT_ERR"});
                                        return;
                                    }

                                    console.log("New Order created without problems!");
                                    res.send(200, {"status": "OK", "order_id": orderId});
                                });
                            });
                        });
                    });
                });
            }
        });
    });
});

function addOrderDetail(prod, oId, res, req, nOrder, done) {
    console.log("Creating Order Detail for " + prod.product_id);

    if (prod.user_id == nOrder.user_id) {
        res.send(404, {"status": "PRODUCT_FROM_BUYER", "product_id": prod.product_id});
        return;
    }

    if (prod.stock < prod.cart_quantity) {
        res.send(200, {"status": "OUT_OF_STOCK", "product_id": prod.product_id, "stock": prod.stock});
        return;
    }

    var query = "INSERT INTO order_detail SET order_id = " + oId + ", product_id = " + prod.product_id + ", quantity = " + prod.cart_quantity + ", final_price = " + prod.buy_price;
    console.log("MySQL Query: " + query);

    req.db.query(query, function(err, result) {
        if (err) {
            req.db.rollback(function() {
                throw err;
            });
            res.send(404, {"status": "CHECKOUT_ERR"});
            return;
        }

        var orderDetailId = result.insertId;

        console.log("Inserted Order Detail ID: " + orderDetailId);

        var query = "INSERT INTO seller_transaction SET order_detail_id = " + orderDetailId + ", user_id = " + prod.user_id + ", earning = " + (prod.buy_price * prod.cart_quantity);
        console.log("MySQL Query: " + query);

        req.db.query(query, function(err, result) {
            if (err) {
                req.db.rollback(function() {
                    throw err;
                });
                res.send(404, {"status": "CHECKOUT_ERR"});
                return;
            }

            var sellerTransactionId = result.insertId;

            console.log("Inserted Seller Transaction ID: " + sellerTransactionId);

            done();
        });
    });
}

module.exports = routes;