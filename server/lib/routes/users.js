var config = require("../config.js"),
    express = require("express");


var routes = express();

routes.get("/users/:uid", function (req, res, next) {
  query = req.db.format("SELECT * FROM user WHERE user_id = ?;", [req.params.uid]);

  console.log("MySQL QUERY: " + query);

  req.db.query(query, function(err, result) {
    if (err) {
      return next(err);
    }

    if (result.length > 0) {
      user =  {
                "id": result[0].user_id,
                "username": result[0].user_name,
                "firstName": result[0].first_name,
                "middleName": result[0].middle_name,
                "lastName": result[0].last_name,
                "email": result[0].email,
                "isAdmin": (result[0].is_admin == 1),
                "created_ts": result[0].created_ts
              };

      res.send(user);
    } else {
      res.send(404);
    }
  });
});

routes.get("/users", function (req, res, next) {
  query = "SELECT * FROM user;";

  console.log("MySQL QUERY: " + query);

  req.db.query(query, function (err, result) {
    if (err) {
      return next(err);
    }
    
    res.send(200, result);
  });
});

routes.post("/users", function (req, res) {
  res.send(201);
});

routes.put("/users/:pid", function (req, res) {
  res.send(200);
});

routes.get("/users/:uid/orders", function (req, res) {
  var fakeOrders = {
    "uid": req.params.uid,
    "orders": [
      {
        "oid": "1234567890",
        "aid": "addressId",
        "ccid": "creditCardId",
        "created_ts": Date.now()
      },
      {
        "oid": "1234567891",
        "aid": "addressId",
        "ccid": "creditCardId",
        "created_ts": Date.now()
      }
    ]
  };

  res.send(fakeOrders);
});

routes.get("/users/:uid/reviews", function (req, res) {
  var fakeReviews = {
    "uid": req.params.uid,
    "reviews": [
      {
        "rid": "132",
        "rate": 5,
        "message": "awesomeeee seller",
        "created_ts": Date.now()
      },
      {
        "rid": "133",
        "rate": 2,
        "message": "Bad seller, slow shipping",
        "created_ts": Date.now()
      },
      {
        "rid": "134",
        "rate": 4,
        "message": "Had problem, but seller resolved",
        "created_ts": Date.now()
      }
    ]
  };

  res.send(fakeReviews);
});

routes.get("/myuser", function (req, res) {
  query = req.db.format("SELECT * FROM user WHERE user_id = ?;", [req.logged_user.user_id]);

  console.log("MySQL QUERY: " + query);

  req.db.query(query, function(err, result) {
    if (err)
      throw err;

    if (result.length > 0) {
      user =  {
                "id": result[0].user_id,
                "username": result[0].user_name,
                "firstName": result[0].first_name,
                "middleName": result[0].middle_name,
                "lastName": result[0].last_name,
                "email": result[0].email,
                "isAdmin": (result[0].is_admin == 1),
                "created_ts": result[0].created_ts
              };

      res.send(user);
    } else {
      res.send(404);
    }
  });
});

routes.get("/rating", function (req, res, next) {
  var userId = req.query.userId;

  if(!userId) {
    return res.send(400, {"error": "No userId provided"});
  }

  var query = "SELECT IFNULL((SELECT SUM(rate)/COUNT(*) FROM seller_review WHERE reviewee_user_id = " + req.db.escape(userId) + "), 0) as avg_seller_rating;";
  console.log("MySQL Query: " + query);

  req.db.query(query, function (err, results) {
    if(err) {
      return next(err);
    }
    ret = results[0];
    res.send(200, ret);
  });

});

module.exports = routes;