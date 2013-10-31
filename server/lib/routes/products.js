var config = require("../config.js"),
    express = require("express");

var routes = express();

var fakeProducts = [{
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
},{
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
},{
  "pid": 2,
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

routes.get("/products/:pid", function (req, res) {
  for (i = 0; i < fakeProducts.length; i++) {
    if (fakeProducts[i].pid == req.params.pid) {
      res.send(fakeProducts[i]);
      return;
    }
  }

  res.send(404);
});

/*
 SELECT product_id, user_id, category_id, description, name, brand, model, dimensions, buy_price, quantity, starting_bid_price, auction_end_ts, (quantity - IFNULL((SELECT SUM(quantity) FROM order_detail od WHERE od.product_id = p.product_id), 0)) AS available FROM product p WHERE (auction_end_ts > NOW() OR auction_end_ts IS NULL) HAVING available > 0;
 */

routes.get("/products/category/:pid", function (req, res) {
  var categoryList = {
    "products": fakeProducts,
     "category": {
           "parentId":"-1",
            "id": "1",
            "name": "Books"
            },
    "subcategory": [
                     {
                    "parentId":"1",
                    "name": "Children",
                    "id": "6"
                      },
                      {
                        "parentId":"1",
                        "name": "Fiction",
                        "id": "7"
                      },
                     {
                        "parentId":"1",
                        "name": "Technology",
                        "id": "8"
                      },
                      {
                        "parentId":"1",
                        "name": "Business",
                        "id": "9"
                      }
                   ]
  };
  res.send(categoryList);
});

routes.get("/products", function (req, res) {
  res.send({"products": fakeProducts});
});

routes.get("/products/:pid/reviews", function (req, res) {
  var fakeReviews = {
    "pid": req.params.pid,
    "reviews": [
      {
        "rid": "123",
        "rate": 5,
        "message": "awesomeeee product",
        "created_ts": Date.now()
      },
      {
        "rid": "124",
        "rate": 2,
        "message": "Not that good...",
        "created_ts": Date.now()
      },
      {
        "rid": "125",
        "rate": 4,
        "message": "Awesome but...",
        "created_ts": Date.now()
      }
    ]
  };

  res.send(fakeReviews);
});




module.exports = routes;