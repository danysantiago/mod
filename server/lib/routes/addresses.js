var config = require("../config.js"),
    express = require("express");


var routes = express();

routes.get("/addresses/:pid", function (req, res) {
  var fakeAddr = {
    "id": req.params.pid,
    "line1": "Urb. Villas Jose",
    "line2": "Calle Mango C-20",
    "city": "Mayaguez",
    "state": "",
    "country": "Puerto Rico",
    "zipcode": "00680",
    "created_ts": Date.now()
  };

  res.send(fakeAddr);
});


module.exports = routes;