package org.cloudsky.cordovaPlugins;
//package com.subin.sampleProject;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import org.cloudsky.cordovaPlugins.ZBarScannerActivity;

public class BarcodeminCDV extends CordovaPlugin {

    // Configuration ---------------------------------------------------

    private static int SCAN_CODE = 1;


    // State -----------------------------------------------------------

    private boolean isInProgress = false;
    private CallbackContext scanCallbackContext;


    static public final int PERMISSION_DENIED_ERROR = 20000;

    private void callCamera() {
        Log.d("xx", "callCamera");
        // String[] PERMISSIONS = {Manifest.permission.CAMERA};
        cordova.requestPermission(this, 0, Manifest.permission.CAMERA);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        if (prefs.getBoolean("showsettings", false)) {
            switchToAppSettings();

        }
    }

    public void switchToAppSettings() {
        Log.d("xx", "switchToAppSettings");
        new AlertDialog.Builder(this.cordova.getActivity())
                .setTitle("Camera Permission")
                .setMessage("Please Go to Settings and enable Camera Permission")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Log.d("xx", "Switch to App Settings");
                        Intent appIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", cordova.getActivity().getPackageName(), null);
                        appIntent.setData(uri);
                        cordova.getActivity().startActivity(appIntent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        scanCallbackContext.error("Permission Error");
                        Log.d("xx", "cancel");
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create()
                .show();

    }

    public void onRequestPermissionResult(int requestCode, String[] permissions,
                                          int[] grantResults) throws JSONException {
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                Log.d("xx", "PERMISSION_DENIED");


                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("showsettings", true);
                editor.commit();
                this.scanCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, PERMISSION_DENIED_ERROR));
                return;
            }
            if (r == PackageManager.PERMISSION_GRANTED) {
                Log.d("xx", "PERMISSION_GRANTED");
                showScanner();


            }
        }
    }


    public void showScanner() {
        Intent scanIntent = new Intent(ctx, ZBarScannerActivity.class);
        // scanIntent.putExtra(ZBarScannerActivity.EXTRA_PARAMS, params.toString());
        cordova.startActivityForResult(this, scanIntent, SCAN_CODE);
    }
    // Plugin API ------------------------------------------------------

    Context ctx;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
            throws JSONException {
        if (action.equals("scanBarcode")) {
            if (isInProgress) {
                callbackContext.error("A scan is already in progress!");
            } else {
                isInProgress = true;
                scanCallbackContext = callbackContext;
                ctx = cordova.getActivity().getApplicationContext();
                if (cordova.hasPermission(Manifest.permission.CAMERA)) {
                    showScanner();
                } else {
                    callCamera();
                }
            }
            return true;
        } else {
            return false;
        }
    }


    // External results handler ----------------------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == SCAN_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    String barcodeValue = result.getStringExtra(ZBarScannerActivity.EXTRA_QRVALUE);
                    scanCallbackContext.success(barcodeValue);
                    break;
                case Activity.RESULT_CANCELED:
                    scanCallbackContext.success("0");
                    break;
                case ZBarScannerActivity.RESULT_ERROR:
                    scanCallbackContext.success("0");
                    break;
                default:
                    scanCallbackContext.success("0");
            }
            isInProgress = false;
            scanCallbackContext = null;
        }
    }
}
