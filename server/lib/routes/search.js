var config = require("../config.js"),
    express = require("express");

var routes = express();

var fakeProducts = [{
  "aid": 0,
  "uid": 0,
  "cid": 3,
  "description": "Some fake description",
  "name": "FakeAwesomeProduct",
  "brand": "Pier Brand",
  "model": "ES-132",
  "dimensions": "3'x3'x3'",
  "buyout_price": 99.99,
  "quantity": 4,
  "bid_price": -1,
  "auction_ends": "11/15/2013",
  "image_src": "not yet implemented",
  "created_ts": Date.now()
},{
  "aid": 1,
  "uid": 0,
  "cid": 9,
  "description": "Some other fake description",
  "name": "FakeAwesomeProduct Plus",
  "brand": "Sony",
  "model": "T3i",
  "dimensions": "3'x3'x3'",
  "buyout_price": 600.00,
  "quantity": 1,
  "bid_price": 200.00,
  "auction_ends": "12/15/2013",
  "image_src": "not yet implemented",
  "created_ts": Date.now()
},{
  "aid": 2,
  "uid": 0,
  "cid": 13,
  "description": "The Super Mega Pro Laptop ASUS!",
  "name": "ASUS U46E",
  "brand": "ASUS",
  "model": "U46E",
  "dimensions": "3'x3'x3'",
  "buyout_price": 800.00,
  "quantity": 10,
  "bid_price": -1,
  "auction_ends": "12/15/2013",
  "image_src": "not yet implemented",
  "created_ts": Date.now()
}];

routes.get("/search/", function (req, res) {
 var fakeList = {
    "query": "Select * from Search",
    "products": fakeProducts
  };

  res.send(fakeList);
});

module.exports = routes;