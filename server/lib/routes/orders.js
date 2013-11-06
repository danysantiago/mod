var config = require("../config.js"),
    express = require("express"),
    async = require("async");


var routes = express();

routes.get("/orders", function (req, res, next) {
  var userId = req.query.userId;

  if(!userId) {
    res.send(400, {"error": "No userId provided"});
  }

  var query = "SELECT `order`.order_id, `order`.created_ts, sum(final_price) as order_total, count(order_detail_id) as details_size\n" + 
              "FROM `order` inner join order_detail on `order`.order_id=order_detail.order_id\n" +
              "WHERE user_id = " + req.db.escape(userId) + "\n" +
              "GROUP BY `order`.order_id";

  console.log("MySQL Query: " + query);

  req.db.query(query, function (err, results) {
    if(err) {
      return next(err);
    }

    res.send(200, results);
  });

});

routes.get("/orders/details", function (req, res, next) {
  var orderId = req.query.orderId;

  if(!orderId) {
    res.send(400, {"error": "No orderId provided"});
  }

  var query = "SELECT *\n" +
              "FROM `order`\n" +
              "WHERE order_id=" + req.db.escape(orderId);

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

      "details": function (done) { //Get details
        var dQuery= "SELECT *\n" +
                    "FROM order_detail inner join product on order_detail.product_id=product.product_id\n" +
                    "WHERE order_id=" + order.order_id;
        req.db.query(dQuery, done);
      },

    }, function (err, results) {
      if (err) {
        return next(err);
      }

      //Find out whats up with the second index thing, arrays inside arrays? How crazy
      //is SQL ???
      order.address = results.address[0][0];
      order.creditCard = results.creditCard[0][0];
      order.details = results.details[0];

      res.send(200, order);
    });

  });

});

module.exports = routes;