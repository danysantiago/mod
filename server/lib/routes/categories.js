var config = require("../config.js"),
    express = require("express");


var routes = express();

routes.get("/categories/:name", function (req, res) {
  

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
});


module.exports = routes;