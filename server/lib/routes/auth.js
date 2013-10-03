var config = require("../config.js"),
    express = require("express");


var routes = express();

var dummyAccounts = {
  "test": "test",
  "juan": "12345"
};

//Login route
routes.post("/login", express.bodyParser(), function (req, res) {
  if(req.body) {
    var pass = dummyAccounts[req.body.user];
    if(pass && pass === req.body.pass) {
      var fakeUser = {
        "id": req.params.uid,
        "auth_token": "5d4nyc0015",
        "username": "MyUsername",
        "firstName": "Juan",
        "middleInitial": "",
        "lastName": "Del Pueblo",
        "email": "juan.pueblo00@uprm.edu",
        "isAdmin": false,
        "created_ts": Date.now()
      };
      res.send({"status": "OK", "account": fakeUser});
    } else {
      res.send({"status": "BAD CREDENTIALS"});
    }
  } else {
    res.send(400);
  }
});

//Auth check
routes.use(function (req, res, next) {
  console.log(req.headers);
  if(req.headers.auth_token) {
    next();
  } else {
    res.send(401);
  }
});

module.exports = routes;