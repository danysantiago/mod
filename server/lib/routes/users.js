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
    
    res.send(200, {"result": result});
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

routes.post("/users/register", express.bodyParser(), function (req, res, next) {
  var newUser = req.body;

  console.log(req.body);

  var query = "INSERT INTO `modstore`.`user` (`user_name`, `user_password`, `first_name`, `middle_name`, `last_name`, `email`, `is_admin`) VALUES (?, MD5(?), ?, ?, ?, ?, ?);"
  req.db.query(query, [newUser.user_name, newUser.user_password, newUser.first_name, newUser.middle_name, newUser.last_name, newUser.email, newUser.is_admin], function (err, result) {
    if (err) {

      if(err.code === "ER_DUP_ENTRY") {
        res.send(200, {"status": "email taken"});
        return;
      }

      return next(err);
    }

    console.log(result);

    res.send(200, {"status": "ok"});
  });


});

routes.post("/users/updatePassword", express.bodyParser(), function (req, res, next) {
 console.log(req.body);

 var query = "UPDATE `modstore`.`user` SET `user_password`= MD5(?) WHERE `user_id`=?;";
 req.db.query(query, [req.body.password, req.body.userId], function (err, result) {
  if (err) {
    return next(err);
  }

  res.send(200, {});
 });

});

routes.put("/users", express.bodyParser(), function (req, res, next) {
 console.log(req.body);

 var userId = req.body.userId;
 var firstName = req.body.firstName;
 var middleName = req.body.middleName;
 var lastName = req.body.lastName;
 var email = req.body.email;

 var sets = [];

 if (userId == undefined) {
  res.send(404, {"status" : "NO_USER"});
  return;
 }

 if (firstName != undefined)
  sets.push("first_name = " + req.db.escape(firstName));
 if (middleName != undefined)
  sets.push("middle_name = " + req.db.escape(middleName));
 if (lastName != undefined)
  sets.push("last_name = " + req.db.escape(lastName));
 if (email != undefined)
  sets.push("email = " + req.db.escape(email));

  if (sets.length == 0) {
    res.send(404, {"status" : "NOTHING_TO_UPDATE"});
    return;
  }

 var query = "UPDATE user SET " + sets.join(", ") + " WHERE user_id = " + req.body.userId;
 console.log("MySQL Query: " + query);
 
 req.db.query(query, [req.body.password, req.body.userId], function (err, result) {
  if (err) {
    return next(err);
  }

  res.send(200, {});
 });

});

module.exports = routes;