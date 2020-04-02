package com.airbnb.android.react.maps;

import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import java.util.Map;
import javax.annotation.Nullable;

public class AirMapOverlayManager extends ViewGroupManager<AirMapOverlay> {
    private final DisplayMetrics metrics;

    public String getName() {
        return "AIRMapOverlay";
    }

    public AirMapOverlayManager(ReactApplicationContext reactApplicationContext) {
        if (VERSION.SDK_INT >= 17) {
            this.metrics = new DisplayMetrics();
            ((WindowManager) reactApplicationContext.getSystemService("window")).getDefaultDisplay().getRealMetrics(this.metrics);
            return;
        }
        this.metrics = reactApplicationContext.getResources().getDisplayMetrics();
    }

    public AirMapOverlay createViewInstance(ThemedReactContext themedReactContext) {
        return new AirMapOverlay(themedReactContext);
    }

    @ReactProp(name = "bounds")
    public void setBounds(AirMapOverlay airMapOverlay, ReadableArray readableArray) {
        airMapOverlay.setBounds(readableArray);
    }

    @ReactProp(defaultFloat = 1.0f, name = "zIndex")
    public void setZIndex(AirMapOverlay airMapOverlay, float f) {
        airMapOverlay.setZIndex(f);
    }

    @ReactProp(name = "image")
    public void setImage(AirMapOverlay airMapOverlay, @Nullable String str) {
        airMapOverlay.setImage(str);
    }

    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        String str = "onPress";
        return MapBuilder.m125of(str, MapBuilder.m125of("registrationName", str));
    }
}
