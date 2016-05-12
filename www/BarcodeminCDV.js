var exec = require("cordova/exec");



module.exports.scanBarcode = function(callback) {
    exec(callback, null, 'BarcodeminCDV', 'scanBarcode', [] );
};


