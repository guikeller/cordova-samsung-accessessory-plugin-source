package com.github.guikeller.cordova.samsung.accessory;

import android.os.Binder;
import android.util.Log;

/**
 * This class extends a binder and keeps a reference to the Samsung Accessory Agent/Service
 * @author guikeller
 */
public class SamsungAccessoryBinder extends Binder {

    private static final String TAG = SamsungAccessoryBinder.class.getSimpleName();

    private static SamsungAccessoryAgent service;

    public SamsungAccessoryBinder(SamsungAccessoryAgent service) {
        Log.i(TAG, "constructor");
        this.service = service;
    }

    protected SamsungAccessoryAgent getService() {
        Log.i(TAG, "getService");
        return service;
    }

}
