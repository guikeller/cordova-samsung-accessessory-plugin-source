package com.github.guikeller.cordova.samsung.accessory;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.accessory.SA;
import com.samsung.android.sdk.accessory.SAAgent;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;

import java.io.IOException;

/**
 * This class extends the SAAgent to exchange messages with a consumer
 * A Samsung Agent extends "Service" - so it can run on background
 * @author guikeller
 */
public class SamsungAccessoryAgent extends SAAgent {

    private static final String TAG = SamsungAccessoryAgent.class.getSimpleName();
    private static final Class<SamsungAccessorySocket> SA_SOCKET_CLASS = SamsungAccessorySocket.class;
    private SamsungAccessorySocket samsungServiceSocket;

    public SamsungAccessoryAgent() {
        super(TAG, SA_SOCKET_CLASS);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate");
        initializeSamsungAccessoryBridge();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind");
        return new SamsungAccessoryBinder(this);
    }

    @Override
    protected void onServiceConnectionRequested(SAPeerAgent peerAgent) {
        Log.i(TAG,"onServiceConnectionRequested");
        if (peerAgent != null) {
            acceptServiceConnectionRequest(peerAgent);
        }
    }

    @Override
    protected void onServiceConnectionResponse(SAPeerAgent peerAgent, SASocket socket, int result) {
        Log.i(TAG,"onServiceConnectionResponse");
        if (result == SAAgent.CONNECTION_SUCCESS) {
            if (socket != null) {
                this.samsungServiceSocket = (SamsungAccessorySocket) socket;
            }
        }
    }

    @Override
    protected void onFindPeerAgentsResponse(SAPeerAgent[] peerAgents, int result) {
        Log.i(TAG, "onFindPeerAgentsResponse");
        if ((result == SAAgent.PEER_AGENT_FOUND) && (peerAgents != null)) {
            for(SAPeerAgent peerAgent : peerAgents) {
                super.requestServiceConnection(peerAgent);
            }
        }
    }

    public void findPeers() {
        Log.i(TAG, "onFindPeerAgentsResponse");
        super.findPeerAgents();
    }

    public void sendMessage(String msg){
        try {
            Log.i(TAG,"sendMessage :: msg: "+msg);
            int channelId = getServiceChannelId(0);
            this.samsungServiceSocket.send(channelId, msg.getBytes());
        }catch (IOException ioex) {
            Log.e(TAG, ioex.getMessage(), ioex);
        }
    }

    public void registerMessageListener(SamsungAccessoryMessageListener listener){
        Log.i(TAG,"registerMessageListener :: listener: "+listener);
        if (this.samsungServiceSocket != null) {
            this.samsungServiceSocket.registerMessageListener(listener);
        }
    }

    private void initializeSamsungAccessoryBridge() {
        try {
            Log.i(TAG,"initializeSamsungAccessoryBridge");
            SA samsungAccessory = new SA();
            samsungAccessory.initialize(this);
        } catch (SsdkUnsupportedException se) {
            processUnsupportedException(se);
        } catch (Exception ex) {
            Log.e(TAG, "An unexpected error happened :: msg: "+ex.getMessage(), ex);
            stopSelf();
        }
    }

    // This was given as an example on the Samsung Accessory SDK, slightly modified it
    private void processUnsupportedException(SsdkUnsupportedException exception) {
        Log.i(TAG,"processUnsupportedException");
        int errType = exception.getType();
        if ( errType == SsdkUnsupportedException.VENDOR_NOT_SUPPORTED ||
                errType == SsdkUnsupportedException.DEVICE_NOT_SUPPORTED ) {
            Log.w(TAG, "Vendor or Device not supported :: msg: "+exception.getMessage(), exception);
            stopSelf();
        } else if (errType == SsdkUnsupportedException.LIBRARY_NOT_INSTALLED) {
            Log.e(TAG, "You need to install the Samsung Accessory SDK to use this application.", exception);
        } else if (errType == SsdkUnsupportedException.LIBRARY_UPDATE_IS_REQUIRED) {
            Log.e(TAG, "You need to update the Samsung Accessory SDK to use this application.", exception);
        } else if (errType == SsdkUnsupportedException.LIBRARY_UPDATE_IS_RECOMMENDED) {
            Log.e(TAG, "We recommend that you update your Samsung Accessory SDK before using this application.", exception);
        } else {
            Log.e(TAG, "An unexpected error happened :: msg: "+exception.getMessage(), exception);
        }
    }

}
