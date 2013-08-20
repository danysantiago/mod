var config = require("./lib/config.js"),
	express = require("express"),
	app = express(),
	pg = require("pg");

app.configure(function() {
	app.set("name", config.appName);
});

dbClient = new pg.Client(config.dbAddress);
dbClient.connect(function (err) {
    if(err) {
        return console.error("Could not connect to postgres", err);
    }

    console.log("Database connection sucessful");

    app.listen(config.appPort);
    console.log("App started, listening at port %s", config.appPort);
});
