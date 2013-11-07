var config = require("../config.js"),
    express = require("express");

var routes = express();

var fakeSoldOrderDetails = [{
  "oid": 0,
  "odid": 0,
  "uid": 0,
  "aid": 0,
  "quantity": 1,
  "price": 89.99,
  "tracking_number": "8903289032832020",
  "product": {
    "pid": 0,
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
  }
},{
  "oid": 1,
  "odid": 1,
  "uid": 0,
  "aid": 0,
  "quantity": 1,
  "price": 250.00,
  "tracking_number": "",
    "product": {
    "pid": 1,
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
  }
}];

routes.get("/sold_orderdetails", function (req, res) {
  res.send({"sold_orderdetails": fakeSoldOrderDetails});
});

module.exports = routes;