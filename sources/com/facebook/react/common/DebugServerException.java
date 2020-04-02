package com.facebook.react.common;

import android.net.Uri;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.facebook.common.logging.FLog;
import com.facebook.react.devsupport.StackTraceHelper;
import org.json.JSONException;
import org.json.JSONObject;

public class DebugServerException extends RuntimeException {
    private static final String GENERIC_ERROR_MESSAGE = "\n\nTry the following to fix the issue:\n• Ensure that the packager server is running\n• Ensure that your device/emulator is connected to your machine and has USB debugging enabled - run 'adb devices' to see a list of connected devices\n• Ensure Airplane Mode is disabled\n• If you're on a physical device connected to the same machine, run 'adb reverse tcp:<PORT> tcp:<PORT>' to forward requests from your device\n• If your device is on the same Wi-Fi network, set 'Debug server host & port for device' in 'Dev settings' to your machine's IP address and the port of the local dev server - e.g. 10.0.1.1:<PORT>\n\n";

    public static DebugServerException makeGeneric(String str, String str2, Throwable th) {
        return makeGeneric(str, str2, "", th);
    }

    public static DebugServerException makeGeneric(String str, String str2, String str3, Throwable th) {
        String replace = GENERIC_ERROR_MESSAGE.replace("<PORT>", String.valueOf(Uri.parse(str).getPort()));
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(replace);
        sb.append(str3);
        return new DebugServerException(sb.toString(), th);
    }

    private DebugServerException(String str, String str2, int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("\n  at ");
        sb.append(str2);
        String str3 = ":";
        sb.append(str3);
        sb.append(i);
        sb.append(str3);
        sb.append(i2);
        super(sb.toString());
    }

    public DebugServerException(String str) {
        super(str);
    }

    public DebugServerException(String str, Throwable th) {
        super(str, th);
    }

    @Nullable
    public static DebugServerException parse(String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str2);
            return new DebugServerException(jSONObject.getString("message"), shortenFileName(jSONObject.getString("filename")), jSONObject.getInt(StackTraceHelper.LINE_NUMBER_KEY), jSONObject.getInt(StackTraceHelper.COLUMN_KEY));
        } catch (JSONException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Could not parse DebugServerException from: ");
            sb.append(str2);
            FLog.m91w(ReactConstants.TAG, sb.toString(), (Throwable) e);
            return null;
        }
    }

    private static String shortenFileName(String str) {
        String[] split = str.split("/");
        return split[split.length - 1];
    }
}
