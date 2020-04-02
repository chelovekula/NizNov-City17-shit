package com.reactnativecommunity.netinfo;

import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import androidx.core.net.ConnectivityManagerCompat;
import com.brentvatne.react.ReactVideoViewManager;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.reactnativecommunity.netinfo.types.CellularGeneration;
import com.reactnativecommunity.netinfo.types.ConnectionType;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

abstract class ConnectivityReceiver {
    @Nullable
    private CellularGeneration mCellularGeneration = null;
    @Nonnull
    private ConnectionType mConnectionType = ConnectionType.UNKNOWN;
    private final ConnectivityManager mConnectivityManager;
    private boolean mIsInternetReachable = false;
    private Boolean mIsInternetReachableOverride;
    private final ReactApplicationContext mReactContext;
    private final TelephonyManager mTelephonyManager;
    private final WifiManager mWifiManager;

    /* access modifiers changed from: 0000 */
    public abstract void register();

    /* access modifiers changed from: 0000 */
    public abstract void unregister();

    ConnectivityReceiver(ReactApplicationContext reactApplicationContext) {
        this.mReactContext = reactApplicationContext;
        this.mConnectivityManager = (ConnectivityManager) reactApplicationContext.getSystemService("connectivity");
        this.mWifiManager = (WifiManager) reactApplicationContext.getApplicationContext().getSystemService("wifi");
        this.mTelephonyManager = (TelephonyManager) reactApplicationContext.getSystemService("phone");
    }

    public void getCurrentState(@Nullable String str, Promise promise) {
        promise.resolve(createConnectivityEventMap(str));
    }

    public void setIsInternetReachableOverride(boolean z) {
        this.mIsInternetReachableOverride = Boolean.valueOf(z);
        updateConnectivity(this.mConnectionType, this.mCellularGeneration, this.mIsInternetReachable);
    }

    public void clearIsInternetReachableOverride() {
        this.mIsInternetReachableOverride = null;
    }

    /* access modifiers changed from: 0000 */
    public ReactApplicationContext getReactContext() {
        return this.mReactContext;
    }

    /* access modifiers changed from: 0000 */
    public ConnectivityManager getConnectivityManager() {
        return this.mConnectivityManager;
    }

    /* access modifiers changed from: 0000 */
    public void updateConnectivity(@Nonnull ConnectionType connectionType, @Nullable CellularGeneration cellularGeneration, boolean z) {
        Boolean bool = this.mIsInternetReachableOverride;
        if (bool != null) {
            z = bool.booleanValue();
        }
        boolean z2 = true;
        boolean z3 = connectionType != this.mConnectionType;
        boolean z4 = cellularGeneration != this.mCellularGeneration;
        if (z == this.mIsInternetReachable) {
            z2 = false;
        }
        if (z3 || z4 || z2) {
            this.mConnectionType = connectionType;
            this.mCellularGeneration = cellularGeneration;
            this.mIsInternetReachable = z;
            sendConnectivityChangedEvent();
        }
    }

    private void sendConnectivityChangedEvent() {
        ((RCTDeviceEventEmitter) getReactContext().getJSModule(RCTDeviceEventEmitter.class)).emit("netInfo.networkStatusDidChange", createConnectivityEventMap(null));
    }

    private WritableMap createConnectivityEventMap(@Nullable String str) {
        String str2;
        WritableMap createMap = Arguments.createMap();
        createMap.putBoolean("isWifiEnabled", this.mWifiManager.isWifiEnabled());
        if (str != null) {
            str2 = str;
        } else {
            str2 = this.mConnectionType.label;
        }
        createMap.putString(ReactVideoViewManager.PROP_SRC_TYPE, str2);
        boolean z = true;
        boolean z2 = !this.mConnectionType.equals(ConnectionType.NONE) && !this.mConnectionType.equals(ConnectionType.UNKNOWN);
        createMap.putBoolean("isConnected", z2);
        if (!this.mIsInternetReachable || (str != null && !str.equals(this.mConnectionType.label))) {
            z = false;
        }
        createMap.putBoolean("isInternetReachable", z);
        if (str == null) {
            str = this.mConnectionType.label;
        }
        WritableMap createDetailsMap = createDetailsMap(str);
        if (z2) {
            createDetailsMap.putBoolean("isConnectionExpensive", ConnectivityManagerCompat.isActiveNetworkMetered(getConnectivityManager()));
        }
        createMap.putMap("details", createDetailsMap);
        return createMap;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(8:15|16|(1:20)|21|22|23|24|(2:25|26)) */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0054 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0063 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0080 */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00e3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.facebook.react.bridge.WritableMap createDetailsMap(@javax.annotation.Nonnull java.lang.String r8) {
        /*
            r7 = this;
            com.facebook.react.bridge.WritableMap r0 = com.facebook.react.bridge.Arguments.createMap()
            int r1 = r8.hashCode()
            r2 = -916596374(0xffffffffc95dd96a, float:-908694.6)
            r3 = 0
            r4 = -1
            r5 = 1
            if (r1 == r2) goto L_0x0020
            r2 = 3649301(0x37af15, float:5.11376E-39)
            if (r1 == r2) goto L_0x0016
            goto L_0x002a
        L_0x0016:
            java.lang.String r1 = "wifi"
            boolean r8 = r8.equals(r1)
            if (r8 == 0) goto L_0x002a
            r8 = 1
            goto L_0x002b
        L_0x0020:
            java.lang.String r1 = "cellular"
            boolean r8 = r8.equals(r1)
            if (r8 == 0) goto L_0x002a
            r8 = 0
            goto L_0x002b
        L_0x002a:
            r8 = -1
        L_0x002b:
            if (r8 == 0) goto L_0x00e3
            if (r8 == r5) goto L_0x0031
            goto L_0x00fb
        L_0x0031:
            android.net.wifi.WifiManager r8 = r7.mWifiManager
            android.net.wifi.WifiInfo r8 = r8.getConnectionInfo()
            if (r8 == 0) goto L_0x00fb
            java.lang.String r1 = r8.getSSID()     // Catch:{ Exception -> 0x0054 }
            if (r1 == 0) goto L_0x0054
            java.lang.String r2 = "<unknown ssid>"
            boolean r2 = r1.contains(r2)     // Catch:{ Exception -> 0x0054 }
            if (r2 != 0) goto L_0x0054
            java.lang.String r2 = "\""
            java.lang.String r6 = ""
            java.lang.String r1 = r1.replace(r2, r6)     // Catch:{ Exception -> 0x0054 }
            java.lang.String r2 = "ssid"
            r0.putString(r2, r1)     // Catch:{ Exception -> 0x0054 }
        L_0x0054:
            int r1 = r8.getRssi()     // Catch:{ Exception -> 0x0063 }
            r2 = 100
            int r1 = android.net.wifi.WifiManager.calculateSignalLevel(r1, r2)     // Catch:{ Exception -> 0x0063 }
            java.lang.String r2 = "strength"
            r0.putInt(r2, r1)     // Catch:{ Exception -> 0x0063 }
        L_0x0063:
            int r1 = r8.getIpAddress()     // Catch:{ Exception -> 0x0080 }
            long r1 = (long) r1     // Catch:{ Exception -> 0x0080 }
            java.math.BigInteger r1 = java.math.BigInteger.valueOf(r1)     // Catch:{ Exception -> 0x0080 }
            byte[] r1 = r1.toByteArray()     // Catch:{ Exception -> 0x0080 }
            com.reactnativecommunity.netinfo.NetInfoUtils.reverseByteArray(r1)     // Catch:{ Exception -> 0x0080 }
            java.net.InetAddress r1 = java.net.InetAddress.getByAddress(r1)     // Catch:{ Exception -> 0x0080 }
            java.lang.String r1 = r1.getHostAddress()     // Catch:{ Exception -> 0x0080 }
            java.lang.String r2 = "ipAddress"
            r0.putString(r2, r1)     // Catch:{ Exception -> 0x0080 }
        L_0x0080:
            int r8 = r8.getIpAddress()     // Catch:{ Exception -> 0x00fb }
            long r1 = (long) r8     // Catch:{ Exception -> 0x00fb }
            java.math.BigInteger r8 = java.math.BigInteger.valueOf(r1)     // Catch:{ Exception -> 0x00fb }
            byte[] r8 = r8.toByteArray()     // Catch:{ Exception -> 0x00fb }
            com.reactnativecommunity.netinfo.NetInfoUtils.reverseByteArray(r8)     // Catch:{ Exception -> 0x00fb }
            java.net.InetAddress r8 = java.net.InetAddress.getByAddress(r8)     // Catch:{ Exception -> 0x00fb }
            java.net.NetworkInterface r8 = java.net.NetworkInterface.getByInetAddress(r8)     // Catch:{ Exception -> 0x00fb }
            java.util.List r8 = r8.getInterfaceAddresses()     // Catch:{ Exception -> 0x00fb }
            java.lang.Object r8 = r8.get(r5)     // Catch:{ Exception -> 0x00fb }
            java.net.InterfaceAddress r8 = (java.net.InterfaceAddress) r8     // Catch:{ Exception -> 0x00fb }
            short r8 = r8.getNetworkPrefixLength()     // Catch:{ Exception -> 0x00fb }
            int r8 = 32 - r8
            int r8 = r4 << r8
            java.util.Locale r1 = java.util.Locale.US     // Catch:{ Exception -> 0x00fb }
            java.lang.String r2 = "%d.%d.%d.%d"
            r4 = 4
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x00fb }
            int r6 = r8 >> 24
            r6 = r6 & 255(0xff, float:3.57E-43)
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Exception -> 0x00fb }
            r4[r3] = r6     // Catch:{ Exception -> 0x00fb }
            int r3 = r8 >> 16
            r3 = r3 & 255(0xff, float:3.57E-43)
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Exception -> 0x00fb }
            r4[r5] = r3     // Catch:{ Exception -> 0x00fb }
            r3 = 2
            int r5 = r8 >> 8
            r5 = r5 & 255(0xff, float:3.57E-43)
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Exception -> 0x00fb }
            r4[r3] = r5     // Catch:{ Exception -> 0x00fb }
            r3 = 3
            r8 = r8 & 255(0xff, float:3.57E-43)
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ Exception -> 0x00fb }
            r4[r3] = r8     // Catch:{ Exception -> 0x00fb }
            java.lang.String r8 = java.lang.String.format(r1, r2, r4)     // Catch:{ Exception -> 0x00fb }
            java.lang.String r1 = "subnet"
            r0.putString(r1, r8)     // Catch:{ Exception -> 0x00fb }
            goto L_0x00fb
        L_0x00e3:
            com.reactnativecommunity.netinfo.types.CellularGeneration r8 = r7.mCellularGeneration
            if (r8 == 0) goto L_0x00ee
            java.lang.String r8 = r8.label
            java.lang.String r1 = "cellularGeneration"
            r0.putString(r1, r8)
        L_0x00ee:
            android.telephony.TelephonyManager r8 = r7.mTelephonyManager
            java.lang.String r8 = r8.getNetworkOperatorName()
            if (r8 == 0) goto L_0x00fb
            java.lang.String r1 = "carrier"
            r0.putString(r1, r8)
        L_0x00fb:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.reactnativecommunity.netinfo.ConnectivityReceiver.createDetailsMap(java.lang.String):com.facebook.react.bridge.WritableMap");
    }
}
