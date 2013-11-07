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
        var dQuery= "SELECT *, od.product_id AS odpid, od.quantity AS odquantity, od.created_ts AS odcreated_ts\n" +
                    "FROM modstore.order_detail AS od INNER JOIN product AS p\n" +
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
          };

          //Update Correct Values of Details
          results.details[0][i].product_id = results.details[0][i].odpid;
          results.details[0][i].quantity = results.details[0][i].odquantity;
          results.details[0][i].created_ts = results.details[0][i].odcreated_ts;

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

module.exports = routes;