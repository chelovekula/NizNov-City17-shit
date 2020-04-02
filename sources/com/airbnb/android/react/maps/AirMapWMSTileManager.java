package com.airbnb.android.react.maps;

import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

public class AirMapWMSTileManager extends ViewGroupManager<AirMapWMSTile> {
    private DisplayMetrics metrics;

    public String getName() {
        return "AIRMapWMSTile";
    }

    public AirMapWMSTileManager(ReactApplicationContext reactApplicationContext) {
        if (VERSION.SDK_INT >= 17) {
            this.metrics = new DisplayMetrics();
            ((WindowManager) reactApplicationContext.getSystemService("window")).getDefaultDisplay().getRealMetrics(this.metrics);
            return;
        }
        this.metrics = reactApplicationContext.getResources().getDisplayMetrics();
    }

    public AirMapWMSTile createViewInstance(ThemedReactContext themedReactContext) {
        return new AirMapWMSTile(themedReactContext);
    }

    @ReactProp(name = "urlTemplate")
    public void setUrlTemplate(AirMapWMSTile airMapWMSTile, String str) {
        airMapWMSTile.setUrlTemplate(str);
    }

    @ReactProp(defaultFloat = -1.0f, name = "zIndex")
    public void setZIndex(AirMapWMSTile airMapWMSTile, float f) {
        airMapWMSTile.setZIndex(f);
    }

    @ReactProp(defaultFloat = 0.0f, name = "minimumZ")
    public void setMinimumZ(AirMapWMSTile airMapWMSTile, float f) {
        airMapWMSTile.setMinimumZ(f);
    }

    @ReactProp(defaultFloat = 100.0f, name = "maximumZ")
    public void setMaximumZ(AirMapWMSTile airMapWMSTile, float f) {
        airMapWMSTile.setMaximumZ(f);
    }

    @ReactProp(defaultInt = 512, name = "tileSize")
    public void setTileSize(AirMapWMSTile airMapWMSTile, int i) {
        airMapWMSTile.setTileSize(i);
    }

    @ReactProp(defaultFloat = 1.0f, name = "opacity")
    public void setOpacity(AirMapWMSTile airMapWMSTile, float f) {
        airMapWMSTile.setOpacity(f);
    }
}
