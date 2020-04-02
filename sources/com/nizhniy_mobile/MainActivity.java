package com.nizhniy_mobile;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactRootView;
import com.swmansion.gesturehandler.react.RNGestureHandlerEnabledRootView;
import org.devio.p010rn.splashscreen.SplashScreen;

public class MainActivity extends ReactActivity {
    /* access modifiers changed from: protected */
    public String getMainComponentName() {
        return "nizhniy_mobile";
    }

    public void onCreate(@Nullable Bundle bundle) {
        SplashScreen.show(this);
        super.onCreate(bundle);
        Log.e("PEPEGA", VERSION.CODENAME);
        if (VERSION.SDK_INT != 26) {
            setRequestedOrientation(1);
        }
    }

    /* access modifiers changed from: protected */
    public ReactActivityDelegate createReactActivityDelegate() {
        return new ReactActivityDelegate(this, getMainComponentName()) {
            /* access modifiers changed from: protected */
            public ReactRootView createRootView() {
                return new RNGestureHandlerEnabledRootView(MainActivity.this);
            }
        };
    }
}
