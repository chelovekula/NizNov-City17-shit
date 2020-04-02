package com.airbnb.android.react.maps;

import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.android.gms.maps.model.LatLng;

public class AirMapCircleManager extends ViewGroupManager<AirMapCircle> {
    private final DisplayMetrics metrics;

    public String getName() {
        return "AIRMapCircle";
    }

    public AirMapCircleManager(ReactApplicationContext reactApplicationContext) {
        if (VERSION.SDK_INT >= 17) {
            this.metrics = new DisplayMetrics();
            ((WindowManager) reactApplicationContext.getSystemService("window")).getDefaultDisplay().getRealMetrics(this.metrics);
            return;
        }
        this.metrics = reactApplicationContext.getResources().getDisplayMetrics();
    }

    public AirMapCircle createViewInstance(ThemedReactContext themedReactContext) {
        return new AirMapCircle(themedReactContext);
    }

    @ReactProp(name = "center")
    public void setCenter(AirMapCircle airMapCircle, ReadableMap readableMap) {
        airMapCircle.setCenter(new LatLng(readableMap.getDouble("latitude"), readableMap.getDouble("longitude")));
    }

    @ReactProp(defaultDouble = 0.0d, name = "radius")
    public void setRadius(AirMapCircle airMapCircle, double d) {
        airMapCircle.setRadius(d);
    }

    @ReactProp(defaultFloat = 1.0f, name = "strokeWidth")
    public void setStrokeWidth(AirMapCircle airMapCircle, float f) {
        airMapCircle.setStrokeWidth(this.metrics.density * f);
    }

    @ReactProp(customType = "Color", defaultInt = -65536, name = "fillColor")
    public void setFillColor(AirMapCircle airMapCircle, int i) {
        airMapCircle.setFillColor(i);
    }

    @ReactProp(customType = "Color", defaultInt = -65536, name = "strokeColor")
    public void setStrokeColor(AirMapCircle airMapCircle, int i) {
        airMapCircle.setStrokeColor(i);
    }

    @ReactProp(defaultFloat = 1.0f, name = "zIndex")
    public void setZIndex(AirMapCircle airMapCircle, float f) {
        airMapCircle.setZIndex(f);
    }
}
