package com.github.guikeller.cordova.samsung.accessory;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * The connection between the provider (us) and consumer (client)
 * @author guikeller
 */
public class SamsungAccessoryServiceConnection implements ServiceConnection {

    private static final String TAG = SamsungAccessoryServiceConnection.class.getSimpleName();
    private boolean serviceBound;
    private IBinder service;

    public SamsungAccessoryServiceConnection() {
        super();
        Log.i(TAG,"constructor");
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.i(TAG,"onServiceConnected :: service: "+service);
        this.service = service;
        this.serviceBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.i(TAG,"onServiceDisconnected");
        this.service = null;
        this.serviceBound = false;
    }

    public SamsungAccessoryAgent getService(){
        Log.i(TAG,"getService");
        SamsungAccessoryAgent service = null;
        if (this.serviceBound && this.service != null) {
            // Through the binder we can get hold of the service / agent
            SamsungAccessoryBinder binder = (SamsungAccessoryBinder) this.service;
            service = binder.getService();
        }
        return service;
    }

}
