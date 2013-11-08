var config = require("../config.js"),
    express = require("express"),
    async = require("async");

var routes = express();

var fakeProducts = [{
  "pid": 0,
  "uid": 0,
  "cid": 3,
  "description": "Some fake description",
  "name": "FakeAwesomeProduct",
  "brand": "Pier Brand",
  "model": "ES-132",
  "dimensions": "3'x3'x3'",
  "buyout_price": 99.99,
  "quantity": 4,
  "bid_price": -1,
  "auction_ends": "11/15/2013",
  "image_src": "not yet implemented",
  "created_ts": Date.now()
},{
  "pid": 1,
  "uid": 0,
  "cid": 9,
  "description": "Some other fake description",
  "name": "FakeAwesomeProduct Plus",
  "brand": "Sony",
  "model": "T3i",
  "dimensions": "3'x3'x3'",
  "buyout_price": 600.00,
  "quantity": 1,
  "bid_price": 200.00,
  "auction_ends": "12/15/2013",
  "image_src": "not yet implemented",
  "created_ts": Date.now()
},{
  "pid": 2,
  "uid": 0,
  "cid": 13,
  "description": "The Super Mega Pro Laptop ASUS!",
  "name": "ASUS U46E",
  "brand": "ASUS",
  "model": "U46E",
  "dimensions": "3'x3'x3'",
  "buyout_price": 800.00,
  "quantity": 10,
  "bid_price": -1,
  "auction_ends": "12/15/2013",
  "image_src": "not yet implemented",
  "created_ts": Date.now()
}];

routes.get("/products/selling", function (req, res, next) {
  var userId = req.query.userId;



  if(!userId) {
    return res.send(400, {"error": "No userId provided"});
  }

  var result = {};

  async.series({

    "active": function (done) {
      if(req.query.active === "true") {
        var aQuery = "SELECT *, (P.quantity - IFNULL((SELECT SUM(quantity) FROM order_detail OD WHERE OD.product_id = P.product_id GROUP BY OD.product_id),0)) AS stock FROM product P WHERE user_id = " + req.db.escape(userId);
        console.log("MySQL Query: " + aQuery);
        req.db.query(aQuery, done);
      } else {
        done();
      }
    },

    "sold": function (done) {
      if(req.query.sold === "true") {
        var sQuery = "SELECT *, (OD.final_price * OD.quantity) AS total_price, OD.quantity as order_quantity, (SELECT user_id FROM `order` O WHERE O.order_id = OD.order_id) as buyer_user_id FROM product P INNER JOIN order_detail OD ON OD.product_id = P.product_id WHERE P.user_id = " + req.db.escape(userId);
        console.log("MySQL Query: " + sQuery);
        req.db.query(sQuery, done);
      } else {
        done();
      }
    }

    "notsold": function (done) {
      if(req.query.notsold === "true") {
        var sQuery = "SELECT *, (OD.final_price * OD.quantity) AS total_price, OD.quantity as order_quantity, (SELECT user_id FROM `order` O WHERE O.order_id = OD.order_id) as buyer_user_id FROM product P INNER JOIN order_detail OD ON OD.product_id = P.product_id WHERE P.user_id = " + req.db.escape(userId);
        console.log("MySQL Query: " + sQuery);
        req.db.query(sQuery, done);
      } else {
        done();
      }
    }

  }, function (err, results) {
    if (err) {
      return next(err);
    }

    result.active = results.active ? results.active[0] : undefined;
    result.sold = results.sold ? results.sold[0] : undefined;
    result.notsold = results.notsold[0] : undefined;

    res.send(200, result);
  });

});

routes.get("/products/bidding", function (req, res, next) {
  var userId = req.query.userId;


  if(!userId) {
    return res.send(400, {"error": "No userId provided"});
  }

  var esqUserId = req.db.escape(userId);

  var result = {};

  async.series({

    "bidding": function (done) {
      if(req.query.bidding === "true") {
        var bQuery = "SELECT * FROM (SELECT product_id, MAX(bid_amount) as my_last_bid, (SELECT MAX(B2.bid_amount) FROM bid B2 WHERE B2.product_id = B.product_id GROUP BY B2.product_id) as current_bid FROM bid B WHERE B.user_id = " + esqUserId + " GROUP BY product_id) T INNER JOIN product P ON T.product_id = P.product_id WHERE P.auction_end_ts > NOW()";
        console.log("MySQL Query: " + bQuery);
        req.db.query(bQuery, done);
      } else {
        done();
      }
    },

    "not_won": function (done) {
      if(req.query.not_won === "true") {
        var nwQuery = "SELECT *, (SELECT MAX(bid_amount) FROM bid B1 WHERE B1.product_id = P.product_id GROUP BY product_id) AS max_bid, (SELECT MAX(bid_amount) FROM bid B2 WHERE B2.product_id = P.product_id AND B2.user_id = " + esqUserId + " GROUP BY product_id) AS my_latest_bid FROM product P WHERE product_id IN (SELECT product_id FROM bid B WHERE B.user_id = " + esqUserId + ") AND product_id IN (SELECT product_id FROM order_detail) AND product_id NOT IN (SELECT product_id FROM `order` O INNER JOIN order_detail OD ON O.order_id = OD.order_id INNER JOIN order_detail_winning_bid WB ON WB.order_detail_id = OD.order_detail_id WHERE O.user_id = " + esqUserId + ")";
        console.log("MySQL Query: " + nwQuery);
        req.db.query(nwQuery, done);
      } else {
        done();
      }
    }

  }, function (err, results) {
    if (err) {
      return next(err);
    }

    result.bidding = results.bidding ? results.bidding[0] : undefined;
    result.not_won = results.not_won ? results.not_won[0] : undefined;

    res.send(200, result);
  });

});

routes.get("/products/:pid", function (req, res) {
  for (i = 0; i < fakeProducts.length; i++) {
    if (fakeProducts[i].pid == req.params.pid) {
      res.send(fakeProducts[i]);
      return;
    }
  }

  res.send(404);
});

/*
 SELECT product_id, user_id, category_id, description, name, brand, model, dimensions, buy_price, quantity, starting_bid_price, auction_end_ts, (quantity - IFNULL((SELECT SUM(quantity) FROM order_detail od WHERE od.product_id = p.product_id), 0)) AS available FROM product p WHERE (auction_end_ts > NOW() OR auction_end_ts IS NULL) HAVING available > 0;
 */

routes.get("/products/category/:pid", function (req, res) {
  var categoryList = {
    "products": fakeProducts,
     "category": {
           "parentId":"-1",
            "id": "1",
            "name": "Books"
            },
    "subcategory": [
                     {
                    "parentId":"1",
                    "name": "Children",
                    "id": "6"
                      },
                      {
                        "parentId":"1",
                        "name": "Fiction",
                        "id": "7"
                      },
                     {
                        "parentId":"1",
                        "name": "Technology",
                        "id": "8"
                      },
                      {
                        "parentId":"1",
                        "name": "Business",
                        "id": "9"
                      }
                   ]
  };
  res.send(categoryList);
});

routes.get("/products", function (req, res) {
  res.send({"products": fakeProducts});
});

routes.get("/products/:pid/reviews", function (req, res) {
  var fakeReviews = {
    "pid": req.params.pid,
    "reviews": [
      {
        "rid": "123",
        "rate": 5,
        "message": "awesomeeee product",
        "created_ts": Date.now()
      },
      {
        "rid": "124",
        "rate": 2,
        "message": "Not that good...",
        "created_ts": Date.now()
      },
      {
        "rid": "125",
        "rate": 4,
        "message": "Awesome but...",
        "created_ts": Date.now()
      }
    ]
  };

  res.send(fakeReviews);
});

module.exports = routes;