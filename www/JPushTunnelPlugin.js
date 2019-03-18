var exec = require('cordova/exec');

function  JPushTunnelPlugin() {};


//new一个Channel的类对象，并赋值给module.exports
var tunnelModel = new JPushTunnelPlugin();
module.exports = tunnelModel;