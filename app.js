//var robot = require("robotjs");
var cp = require('child_process');
var express = require('express');
var app = express();

app.post('/run', function(req, res) {

  var command = req.body.command;
  cp.exec(command);
  res.send('Launch: ' + command);
});

app.post('/terminal', function(req, res) {

  var command = req.body.command;
  cp.exec('gnome-terminal');
  res.send('Open Terminal');
});

app.post('/type', function(req, res) {

  var text = req.body.text;
  //robot.typeString(text);
  res.send('Type: ' + text);
});

app.post('/enter', function(req, res) {

  var text = req.body.text;
  //robot.keyTap('enter');
  res.send('Enter Key');
});

var server = app.listen(3000, function() {

  //var host = server.address().address;
  var host = '127.0.0.1';
  var port = server.address().port;
  
  console.log('Listening at http://%s:%s', host, port);
});
