var config = require("../config.js"),
    express = require("express"),
    url = require("url"),
    request = require("request"),
    async = require("async"),
    _ = require("underscore");


var simpleEncoding =
  'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

// This function scales the submitted values so that
// maxVal becomes the highest value.
function simpleEncode(valueArray,maxValue) {
  var chartData = ['s:'];
  for (var i = 0; i < valueArray.length; i++) {
    var currentValue = valueArray[i];
    if (!isNaN(currentValue) && currentValue >= 0) {
    chartData.push(simpleEncoding.charAt(Math.round((simpleEncoding.length-1) *
      currentValue / maxValue)));
    }
      else {
      chartData.push('_');
      }
  }
  return chartData.join('');
}

// Same as simple encoding, but for extended encoding.
var EXTENDED_MAP=
  'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-.';
var EXTENDED_MAP_LENGTH = EXTENDED_MAP.length;
function extendedEncode(arrVals, maxVal) {
  var chartData = 'e:';

  for(i = 0, len = arrVals.length; i < len; i++) {
    // In case the array vals were translated to strings.
    var numericVal = new Number(arrVals[i]);
    // Scale the value to maxVal.
    var scaledVal = Math.floor(EXTENDED_MAP_LENGTH *
        EXTENDED_MAP_LENGTH * numericVal / maxVal);

    if(scaledVal > (EXTENDED_MAP_LENGTH * EXTENDED_MAP_LENGTH) - 1) {
      chartData += "..";
    } else if (scaledVal < 0) {
      chartData += '__';
    } else {
      // Calculate first and second digits and add them to the output.
      var quotient = Math.floor(scaledVal / EXTENDED_MAP_LENGTH);
      var remainder = scaledVal - EXTENDED_MAP_LENGTH * quotient;
      chartData += EXTENDED_MAP.charAt(quotient) + EXTENDED_MAP.charAt(remainder);
    }
  }

  return chartData;
}

var monthMap = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"];

var routes = express();

/**
http://chart.googleapis.com/chart
  ?chxl=0:|Jan|Feb|Mar|Jun|Jul|Aug
  &chxr=0,0,5
  &chxs=0,676767,14,0.5,l,676767
  &chxt=x,y
  &chs=600x300
  &cht=lc
  &chco=0000FF
  &chd=s:GMSYflrx39
  &chg=20,0
  &chls=2
  &chtt=Sales+by+Month
  &chts=000000,15
**/
routes.get("/charts/sales/months", function (req, res) {

  var limit = 6;

  async.waterfall([
    function (callback) {
      var query = "SELECT YEAR(created_ts) as `year`, MONTH(created_ts) as `month`, SUM(quantity*final_price) as total_sales FROM order_detail GROUP BY `year`, `month` ORDER BY `year` DESC, `month` DESC LIMIT 6";
      req.db.query(query, callback);
    },

    function (results, something, callback) {

      console.log(results);
      console.log(callback);

      var date = new Date();
      var currMonth = date.getMonth() + 1;

      var months = [];
      var sales = [];

      var i = 0;
      var month = currMonth;
      var maxVal = 0;
      while(i < limit) {
        if(month === 0) {
          month = 12;
        }

        months.unshift(month);

        var found = false;
        for(var j = 0; j < results.length; j++) {
          if(results[j].month === month) {
            sales.unshift(results[j].total_sales);
            found = true;

            if(maxVal < results[j].total_sales) {
              maxVal = results[j].total_sales;
            }

            break;
          }
        }

        if(!found) {
          sales.unshift(0);
        }

        month--;
        i++;
      }

      var monthsString = _.reduce(months, function (memo, monthNum) {
        return memo + monthMap[monthNum-1] + "|";
      }, "|");

      var chartUrlObj = {
        "protocol": "http",
        "host": "chart.googleapis.com",
        "pathname": "/chart",
        "query": {
          "chxl": "0:" + monthsString.substring(0,monthsString.length-1),
          "chxr": "0,0,5|1,0,"+ Math.round(maxVal),
          "chxs": "0,000000,14,0.5,l,676767|1,000000,11.5,0,lt,676767",
          "chxt": "x,y",
          "chs": "600x300",
          "cht": "lc",
          "chco": "0000FF",
          "chd": extendedEncode(sales, Math.round(maxVal)),
          "chg": "20,0",
          "chls": "2",
          "chtt": "Sales+by+Month",
          "chts": "000000,15"
        }
      };

      console.log(sales);
      console.log(chartUrlObj)

      callback(null, url.format(chartUrlObj));
    },

  ], function (err, chartUrl) {
    console.log(chartUrl);
    request(chartUrl).pipe(res);
  });

});

//Sales by weeks:
//SELECT YEAR(created_ts) as `year`, WEEK(created_ts) as `week`, SUM(quantity*final_price) as total_sales FROM order_detail GROUP BY `year`, `week` ORDER BY `year` DESC, `week` DESC LIMIT 7;

//Sales by days
//SELECT YEAR(created_ts) as `year`, DAYOFYEAR(created_ts) as `year_days`, SUM(quantity*final_price) as total_sales FROM order_detail GROUP BY `year`, `year_days` ORDER BY `year` DESC, `year_days` DESC LIMIT 14;

module.exports = routes;