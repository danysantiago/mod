var config = require("../config.js"),
    express = require("express"),
    async = require("async");


var routes = express();

routes.get("/orders", function (req, res, next) {
  var userId = req.query.userId;

  if(!userId) {
    return res.send(400, {"error": "No userId provided"});
  }

  var query = "SELECT `order`.order_id, `order`.created_ts, `order`.user_id, `order`.address_id, `order`.credit_card_id, sum(final_price*quantity) as order_total, sum(quantity) as details_size\n" + 
              "FROM `order` inner join order_detail on `order`.order_id=order_detail.order_id\n" +
              "WHERE user_id = " + req.db.escape(userId) + "\n" +
              "GROUP BY `order`.order_id";

  console.log("MySQL Query: " + query);

  req.db.query(query, function (err, results) {
    if(err) {
      return next(err);
    }
    ret = {
      "orders_list":results
    }
    res.send(200, ret);
  });

});

routes.get("/orders/details", function (req, res, next) {
  var orderId = req.query.orderId;

  if(!orderId) {
    return res.send(400, {"error": "No orderId provided"});
  }

  var query = "SELECT `order`.order_id, `order`.created_ts, `order`.user_id, `order`.address_id, `order`.credit_card_id, sum(final_price*quantity) as order_total, sum(quantity) as details_size\n" + 
              "FROM `order` inner join order_detail on `order`.order_id=order_detail.order_id\n" +
              "WHERE `order`.order_id = " + req.db.escape(orderId) + "\n" +
              "GROUP BY `order`.order_id";

  console.log("MySQL Query: " + query);

  req.db.query(query, function (err, order) {
    if(err) {
      return next(err);
    }

    if(order.length === 0) {
      return res.send(404, {'error': 'Order does not exist'});
    }

    order = order[0];

    async.series({

      "address": function (done) { //Get address
        var aQuery = "SELECT * FROM address WHERE address_id=" + order.address_id;
        req.db.query(aQuery, done);
      },

      "creditCard": function (done) { //Get credit card
        var ccQuery = "SELECT * FROM credit_card WHERE creditcard_id=" + order.credit_card_id;
        req.db.query(ccQuery, done);
      },

      "details": function (done) { //Get order details
        var dQuery= "SELECT *, od.product_id AS odpid, od.quantity AS odquantity, od.created_ts AS odcreated_ts\n" +
                    ", (od.order_detail_id in (SELECT wb.order_detail_id FROM order_detail_winning_bid wb)) as won_by_bid\n" + 
                    "FROM modstore.order_detail AS od INNER JOIN product AS p LEFT JOIN (SELECT product_id as pipd, min(product_image_id) as product_image_id, image_src FROM product_image GROUP BY product_id) PI ON pipd = p.product_id\n" +
                    "WHERE p.product_id = od.product_id AND order_id =" + order.order_id;
        
        req.db.query(dQuery, done);
      },

    }, function (err, results) {
      if (err) {
        return next(err);
      }


      //Find out whats up with the second index thing, arrays inside arrays? How crazy
      //is SQL ???
      order.address = results.address[0][0];
      order.creditcard = results.creditCard[0][0];

      //Format Details Result
      for(i=0; i<results.details[0].length; i++){
          //Add "Product File"
          results.details[0][i].product = {
              "product_id": results.details[0][i].product_id,
              "user_id": results.details[0][i].user_id,
              "category_id": results.details[0][i].category_id,
              "description": results.details[0][i].description,
              "name": results.details[0][i].name,
              "brand": results.details[0][i].brand,
              "model": results.details[0][i].model,
              "dimensions": results.details[0][i].dimensions,
              "buy_price": results.details[0][i].buy_price,
              "quantity": results.details[0][i].quantity,
              "starting_bid_price": results.details[0][i].starting_bid_price,
              "auction_end_ts":results.details[0][i].auction_end_ts,
              "created_ts": results.details[0][i].created_ts,
              "image_src": results.details[0][i].image_src,
          };

          //Update Correct Values of Details
          results.details[0][i].product_id = results.details[0][i].odpid;
          results.details[0][i].quantity = results.details[0][i].odquantity;
          results.details[0][i].created_ts = results.details[0][i].odcreated_ts;
          results.details[0][i].won_by_bid = (results.details[0][i].won_by_bid == 1);

          //Delete Duplicates
          delete results.details[0][i].user_id;
          delete results.details[0][i].category_id;
          delete results.details[0][i].description;
          delete results.details[0][i].name;
          delete results.details[0][i].brand;
          delete results.details[0][i].model;
          delete results.details[0][i].dimensions;
          delete results.details[0][i].buy_price;
          delete results.details[0][i].starting_bid_price;
          delete results.details[0][i].auction_end_ts;
          delete results.details[0][i].odpid;
          delete results.details[0][i].odquantity;
          delete results.details[0][i].odcreated_ts;
      }
      
      ret = {
          "order": order,
          "details": results.details[0],
      };
      
      res.send(200, ret);
    });

  });

});

routes.get("/bids", function (req, res, next) {
  var productId = req.query.productId;

  if(!productId) {
    return res.send(400, {"error": "No productId provided"});
  }

  var query = "SELECT *, (SELECT user_name FROM user U WHERE B.user_id = U.user_id) as username FROM bid B WHERE B.product_id = " + req.db.escape(productId) + ";";
  console.log("MySQL Query: " + query);

  req.db.query(query, function (err, results) {
    if(err) {
      return next(err);
    }
    ret = {
      "bids_list" : results
    };
    res.send(200, ret);
  });

});

routes.post("/bids", express.bodyParser(), function (req, res, next) {
  var newBid = req.body;

  if (newBid.userId == undefined) {
    res.send("404", {"status" : "NO_USER"});
    return;
  }

  if (newBid.productId == undefined) {
    res.send("404", {"status" : "NO_PRODUCT"});
    return;
  }

  if (newBid.bidAmount == undefined) {
    res.send("404", {"status" : "NO_BIDAMOUNT"});
    return;
  }

  async.series({
      "user": function (done) {
          var query = "SELECT * FROM user WHERE user_id = " + req.db.escape(newBid.userId);
          console.log("MySQL Query: " + query);
          req.db.query(query, done);
      },
      "cc": function (done) {
          var query = "SELECT *, (expiration_date > NOW()) as valid FROM credit_card WHERE is_primary = 1 AND user_id = " + req.db.escape(newBid.userId);
          console.log("MySQL Query: " + query);
          req.db.query(query, done);
      },
      "addr": function (done) {
          var query = "SELECT * FROM address WHERE is_primary = 1 AND user_id = " + req.db.escape(newBid.userId);
          console.log("MySQL Query: " + query);
          req.db.query(query, done);
      },
      "product": function (done) {
          var query = "SELECT (auction_end_ts < NOW()) as ended, P.user_id, P.product_id, (P.quantity - IFNULL((SELECT SUM(quantity) FROM order_detail OD WHERE OD.product_id = P.product_id GROUP BY OD.product_id),0)) AS stock, IFNULL((SELECT MAX(bid_amount) FROM bid B WHERE B.product_id = P.product_id), starting_bid_price) as last_bid FROM product P WHERE auction_end_ts IS NOT NULL AND product_id = " + req.db.escape(newBid.productId); 
          console.log("MySQL Query: " + query);
          req.db.query(query, done);
      }
    }, 
    function (err, resultsTemp) {
      if (err)
          throw err;

      var user = (resultsTemp.user[0].length > 0) ? resultsTemp.user[0][0] : null;
      var cc = (resultsTemp.cc[0].length > 0) ? resultsTemp.cc[0][0] : null;
      var addr = (resultsTemp.addr[0].length > 0) ? resultsTemp.addr[0][0] : null;
      var prod = (resultsTemp.product[0].length > 0) ? resultsTemp.product[0][0] : null;

      if (user == null) {
        res.send("200", {"status" : "INVALID_USER"});
        return;
      }

      if (cc == null) {
        res.send("200", {"status" : "NO_DEFAULT_CC"});
        return;
      }

      if (addr == null) {
        res.send("200", {"status" : "NO_DEFAULT_ADDR"});
        return;
      }

      if (prod == null) {
        res.send("200", {"status" : "INVALID_PRODUCT"});
        return;
      }

      if (prod.user_id == newBid.userId) {
        res.send("200", {"status" : "PRODUCT_FROM_BUYER"});
        return;
      }

      if (prod.ended == 1) {
        res.send("200", {"status" : "AUCTION_ENDED"});
        return;
      }

      console.log("max: " + prod.last_bid  + " actual: " + newBid.bidAmount);

      if (prod.last_bid >= newBid.bidAmount) {
        res.send("200", {"status" : "OUTBIDDED"});
        return;
      }

      var query = "INSERT INTO bid SET bid_amount = " + req.db.escape(newBid.bidAmount) + ", user_id = " + user.user_id + ", product_id = " + prod.product_id;
      console.log("MySQL Query: " + query);

      req.db.query(query, function(err, result) {
        if (err) {
          res.send("404", {"status" : "BIDDING_DB_ERR"});
          throw err;
        }

        res.send("200", {"status" : "OK"});
      });
    });
});

module.exports = routes;