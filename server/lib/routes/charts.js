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

/* For a given date, get the ISO week number
 *
 * Based on information at:
 *
 *    http://www.merlyn.demon.co.uk/weekcalc.htm#WNR
 *
 * Algorithm is to find nearest thursday, it's year
 * is the year of the week number. Then get weeks
 * between that date and the first day of that year.
 *
 * Note that dates in one year can be weeks of previous
 * or next year, overlap is up to 3 days.
 *
 * e.g. 2014/12/29 is Monday in week  1 of 2015
 *      2012/1/1   is Sunday in week 52 of 2011
 */
function getWeekNumber(d) {
    // Copy date so don't modify original
    d = new Date(d);
    d.setHours(0,0,0);
    // Set to nearest Thursday: current date + 4 - current day number
    // Make Sunday's day number 7
    d.setDate(d.getDate() + 4 - (d.getDay()||7));
    // Get first day of year
    var yearStart = new Date(d.getFullYear(),0,1);
    // Calculate full weeks to nearest Thursday
    var weekNo = Math.ceil(( ( (d - yearStart) / 86400000) + 1)/7)
    // Return array of year and week number
    return weekNo;
}

function dayOfYear() {
  var now = new Date();
  var start = new Date(now.getFullYear(), 0, 0);
  var diff = now - start;
  var oneDay = 1000 * 60 * 60 * 24;
  var day = Math.floor(diff / oneDay);
  return day;
}

var monthMap = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"];

var routes = express();

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
          "chtt": "Sales by Month",
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

routes.get("/charts/sales/weeks", function (req, res) {

  var limit = 7;

  async.waterfall([
    function (callback) {
      var query = "SELECT YEAR(created_ts) as `year`, WEEK(created_ts) as `week`, SUM(quantity*final_price) as total_sales FROM order_detail GROUP BY `year`, `week` ORDER BY `year` DESC, `week` DESC LIMIT 7";
      req.db.query(query, callback);
    },

    function (results, something, callback) {

      console.log(results);

      var date = new Date();
      var currWeek = getWeekNumber(new Date());

      var weeks = [];
      var sales = [];

      var i = 0;
      var week = currWeek;
      var maxVal = 0;

      while(i < limit) {
        if(week === 0) {
          week = 52;
        }

        console.log(week);

        weeks.unshift(week);

        var found = false;
        for(var j = 0; j < results.length; j++) {
          if(results[j].week === week) {
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

        week--;
        i++;
      }

      weeks[weeks.length-1] = 'Curr Week';
      var weeksString = weeks.join('|');

      var chartUrlObj = {
        "protocol": "http",
        "host": "chart.googleapis.com",
        "pathname": "/chart",
        "query": {
          "chxl": "0:|" + weeksString,
          "chxr": "0,0,5|1,0,"+ Math.round(maxVal),
          "chxs": "0,000000,14,0.5,l,676767|1,000000,11.5,0,lt,676767",
          "chxt": "x,y",
          "chs": "600x300",
          "cht": "lc",
          "chco": "0000FF",
          "chd": extendedEncode(sales, Math.round(maxVal)),
          "chg": "20,0",
          "chls": "2",
          "chtt": "Sales by Weeks",
          "chts": "000000,15"
        }
      };

      console.log(sales);
      console.log(weeks);
      console.log(chartUrlObj)

      callback(null, url.format(chartUrlObj));
    },

  ], function (err, chartUrl) {
    console.log(chartUrl);
    request(chartUrl).pipe(res);
  });

});

routes.get("/charts/sales/days", function (req, res) {

  var limit = 14;

  async.waterfall([
    function (callback) {
      var query = "SELECT YEAR(created_ts) as `year`, DAYOFYEAR(created_ts) as `year_days`, SUM(quantity*final_price) as total_sales FROM order_detail GROUP BY `year`, `year_days` ORDER BY `year` DESC, `year_days` DESC LIMIT 14";
      req.db.query(query, callback);
    },

    function (results, something, callback) {

      console.log(results);

      var date = new Date();
      var currDay = dayOfYear()

      var days = [];
      var sales = [];

      var i = 0;
      var day = currDay;
      var maxVal = 0;

      while(i < limit) {
        if(day === 0) {
          day = 52;
        }

        console.log(day);

        days.unshift(day);

        var found = false;
        for(var j = 0; j < results.length; j++) {
          if(results[j].year_days === day) {
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

        day--;
        i++;
      }

      days[days.length-1] = 'Today';
      var daysString = days.join('|');

      var chartUrlObj = {
        "protocol": "http",
        "host": "chart.googleapis.com",
        "pathname": "/chart",
        "query": {
          "chxl": "0:|" + daysString,
          "chxr": "0,0,5|1,0,"+ Math.round(maxVal),
          "chxs": "0,000000,14,0.5,l,676767|1,000000,11.5,0,lt,676767",
          "chxt": "x,y",
          "chs": "600x300",
          "cht": "lc",
          "chco": "0000FF",
          "chd": extendedEncode(sales, Math.round(maxVal)),
          "chg": "20,0",
          "chls": "2",
          "chtt": "Sales by Days",
          "chts": "000000,15"
        }
      };

      console.log(sales);
      console.log(days);
      console.log(chartUrlObj)

      callback(null, url.format(chartUrlObj));
    },

  ], function (err, chartUrl) {
    console.log(chartUrl);
    request(chartUrl).pipe(res);
  });

});

routes.get("/charts/sales/products/:time", function (req, res) {

  var limit = 14;

  async.waterfall([
    function (callback) {
      var query;

      console.log(req.params.time);
      if(req.params.time === 'months') {
        query = "SELECT product_id, SUM(quantity*final_price) as total_sales FROM order_detail WHERE created_ts BETWEEN DATE_SUB(NOW(), INTERVAL 6 MONTH) AND NOW() GROUP BY product_id ORDER BY total_sales DESC";
      } else if (req.params.time === 'weeks') {
        query = "SELECT product_id, SUM(quantity*final_price) as total_sales FROM order_detail WHERE created_ts BETWEEN DATE_SUB(NOW(), INTERVAL 7 WEEK) AND NOW() GROUP BY product_id ORDER BY total_sales DESC";
      } else if (req.params.time === 'days') {
        query = "SELECT product_id, SUM(quantity*final_price) as total_sales FROM order_detail WHERE created_ts BETWEEN DATE_SUB(NOW(), INTERVAL 14 DAY) AND NOW() GROUP BY product_id ORDER BY total_sales DESC";
      } else {
        return res.send(400, {'error': 'Invalid time parameter'});
      }

      req.db.query(query, callback);
    },

    function (results, something, callback) {

      console.log(results);

      var maxVal = 0;
      var halfMaxVal;

      productsIds = [];
      sales = [];

      var i = 0;
      for (i = 0; i < results.length; i++) {
        productsIds.push(results[i].product_id);
        sales.push(results[i].total_sales);

        if(maxVal < results[i].total_sales) {
          maxVal = results[i].total_sales;
        }
      }

      halfMaxVal = Math.round(maxVal/2);

      var productsIdsString = productsIds.join('|');

      var chartTitle;
      if(req.params.time === 'months') {
        chartTitle = "Product Sales, Last 6 Months";
      } else if (req.params.time === 'weeks') {
        chartTitle = "Product Sales, Last 7 Weeks";
      } else if (req.params.time === 'days') {
        chartTitle = "Product Sales, Last 14 Days";
      }

      var chartUrlObj = {
        "protocol": "http",
        "host": "chart.googleapis.com",
        "pathname": "/chart",
        "query": {
          "chxl": "0:|" + productsIdsString + '|1:|0|' + halfMaxVal + '|' + maxVal,
          "chxt": "x,y",
          "chs": "600x300",
          "cht": "bvg",
          "chco": "0000FF",
          "chds": '0,' + maxVal,
          "chg": "20,0",
          "chd": extendedEncode(sales, Math.round(maxVal)),
          "chtt": chartTitle,
        }
      };

      console.log(productsIds);
      console.log(sales);

      callback(null, url.format(chartUrlObj));
    },

  ], function (err, chartUrl) {
    console.log(chartUrl);
    request(chartUrl).pipe(res);
  });

});

module.exports = routes;

