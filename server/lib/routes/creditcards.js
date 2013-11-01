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

routes.get("/cc", function (req, res) {
  query = "SELECT * FROM credit_card WHERE user_id = ?;";
  query = req.db.format(query, [req.logged_user.user_id])

  console.log("MySQL QUERY: " + query);

  req.db.query(query, function(err, results) {
    if (err)
      throw err;

    var out = [];

    for (i = 0; i < results.length; i++) {
      tempDate = new Date(results[i].expiration_date.toJSON());
      expDate = tempDate.getMonth() + "/" + tempDate.getFullYear();
      expDate = (expDate.length == 6) ? ("0" + expDate) : expDate;

      rec =   {
                "ccid": results[i].creditcard_id,
                "aid": results[i].address_id, 
                "number": results[i].number,
                "scode": results[i].security_code,
                "name": results[i].name,
                "type": results[i].type,
                "expirationDate": expDate,
                "isDefault" : (results[i].is_primary == 1),
                "created_ts": results[i].created_ts
              };

      out.push(rec);
    }

    res.send({"creditcards":out});
  });
});

module.exports = routes;