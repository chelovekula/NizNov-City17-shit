package com.reactnativecommunity.netinfo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.os.Build.VERSION;
import com.facebook.react.bridge.ReactApplicationContext;
import com.reactnativecommunity.netinfo.types.CellularGeneration;
import com.reactnativecommunity.netinfo.types.ConnectionType;

@TargetApi(24)
class NetworkCallbackConnectivityReceiver extends ConnectivityReceiver {
    Network mNetwork = null;
    private final ConnectivityNetworkCallback mNetworkCallback = new ConnectivityNetworkCallback();
    NetworkCapabilities mNetworkCapabilities = null;

    private class ConnectivityNetworkCallback extends NetworkCallback {
        private ConnectivityNetworkCallback() {
        }

        public void onAvailable(Network network) {
            NetworkCallbackConnectivityReceiver networkCallbackConnectivityReceiver = NetworkCallbackConnectivityReceiver.this;
            networkCallbackConnectivityReceiver.mNetwork = network;
            networkCallbackConnectivityReceiver.mNetworkCapabilities = networkCallbackConnectivityReceiver.getConnectivityManager().getNetworkCapabilities(network);
            NetworkCallbackConnectivityReceiver.this.updateAndSend();
        }

        public void onLosing(Network network, int i) {
            NetworkCallbackConnectivityReceiver networkCallbackConnectivityReceiver = NetworkCallbackConnectivityReceiver.this;
            networkCallbackConnectivityReceiver.mNetwork = network;
            networkCallbackConnectivityReceiver.mNetworkCapabilities = networkCallbackConnectivityReceiver.getConnectivityManager().getNetworkCapabilities(network);
            NetworkCallbackConnectivityReceiver.this.updateAndSend();
        }

        public void onLost(Network network) {
            NetworkCallbackConnectivityReceiver networkCallbackConnectivityReceiver = NetworkCallbackConnectivityReceiver.this;
            networkCallbackConnectivityReceiver.mNetwork = null;
            networkCallbackConnectivityReceiver.mNetworkCapabilities = null;
            networkCallbackConnectivityReceiver.updateAndSend();
        }

        public void onUnavailable() {
            NetworkCallbackConnectivityReceiver networkCallbackConnectivityReceiver = NetworkCallbackConnectivityReceiver.this;
            networkCallbackConnectivityReceiver.mNetwork = null;
            networkCallbackConnectivityReceiver.mNetworkCapabilities = null;
            networkCallbackConnectivityReceiver.updateAndSend();
        }

        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            NetworkCallbackConnectivityReceiver networkCallbackConnectivityReceiver = NetworkCallbackConnectivityReceiver.this;
            networkCallbackConnectivityReceiver.mNetwork = network;
            networkCallbackConnectivityReceiver.mNetworkCapabilities = networkCapabilities;
            networkCallbackConnectivityReceiver.updateAndSend();
        }

        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            if (NetworkCallbackConnectivityReceiver.this.mNetwork != null) {
                NetworkCallbackConnectivityReceiver networkCallbackConnectivityReceiver = NetworkCallbackConnectivityReceiver.this;
                networkCallbackConnectivityReceiver.mNetwork = network;
                networkCallbackConnectivityReceiver.mNetworkCapabilities = networkCallbackConnectivityReceiver.getConnectivityManager().getNetworkCapabilities(network);
            }
            NetworkCallbackConnectivityReceiver.this.updateAndSend();
        }
    }

    public NetworkCallbackConnectivityReceiver(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    /* access modifiers changed from: 0000 */
    @SuppressLint({"MissingPermission"})
    public void register() {
        try {
            getConnectivityManager().registerDefaultNetworkCallback(this.mNetworkCallback);
        } catch (SecurityException unused) {
        }
    }

    /* access modifiers changed from: 0000 */
    public void unregister() {
        try {
            getConnectivityManager().unregisterNetworkCallback(this.mNetworkCallback);
        } catch (IllegalArgumentException | SecurityException unused) {
        }
    }

    /* access modifiers changed from: 0000 */
    @SuppressLint({"MissingPermission"})
    public void updateAndSend() {
        ConnectionType connectionType = ConnectionType.UNKNOWN;
        NetworkCapabilities networkCapabilities = this.mNetworkCapabilities;
        CellularGeneration cellularGeneration = null;
        boolean z = false;
        if (networkCapabilities != null) {
            if (networkCapabilities.hasTransport(2)) {
                connectionType = ConnectionType.BLUETOOTH;
            } else if (this.mNetworkCapabilities.hasTransport(0)) {
                connectionType = ConnectionType.CELLULAR;
            } else if (this.mNetworkCapabilities.hasTransport(3)) {
                connectionType = ConnectionType.ETHERNET;
            } else if (this.mNetworkCapabilities.hasTransport(1)) {
                connectionType = ConnectionType.WIFI;
            } else if (this.mNetworkCapabilities.hasTransport(4)) {
                connectionType = ConnectionType.VPN;
            }
            NetworkInfo networkInfo = this.mNetwork != null ? getConnectivityManager().getNetworkInfo(this.mNetwork) : null;
            r5 = VERSION.SDK_INT >= 28 ? !this.mNetworkCapabilities.hasCapability(21) : (this.mNetwork == null || networkInfo == null || networkInfo.getDetailedState().equals(DetailedState.CONNECTED)) ? false : true;
            if (this.mNetworkCapabilities.hasCapability(12) && this.mNetworkCapabilities.hasCapability(16) && !r5) {
                z = true;
            }
            if (this.mNetwork != null && connectionType == ConnectionType.CELLULAR && z) {
                cellularGeneration = CellularGeneration.fromNetworkInfo(networkInfo);
            }
        } else {
            connectionType = ConnectionType.NONE;
        }
        updateConnectivity(connectionType, cellularGeneration, z);
    }
}
