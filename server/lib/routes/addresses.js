var config = require("../config.js"),
    express = require("express");

var routes = express();

routes.get("/addresses/:aid", function (req, res) {
  query = "SELECT * FROM address WHERE user_id = ? && address_id = ?;";
  query = req.db.format(query, [req.logged_user.user_id, req.params.aid]);

  console.log("MySQL QUERY: " + query);

  req.db.query(query, function(err, result) {
    if (err)
      throw err;

    if (result.length > 0) {
      rec = {
              "aid": result[0].address_id,
              "line1": result[0].line1,
              "line2": result[0].line2,
              "city": result[0].city,
              "state": result[0].state,
              "country": result[0].country,
              "zipcode": result[0].zipcode,
              "isDefault" : (result[0].is_primary == 1),
              "created_ts": result[0].created_ts
            };

      res.send(rec);
    } else {
      res.send(404);
    }
  });
});

routes.get("/addresses", function (req, res, next) {
  var userId = req.query.userId;

  if(!userId) {
    return res.send(400, {"error": "No userId provided"});
  }

  query = "SELECT * FROM address WHERE user_id = ?;";
  query = req.db.format(query, [userId]);

  console.log("MySQL QUERY: " + query);

  req.db.query(query, function (err, addresses) {
    if (err) {
      return next(err);
    }

    res.send({"addresses":addresses});
  });
});

module.exports = routes;