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

module.exports = routes;