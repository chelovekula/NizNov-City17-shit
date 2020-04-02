package com.airbnb.android.react.maps;

import android.app.Activity;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MapsPackage implements ReactPackage {
    public MapsPackage(Activity activity) {
    }

    public MapsPackage() {
    }

    public List<NativeModule> createNativeModules(ReactApplicationContext reactApplicationContext) {
        return Arrays.asList(new NativeModule[]{new AirMapModule(reactApplicationContext)});
    }

    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
        AirMapCalloutManager airMapCalloutManager = new AirMapCalloutManager();
        AirMapMarkerManager airMapMarkerManager = new AirMapMarkerManager();
        AirMapPolylineManager airMapPolylineManager = new AirMapPolylineManager(reactApplicationContext);
        AirMapPolygonManager airMapPolygonManager = new AirMapPolygonManager(reactApplicationContext);
        AirMapCircleManager airMapCircleManager = new AirMapCircleManager(reactApplicationContext);
        AirMapManager airMapManager = new AirMapManager(reactApplicationContext);
        AirMapLiteManager airMapLiteManager = new AirMapLiteManager(reactApplicationContext);
        AirMapUrlTileManager airMapUrlTileManager = new AirMapUrlTileManager(reactApplicationContext);
        AirMapWMSTileManager airMapWMSTileManager = new AirMapWMSTileManager(reactApplicationContext);
        AirMapLocalTileManager airMapLocalTileManager = new AirMapLocalTileManager(reactApplicationContext);
        AirMapOverlayManager airMapOverlayManager = new AirMapOverlayManager(reactApplicationContext);
        airMapManager.setMarkerManager(airMapMarkerManager);
        return Arrays.asList(new ViewManager[]{airMapCalloutManager, airMapMarkerManager, airMapPolylineManager, airMapPolygonManager, airMapCircleManager, airMapManager, airMapLiteManager, airMapUrlTileManager, airMapWMSTileManager, airMapLocalTileManager, airMapOverlayManager});
    }
}
