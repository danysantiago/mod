var config = require("./lib/config.js"),
	express = require("express"),
	app = express(),
	mysql = require("mysql");

// Routes middlewares
var products = require("./lib/routes/products.js");
var users = require("./lib/routes/users.js");
var categories = require("./lib/routes/categories.js");
var creditcards = require("./lib/routes/creditcards.js");
var addresses = require("./lib/routes/addresses.js");

app.configure(function() {
	app.set("name", config.appName);
});

/* Request Logger */
app.use(function (req, res, next) {
    console.log("%s %s", req.method, req.url);
    next();
});

// Static content
app.use(express.static(__dirname + "/public"));

app.get("/example", function (req, res) {
    res.send({"message": "Hi from the EC2"});
});

// Routes use
app.use(products);
app.use(users);
app.use(categories);
app.use(creditcards);
app.use(addresses);

//DB Connection & port app listening
dbClient = mysql.createConnection(config.dbAddress);
dbClient.connect(function (err) {
    if(err) {
        return console.error("Could not connect to mysql", err);
    }

    console.log("Database connection successful");

    app.listen(config.appPort);
    console.log("App started, listening at port %s", config.appPort);
});
