package com.facebook.common.logging;

public class FLog {
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    private static LoggingDelegate sHandler = FLogDefaultLoggingDelegate.getInstance();

    public static void setLoggingDelegate(LoggingDelegate loggingDelegate) {
        if (loggingDelegate != null) {
            sHandler = loggingDelegate;
            return;
        }
        throw new IllegalArgumentException();
    }

    public static boolean isLoggable(int i) {
        return sHandler.isLoggable(i);
    }

    public static void setMinimumLoggingLevel(int i) {
        sHandler.setMinimumLoggingLevel(i);
    }

    public static int getMinimumLoggingLevel() {
        return sHandler.getMinimumLoggingLevel();
    }

    /* renamed from: v */
    public static void m78v(String str, String str2) {
        if (sHandler.isLoggable(2)) {
            sHandler.mo7495v(str, str2);
        }
    }

    /* renamed from: v */
    public static void m79v(String str, String str2, Object obj) {
        if (sHandler.isLoggable(2)) {
            sHandler.mo7495v(str, formatString(str2, obj));
        }
    }

    /* renamed from: v */
    public static void m80v(String str, String str2, Object obj, Object obj2) {
        if (sHandler.isLoggable(2)) {
            sHandler.mo7495v(str, formatString(str2, obj, obj2));
        }
    }

    /* renamed from: v */
    public static void m81v(String str, String str2, Object obj, Object obj2, Object obj3) {
        if (sHandler.isLoggable(2)) {
            sHandler.mo7495v(str, formatString(str2, obj, obj2, obj3));
        }
    }

    /* renamed from: v */
    public static void m82v(String str, String str2, Object obj, Object obj2, Object obj3, Object obj4) {
        if (sHandler.isLoggable(2)) {
            sHandler.mo7495v(str, formatString(str2, obj, obj2, obj3, obj4));
        }
    }

    /* renamed from: v */
    public static void m70v(Class<?> cls, String str) {
        if (sHandler.isLoggable(2)) {
            sHandler.mo7495v(getTag(cls), str);
        }
    }

    /* renamed from: v */
    public static void m71v(Class<?> cls, String str, Object obj) {
        if (sHandler.isLoggable(2)) {
            sHandler.mo7495v(getTag(cls), formatString(str, obj));
        }
    }

    /* renamed from: v */
    public static void m72v(Class<?> cls, String str, Object obj, Object obj2) {
        if (sHandler.isLoggable(2)) {
            sHandler.mo7495v(getTag(cls), formatString(str, obj, obj2));
        }
    }

    /* renamed from: v */
    public static void m73v(Class<?> cls, String str, Object obj, Object obj2, Object obj3) {
        if (isLoggable(2)) {
            m70v(cls, formatString(str, obj, obj2, obj3));
        }
    }

    /* renamed from: v */
    public static void m74v(Class<?> cls, String str, Object obj, Object obj2, Object obj3, Object obj4) {
        if (sHandler.isLoggable(2)) {
            sHandler.mo7495v(getTag(cls), formatString(str, obj, obj2, obj3, obj4));
        }
    }

    /* renamed from: v */
    public static void m84v(String str, String str2, Object... objArr) {
        if (sHandler.isLoggable(2)) {
            sHandler.mo7495v(str, formatString(str2, objArr));
        }
    }

    /* renamed from: v */
    public static void m85v(String str, Throwable th, String str2, Object... objArr) {
        if (sHandler.isLoggable(2)) {
            sHandler.mo7496v(str, formatString(str2, objArr), th);
        }
    }

    /* renamed from: v */
    public static void m76v(Class<?> cls, String str, Object... objArr) {
        if (sHandler.isLoggable(2)) {
            sHandler.mo7495v(getTag(cls), formatString(str, objArr));
        }
    }

    /* renamed from: v */
    public static void m77v(Class<?> cls, Throwable th, String str, Object... objArr) {
        if (sHandler.isLoggable(2)) {
            sHandler.mo7496v(getTag(cls), formatString(str, objArr), th);
        }
    }

    /* renamed from: v */
    public static void m83v(String str, String str2, Throwable th) {
        if (sHandler.isLoggable(2)) {
            sHandler.mo7496v(str, str2, th);
        }
    }

    /* renamed from: v */
    public static void m75v(Class<?> cls, String str, Throwable th) {
        if (sHandler.isLoggable(2)) {
            sHandler.mo7496v(getTag(cls), str, th);
        }
    }

    /* renamed from: d */
    public static void m38d(String str, String str2) {
        if (sHandler.isLoggable(3)) {
            sHandler.mo7484d(str, str2);
        }
    }

    /* renamed from: d */
    public static void m39d(String str, String str2, Object obj) {
        if (sHandler.isLoggable(3)) {
            sHandler.mo7484d(str, formatString(str2, obj));
        }
    }

    /* renamed from: d */
    public static void m40d(String str, String str2, Object obj, Object obj2) {
        if (sHandler.isLoggable(3)) {
            sHandler.mo7484d(str, formatString(str2, obj, obj2));
        }
    }

    /* renamed from: d */
    public static void m41d(String str, String str2, Object obj, Object obj2, Object obj3) {
        if (sHandler.isLoggable(3)) {
            sHandler.mo7484d(str, formatString(str2, obj, obj2, obj3));
        }
    }

    /* renamed from: d */
    public static void m42d(String str, String str2, Object obj, Object obj2, Object obj3, Object obj4) {
        if (sHandler.isLoggable(3)) {
            sHandler.mo7484d(str, formatString(str2, obj, obj2, obj3, obj4));
        }
    }

    /* renamed from: d */
    public static void m30d(Class<?> cls, String str) {
        if (sHandler.isLoggable(3)) {
            sHandler.mo7484d(getTag(cls), str);
        }
    }

    /* renamed from: d */
    public static void m31d(Class<?> cls, String str, Object obj) {
        if (sHandler.isLoggable(3)) {
            sHandler.mo7484d(getTag(cls), formatString(str, obj));
        }
    }

    /* renamed from: d */
    public static void m32d(Class<?> cls, String str, Object obj, Object obj2) {
        if (sHandler.isLoggable(3)) {
            sHandler.mo7484d(getTag(cls), formatString(str, obj, obj2));
        }
    }

    /* renamed from: d */
    public static void m33d(Class<?> cls, String str, Object obj, Object obj2, Object obj3) {
        if (sHandler.isLoggable(3)) {
            sHandler.mo7484d(getTag(cls), formatString(str, obj, obj2, obj3));
        }
    }

    /* renamed from: d */
    public static void m34d(Class<?> cls, String str, Object obj, Object obj2, Object obj3, Object obj4) {
        if (sHandler.isLoggable(3)) {
            sHandler.mo7484d(getTag(cls), formatString(str, obj, obj2, obj3, obj4));
        }
    }

    /* renamed from: d */
    public static void m44d(String str, String str2, Object... objArr) {
        if (sHandler.isLoggable(3)) {
            m38d(str, formatString(str2, objArr));
        }
    }

    /* renamed from: d */
    public static void m45d(String str, Throwable th, String str2, Object... objArr) {
        if (sHandler.isLoggable(3)) {
            m43d(str, formatString(str2, objArr), th);
        }
    }

    /* renamed from: d */
    public static void m36d(Class<?> cls, String str, Object... objArr) {
        if (sHandler.isLoggable(3)) {
            sHandler.mo7484d(getTag(cls), formatString(str, objArr));
        }
    }

    /* renamed from: d */
    public static void m37d(Class<?> cls, Throwable th, String str, Object... objArr) {
        if (sHandler.isLoggable(3)) {
            sHandler.mo7485d(getTag(cls), formatString(str, objArr), th);
        }
    }

    /* renamed from: d */
    public static void m43d(String str, String str2, Throwable th) {
        if (sHandler.isLoggable(3)) {
            sHandler.mo7485d(str, str2, th);
        }
    }

    /* renamed from: d */
    public static void m35d(Class<?> cls, String str, Throwable th) {
        if (sHandler.isLoggable(3)) {
            sHandler.mo7485d(getTag(cls), str, th);
        }
    }

    /* renamed from: i */
    public static void m62i(String str, String str2) {
        if (sHandler.isLoggable(4)) {
            sHandler.mo7489i(str, str2);
        }
    }

    /* renamed from: i */
    public static void m63i(String str, String str2, Object obj) {
        if (sHandler.isLoggable(4)) {
            sHandler.mo7489i(str, formatString(str2, obj));
        }
    }

    /* renamed from: i */
    public static void m64i(String str, String str2, Object obj, Object obj2) {
        if (sHandler.isLoggable(4)) {
            sHandler.mo7489i(str, formatString(str2, obj, obj2));
        }
    }

    /* renamed from: i */
    public static void m65i(String str, String str2, Object obj, Object obj2, Object obj3) {
        if (sHandler.isLoggable(4)) {
            sHandler.mo7489i(str, formatString(str2, obj, obj2, obj3));
        }
    }

    /* renamed from: i */
    public static void m66i(String str, String str2, Object obj, Object obj2, Object obj3, Object obj4) {
        if (sHandler.isLoggable(4)) {
            sHandler.mo7489i(str, formatString(str2, obj, obj2, obj3, obj4));
        }
    }

    /* renamed from: i */
    public static void m54i(Class<?> cls, String str) {
        if (sHandler.isLoggable(4)) {
            sHandler.mo7489i(getTag(cls), str);
        }
    }

    /* renamed from: i */
    public static void m55i(Class<?> cls, String str, Object obj) {
        if (sHandler.isLoggable(4)) {
            sHandler.mo7489i(getTag(cls), formatString(str, obj));
        }
    }

    /* renamed from: i */
    public static void m56i(Class<?> cls, String str, Object obj, Object obj2) {
        if (sHandler.isLoggable(4)) {
            sHandler.mo7489i(getTag(cls), formatString(str, obj, obj2));
        }
    }

    /* renamed from: i */
    public static void m57i(Class<?> cls, String str, Object obj, Object obj2, Object obj3) {
        if (sHandler.isLoggable(4)) {
            sHandler.mo7489i(getTag(cls), formatString(str, obj, obj2, obj3));
        }
    }

    /* renamed from: i */
    public static void m58i(Class<?> cls, String str, Object obj, Object obj2, Object obj3, Object obj4) {
        if (sHandler.isLoggable(4)) {
            sHandler.mo7489i(getTag(cls), formatString(str, obj, obj2, obj3, obj4));
        }
    }

    /* renamed from: i */
    public static void m68i(String str, String str2, Object... objArr) {
        if (sHandler.isLoggable(4)) {
            sHandler.mo7489i(str, formatString(str2, objArr));
        }
    }

    /* renamed from: i */
    public static void m69i(String str, Throwable th, String str2, Object... objArr) {
        if (sHandler.isLoggable(4)) {
            sHandler.mo7490i(str, formatString(str2, objArr), th);
        }
    }

    /* renamed from: i */
    public static void m60i(Class<?> cls, String str, Object... objArr) {
        if (sHandler.isLoggable(4)) {
            sHandler.mo7489i(getTag(cls), formatString(str, objArr));
        }
    }

    /* renamed from: i */
    public static void m61i(Class<?> cls, Throwable th, String str, Object... objArr) {
        if (isLoggable(4)) {
            sHandler.mo7490i(getTag(cls), formatString(str, objArr), th);
        }
    }

    /* renamed from: i */
    public static void m67i(String str, String str2, Throwable th) {
        if (sHandler.isLoggable(4)) {
            sHandler.mo7490i(str, str2, th);
        }
    }

    /* renamed from: i */
    public static void m59i(Class<?> cls, String str, Throwable th) {
        if (sHandler.isLoggable(4)) {
            sHandler.mo7490i(getTag(cls), str, th);
        }
    }

    /* renamed from: w */
    public static void m90w(String str, String str2) {
        if (sHandler.isLoggable(5)) {
            sHandler.mo7497w(str, str2);
        }
    }

    /* renamed from: w */
    public static void m86w(Class<?> cls, String str) {
        if (sHandler.isLoggable(5)) {
            sHandler.mo7497w(getTag(cls), str);
        }
    }

    /* renamed from: w */
    public static void m92w(String str, String str2, Object... objArr) {
        if (sHandler.isLoggable(5)) {
            sHandler.mo7497w(str, formatString(str2, objArr));
        }
    }

    /* renamed from: w */
    public static void m93w(String str, Throwable th, String str2, Object... objArr) {
        if (sHandler.isLoggable(5)) {
            sHandler.mo7498w(str, formatString(str2, objArr), th);
        }
    }

    /* renamed from: w */
    public static void m88w(Class<?> cls, String str, Object... objArr) {
        if (sHandler.isLoggable(5)) {
            sHandler.mo7497w(getTag(cls), formatString(str, objArr));
        }
    }

    /* renamed from: w */
    public static void m89w(Class<?> cls, Throwable th, String str, Object... objArr) {
        if (isLoggable(5)) {
            m87w(cls, formatString(str, objArr), th);
        }
    }

    /* renamed from: w */
    public static void m91w(String str, String str2, Throwable th) {
        if (sHandler.isLoggable(5)) {
            sHandler.mo7498w(str, str2, th);
        }
    }

    /* renamed from: w */
    public static void m87w(Class<?> cls, String str, Throwable th) {
        if (sHandler.isLoggable(5)) {
            sHandler.mo7498w(getTag(cls), str, th);
        }
    }

    /* renamed from: e */
    public static void m50e(String str, String str2) {
        if (sHandler.isLoggable(6)) {
            sHandler.mo7486e(str, str2);
        }
    }

    /* renamed from: e */
    public static void m46e(Class<?> cls, String str) {
        if (sHandler.isLoggable(6)) {
            sHandler.mo7486e(getTag(cls), str);
        }
    }

    /* renamed from: e */
    public static void m52e(String str, String str2, Object... objArr) {
        if (sHandler.isLoggable(6)) {
            sHandler.mo7486e(str, formatString(str2, objArr));
        }
    }

    /* renamed from: e */
    public static void m53e(String str, Throwable th, String str2, Object... objArr) {
        if (sHandler.isLoggable(6)) {
            sHandler.mo7487e(str, formatString(str2, objArr), th);
        }
    }

    /* renamed from: e */
    public static void m48e(Class<?> cls, String str, Object... objArr) {
        if (sHandler.isLoggable(6)) {
            sHandler.mo7486e(getTag(cls), formatString(str, objArr));
        }
    }

    /* renamed from: e */
    public static void m49e(Class<?> cls, Throwable th, String str, Object... objArr) {
        if (sHandler.isLoggable(6)) {
            sHandler.mo7487e(getTag(cls), formatString(str, objArr), th);
        }
    }

    /* renamed from: e */
    public static void m51e(String str, String str2, Throwable th) {
        if (sHandler.isLoggable(6)) {
            sHandler.mo7487e(str, str2, th);
        }
    }

    /* renamed from: e */
    public static void m47e(Class<?> cls, String str, Throwable th) {
        if (sHandler.isLoggable(6)) {
            sHandler.mo7487e(getTag(cls), str, th);
        }
    }

    public static void wtf(String str, String str2) {
        if (sHandler.isLoggable(6)) {
            sHandler.wtf(str, str2);
        }
    }

    public static void wtf(Class<?> cls, String str) {
        if (sHandler.isLoggable(6)) {
            sHandler.wtf(getTag(cls), str);
        }
    }

    public static void wtf(String str, String str2, Object... objArr) {
        if (sHandler.isLoggable(6)) {
            sHandler.wtf(str, formatString(str2, objArr));
        }
    }

    public static void wtf(String str, Throwable th, String str2, Object... objArr) {
        if (sHandler.isLoggable(6)) {
            sHandler.wtf(str, formatString(str2, objArr), th);
        }
    }

    public static void wtf(Class<?> cls, String str, Object... objArr) {
        if (sHandler.isLoggable(6)) {
            sHandler.wtf(getTag(cls), formatString(str, objArr));
        }
    }

    public static void wtf(Class<?> cls, Throwable th, String str, Object... objArr) {
        if (sHandler.isLoggable(6)) {
            sHandler.wtf(getTag(cls), formatString(str, objArr), th);
        }
    }

    public static void wtf(String str, String str2, Throwable th) {
        if (sHandler.isLoggable(6)) {
            sHandler.wtf(str, str2, th);
        }
    }

    public static void wtf(Class<?> cls, String str, Throwable th) {
        if (sHandler.isLoggable(6)) {
            sHandler.wtf(getTag(cls), str, th);
        }
    }

    private static String formatString(String str, Object... objArr) {
        return String.format(null, str, objArr);
    }

    private static String getTag(Class<?> cls) {
        return cls.getSimpleName();
    }
}
