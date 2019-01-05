package com.maxieds.chameleonminiusb;

import com.maxieds.chameleonminiusb.ChameleonCommands;
import com.maxieds.chameleonminiusb.ChameleonDeviceConfig;
import com.maxieds.chameleonminiusb.LibraryLogging;
import com.maxieds.chameleonminiusb.Utils;
import com.maxieds.chameleonminiusb.ChameleonLibraryLoggingReceiver;
import com.maxieds.chameleonminiusb.ChameleonUSBInterface;
//import com.maxieds.chameleonminiusb.XModem;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class ChameleonEcho extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("echoMethod")) {
            String message = args.getString(0);
            this.echo(message, callbackContext);
            return true;
        }
        callbackContext.error("unknown command");
        return false;
    }

    private void echo(String message, CallbackContext callbackContext) {

        // initialize the Chameleon USB library so it gets up and a' chugging:
        new ChameleonDeviceConfig().chameleonUSBInterfaceInitialize(cordova.getActivity());
        if (ChameleonDeviceConfig.THE_CHAMELEON_DEVICE.chameleonPresent())
            callbackContext.success("USB device CONNECTED");
        else
            callbackContext.error("USB NOT CONNECTED");
    }
}
