var config = require("../config.js"),
    express = require("express");

var routes = express();

var auth_tokens = [];

//Login route
routes.post("/login", express.bodyParser(), function (req, res) {
  if(req.body) {
    console.log(JSON.stringify(req.body));

    query = req.db.format("SELECT * FROM user WHERE user_name = ? AND user_password = MD5(?);", [req.body.user, req.body.pass]);
    console.log("MySQL QUERY: " + query);

    req.db.query(query, function (err, result) {
      if (err)
        throw err;

      if (result.length > 0) {
        // user_token = UUID();
        // auth_tokens[user_token] = {"user_id": results[0].user_id, "user_name": results[0].user_name};

        // res.send({"status": "OK", "account": results[0].user_name, "token": user_token});

        var user = {
                "id": result[0].user_id,
                "username": result[0].user_name,
                "firstName": result[0].first_name,
                "middleName": result[0].middle_name,
                "lastName": result[0].last_name,
                "email": result[0].email,
                "isAdmin": (result[0].is_admin == 1),
                "created_ts": result[0].created_ts
        };

        res.send(200, {"status": "OK", "account": user});
      } else {
        res.send({"status": "BAD CREDENTIALS"});
      }
    });
  } else {
    res.send(400);
  }
});

//Auth check
routes.use(function (req, res, next) {
  //console.log(req.headers);

  // if(req.headers.auth_token) {
  //   if (auth_tokens[req.headers.auth_token]) {
  //     req.logged_user = auth_tokens[req.headers.auth_token];
  //     next();
  //   } else {
  //     res.send(401);
  //   }
  // } else {
  //   res.send(401);
  // }

  next();
});

function UUID() {
  pattern = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx';
  return pattern.replace(/[xy]/g, function(c) {
    var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
    return v.toString(16);
  });
}

module.exports = routes;