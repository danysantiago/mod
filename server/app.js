var config = require("./lib/config.js"),
	express = require("express"),
	app = express(),
	mysql = require("mysql");

app.configure(function() {
	app.set("name", config.appName);
});

/* Request Logger */
app.use(function (req, res, next) {
    console.log("%s %s", req.method, req.url);
    next();
});

app.use(express.static(__dirname + "/public"));

app.get("/example", function (req, res) {
    res.send({"message": "Hi from the EC2"});
})

dbClient = mysql.createConnection(config.dbAddress);
dbClient.connect(function (err) {
    if(err) {
        return console.error("Could not connect to mysql", err);
    }

    console.log("Database connection sucessful");

    app.listen(config.appPort);
    console.log("App started, listening at port %s", config.appPort);
});
