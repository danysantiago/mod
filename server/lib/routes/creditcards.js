var config = require("../config.js"),
    express = require("express");


var routes = express();

routes.get("/cc/:pid", function (req, res) {
  var fakeCC = {
    "id": req.params.pid,
    "number": "1234-5678-0101-1010",
    "scode": "555",
    "name": "Juan Del Pueblo",
    "type": "Visa",
    "expirationDate": "May 2015",
    "created_ts": Date.now()
  };

  res.send(fakeCC);
});


module.exports = routes;