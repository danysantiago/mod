var config = require("../config.js"),
    express = require("express");


var routes = express();

var fakeCC = [
  {
    "ccid": 0,
    "number": "1234-5678-0101-1010",
    "scode": "555",
    "name": "Omar Soto",
    "type": 1, // VISA, MASTERCARD
    "expirationDate": "05/2016",
    "created_ts": Date.now()
  },
  {
    "ccid": 1,
    "number": "1234-5678-0101-1010",
    "scode": "555",
    "name": "Manuel Marquez",
    "type": 2, // VISA, MASTERCARD
    "expirationDate": "05/2016",
    "created_ts": Date.now()
  },
  {
    "ccid": 3,
    "number": "1234-5678-0101-1010",
    "scode": "555",
    "name": "Daniel Santiago",
    "type": 1, // VISA, MASTERCARD
    "expirationDate": "05/2016",
    "created_ts": Date.now()
  }
];

routes.get("/cc/:ccid", function (req, res) {
  id = -1;

  for (i = 0; i < fakeCC.length; i++) {
    if (fakeCC[i].ccid == req.params.ccid) {
      res.send(fakeCC[i]);
      return;
    }
  }

  res.send(404);
});

routes.get("/cc/", function (req, res) {
  res.send({"creditcards":fakeCC});
});

module.exports = routes;