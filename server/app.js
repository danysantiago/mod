var config = require("./lib/config.js"),
	express = require("express"),
	app = express(),
	mysql = require("mysql");

var auth = require("./lib/routes/auth.js");

var sql;

// Routes middlewares
var products = require("./lib/routes/products.js");
var users = require("./lib/routes/users.js");
var categories = require("./lib/routes/categories.js");
var creditcards = require("./lib/routes/creditcards.js");
var addresses = require("./lib/routes/addresses.js");
var cart = require("./lib/routes/cart.js")
var orders = require("./lib/routes/orders.js");
var charts = require("./lib/routes/charts.js");

app.configure(function() {
	app.set("name", config.appName);
});

/* Request Logger */
app.use(function (req, res, next) {
    console.log("Request: %s %s", req.method, req.url);
    next();
});

app.use(function (req, res, next) {
    req.db = sql;
    next();
});

// Static content
app.use(express.static(__dirname + "/public"));

//Auth Middleware
app.use(auth)

// Routes use
app.use(products);
app.use(users);
app.use(categories);
app.use(creditcards);
app.use(addresses);
app.use(cart);
app.use(orders);
app.use(charts)

//Error handler middleware
app.use(function (err, req, res, next) {
    console.log(err);
    res.send(500, {"error": err});
});

//DB Connection & port app listening
sql = dbClient = mysql.createConnection(config.dbAddress);
dbClient.connect(function (err) {
    if(err) {
        return console.error("Could not connect to mysql", err);
    }

    console.log("Database connection successful");

    app.listen(config.appPort);
    console.log("App started, listening at port %s", config.appPort);
});
