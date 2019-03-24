package com.github.guikeller.cordova.samsung.accessory;

public enum CordovaSamsungAccessoryPluginAction {

    INIT("init"),
    SHUTDOWN("shutdown"),
    SEND_MESSAGE("sendMessage"),
    REGISTER_MESSAGE_LISTENER("registerMessageListener");

    private String value;

    private CordovaSamsungAccessoryPluginAction(String value){
        this.value = value;
    }

    public String value(){
        return this.value;
    }

    public static CordovaSamsungAccessoryPluginAction fromValue(String value){
        CordovaSamsungAccessoryPluginAction action = null;
        CordovaSamsungAccessoryPluginAction[] cordovaPluginActions = CordovaSamsungAccessoryPluginAction.values();
        for (CordovaSamsungAccessoryPluginAction cordovaPluginAction : cordovaPluginActions){
            if(cordovaPluginAction.value().equals(value)){
                action = cordovaPluginAction;
            }
        }
        return action;
    }

}
