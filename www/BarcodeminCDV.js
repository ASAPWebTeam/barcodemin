var exec = require("cordova/exec");


module.exports = {

    /**
     * Get an object with the keys 'version', 'build' and 'identifier'.
     *
     * @param {Function} success    Callback method called on success.
     * @param {Function} fail       Callback method called on failure.
     */
    scanBarcode: function(success){
    	params =  {};
    	params.text_title = "";
        params.text_instructions = "";
        params.camera = "back";
        params.flash = "off";
        exec(success, null, 'BarcodeminCDV', 'scanBarcode', []);
    },

   
};
