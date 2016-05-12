var exec = require("cordova/exec");


module.exports = {

    /**
     * Get an object with the keys 'version', 'build' and 'identifier'.
     *
     * @param {Function} success    Callback method called on success.
     * @param {Function} fail       Callback method called on failure.
     */
    scanBarcode: function(success){
        exec(success, null, 'BarcodeminCDV', 'scanBarcode', []);
    },

   
};
