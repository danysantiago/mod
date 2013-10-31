var config = require("../config.js"),
    express = require("express");
    mysql = require("mysql");

var routes = express();

// Omar Soto
routes.get("/categories/:parentId", function (req, res) {
  if (req.params.parentId == -1) {
    query = "SELECT * FROM category WHERE category_id NOT IN (SELECT category_id FROM category_parent);";
  } else {
    query = "SELECT category_id, name, (SELECT parent_category_id FROM category_parent B WHERE A.category_id = B.category_id) as parent_category_id FROM category A WHERE A.category_id = ? UNION SELECT category_id, name, parent_category_id FROM category_parent NATURAL JOIN category WHERE parent_category_id = ?;";
    query = req.db.format(query, [req.params.parentId, req.params.parentId])
  }

  console.log("MySQL QUERY: " + query);

  req.db.query(query, function(err, results) {
    if (err)
      throw err;

    var childrens = [];
    var parent = {"id" : -1, "parentId": -1, "name": ""};

    for (i = 0; i < results.length; i++) {
      rec = {"id" : results[i].category_id, "parentId": ((results[i].parent_category_id != null) ? results[i].parent_category_id : -1), name: results[i].name};

      if (i == 0 && req.params.parentId != -1) {
        parent = rec;
      } else {
        childrens.push(rec);
      }
    }

    res.send({"parent" : parent, "list" : childrens});
  });
});

module.exports = routes;