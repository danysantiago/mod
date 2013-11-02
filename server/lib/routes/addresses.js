var config = require("../config.js"),
    express = require("express");

var routes = express();

routes.get("/addresses/:aid", function (req, res) {
  query = "SELECT * FROM address WHERE user_id = ? && address_id = ?;";
  query = req.db.format(query, [req.logged_user.user_id, req.params.aid])

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

routes.get("/addresses", function (req, res) {
  query = "SELECT * FROM address WHERE user_id = ?;";
  query = req.db.format(query, [req.logged_user.user_id])

  console.log("MySQL QUERY: " + query);

  req.db.query(query, function(err, results) {
    if (err)
      throw err;

    var out = [];

    for (i = 0; i < results.length; i++) {
      rec = {
              "aid": results[i].address_id,
              "line1": results[i].line1,
              "line2": results[i].line2,
              "city": results[i].city,
              "state": results[i].state,
              "country": results[i].country,
              "zipcode": results[i].zipcode,
              "isDefault" : (results[i].is_primary == 1),
              "created_ts": results[i].created_ts
            };

      out.push(rec);
    }

    res.send({"addresses":out});
  });
});

module.exports = routes;