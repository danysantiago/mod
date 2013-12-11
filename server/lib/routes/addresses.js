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

routes.post("/addresses", express.bodyParser(), function (req, res, next) {
  console.log(req.body);

  var addr = req.body;

  var query = "INSERT INTO `modstore`.`address` (`user_id`, `line1`, `line2`, `city`, `state`, `country`, `zipcode`, `is_primary`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);"
  var q = req.db.query(query, [addr.user_id, addr.line1, addr.line2, addr.city, addr.state, addr.country, addr.zipcode, addr.is_primary], function (err, result) {
    if (err) {
      return next(err);
    }

    if (addr.is_primary == 1) {
      var query = "UPDATE address SET is_primary = 0 WHERE address_id != " + result.insertId + " AND user_id = " + req.db.escape(addr.user_id);
      console.log("MySQL Query: " + query);

      req.db.query(query, function (err, result) {
        if (err) {
          return next(err);
        }

        res.send(200, {});
      });
    } else {
      res.send(200, {});
    }
  });

  console.log("MySQL Query: " + q.sql);
});

routes.put("/addresses", express.bodyParser(), function (req, res, next) {
  console.log(req.body);

  var addr = req.body;

  var query = "UPDATE `modstore`.`address` SET `user_id` = ?, `line1` = ?, `line2` = ?, `city` = ?, `state` = ?, `country` = ?, `zipcode` = ?, `is_primary` = ? WHERE `address_id`=?;"
  var q = req.db.query(query, [addr.user_id, addr.line1, addr.line2, addr.city, addr.state, addr.country, addr.zipcode, addr.is_primary, addr.address_id], function (err, result) {
    if (err) {
      return next(err);
    }

    if (addr.is_primary == 1) {
      var query = "UPDATE address SET is_primary = 0 WHERE address_id != " + addr.address_id + " AND user_id = " + req.db.escape(addr.user_id);
      console.log("MySQL Query: " + query);

      req.db.query(query, function (err, result) {
        if (err) {
          return next(err);
        }

        res.send(200, {});
      });
    } else {
      res.send(200, {});
    }
  });

  console.log("MySQL Query: " + q.sql);
});

module.exports = routes;