package com.facebook.common.logging;

public interface LoggingDelegate {
    /* renamed from: d */
    void mo7484d(String str, String str2);

    /* renamed from: d */
    void mo7485d(String str, String str2, Throwable th);

    /* renamed from: e */
    void mo7486e(String str, String str2);

    /* renamed from: e */
    void mo7487e(String str, String str2, Throwable th);

    int getMinimumLoggingLevel();

    /* renamed from: i */
    void mo7489i(String str, String str2);

    /* renamed from: i */
    void mo7490i(String str, String str2, Throwable th);

    boolean isLoggable(int i);

    void log(int i, String str, String str2);

    void setMinimumLoggingLevel(int i);

    /* renamed from: v */
    void mo7495v(String str, String str2);

    /* renamed from: v */
    void mo7496v(String str, String str2, Throwable th);

    /* renamed from: w */
    void mo7497w(String str, String str2);

    /* renamed from: w */
    void mo7498w(String str, String str2, Throwable th);

    void wtf(String str, String str2);

    void wtf(String str, String str2, Throwable th);
}
