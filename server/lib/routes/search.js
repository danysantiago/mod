var config = require("../config.js"),
    express = require("express");


var routes = express();

routes.get("/search/", function (req, res) {
 var fakeList = {
    "query": "Select * from Search",
    "products": [
      {
        "pid": 123,
        "category_id": 3,
        "description": "Some fake description",
        "name": "FakeAwesomeProduct",
        "brand": "Pier Brand",
        "model": "ES-132",
        "dimensions": "3'x3'x3'",
        "price": "$99.99",
        "created_ts": Date.now()
      },
      {
        "pid": 124,
        "category_id": 3,
        "description": "Something something",
        "name": "Another product 2",
        "brand": "NoBrandMe",
        "model": "Meh",
        "dimensijons": "3'x4'x4'",
        "price": "$1.99",
        "created_ts": Date.now()
      },
      {
        "pid": 125,
        "category_id": 3,
        "description": "Some fake description",
        "name": "An item",
        "brand": "China Brand",
        "model": "Chuch Version",
        "dimensions": "2'x3'x3'",
        "price": "$29.99",
        "created_ts": Date.now()
      }
    ]
  };

  res.send(fakeProduct);
});



module.exports = routes;