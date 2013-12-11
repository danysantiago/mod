var config = require("../config.js"),
    express = require("express");

var routes = express();

routes.get("/cc/:ccid", function (req, res) {
  query = "SELECT * FROM credit_card WHERE user_id = ? && creditcard_id = ?;";
  query = req.db.format(query, [req.logged_user.user_id, req.params.ccid])

  console.log("MySQL QUERY: " + query);

  req.db.query(query, function(err, result) {
    if (err)
      throw err;

    if (result.length > 0) {
      tempDate = new Date(result[0].expiration_date);
      expDate = tempDate.getMonth() + "/" + tempDate.getFullYear();
      expDate = (expDate.length == 6) ? ("0" + expDate) : expDate;

      rec = {
              "ccid": result[0].creditcard_id,
              "aid": result[0].address_id, 
              "number": result[0].number,
              "scode": result[0].security_code,
              "name": result[0].name,
              "type": result[0].type,
              "expirationDate": expDate,
              "isDefault" : (result[0].is_primary == 1),
              "created_ts": result[0].created_ts
            };

      res.send(rec);
    } else {
      res.send(404);
    }
  });
});

routes.get("/cc", function (req, res, next) {
  var userId = req.query.userId;

  if(!userId) {
    return res.send(400, {"error": "No userId provided"});
  }

  query = "SELECT * FROM credit_card WHERE user_id = ?;";
  query = req.db.format(query, [userId])

  console.log("MySQL QUERY: " + query);

  req.db.query(query, function (err, credicards) {
    if (err) {
      return next(err);
    }

    res.send({"creditcards": credicards});
  });
});

routes.post("/cc", express.bodyParser(), function (req, res, next) {
  var cc = req.body;

  console.log(req.body);

  var query = "INSERT INTO `modstore`.`credit_card` (`user_id`, `address_id`, `name`, `type`, `number`, `security_code`, `expiration_date`, `is_primary`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);"

  var q = req.db.query(query, [cc.user_id, cc.address_id, cc.name, cc.type, cc.number, cc.security_code, cc.expiration_date, cc.is_primary], function (err, result) {
    if (err) {
      return next(err);
    }

    if (cc.is_primary == 1) {
      var query = "UPDATE credit_card SET is_primary = 0 WHERE creditcard_id != " + result.insertId + " AND user_id = " + req.db.escape(cc.user_id);
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

routes.put("/cc", express.bodyParser(), function (req, res, next) {
  var cc = req.body;

  console.log(req.body);

  var query = "UPDATE `modstore`.`credit_card` SET `address_id`=?, `name`=?, `type`=?, `number`=?, `security_code`=?, `expiration_date`=?, `is_primary`=? WHERE `creditcard_id`=?;"
  var q = req.db.query(query, [cc.address_id, cc.name, cc.type, cc.number, cc.security_code, cc.expiration_date, cc.is_primary, cc.creditcard_id], function (err, result) {
    if (err) {
      return next(err);
    }

    if (cc.is_primary == 1) {
      var query = "UPDATE credit_card SET is_primary = 0 WHERE creditcard_id != " + req.db.escape(cc.creditcard_id) + " AND user_id = " + req.db.escape(cc.user_id);
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