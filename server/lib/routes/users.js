var config = require("../config.js"),
    express = require("express");


var routes = express();

routes.get("/users/:uid", function (req, res) {
  var fakeUser = {
    "id": req.params.uid,
    "userName": "MyUsername",
    "firstName": "Juan",
    "middleInitial": "",
    "lastName": "Del Pueblo",
    "email": "juan.pueblo00@uprm.edu",
    "isAdmin": false,
    "created_ts": Date.now()
  };

  res.send(fakeUser);
});

routes.get("/users/:uid/orders", function (req, res) {
  var fakeOrders = {
    "uid": req.params.uid,
    "orders": [
      {
        "oid": "1234567890",
        "aid": "addressId",
        "ccid": "creditCardId",
        "created_ts": Date.now()
      },
      {
        "oid": "1234567891",
        "aid": "addressId",
        "ccid": "creditCardId",
        "created_ts": Date.now()
      }
    ]
  };

  res.send(fakeOrders);
});

routes.get("/users/:uid/reviews", function (req, res) {
  var fakeReviews = {
    "uid": req.params.uid,
    "reviews": [
      {
        "rid": "132",
        "rate": 5,
        "message": "awesomeeee seller",
        "created_ts": Date.now()
      },
      {
        "rid": "133",
        "rate": 2,
        "message": "Bad seller, slow shipping",
        "created_ts": Date.now()
      },
      {
        "rid": "134",
        "rate": 4,
        "message": "Had problem, but seller resolved",
        "created_ts": Date.now()
      }
    ]
  };

  res.send(fakeReviews);
});

module.exports = routes;