package com.google.android.gms.common.api.internal;

import android.app.Activity;
import com.google.android.gms.common.annotation.KeepForSdk;

@KeepForSdk
public abstract class ActivityLifecycleObserver {
    @KeepForSdk
    public abstract ActivityLifecycleObserver onStopCallOnce(Runnable runnable);

    @KeepForSdk
    /* renamed from: of */
    public static final ActivityLifecycleObserver m132of(Activity activity) {
        return new zaa(activity);
    }
}
