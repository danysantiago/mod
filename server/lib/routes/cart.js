var config = require("../config.js"),
    express = require("express");

var routes = express();

routes.get("/cart", function (req, res, next) {
  var userId = req.query.userId;

  if(!userId) {
    return res.send(400, {"error": "No userId provided"});
  }

  var query = "SELECT *, IFNULL((SELECT MAX(bid_amount) FROM bid B WHERE B.product_id = P.product_id), starting_bid_price) as actual_bid, IFNULL((SELECT SUM(rate)/COUNT(*) FROM seller_review WHERE reviewee_user_id = P.user_id), 0) as avg_seller_rating, (quantity - IFNULL((SELECT SUM(quantity) FROM order_detail OD WHERE P.product_id = OD.product_id), 0)) as stock FROM product P\n" +
              " LEFT JOIN (SELECT product_id as pipd, min(product_image_id) as product_image_id, image_src FROM product_image GROUP BY product_id) PI ON pipd = P.product_id INNER JOIN (SELECT product_id, quantity as cart_quantity FROM cart WHERE user_id = " + req.db.escape(userId) + ") CA ON CA.product_id = P.product_id WHERE (IFNULL(auction_end_ts, NOW() + 1) > NOW()) HAVING (stock > 0)";

  console.log("MySQL Query: " + query);

  req.db.query(query, function (err, results) {
    if(err) {
      return next(err);
    }

    var total = 0;
    var i = 0;

    for (i = 0; i < results.length; i++) {
      total += results[i].buy_price * results[i].cart_quantity;
    }

    ret = {
      "results":results,
      "total":total
    }
    res.send(200, ret);
  });

});

routes.delete("/cart", express.bodyParser(), function (req, res, next) {
  console.log(req.body);
  var query_params = req.body;

  var query = "DELETE FROM `modstore`.`cart` WHERE `product_id` = ? AND `user_id` = ?";

  console.log("MySQL Query: "+query);
  req.db.query(query, [query_params.productId, query_params.userId], function (err, result) {
    if (err) {
        console.log(err);
        res.send(200, {"status": "error"});
        return;
    }
    if(result.affectedRows <= 0){
      res.send(200, {"status": "error"});
      return;
    }
    res.send(200, {"status": "ok"});
  });
});

routes.post("/cart", express.bodyParser(), function (req, res, next) {
  var userId = req.body.userId;
  var productId = req.body.productId;

  if (userId == undefined) {
    res.send(404, {"status": "NO_USER"});
    return;
  } else if (productId == undefined) {
    res.send(404, {"status": "NO_PRODUCT"});
    return;
  }

  var query = "SELECT *, (P.quantity - IFNULL((SELECT SUM(quantity) FROM order_detail OD WHERE OD.product_id = P.product_id GROUP BY OD.product_id),0)) AS stock FROM product P WHERE P.product_id = " + productId;
  console.log("MySQL Query: " + query);

  req.db.query(query, function (err, result) {
    if (result.length == 0) {
      res.send(404, {"status": "INVALID_PRODUCT"});
      return;
    }

    var product = result[0];

    if (product.stock == 0) {
      res.send(404, {"status": "OUT_OF_STOCK"});
      return;
    } else if (product.user_id == userId) {
      res.send(404, {"status": "PRODUCT_FROM_BUYER"});
      return;
    } else if (product.starting_bid_price != null) {
      res.send(404, {"status": "PRODUCT_IS_AUCTION"});
      return;
    }

    var query = "INSERT INTO cart SET user_id = " + req.db.escape(userId) + ", product_id = " + req.db.escape(productId) + ", quantity = 1;";
    console.log("MySQL Query: " + query);

    req.db.query(query, function (err, result) {
      if (err) {
          console.log(err);
          res.send(404, {"status": ((err.code == "ER_NO_REFERENCED_ROW_") ? "FOREIGN_FAIL" : "DB_ERR")});
          return;
      }
      if(result.affectedRows <= 0){
        res.send(404, {"status": "NO_INSERTED"});
        return;
      }
      res.send(200, {"status": "ok"});
    });
  });
});

routes.put("/cart", express.bodyParser(), function (req, res, next) {
  var userId = req.body.userId;
  var productId = req.body.productId;
  var quantity = req.body.quantity;

  if (userId == undefined) {
    res.send(404, {"status": "NO_USER"});
    return;
  } else if (productId == undefined) {
    res.send(404, {"status": "NO_PRODUCT"});
    return;
  } else if (quantity == undefined) {
    res.send(404, {"status": "NO_QUANTITY"});
    return;
  } else if (quantity <= 0) {
    res.send(404, {"status": "INVALID_QUANTITY"});
    return;
  }

  var query = "SELECT *, (P.quantity - IFNULL((SELECT SUM(quantity) FROM order_detail OD WHERE OD.product_id = P.product_id GROUP BY OD.product_id),0)) AS stock FROM product P WHERE P.product_id = " + productId;
  console.log("MySQL Query: " + query);

  req.db.query(query, function (err, result) {
    if (result.length == 0) {
      res.send(404, {"status": "INVALID_PRODUCT"});
      return;
    }

    var product = result[0];

    if ((product.stock - quantity) <= 0) {
      res.send(404, {"status": "OUT_OF_STOCK"});
      return;
    } else if (product.user_id == userId) {
      res.send(404, {"status": "PRODUCT_FROM_BUYER"});
      return;
    }

    var query = "UPDATE cart SET quantity = " + req.db.escape(quantity) + " WHERE user_id = " + req.db.escape(userId) + " AND product_id = " + req.db.escape(productId);
    console.log("MySQL Query: " + query);

    req.db.query(query, function (err, result) {
      if (err) {
          console.log(err);
          res.send(404, {"status": ((err.code == "ER_NO_REFERENCED_ROW_") ? "FOREIGN_FAIL" : "DB_ERR")});
          return;
      }
      if(result.affectedRows <= 0){
        res.send(404, {"status": "NO_UPDATED"});
        return;
      }
      res.send(200, {"status": "ok"});
    });
  });
});

module.exports = routes;