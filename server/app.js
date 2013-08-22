var config = require("./lib/config.js"),
	express = require("express"),
	app = express(),
	mysql = require("mysql");

app.configure(function() {
	app.set("name", config.appName);
});

app.use(express.static(__dirname + "/public"));

dbClient = mysql.createConnection(config.dbAddress);
dbClient.connect(function (err) {
    if(err) {
        return console.error("Could not connect to mysql", err);
    }

    console.log("Database connection sucessful");

    app.listen(config.appPort);
    console.log("App started, listening at port %s", config.appPort);
});
