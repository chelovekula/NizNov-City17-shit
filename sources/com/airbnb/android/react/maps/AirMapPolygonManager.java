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

public class AirMapPolygonManager extends ViewGroupManager<AirMapPolygon> {
    private final DisplayMetrics metrics;

    public String getName() {
        return "AIRMapPolygon";
    }

    public AirMapPolygonManager(ReactApplicationContext reactApplicationContext) {
        if (VERSION.SDK_INT >= 17) {
            this.metrics = new DisplayMetrics();
            ((WindowManager) reactApplicationContext.getSystemService("window")).getDefaultDisplay().getRealMetrics(this.metrics);
            return;
        }
        this.metrics = reactApplicationContext.getResources().getDisplayMetrics();
    }

    public AirMapPolygon createViewInstance(ThemedReactContext themedReactContext) {
        return new AirMapPolygon(themedReactContext);
    }

    @ReactProp(name = "coordinates")
    public void setCoordinate(AirMapPolygon airMapPolygon, ReadableArray readableArray) {
        airMapPolygon.setCoordinates(readableArray);
    }

    @ReactProp(name = "holes")
    public void setHoles(AirMapPolygon airMapPolygon, ReadableArray readableArray) {
        airMapPolygon.setHoles(readableArray);
    }

    @ReactProp(defaultFloat = 1.0f, name = "strokeWidth")
    public void setStrokeWidth(AirMapPolygon airMapPolygon, float f) {
        airMapPolygon.setStrokeWidth(this.metrics.density * f);
    }

    @ReactProp(customType = "Color", defaultInt = -65536, name = "fillColor")
    public void setFillColor(AirMapPolygon airMapPolygon, int i) {
        airMapPolygon.setFillColor(i);
    }

    @ReactProp(customType = "Color", defaultInt = -65536, name = "strokeColor")
    public void setStrokeColor(AirMapPolygon airMapPolygon, int i) {
        airMapPolygon.setStrokeColor(i);
    }

    @ReactProp(defaultBoolean = false, name = "tappable")
    public void setTappable(AirMapPolygon airMapPolygon, boolean z) {
        airMapPolygon.setTappable(z);
    }

    @ReactProp(defaultBoolean = false, name = "geodesic")
    public void setGeodesic(AirMapPolygon airMapPolygon, boolean z) {
        airMapPolygon.setGeodesic(z);
    }

    @ReactProp(defaultFloat = 1.0f, name = "zIndex")
    public void setZIndex(AirMapPolygon airMapPolygon, float f) {
        airMapPolygon.setZIndex(f);
    }

    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        String str = "onPress";
        return MapBuilder.m125of(str, MapBuilder.m125of("registrationName", str));
    }
}
