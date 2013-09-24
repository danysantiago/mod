var config = require("../config.js"),
    express = require("express");


var routes = express();

routes.get("/categories/:name", function (req, res) {
  var fakeList = [
    {
      "id": 123,
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
      "id": 124,
      "category_id": 3,
      "description": "Something something",
      "name": "Another product 2",
      "brand": "NoBrandMe",
      "model": "Meh",
      "dimensions": "3'x4'x4'",
      "price": "$1.99",
      "created_ts": Date.now()
    },
    {
      "id": 125,
      "category_id": 3,
      "description": "Some fake description",
      "name": "An item",
      "brand": "China Brand",
      "model": "Chuch Version",
      "dimensions": "2'x3'x3'",
      "price": "$29.99",
      "created_ts": Date.now()
    },

  ];

  res.send(fakeList);
});


module.exports = routes;