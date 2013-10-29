var config = require("../config.js"),
    express = require("express");
    mysql = require("mysql");

var fakeCategories = [
  {"id": 0, "parentId": -1, "name": "Books"},
  {"id": 1, "parentId": 0, "name": "Children"},
  {"id": 2, "parentId": 0, "name": "Fiction"},
  {"id": 3, "parentId": 0, "name": "Technology"},
  {"id": 4, "parentId": 0, "name": "Business"},
  {"id": 5, "parentId": -1, "name": "Electronics"},
  {"id": 6, "parentId": 5, "name": "Television"},
  {"id": 7, "parentId": 5, "name": "Audio"},
  {"id": 8, "parentId": 5, "name": "Phones"},
  {"id": 9, "parentId": 5, "name": "Camera"},
  {"id": 10, "parentId": 5, "name": "Video"},
  {"id": 11, "parentId": -1, "name": "Computers"},
  {"id": 12, "parentId": 11, "name": "Laptops"},
  {"id": 13, "parentId": 11, "name": "Desktops"},
  {"id": 14, "parentId": 11, "name": "Tablets"},
  {"id": 15, "parentId": 11, "name": "Printers"},
  {"id": 16, "parentId": -1, "name": "Clothing"},
  {"id": 17, "parentId": 16, "name": "Childen"},
  {"id": 18, "parentId": 16, "name": "Men"},
  {"id": 19, "parentId": 18, "name": "Shirts"},
  {"id": 20, "parentId": 18, "name": "Pants"},
  {"id": 21, "parentId": 18, "name": "Socks"},
  {"id": 22, "parentId": 16, "name": "Women"},
  {"id": 23, "parentId": 22, "name": "Shirts"},
  {"id": 24, "parentId": 22, "name": "Pants"},
  {"id": 25, "parentId": 22, "name": "Dresses"},
  {"id": 26, "parentId": -1, "name": "Shoes"},
  {"id": 27, "parentId": 26, "name": "Children"},
  {"id": 28, "parentId": 26, "name": "Women"},
  {"id": 29, "parentId": 26, "name": "Men"},
  {"id": 30, "parentId": -1, "name": "Sports"},
  {"id": 31, "parentId": 30, "name": "Bicycles"},
  {"id": 32, "parentId": 31, "name": "Frames"},
  {"id": 33, "parentId": 31, "name": "Wheels"},
  {"id": 34, "parentId": 31, "name": "Helmet"},
  {"id": 35, "parentId": 31, "name": "Parts"},
  {"id": 36, "parentId": 30, "name": "Fishing"},
  {"id": 37, "parentId": 30, "name": "Baseball"},
  {"id": 38, "parentId": 30, "name": "Golf"},
  {"id": 39, "parentId": 30, "name": "Basketball"},
];

var routes = express();

var pool  = mysql.createPool({
  host     : 'ec2-54-226-36-4.compute-1.amazonaws.com',
  user     : 'root',
  password : 'aguacate',
  database : 'modstore'
});

/*routes.get("/categories/:parentId", function (req, res) {
  var childrens = [];
  var parent = {"id" : -1, "parentId": -1, "name": ""};

  for (i = 0; i < fakeCategories.length; i++) {
    if (fakeCategories[i].id == req.params.parentId) {
      parent = fakeCategories[i];
    }

    if (fakeCategories[i].parentId == req.params.parentId) {
      childrens.push(fakeCategories[i]);
    }
  }

  res.send({"parent" : parent, "list" : childrens});
});*/

routes.get("/categories/:parentId", function (req, res) {
  pool.getConnection(function(err, conn) {
    if (err)
      throw err;

    if (req.params.parentId == -1) {
      query = "SELECT * FROM category WHERE id NOT IN (SELECT child_category_id FROM category_parent);";
    } else {
      query = "SELECT id, name, (SELECT parent_category_id FROM category_parent WHERE child_category_id=id) as parent_category_id FROM category WHERE id=" + req.params.parentId + " UNION SELECT id, name, parent_category_id FROM category INNER JOIN category_parent ON id=child_category_id WHERE parent_category_id=" + req.params.parentId;
    }

    console.log("MySQL QUERY: " + query);

    conn.query(query, function(err, results) {
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

      conn.release();

      res.send({"parent" : parent, "list" : childrens});
    });
  });
});

routes.get("/categories", function (req, res) {
  res.send({"categories": fakeCategories});
});

module.exports = routes;