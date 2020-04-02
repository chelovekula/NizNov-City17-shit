package com.airbnb.android.react.maps;

import android.view.View;
import com.brentvatne.react.ReactVideoViewManager;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

public class AirMapManager extends ViewGroupManager<AirMapView> {
    private static final int ANIMATE_CAMERA = 12;
    private static final int ANIMATE_TO_BEARING = 4;
    private static final int ANIMATE_TO_COORDINATE = 2;
    private static final int ANIMATE_TO_NAVIGATION = 9;
    private static final int ANIMATE_TO_REGION = 1;
    private static final int ANIMATE_TO_VIEWING_ANGLE = 3;
    private static final int FIT_TO_COORDINATES = 7;
    private static final int FIT_TO_ELEMENTS = 5;
    private static final int FIT_TO_SUPPLIED_MARKERS = 6;
    private static final String REACT_CLASS = "AIRMap";
    private static final int SET_CAMERA = 11;
    private static final int SET_INDOOR_ACTIVE_LEVEL_INDEX = 10;
    private static final int SET_MAP_BOUNDARIES = 8;
    private final Map<String, Integer> MAP_TYPES = MapBuilder.m129of("standard", Integer.valueOf(1), "satellite", Integer.valueOf(2), "hybrid", Integer.valueOf(4), "terrain", Integer.valueOf(3), ViewProps.NONE, Integer.valueOf(0));
    private final ReactApplicationContext appContext;
    protected GoogleMapOptions googleMapOptions;
    private AirMapMarkerManager markerManager;

    public String getName() {
        return REACT_CLASS;
    }

    public AirMapManager(ReactApplicationContext reactApplicationContext) {
        this.appContext = reactApplicationContext;
        this.googleMapOptions = new GoogleMapOptions();
    }

    public AirMapMarkerManager getMarkerManager() {
        return this.markerManager;
    }

    public void setMarkerManager(AirMapMarkerManager airMapMarkerManager) {
        this.markerManager = airMapMarkerManager;
    }

    /* access modifiers changed from: protected */
    public AirMapView createViewInstance(ThemedReactContext themedReactContext) {
        return new AirMapView(themedReactContext, this.appContext, this, this.googleMapOptions);
    }

    private void emitMapError(ThemedReactContext themedReactContext, String str, String str2) {
        WritableMap createMap = Arguments.createMap();
        createMap.putString("message", str);
        createMap.putString(ReactVideoViewManager.PROP_SRC_TYPE, str2);
        ((RCTDeviceEventEmitter) themedReactContext.getJSModule(RCTDeviceEventEmitter.class)).emit("onError", createMap);
    }

    @ReactProp(name = "region")
    public void setRegion(AirMapView airMapView, ReadableMap readableMap) {
        airMapView.setRegion(readableMap);
    }

    @ReactProp(name = "initialRegion")
    public void setInitialRegion(AirMapView airMapView, ReadableMap readableMap) {
        airMapView.setInitialRegion(readableMap);
    }

    @ReactProp(name = "camera")
    public void setCamera(AirMapView airMapView, ReadableMap readableMap) {
        airMapView.setCamera(readableMap);
    }

    @ReactProp(name = "initialCamera")
    public void setInitialCamera(AirMapView airMapView, ReadableMap readableMap) {
        airMapView.setInitialCamera(readableMap);
    }

    @ReactProp(name = "mapType")
    public void setMapType(AirMapView airMapView, @Nullable String str) {
        airMapView.map.setMapType(((Integer) this.MAP_TYPES.get(str)).intValue());
    }

    @ReactProp(name = "customMapStyleString")
    public void setMapStyle(AirMapView airMapView, @Nullable String str) {
        airMapView.map.setMapStyle(new MapStyleOptions(str));
    }

    @ReactProp(name = "mapPadding")
    public void setMapPadding(AirMapView airMapView, @Nullable ReadableMap readableMap) {
        int i;
        int i2;
        int i3;
        double d = (double) airMapView.getResources().getDisplayMetrics().density;
        int i4 = 0;
        if (readableMap != null) {
            String str = ViewProps.LEFT;
            if (readableMap.hasKey(str)) {
                double d2 = readableMap.getDouble(str);
                Double.isNaN(d);
                i3 = (int) (d2 * d);
            } else {
                i3 = 0;
            }
            String str2 = ViewProps.TOP;
            if (readableMap.hasKey(str2)) {
                double d3 = readableMap.getDouble(str2);
                Double.isNaN(d);
                i2 = (int) (d3 * d);
            } else {
                i2 = 0;
            }
            String str3 = ViewProps.RIGHT;
            if (readableMap.hasKey(str3)) {
                double d4 = readableMap.getDouble(str3);
                Double.isNaN(d);
                i = (int) (d4 * d);
            } else {
                i = 0;
            }
            String str4 = ViewProps.BOTTOM;
            if (readableMap.hasKey(str4)) {
                double d5 = readableMap.getDouble(str4);
                Double.isNaN(d);
                i4 = (int) (d5 * d);
            }
        } else {
            i3 = 0;
            i2 = 0;
            i = 0;
        }
        airMapView.map.setPadding(i3, i2, i, i4);
    }

    @ReactProp(defaultBoolean = false, name = "showsUserLocation")
    public void setShowsUserLocation(AirMapView airMapView, boolean z) {
        airMapView.setShowsUserLocation(z);
    }

    @ReactProp(defaultBoolean = true, name = "showsMyLocationButton")
    public void setShowsMyLocationButton(AirMapView airMapView, boolean z) {
        airMapView.setShowsMyLocationButton(z);
    }

    @ReactProp(defaultBoolean = true, name = "toolbarEnabled")
    public void setToolbarEnabled(AirMapView airMapView, boolean z) {
        airMapView.setToolbarEnabled(z);
    }

    @ReactProp(defaultBoolean = false, name = "handlePanDrag")
    public void setHandlePanDrag(AirMapView airMapView, boolean z) {
        airMapView.setHandlePanDrag(z);
    }

    @ReactProp(defaultBoolean = false, name = "showsTraffic")
    public void setShowTraffic(AirMapView airMapView, boolean z) {
        airMapView.map.setTrafficEnabled(z);
    }

    @ReactProp(defaultBoolean = false, name = "showsBuildings")
    public void setShowBuildings(AirMapView airMapView, boolean z) {
        airMapView.map.setBuildingsEnabled(z);
    }

    @ReactProp(defaultBoolean = false, name = "showsIndoors")
    public void setShowIndoors(AirMapView airMapView, boolean z) {
        airMapView.map.setIndoorEnabled(z);
    }

    @ReactProp(defaultBoolean = false, name = "showsIndoorLevelPicker")
    public void setShowsIndoorLevelPicker(AirMapView airMapView, boolean z) {
        airMapView.map.getUiSettings().setIndoorLevelPickerEnabled(z);
    }

    @ReactProp(defaultBoolean = false, name = "showsCompass")
    public void setShowsCompass(AirMapView airMapView, boolean z) {
        airMapView.map.getUiSettings().setCompassEnabled(z);
    }

    @ReactProp(defaultBoolean = false, name = "scrollEnabled")
    public void setScrollEnabled(AirMapView airMapView, boolean z) {
        airMapView.map.getUiSettings().setScrollGesturesEnabled(z);
    }

    @ReactProp(defaultBoolean = false, name = "zoomEnabled")
    public void setZoomEnabled(AirMapView airMapView, boolean z) {
        airMapView.map.getUiSettings().setZoomGesturesEnabled(z);
    }

    @ReactProp(defaultBoolean = true, name = "zoomControlEnabled")
    public void setZoomControlEnabled(AirMapView airMapView, boolean z) {
        airMapView.map.getUiSettings().setZoomControlsEnabled(z);
    }

    @ReactProp(defaultBoolean = false, name = "rotateEnabled")
    public void setRotateEnabled(AirMapView airMapView, boolean z) {
        airMapView.map.getUiSettings().setRotateGesturesEnabled(z);
    }

    @ReactProp(defaultBoolean = false, name = "cacheEnabled")
    public void setCacheEnabled(AirMapView airMapView, boolean z) {
        airMapView.setCacheEnabled(z);
    }

    @ReactProp(defaultBoolean = false, name = "loadingEnabled")
    public void setLoadingEnabled(AirMapView airMapView, boolean z) {
        airMapView.enableMapLoading(z);
    }

    @ReactProp(defaultBoolean = true, name = "moveOnMarkerPress")
    public void setMoveOnMarkerPress(AirMapView airMapView, boolean z) {
        airMapView.setMoveOnMarkerPress(z);
    }

    @ReactProp(customType = "Color", name = "loadingBackgroundColor")
    public void setLoadingBackgroundColor(AirMapView airMapView, @Nullable Integer num) {
        airMapView.setLoadingBackgroundColor(num);
    }

    @ReactProp(customType = "Color", name = "loadingIndicatorColor")
    public void setLoadingIndicatorColor(AirMapView airMapView, @Nullable Integer num) {
        airMapView.setLoadingIndicatorColor(num);
    }

    @ReactProp(defaultBoolean = false, name = "pitchEnabled")
    public void setPitchEnabled(AirMapView airMapView, boolean z) {
        airMapView.map.getUiSettings().setTiltGesturesEnabled(z);
    }

    @ReactProp(name = "minZoomLevel")
    public void setMinZoomLevel(AirMapView airMapView, float f) {
        airMapView.map.setMinZoomPreference(f);
    }

    @ReactProp(name = "maxZoomLevel")
    public void setMaxZoomLevel(AirMapView airMapView, float f) {
        airMapView.map.setMaxZoomPreference(f);
    }

    @ReactProp(name = "kmlSrc")
    public void setKmlSrc(AirMapView airMapView, String str) {
        if (str != null) {
            airMapView.setKmlSrc(str);
        }
    }

    public void receiveCommand(AirMapView airMapView, int i, @Nullable ReadableArray readableArray) {
        AirMapView airMapView2 = airMapView;
        ReadableArray readableArray2 = readableArray;
        String str = "latitude";
        String str2 = "longitude";
        switch (i) {
            case 1:
                ReadableMap map = readableArray2.getMap(0);
                Integer valueOf = Integer.valueOf(readableArray2.getInt(1));
                Double valueOf2 = Double.valueOf(map.getDouble(str2));
                Double valueOf3 = Double.valueOf(map.getDouble(str));
                Double valueOf4 = Double.valueOf(map.getDouble("longitudeDelta"));
                Double valueOf5 = Double.valueOf(map.getDouble("latitudeDelta"));
                airMapView2.animateToRegion(new LatLngBounds(new LatLng(valueOf3.doubleValue() - (valueOf5.doubleValue() / 2.0d), valueOf2.doubleValue() - (valueOf4.doubleValue() / 2.0d)), new LatLng(valueOf3.doubleValue() + (valueOf5.doubleValue() / 2.0d), valueOf2.doubleValue() + (valueOf4.doubleValue() / 2.0d))), valueOf.intValue());
                return;
            case 2:
                ReadableMap map2 = readableArray2.getMap(0);
                Integer valueOf6 = Integer.valueOf(readableArray2.getInt(1));
                airMapView2.animateToCoordinate(new LatLng(Double.valueOf(map2.getDouble(str)).doubleValue(), Double.valueOf(map2.getDouble(str2)).doubleValue()), valueOf6.intValue());
                return;
            case 3:
                airMapView2.animateToViewingAngle((float) readableArray2.getDouble(0), Integer.valueOf(readableArray2.getInt(1)).intValue());
                return;
            case 4:
                airMapView2.animateToBearing((float) readableArray2.getDouble(0), Integer.valueOf(readableArray2.getInt(1)).intValue());
                return;
            case 5:
                airMapView2.fitToElements(readableArray2.getBoolean(0));
                return;
            case 6:
                airMapView2.fitToSuppliedMarkers(readableArray2.getArray(0), readableArray2.getMap(1), readableArray2.getBoolean(2));
                return;
            case 7:
                airMapView2.fitToCoordinates(readableArray2.getArray(0), readableArray2.getMap(1), readableArray2.getBoolean(2));
                return;
            case 8:
                airMapView2.setMapBoundaries(readableArray2.getMap(0), readableArray2.getMap(1));
                return;
            case 9:
                ReadableMap map3 = readableArray2.getMap(0);
                airMapView2.animateToNavigation(new LatLng(Double.valueOf(map3.getDouble(str)).doubleValue(), Double.valueOf(map3.getDouble(str2)).doubleValue()), (float) readableArray2.getDouble(1), (float) readableArray2.getDouble(2), Integer.valueOf(readableArray2.getInt(3)).intValue());
                return;
            case 10:
                airMapView2.setIndoorActiveLevelIndex(readableArray2.getInt(0));
                return;
            case 11:
                airMapView2.animateToCamera(readableArray2.getMap(0), 0);
                return;
            case 12:
                airMapView2.animateToCamera(readableArray2.getMap(0), Integer.valueOf(readableArray2.getInt(1)).intValue());
                return;
            default:
                return;
        }
    }

    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        String str = "registrationName";
        Map of = MapBuilder.m131of("onMapReady", MapBuilder.m125of(str, "onMapReady"), "onPress", MapBuilder.m125of(str, "onPress"), "onLongPress", MapBuilder.m125of(str, "onLongPress"), "onMarkerPress", MapBuilder.m125of(str, "onMarkerPress"), "onMarkerSelect", MapBuilder.m125of(str, "onMarkerSelect"), "onMarkerDeselect", MapBuilder.m125of(str, "onMarkerDeselect"), "onCalloutPress", MapBuilder.m125of(str, "onCalloutPress"));
        of.putAll(MapBuilder.m131of("onUserLocationChange", MapBuilder.m125of(str, "onUserLocationChange"), "onMarkerDragStart", MapBuilder.m125of(str, "onMarkerDragStart"), "onMarkerDrag", MapBuilder.m125of(str, "onMarkerDrag"), "onMarkerDragEnd", MapBuilder.m125of(str, "onMarkerDragEnd"), "onPanDrag", MapBuilder.m125of(str, "onPanDrag"), "onKmlReady", MapBuilder.m125of(str, "onKmlReady"), "onPoiClick", MapBuilder.m125of(str, "onPoiClick")));
        String str2 = "onIndoorLevelActivated";
        String str3 = "onIndoorBuildingFocused";
        of.putAll(MapBuilder.m126of(str2, MapBuilder.m125of(str, str2), str3, MapBuilder.m125of(str, str3)));
        return of;
    }

    @Nullable
    public Map<String, Integer> getCommandsMap() {
        Map<String, Integer> CreateMap = CreateMap("setCamera", Integer.valueOf(11), "animateCamera", Integer.valueOf(12), "animateToRegion", Integer.valueOf(1), "animateToCoordinate", Integer.valueOf(2), "animateToViewingAngle", Integer.valueOf(3), "animateToBearing", Integer.valueOf(4), "fitToElements", Integer.valueOf(5), "fitToSuppliedMarkers", Integer.valueOf(6), "fitToCoordinates", Integer.valueOf(7), "animateToNavigation", Integer.valueOf(9));
        CreateMap.putAll(MapBuilder.m126of("setMapBoundaries", Integer.valueOf(8), "setIndoorActiveLevelIndex", Integer.valueOf(10)));
        return CreateMap;
    }

    public static <K, V> Map<K, V> CreateMap(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        HashMap hashMap = new HashMap();
        K k11 = k;
        V v11 = v;
        hashMap.put(k, v);
        K k12 = k2;
        V v12 = v2;
        hashMap.put(k2, v2);
        K k13 = k3;
        V v13 = v3;
        hashMap.put(k3, v3);
        K k14 = k4;
        V v14 = v4;
        hashMap.put(k4, v4);
        K k15 = k5;
        V v15 = v5;
        hashMap.put(k5, v5);
        K k16 = k6;
        V v16 = v6;
        hashMap.put(k6, v6);
        K k17 = k7;
        hashMap.put(k7, v7);
        hashMap.put(k8, v8);
        hashMap.put(k9, v9);
        hashMap.put(k10, v10);
        return hashMap;
    }

    public LayoutShadowNode createShadowNodeInstance() {
        return new SizeReportingShadowNode();
    }

    public void addView(AirMapView airMapView, View view, int i) {
        airMapView.addFeature(view, i);
    }

    public int getChildCount(AirMapView airMapView) {
        return airMapView.getFeatureCount();
    }

    public View getChildAt(AirMapView airMapView, int i) {
        return airMapView.getFeatureAt(i);
    }

    public void removeViewAt(AirMapView airMapView, int i) {
        airMapView.removeFeatureAt(i);
    }

    public void updateExtraData(AirMapView airMapView, Object obj) {
        airMapView.updateExtraData(obj);
    }

    /* access modifiers changed from: 0000 */
    public void pushEvent(ThemedReactContext themedReactContext, View view, String str, WritableMap writableMap) {
        ((RCTEventEmitter) themedReactContext.getJSModule(RCTEventEmitter.class)).receiveEvent(view.getId(), str, writableMap);
    }

    public void onDropViewInstance(AirMapView airMapView) {
        airMapView.doDestroy();
        super.onDropViewInstance(airMapView);
    }
}
