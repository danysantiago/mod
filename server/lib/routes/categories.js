var config = require("../config.js"),
    express = require("express");
    mysql = require("mysql");

var routes = express();

// Omar Soto
routes.get("/categories/:parentId", function (req, res) {
  if (req.params.parentId == -1) {
    query = "SELECT * FROM category WHERE id NOT IN (SELECT child_category_id FROM category_parent);";
  } else {
    query = "SELECT id, name, (SELECT parent_category_id FROM category_parent WHERE child_category_id=id) as parent_category_id FROM category WHERE id=" + mysql.escape(req.params.parentId) + " UNION SELECT id, name, parent_category_id FROM category INNER JOIN category_parent ON id=child_category_id WHERE parent_category_id=" + mysql.escape(req.params.parentId);
  }

  console.log("MySQL QUERY: " + query);

  req.db.query(query, function(err, results) {
    if (err)
      throw err;

    var childrens = [];
    var parent = {"id" : -1, "parentId": -1, "name": ""};

    for (i = 0; i < results.length; i++) {
      rec = {"id" : results[i].id, "parentId": ((results[i].parent_category_id != null) ? results[i].parent_category_id : -1), name: results[i].name};

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