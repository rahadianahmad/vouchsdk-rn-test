package com.vouchtest

import android.app.Activity
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
//import sg.vouch.vouchsdk.VouchSDK
import javax.annotation.Nullable


class VouchManager(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext){

//    val context = reactContext.currentActivity!!
    override fun getName(): String {
        return "Vouch"
    }

    private  fun sendEvent(reactContext: ReactApplicationContext, eventName: String,   params: WritableMap){
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit(eventName, params)
    }

    val params: WritableMap = Arguments.createMap()


    @ReactMethod
    fun openChat(id: String, promise: Promise){
       promise.resolve(id)

//        VouchSDK.setCredential(username = "", password = "")
//                .setApiKey("pmWWS8E.LHWbcHHPHvxV8jAs6qldAsf6qtlJ3ZtIwxF")
//                .isUsingEntranceAnimation(true)
//                .openChatActivity(currentActivity!!)
    }

}