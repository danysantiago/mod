var config = require("../config.js"),
    express = require("express");


var routes = express();

routes.get("/products/:pid", function (req, res) {
  var fakeProduct = {
    "id": req.params.pid,
    "category_id": 3,
    "description": "Some fake description",
    "name": "FakeAwesomeProduct",
    "brand": "Pier Brand",
    "model": "ES-132",
    "dimensions": "3'x3'x3'",
    "price": "$99.99",
    "created_ts": Date.now()
  };

  res.send(fakeProduct);
});


module.exports = routes;