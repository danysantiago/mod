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

routes.get("/products/:pid/reviews", function (req, res) {
  var fakeReviews = {
    "pid": req.params.pid,
    "reviews": [
      {
        "rid": "123",
        "rate": 5,
        "message": "awesomeeee product",
        "created_ts": Date.now()
      },
      {
        "rid": "124",
        "rate": 2,
        "message": "Not that good...",
        "created_ts": Date.now()
      },
      {
        "rid": "125",
        "rate": 4,
        "message": "Awesome but...",
        "created_ts": Date.now()
      }
    ]
  };

  res.send(fakeReviews);
});


module.exports = routes;