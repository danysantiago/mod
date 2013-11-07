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

      "details": function (done) { //Get order details
        var dQuery= "SELECT *, (quantity * final_price) as total_price\n" +
                    "FROM order_detail WHERE order_id=" + order.order_id;
        
        req.db.query(dQuery, done);
      },

      "product": function (done) { //Get product
        var eQuery= "SELECT *\n" +
                    "FROM order_detail od inner join product on od.product_id=product.product_id WHERE order_id=" + order.order_id;
        
        req.db.query(eQuery, done);
      }

    }, function (err, results) {
      if (err) {
        return next(err);
      }


      //Find out whats up with the second index thing, arrays inside arrays? How crazy
      //is SQL ???
      order.address = results.address[0][0];
      order.creditcard = results.creditCard[0][0];
      
      ret = {
          "order": order,
          "details": results.details[0],
      };
      
      ret.details.product = results.product[0];

      res.send(200, ret);
    });

  });

});

module.exports = routes;