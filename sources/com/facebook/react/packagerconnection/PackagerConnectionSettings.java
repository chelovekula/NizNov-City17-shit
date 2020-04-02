package com.facebook.react.packagerconnection;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.facebook.common.logging.FLog;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.modules.systeminfo.AndroidInfoHelpers;

public class PackagerConnectionSettings {
    private static final String PREFS_DEBUG_SERVER_HOST_KEY = "debug_http_host";
    private static final String TAG = "PackagerConnectionSettings";
    private final Context mAppContext;
    private final String mPackageName;
    private final SharedPreferences mPreferences;

    public PackagerConnectionSettings(Context context) {
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.mPackageName = context.getPackageName();
        this.mAppContext = context;
    }

    public String getDebugServerHost() {
        String string = this.mPreferences.getString(PREFS_DEBUG_SERVER_HOST_KEY, null);
        if (!TextUtils.isEmpty(string)) {
            return (String) Assertions.assertNotNull(string);
        }
        String serverHost = AndroidInfoHelpers.getServerHost(this.mAppContext);
        if (serverHost.equals(AndroidInfoHelpers.DEVICE_LOCALHOST)) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("You seem to be running on device. Run '");
            sb.append(AndroidInfoHelpers.getAdbReverseTcpCommand(this.mAppContext));
            sb.append("' to forward the debug server's port to the device.");
            FLog.m90w(str, sb.toString());
        }
        return serverHost;
    }

    public void setDebugServerHost(String str) {
        this.mPreferences.edit().putString(PREFS_DEBUG_SERVER_HOST_KEY, str).apply();
    }

    public String getInspectorServerHost() {
        return AndroidInfoHelpers.getInspectorProxyHost(this.mAppContext);
    }

    @Nullable
    public String getPackageName() {
        return this.mPackageName;
    }
}
