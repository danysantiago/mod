var config = require("../config.js"),
    express = require("express");

var fakeCategories = [
  {"id": 0, "parentId": -1, "name": "Books"},
  {"id": 1, "parentId": 0, "name": "Children"},
  {"id": 2, "parentId": 0, "name": "Fiction"},
  {"id": 3, "parentId": 0, "name": "Technology"},
  {"id": 4, "parentId": 0, "name": "Business"},
  {"id": 5, "parentId": -1, "name": "Electronics"},
  {"id": 6, "parentId": 5, "name": "Television"},
  {"id": 7, "parentId": 5, "name": "Audio"},
  {"id": 8, "parentId": 5, "name": "Phones"},
  {"id": 9, "parentId": 5, "name": "Camera"},
  {"id": 10, "parentId": 5, "name": "Video"},
  {"id": 11, "parentId": -1, "name": "Computers"},
  {"id": 12, "parentId": 11, "name": "Laptops"},
  {"id": 13, "parentId": 11, "name": "Desktops"},
  {"id": 14, "parentId": 11, "name": "Tablets"},
  {"id": 15, "parentId": 11, "name": "Printers"},
  {"id": 16, "parentId": -1, "name": "Clothing"},
  {"id": 17, "parentId": 16, "name": "Childen"},
  {"id": 18, "parentId": 16, "name": "Men"},
  {"id": 19, "parentId": 18, "name": "Shirts"},
  {"id": 20, "parentId": 18, "name": "Pants"},
  {"id": 21, "parentId": 18, "name": "Socks"},
  {"id": 22, "parentId": 16, "name": "Women"},
  {"id": 23, "parentId": 22, "name": "Shirts"},
  {"id": 24, "parentId": 22, "name": "Pants"},
  {"id": 25, "parentId": 22, "name": "Dresses"},
  {"id": 26, "parentId": -1, "name": "Shoes"},
  {"id": 27, "parentId": 26, "name": "Children"},
  {"id": 28, "parentId": 26, "name": "Women"},
  {"id": 29, "parentId": 26, "name": "Men"},
  {"id": 30, "parentId": -1, "name": "Sports"},
  {"id": 31, "parentId": 30, "name": "Bicycles"},
  {"id": 32, "parentId": 31, "name": "Frames"},
  {"id": 33, "parentId": 31, "name": "Wheels"},
  {"id": 34, "parentId": 31, "name": "Helmet"},
  {"id": 35, "parentId": 31, "name": "Parts"},
  {"id": 36, "parentId": 30, "name": "Fishing"},
  {"id": 37, "parentId": 30, "name": "Baseball"},
  {"id": 38, "parentId": 30, "name": "Golf"},
  {"id": 39, "parentId": 30, "name": "Basketball"},
];

var routes = express();

routes.get("/categories/:parentId", function (req, res) {
  var childrens = [];
  var parent = {"id" : -1, "parentId": -1, "name": ""};

  for (i = 0; i < fakeCategories.length; i++) {
    if (fakeCategories[i].id == req.params.parentId) {
      parent = fakeCategories[i];
    }

    if (fakeCategories[i].parentId == req.params.parentId) {
      childrens.push(fakeCategories[i]);
    }
  }

  res.send({"parent" : parent, "list" : childrens});
});

routes.get("/categories", function (req, res) {
  res.send(fakeCategories);
});

/*routes.get("/categories/:name", function (req, res) {
  

  if(req.params.name == "-1"){
    var fakeList = {
        "parent": {
            "parentid": "-1",
            "id": "-1",
            "name": ""
        },
        "list": [
          {
            "parentid":"-1",
            "name": "Electronics",
            "id": "0"
          },
          {
            "parentid":"-1",
            "name": "Books",
            "id": "1"
          },
         {
            "parentid":"-1",
            "name": "Computers",
            "id": "2"
          },
          {
            "parentid":"-1",
            "name": "Clothing",
            "id": "3"
          },
          {
            "parentid":"-1",
            "name": "Shoes",
            "id": "4"
          },
          {
            "parentid":"-1",
            "name": "Sports",
            "id": "5"
          }
        ]
    };
  }
  else if(req.params.name == "6"){ //Set Up for temp
    var fakeList = {
        "parent": {
            "parentid":"1",
            "id": "6",
            "name": "Children"
        },
        "list": [
        ]
    };
  }
  else{
    var fakeList = {
        "parent": {
            "parentid":"-1",
            "id": "1",
            "name": "Books"
        },
        "list": [
          {
            "parentid":"1",
            "name": "Children",
            "id": "6"
          },
          {
            "parentid":"1",
            "name": "Fiction",
            "id": "7"
          },
         {
            "parentid":"1",
            "name": "Technology",
            "id": "8"
          },
          {
            "parentid":"1",
            "name": "Business",
            "id": "9"
          }
        ]
    };
  }

  

  res.send(fakeList);
});*/

module.exports = routes;