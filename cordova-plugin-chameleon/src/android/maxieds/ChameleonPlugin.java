package com.maxieds.chameleonminiusb;

import java.io.InputStream;

//import com.maxieds.chameleonminiusb.ChameleonCommands;
import com.maxieds.chameleonminiusb.ChameleonDeviceConfig;
//import com.maxieds.chameleonminiusb.LibraryLogging;
//import com.maxieds.chameleonminiusb.Utils;
//import com.maxieds.chameleonminiusb.ChameleonLibraryLoggingReceiver;
import com.maxieds.chameleonminiusb.ChameleonUSBInterface;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChameleonPlugin extends CordovaPlugin {

    ChameleonUSBInterface usb = null;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        switch (action) {

        case "initialize":
            this.initialize(callbackContext);
            return true;

        case "isPresent":
            this.isPresent(callbackContext);
            return true;

        case "selectSlot":
            int slotNum = args.getInt(0);
            this.selectSlot(slotNum, callbackContext);
            return true;

        case "uploadArray":
            // parse arg[0] into a byte array:
            JSONObject jsonObj = args.getJSONObject(0);
            byte[] byteArray = new byte[jsonObj.length()];
            for (int i = 0; i < jsonObj.length(); i++) {
                byteArray[i] = (byte) jsonObj.getInt(Integer.toString(i));
            }
            this.uploadArray(byteArray, callbackContext);
            return true;

        case "shutdown":
            this.shutdown(callbackContext);
            return true;

        default:
            return false;
        }
    }

    private void initialize(CallbackContext callbackContext) {
        // initialize the Chameleon USB library
        this.usb = new ChameleonDeviceConfig();
        boolean res = this.usb.chameleonUSBInterfaceInitialize(cordova.getActivity());
        if (res)
            callbackContext.success("SUCCESS: USB INITIALIZED");
        else
            callbackContext.error("ERROR: USB NOT INITLALIZED");
    }

    private void isPresent(CallbackContext callbackContext) {
        // test if usb is initialized
        if (this.usb == null) {
            callbackContext.error("ERROR: USB must be initialize before any other operation");
            return;
        }
        // call library native function
        if (ChameleonDeviceConfig.THE_CHAMELEON_DEVICE.chameleonPresent())
            callbackContext.success("USB device CONNECTED");
        else
            callbackContext.error("USB NOT CONNECTED");
    }

    private void selectSlot(int slotNum, CallbackContext callbackContext) {
        // test if usb is initialized
        if (this.usb == null) {
            callbackContext.error("ERROR: USB must be initialize before any other operation");
            return;
        }
        // call library native function
        boolean res = this.usb.prepareChameleonEmulationSlot(slotNum, true);
        if (res)
            callbackContext.success("SUCCESS: SLOT n°" + slotNum + " is SELECTED");
        else
            callbackContext.error("ERROR: FAILED TO SELECT SLOT n°" + slotNum);
    }

    private void uploadArray(byte[] byteArray, CallbackContext callbackContext) {
        // test if usb is initialized
        if (this.usb == null) {
            callbackContext.error("ERROR: USB must be initialize before any other operation");
            return;
        }

        // TODO: set a timeout to prevent application blocking

        // call library native function
        boolean res = this.usb.chameleonUpload(byteArray);
        if (res)
            callbackContext.success("SUCCESS: BYTE ARRAY UPLOAD");
        else
            callbackContext.error("ERROR: BYTE ARRAY NOT UPLOADED");
    }

    private void shutdown(CallbackContext callbackContext) {
        // test if usb is initialized
        if (this.usb == null) {
            callbackContext.error("ERROR: USB must be initialize before any other operation");
            return;
        }
        // call library native function
        boolean res = this.usb.chameleonUSBInterfaceShutdown();
        if (res)
            callbackContext.success("SUCCESS: USB SHUTDOWN PROPERLY");
        else
            callbackContext.error("ERROR: USB SHUTDOWN MAY HAVE FAILED");
        // reset usb
        this.usb = null;
    }

}
