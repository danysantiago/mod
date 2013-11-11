var config = require("../config.js"), express = require("express"), async = require("async");

var routes = express();

routes.get("/products/selling", function (req, res, next) {
  var userId = req.query.userId;

  if(!userId) {
    return res.send(400, {"error": "No userId provided"});
  }

  var result = {};

  async.series({
    "active": function (done) {
      if(req.query.active === "true") {
        var aQuery = "SELECT *, (P.quantity - IFNULL((SELECT SUM(quantity) FROM order_detail OD WHERE OD.product_id = P.product_id GROUP BY OD.product_id),0)) AS stock FROM product P  LEFT JOIN (SELECT product_id, min(product_image_id) as product_image_id, image_src FROM product_image GROUP BY product_id) PI ON PI.product_id = P.product_id WHERE user_id = " + req.db.escape(userId);
        console.log("MySQL Query: " + aQuery);
        req.db.query(aQuery, done);
      } else {
        done();
      }
    },

    "sold": function (done) {
      if(req.query.sold === "true") {
        var sQuery = "SELECT *, (OD.final_price * OD.quantity) AS total_price, OD.quantity as order_quantity, (SELECT user_id FROM `order` O WHERE O.order_id = OD.order_id) as buyer_user_id FROM product P INNER JOIN order_detail OD ON OD.product_id = P.product_id LEFT JOIN (SELECT product_id, min(product_image_id) as product_image_id, image_src FROM product_image GROUP BY product_id) PI ON PI.product_id = P.product_id WHERE P.user_id = " + req.db.escape(userId);
        console.log("MySQL Query: " + sQuery);
        req.db.query(sQuery, done);
      } else {
        done();
      }
    },

    "not_sold": function (done) {
      if(req.query.not_sold === "true") {
        u = req.db.escape(userId);
        var wQuery = "SELECT * FROM product P  LEFT JOIN (SELECT product_id, min(product_image_id) as product_image_id, image_src FROM product_image GROUP BY product_id) PI ON PI.product_id = P.product_id WHERE P.auction_end_ts < NOW() AND P.product_id NOT IN (SELECT OD.product_id FROM order_detail OD) AND P.user_id = " + u;
        console.log("MySQL Query: " + wQuery);
        req.db.query(wQuery, done);
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
    result.not_sold = results.not_sold ? results.not_sold[0] : undefined;

    res.send(200, result);
  });

});

routes.get("/products/search", function (req, res, next) {
  var searchString = req.query.searchString;
  var sort = req.query.sort; /* best, price_asc, price_desc, time_asc, time_desc */
  var category = req.query.category;
  var sellerRating = req.query.sellerRating;
  var type = req.query.type; /* all, both, buy, bid */
  var priceFrom = req.query.priceFrom;
  var priceTo = req.query.priceTo;

  var query = "SELECT *, IFNULL((SELECT MAX(bid_amount) FROM bid B WHERE B.product_id = P.product_id), starting_bid_price) as actual_bid, IFNULL((SELECT SUM(rate)/COUNT(*) FROM seller_review WHERE reviewee_user_id = P.user_id), 0) as avg_seller_rating, (quantity - IFNULL((SELECT SUM(quantity) FROM order_detail OD WHERE P.product_id = OD.product_id), 0)) as stock FROM product P LEFT JOIN (SELECT product_id, min(product_image_id) as product_image_id, image_src FROM product_image GROUP BY product_id) PI ON PI.product_id = P.product_id";
  var wheres = ["(IFNULL(auction_end_ts, NOW() + 1) > NOW())"]; // To show the items that are within the ending time of the auction, in case it exists.
  var havings = ["(stock > 0)"]; // A stock need to be available in order to be shown.
  var order = null;

  // Set WHERE by Search String
  if (searchString) {
    searchString = req.db.escape("%" + searchString + "%");
    wheres.push("(description LIKE " + searchString + " OR `name` LIKE " + searchString + ")");
  }

  // Set HAVING by Seller Rating
  if (sellerRating) {
    havings.push("(avg_seller_rating = " + req.db.escape(sellerRating) + ")");
  }

  // Set WHERE for TYPE of the Selling of a Product
  if (type) {
    if (type == "all") {
      wheres.push("(starting_bid_price > 0 OR buy_price > 0)");
    } else if (type == "both") {
      wheres.push("(starting_bid_price > 0 AND buy_price > 0)");
    } else if (type == "buy") {
      wheres.push("(starting_bid_price IS null AND buy_price > 0)");
    } else if (type == "bid") {
      wheres.push("(starting_bid_price > 0 AND buy_price IS NULL)");
    }
  }

  // Set WHERE for PRICE FROM
  if (priceFrom) {
    temp = req.db.escape(priceFrom);
    if (type) {
      if (type == "all") {
        havings.push("(actual_bid >= " + temp + " OR buy_price >= " + temp + ")");
      } else if (type == "both") {
        havings.push("(actual_bid >= " + temp + " AND buy_price >= " + temp + ")");
      } else if (type == "buy") {
        wheres.push("(buy_price >= " + temp + ")");
      } else if (type == "bid") {
        havings.push("(actual_bid >= " + temp + ")");
      }
    }
  }

  // Set WHERE for PRICE TO
  if (priceTo) {
    temp = req.db.escape(priceTo);
    if (type) {
      if (type == "all") {
        havings.push("(actual_bid <= " + temp + " OR buy_price <= " + temp + ")");
      } else if (type == "both") {
        havings.push("(actual_bid <= " + temp + " AND buy_price <= " + temp + ")");
      } else if (type == "buy") {
        wheres.push("(buy_price <= " + temp + ")");
      } else if (type == "bid") {
        havings.push("(actual_bid <= " + temp + ")");
      }
    }
  }

  // Set ORDER BY statement
  if (sort) {
    if (sort == "best") {
      order = "product_id ASC";
    } else if (sort == "price_asc" || sort == "price_desc") {
      temp = (sort == "price_asc") ? "ASC" : "DESC";
      if (type) {
        if (type == "all") {
          order = "buy_price " + temp + ", actual_bid " + temp;
        } else if (type == "buy") {
          order = "buy_price " + temp;
        } else if (type == "bid") {
          order = "actual_bid " + temp;
        }
      } else {
        order = "buy_price " + temp;
      }
    } else if (sort == "time_asc") {
      order = "created_ts ASC";
    } else if (sort == "time_desc") {
      order = "created_ts DESC";
    }
  }

  if (category) {
    if (!isNaN(category)) {
      getAllCategoriesUnder(category, req, function(cats) {
        // Set WHERE by Category
        wheres.push("(category_id IN (" + cats.join(",") + "))");
        endProductsSearch(req, res, query, wheres, havings, order);
      });
    } else {
      res.send(400, {"error": "The category must be a number."});
    }
  } else {
    endProductsSearch(req, res, query, wheres, havings, order);
  }
});

function endProductsSearch(req, res, query, wheres, havings, order) {
  // Build the FINAL MySQL QUERY
  if (wheres.length > 0) {
    query += " WHERE " + wheres.join(" AND ");
  }

  if (havings.length > 0) {
    query += " HAVING " + havings.join(" AND ");
  }

  if (order) {
    query += " ORDER BY " + order;
  }

  console.log("MySQL QUERY: " + query);

  req.db.query(query, function(err, results) {
    if (err)
      throw err;

    if (results.length > 0) {
      /*for (i = 0; i < results.length; i++) {
        //delete results[i].actual_bid;
        delete results[i].avg_seller_rating;

        //results[i].quantity = results[i].stock;
        //delete results[i].stock;
      }*/

      res.send({"results" : results});
    } else {
      res.send({"results" : []});
    }
  });
}

function getAllCategoriesUnder(parent, req, cback) {
  var query = "SELECT * FROM category_parent";

  req.db.query(query, function(err, results) {
    if (err) {
      return [parent];
    } else {
      var result;
      if (parent == -1) {
        var i = 0;
        result = [];

        for (i = 0; i < results.length; i++) {
          result.push(results[i].category_id);
        }
      } else {
        result = getAllCategoriesUnder2(parent, results);
      }

      if (cback) {
        cback(result);
      }
    }
  });
}

function getAllCategoriesUnder2(parent, cats) {
  var ret = [parent];
  var i = 0;


  for (i = 0; i < cats.length; i++) {
    if (cats[i].parent_category_id == parent) {
      ret = ret.concat(getAllCategoriesUnder2(cats[i].category_id, cats));
    }
  }

  return ret;
}

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
        var bQuery = "SELECT * FROM (SELECT product_id, MAX(bid_amount) as my_last_bid, (SELECT MAX(B2.bid_amount) FROM bid B2 WHERE B2.product_id = B.product_id GROUP BY B2.product_id) as current_bid FROM bid B WHERE B.user_id = " + esqUserId + " GROUP BY product_id) T INNER JOIN product P ON T.product_id = P.product_id LEFT JOIN (SELECT product_id, min(product_image_id) as product_image_id, image_src FROM product_image GROUP BY product_id) PI ON PI.product_id = P.product_id WHERE P.auction_end_ts > NOW()";
        console.log("MySQL Query: " + bQuery);
        req.db.query(bQuery, done);
      } else {
        done();
      }
    },

    "not_won": function (done) {
      if(req.query.not_won === "true") {
        var nwQuery = "SELECT *, (SELECT MAX(bid_amount) FROM bid B1 WHERE B1.product_id = P.product_id GROUP BY product_id) AS max_bid, (SELECT MAX(bid_amount) FROM bid B2 WHERE B2.product_id = P.product_id AND B2.user_id = " + esqUserId + " GROUP BY product_id) AS my_last_bid FROM product P LEFT JOIN (SELECT I.product_id, min(I.product_image_id) as product_image_id, I.image_src FROM product_image I GROUP BY I.product_id) PI ON PI.product_id = P.product_id WHERE P.product_id IN (SELECT product_id FROM bid B WHERE B.user_id = " + esqUserId + ") AND P.product_id IN (SELECT product_id FROM order_detail) AND P.product_id NOT IN (SELECT product_id FROM `order` O INNER JOIN order_detail OD ON O.order_id = OD.order_id INNER JOIN order_detail_winning_bid WB ON WB.order_detail_id = OD.order_detail_id WHERE O.user_id = " + esqUserId + ")";
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

routes.get("/products", function (req, res, nex) {
  var productId = req.query.productId;
  var userId = req.query.userId;

  if(!productId) {
    return res.send(400, {"error": "No productId provided"});
  }

  var result = {};

  async.waterfall([
    function (done) {
      //var pQuery = "SELECT *, IFNULL((SELECT MAX(bid_amount) FROM bid B WHERE B.product_id = P.product_id), starting_bid_price) as actual_bid, IFNULL((SELECT SUM(rate)/COUNT(*) FROM seller_review WHERE reviewee_user_id = P.user_id), 0) as avg_seller_rating, (quantity - IFNULL((SELECT SUM(quantity) FROM order_detail OD WHERE P.product_id = OD.product_id), 0)) as stock FROM product P WHERE P.product_id=" + req.db.escape(productId);
      var pQuery = "SELECT *, IFNULL((SELECT SUM(rate)/COUNT(*) FROM seller_review WHERE reviewee_user_id = P.user_id), 0) as avg_seller_rating, (quantity - IFNULL((SELECT SUM(quantity) FROM order_detail OD WHERE P.product_id = OD.product_id), 0)) as stock FROM product P LEFT JOIN (SELECT B.product_id as bpid, MAX(bid_amount) as actual_bid, B.user_id as winning_user_id FROM bid B WHERE B.product_id = " + req.db.escape(productId) + " AND bid_amount = (SELECT MAX(bid_amount) FROM bid WHERE bid.product_id = " + req.db.escape(productId) + ")) AS T ON T.bpid = P.product_id WHERE P.product_id = "  + req.db.escape(productId);
      console.log("MySQL Query: " + pQuery);
      req.db.query(pQuery, function (err, product) {
        if (err) {
          return done(err);
        }

        if(product.length === 0) {
          return res.send(404, {"error": "Product not found."});
        }

        result.product = product[0];
        done();
      });
    },

    function (done) {

      uQuery = "SELECT * FROM user WHERE user_id=" + req.db.escape(result.product.user_id);
      console.log("MySQL Query: " + uQuery);
      req.db.query(uQuery, function (err, seller) {
        if (err) {
          return done(err);
        }

        result.seller = seller[0];
        done();
      });
    },

    function (done) {
      iQUery = "SELECT * FROM product_image WHERE product_id=" + req.db.escape(result.product.product_id);
      console.log("MySQL Query: " + iQUery);
      req.db.query(iQUery, function (err, images) {
        if (err) {
          return done(err);
        }

        result.images = images;
        done();
      });
    }

  ], function (err, results) {
    if (err) {
      return next(err);
    }

    res.send(200, result);
  });

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

routes.get("/products/reviews", function (req, res) {
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