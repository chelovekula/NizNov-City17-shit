package com.airbnb.android.react.maps;

import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

public class AirMapLocalTileManager extends ViewGroupManager<AirMapLocalTile> {
    private DisplayMetrics metrics;

    public String getName() {
        return "AIRMapLocalTile";
    }

    public AirMapLocalTileManager(ReactApplicationContext reactApplicationContext) {
        if (VERSION.SDK_INT >= 17) {
            this.metrics = new DisplayMetrics();
            ((WindowManager) reactApplicationContext.getSystemService("window")).getDefaultDisplay().getRealMetrics(this.metrics);
            return;
        }
        this.metrics = reactApplicationContext.getResources().getDisplayMetrics();
    }

    public AirMapLocalTile createViewInstance(ThemedReactContext themedReactContext) {
        return new AirMapLocalTile(themedReactContext);
    }

    @ReactProp(name = "pathTemplate")
    public void setPathTemplate(AirMapLocalTile airMapLocalTile, String str) {
        airMapLocalTile.setPathTemplate(str);
    }

    @ReactProp(defaultFloat = 256.0f, name = "tileSize")
    public void setTileSize(AirMapLocalTile airMapLocalTile, float f) {
        airMapLocalTile.setTileSize(f);
    }

    @ReactProp(defaultFloat = -1.0f, name = "zIndex")
    public void setZIndex(AirMapLocalTile airMapLocalTile, float f) {
        airMapLocalTile.setZIndex(f);
    }
}
