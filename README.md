Super fast barcode scanner for iOS by ASAPsystems.com

The following barcode types are currently supported:

IOS
QR_CODE
DATA_MATRIX
UPC_E
UPC_A
EAN_8
EAN_13
CODE_128
CODE_39
ITF

you can use it simply by :

    barcodemin.scanBarcode(function (barcode) {
            alert("Barcode scanned: " + barcode);

            // it will return 0 if the user press the cancel button
    });



you can install it by 


    cordova plugin add barcodemin
    or
    cordova plugin add https://github.com/ASAPWebTeam/barcodemin.git
