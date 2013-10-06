var config = require("../config.js"),
    express = require("express");


var routes = express();

routes.get("/categories/:name", function (req, res) {
  

  if(req.params.name == "-1"){
    var fakeList = {
        "parent": {
            "id": "-1",
            "name": ""
        },
        "list": [
          {
            "name": "Electronics",
            "id": "0"
          },
          {
            "name": "Books",
            "id": "1"
          },
         {
            "name": "Computers",
            "id": "2"
          },
          {
            "name": "Clothing",
            "id": "3"
          },
          {
            "name": "Shoes",
            "id": "4"
          },
          {
            "name": "Sports",
            "id": "5"
          }
        ]
    };
  }
  else if(req.params.name == "6"){ //Set Up for temp
    var fakeList = {
        "parent": {
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
            "id": "1",
            "name": "Books"
        },
        "list": [
          {
            "name": "Children",
            "id": "6"
          },
          {
            "name": "Fiction",
            "id": "7"
          },
         {
            "name": "Technology",
            "id": "8"
          },
          {
            "name": "Business",
            "id": "9"
          }
        ]
    };
  }

  

  res.send(fakeList);
});


module.exports = routes;