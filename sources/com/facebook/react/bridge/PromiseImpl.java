package com.facebook.react.bridge;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PromiseImpl implements Promise {
    private static final String ERROR_DEFAULT_CODE = "EUNSPECIFIED";
    private static final String ERROR_DEFAULT_MESSAGE = "Error not specified.";
    private static final String ERROR_MAP_KEY_CODE = "code";
    private static final String ERROR_MAP_KEY_MESSAGE = "message";
    private static final String ERROR_MAP_KEY_NATIVE_STACK = "nativeStackAndroid";
    private static final String ERROR_MAP_KEY_USER_INFO = "userInfo";
    private static final int ERROR_STACK_FRAME_LIMIT = 50;
    private static final String STACK_FRAME_KEY_CLASS = "class";
    private static final String STACK_FRAME_KEY_FILE = "file";
    private static final String STACK_FRAME_KEY_LINE_NUMBER = "lineNumber";
    private static final String STACK_FRAME_KEY_METHOD_NAME = "methodName";
    @Nullable
    private Callback mReject;
    @Nullable
    private Callback mResolve;

    public PromiseImpl(@Nullable Callback callback, @Nullable Callback callback2) {
        this.mResolve = callback;
        this.mReject = callback2;
    }

    public void resolve(Object obj) {
        Callback callback = this.mResolve;
        if (callback != null) {
            callback.invoke(obj);
            this.mResolve = null;
            this.mReject = null;
        }
    }

    public void reject(String str, String str2) {
        reject(str, str2, null, null);
    }

    public void reject(String str, Throwable th) {
        reject(str, null, th, null);
    }

    public void reject(String str, String str2, Throwable th) {
        reject(str, str2, th, null);
    }

    public void reject(Throwable th) {
        reject(null, null, th, null);
    }

    public void reject(Throwable th, WritableMap writableMap) {
        reject(null, null, th, writableMap);
    }

    public void reject(String str, @NonNull WritableMap writableMap) {
        reject(str, null, null, writableMap);
    }

    public void reject(String str, Throwable th, WritableMap writableMap) {
        reject(str, null, th, writableMap);
    }

    public void reject(String str, String str2, @NonNull WritableMap writableMap) {
        reject(str, str2, null, writableMap);
    }

    public void reject(@Nullable String str, @Nullable String str2, @Nullable Throwable th, @Nullable WritableMap writableMap) {
        if (this.mReject == null) {
            this.mResolve = null;
            return;
        }
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        String str3 = ERROR_MAP_KEY_CODE;
        if (str == null) {
            writableNativeMap.putString(str3, ERROR_DEFAULT_CODE);
        } else {
            writableNativeMap.putString(str3, str);
        }
        String str4 = ERROR_MAP_KEY_MESSAGE;
        if (str2 != null) {
            writableNativeMap.putString(str4, str2);
        } else if (th != null) {
            writableNativeMap.putString(str4, th.getMessage());
        } else {
            writableNativeMap.putString(str4, ERROR_DEFAULT_MESSAGE);
        }
        String str5 = ERROR_MAP_KEY_USER_INFO;
        if (writableMap != null) {
            writableNativeMap.putMap(str5, writableMap);
        } else {
            writableNativeMap.putNull(str5);
        }
        String str6 = ERROR_MAP_KEY_NATIVE_STACK;
        if (th != null) {
            StackTraceElement[] stackTrace = th.getStackTrace();
            WritableNativeArray writableNativeArray = new WritableNativeArray();
            int i = 0;
            while (i < stackTrace.length && i < 50) {
                StackTraceElement stackTraceElement = stackTrace[i];
                WritableNativeMap writableNativeMap2 = new WritableNativeMap();
                writableNativeMap2.putString(STACK_FRAME_KEY_CLASS, stackTraceElement.getClassName());
                writableNativeMap2.putString("file", stackTraceElement.getFileName());
                writableNativeMap2.putInt("lineNumber", stackTraceElement.getLineNumber());
                writableNativeMap2.putString(STACK_FRAME_KEY_METHOD_NAME, stackTraceElement.getMethodName());
                writableNativeArray.pushMap(writableNativeMap2);
                i++;
            }
            writableNativeMap.putArray(str6, writableNativeArray);
        } else {
            writableNativeMap.putArray(str6, new WritableNativeArray());
        }
        this.mReject.invoke(writableNativeMap);
        this.mResolve = null;
        this.mReject = null;
    }

    @Deprecated
    public void reject(String str) {
        reject(null, str, null, null);
    }
}
