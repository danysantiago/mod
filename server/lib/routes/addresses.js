var config = require("../config.js"),
    express = require("express");

var routes = express();

var fakeAddrs = [{
    "aid": 0,
    "line1": "Urb. Villas Jose",
    "line2": "Calle Mango C-20",
    "city": "Mayaguez",
    "state": "",
    "country": "Puerto Rico",
    "zipcode": "00680",
    "created_ts": Date.now()
  },
  {
    "aid": 1,
    "line1": "Urb. Villa del Carmen",
    "line2": "Ave. Constancia 4653",
    "city": "Ponce",
    "state": "",
    "country": "Puerto Rico",
    "zipcode": "00716",
    "created_ts": Date.now()
  },
  {
    "aid": 2,
    "line1": "Urb. San Antonio",
    "line2": "Calle Duende 234",
    "city": "Ponce",
    "state": "",
    "country": "Puerto Rico",
    "zipcode": "00731",
    "created_ts": Date.now()
  }
];

routes.get("/addresses/:aid", function (req, res) {
  for (i = 0; i < fakeAddrs.length; i++) {
    if (fakeAddrs[i].ccid == req.params.aid) {
      res.send(fakeAddrs[i]);
      return;
    }
  }

  res.send(404);
});

routes.get("/addresses", function (req, res) {
  res.send({"addresses":fakeCC});
});

module.exports = routes;