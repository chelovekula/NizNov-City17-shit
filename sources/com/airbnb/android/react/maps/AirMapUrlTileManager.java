package com.airbnb.android.react.maps;

import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

public class AirMapUrlTileManager extends ViewGroupManager<AirMapUrlTile> {
    private DisplayMetrics metrics;

    public String getName() {
        return "AIRMapUrlTile";
    }

    public AirMapUrlTileManager(ReactApplicationContext reactApplicationContext) {
        if (VERSION.SDK_INT >= 17) {
            this.metrics = new DisplayMetrics();
            ((WindowManager) reactApplicationContext.getSystemService("window")).getDefaultDisplay().getRealMetrics(this.metrics);
            return;
        }
        this.metrics = reactApplicationContext.getResources().getDisplayMetrics();
    }

    public AirMapUrlTile createViewInstance(ThemedReactContext themedReactContext) {
        return new AirMapUrlTile(themedReactContext);
    }

    @ReactProp(name = "urlTemplate")
    public void setUrlTemplate(AirMapUrlTile airMapUrlTile, String str) {
        airMapUrlTile.setUrlTemplate(str);
    }

    @ReactProp(defaultFloat = -1.0f, name = "zIndex")
    public void setZIndex(AirMapUrlTile airMapUrlTile, float f) {
        airMapUrlTile.setZIndex(f);
    }

    @ReactProp(defaultFloat = 0.0f, name = "minimumZ")
    public void setMinimumZ(AirMapUrlTile airMapUrlTile, float f) {
        airMapUrlTile.setMinimumZ(f);
    }

    @ReactProp(defaultFloat = 100.0f, name = "maximumZ")
    public void setMaximumZ(AirMapUrlTile airMapUrlTile, float f) {
        airMapUrlTile.setMaximumZ(f);
    }

    @ReactProp(defaultBoolean = false, name = "flipY")
    public void setFlipY(AirMapUrlTile airMapUrlTile, boolean z) {
        airMapUrlTile.setFlipY(z);
    }
}
