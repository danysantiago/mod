var config = require("../config.js"),
    express = require("express");


var routes = express();

routes.get("/users/:pid", function (req, res) {
  var fakeUser = {
    "id": req.params.pid,
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

routes.post("/users", function (req, res) {
  res.send(201);
});

routes.put("/users/:pid", function (req, res) {
  res.send(200);
});


module.exports = routes;