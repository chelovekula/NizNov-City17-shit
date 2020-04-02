package com.airbnb.android.react.maps;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.location.Location;
import android.os.Build.VERSION;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import androidx.core.content.PermissionChecker;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener;
import com.google.android.gms.maps.GoogleMap.OnIndoorStateChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.GoogleMap.OnPoiClickListener;
import com.google.android.gms.maps.GoogleMap.OnPolygonClickListener;
import com.google.android.gms.maps.GoogleMap.OnPolylineClickListener;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition.Builder;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.IndoorLevel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import org.xmlpull.v1.XmlPullParserException;

public class AirMapView extends MapView implements InfoWindowAdapter, OnMarkerDragListener, OnMapReadyCallback, OnPoiClickListener, OnIndoorStateChangeListener {
    private static final String[] PERMISSIONS = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
    private ViewAttacherGroup attacherGroup;
    private final int baseMapPadding = 50;
    private LatLngBounds boundsToMove;
    private boolean cacheEnabled = false;
    private ImageView cacheImageView;
    /* access modifiers changed from: private */
    public LatLngBounds cameraLastIdleBounds;
    /* access modifiers changed from: private */
    public int cameraMoveReason = 0;
    private CameraUpdate cameraToSet;
    /* access modifiers changed from: private */
    public final ThemedReactContext context;
    /* access modifiers changed from: private */
    public boolean destroyed = false;
    /* access modifiers changed from: private */
    public final EventDispatcher eventDispatcher;
    private final List<AirMapFeature> features = new ArrayList();
    private final GestureDetectorCompat gestureDetector;
    /* access modifiers changed from: private */
    public boolean handlePanDrag = false;
    private boolean initialCameraSet = false;
    private boolean initialRegionSet = false;
    /* access modifiers changed from: private */
    public Boolean isMapLoaded = Boolean.valueOf(false);
    private KmlLayer kmlLayer;
    private LifecycleEventListener lifecycleListener;
    private Integer loadingBackgroundColor = null;
    private Integer loadingIndicatorColor = null;
    /* access modifiers changed from: private */
    public final AirMapManager manager;
    public GoogleMap map;
    private RelativeLayout mapLoadingLayout;
    private ProgressBar mapLoadingProgressBar;
    private final Map<Marker, AirMapMarker> markerMap = new HashMap();
    /* access modifiers changed from: private */
    public boolean moveOnMarkerPress = true;
    /* access modifiers changed from: private */
    public boolean paused = false;
    /* access modifiers changed from: private */
    public final Map<Polygon, AirMapPolygon> polygonMap = new HashMap();
    /* access modifiers changed from: private */
    public final Map<Polyline, AirMapPolyline> polylineMap = new HashMap();
    /* access modifiers changed from: private */
    public boolean showUserLocation = false;

    private static boolean contextHasBug(Context context2) {
        return context2 == null || context2.getResources() == null || context2.getResources().getConfiguration() == null;
    }

    private static Context getNonBuggyContext(ThemedReactContext themedReactContext, ReactApplicationContext reactApplicationContext) {
        if (!contextHasBug(reactApplicationContext.getCurrentActivity())) {
            return reactApplicationContext.getCurrentActivity();
        }
        if (!contextHasBug(themedReactContext)) {
            return themedReactContext;
        }
        if (!contextHasBug(themedReactContext.getCurrentActivity())) {
            return themedReactContext.getCurrentActivity();
        }
        return !contextHasBug(themedReactContext.getApplicationContext()) ? themedReactContext.getApplicationContext() : themedReactContext;
    }

    public AirMapView(ThemedReactContext themedReactContext, ReactApplicationContext reactApplicationContext, AirMapManager airMapManager, GoogleMapOptions googleMapOptions) {
        super(getNonBuggyContext(themedReactContext, reactApplicationContext), googleMapOptions);
        this.manager = airMapManager;
        this.context = themedReactContext;
        super.onCreate(null);
        super.onResume();
        super.getMapAsync(this);
        this.gestureDetector = new GestureDetectorCompat(themedReactContext, new SimpleOnGestureListener() {
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (AirMapView.this.handlePanDrag) {
                    AirMapView.this.onPanDrag(motionEvent2);
                }
                return false;
            }
        });
        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                if (!AirMapView.this.paused) {
                    AirMapView.this.cacheView();
                }
            }
        });
        this.eventDispatcher = ((UIManagerModule) themedReactContext.getNativeModule(UIManagerModule.class)).getEventDispatcher();
        this.attacherGroup = new ViewAttacherGroup(this.context);
        LayoutParams layoutParams = new LayoutParams(0, 0);
        layoutParams.width = 0;
        layoutParams.height = 0;
        layoutParams.leftMargin = 99999999;
        layoutParams.topMargin = 99999999;
        this.attacherGroup.setLayoutParams(layoutParams);
        addView(this.attacherGroup);
    }

    public void onMapReady(final GoogleMap googleMap) {
        if (!this.destroyed) {
            this.map = googleMap;
            this.map.setInfoWindowAdapter(this);
            this.map.setOnMarkerDragListener(this);
            this.map.setOnPoiClickListener(this);
            this.map.setOnIndoorStateChangeListener(this);
            this.manager.pushEvent(this.context, this, "onMapReady", new WritableNativeMap());
            googleMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
                public void onMyLocationChange(Location location) {
                    WritableNativeMap writableNativeMap = new WritableNativeMap();
                    WritableNativeMap writableNativeMap2 = new WritableNativeMap();
                    writableNativeMap2.putDouble("latitude", location.getLatitude());
                    writableNativeMap2.putDouble("longitude", location.getLongitude());
                    writableNativeMap2.putDouble("altitude", location.getAltitude());
                    writableNativeMap2.putDouble("timestamp", (double) location.getTime());
                    writableNativeMap2.putDouble("accuracy", (double) location.getAccuracy());
                    writableNativeMap2.putDouble("speed", (double) location.getSpeed());
                    if (VERSION.SDK_INT >= 18) {
                        writableNativeMap2.putBoolean("isFromMockProvider", location.isFromMockProvider());
                    }
                    writableNativeMap.putMap("coordinate", writableNativeMap2);
                    AirMapView.this.manager.pushEvent(AirMapView.this.context, this, "onUserLocationChange", writableNativeMap);
                }
            });
            googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
                public boolean onMarkerClick(Marker marker) {
                    AirMapMarker access$500 = AirMapView.this.getMarkerMap(marker);
                    WritableMap makeClickEventData = AirMapView.this.makeClickEventData(marker.getPosition());
                    String str = "marker-press";
                    String str2 = "action";
                    makeClickEventData.putString(str2, str);
                    String str3 = "id";
                    makeClickEventData.putString(str3, access$500.getIdentifier());
                    AirMapView.this.manager.pushEvent(AirMapView.this.context, this, "onMarkerPress", makeClickEventData);
                    WritableMap makeClickEventData2 = AirMapView.this.makeClickEventData(marker.getPosition());
                    makeClickEventData2.putString(str2, str);
                    makeClickEventData2.putString(str3, access$500.getIdentifier());
                    AirMapView.this.manager.pushEvent(AirMapView.this.context, access$500, "onPress", makeClickEventData2);
                    if (this.moveOnMarkerPress) {
                        return false;
                    }
                    marker.showInfoWindow();
                    return true;
                }
            });
            googleMap.setOnPolygonClickListener(new OnPolygonClickListener() {
                public void onPolygonClick(Polygon polygon) {
                    WritableMap makeClickEventData = AirMapView.this.makeClickEventData((LatLng) polygon.getPoints().get(0));
                    makeClickEventData.putString("action", "polygon-press");
                    AirMapView.this.manager.pushEvent(AirMapView.this.context, (View) AirMapView.this.polygonMap.get(polygon), "onPress", makeClickEventData);
                }
            });
            googleMap.setOnPolylineClickListener(new OnPolylineClickListener() {
                public void onPolylineClick(Polyline polyline) {
                    WritableMap makeClickEventData = AirMapView.this.makeClickEventData((LatLng) polyline.getPoints().get(0));
                    makeClickEventData.putString("action", "polyline-press");
                    AirMapView.this.manager.pushEvent(AirMapView.this.context, (View) AirMapView.this.polylineMap.get(polyline), "onPress", makeClickEventData);
                }
            });
            googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
                public void onInfoWindowClick(Marker marker) {
                    WritableMap makeClickEventData = AirMapView.this.makeClickEventData(marker.getPosition());
                    String str = "callout-press";
                    String str2 = "action";
                    makeClickEventData.putString(str2, str);
                    String str3 = "onCalloutPress";
                    AirMapView.this.manager.pushEvent(AirMapView.this.context, this, str3, makeClickEventData);
                    WritableMap makeClickEventData2 = AirMapView.this.makeClickEventData(marker.getPosition());
                    makeClickEventData2.putString(str2, str);
                    AirMapMarker access$500 = AirMapView.this.getMarkerMap(marker);
                    AirMapView.this.manager.pushEvent(AirMapView.this.context, access$500, str3, makeClickEventData2);
                    WritableMap makeClickEventData3 = AirMapView.this.makeClickEventData(marker.getPosition());
                    makeClickEventData3.putString(str2, str);
                    AirMapCallout calloutView = access$500.getCalloutView();
                    if (calloutView != null) {
                        AirMapView.this.manager.pushEvent(AirMapView.this.context, calloutView, "onPress", makeClickEventData3);
                    }
                }
            });
            googleMap.setOnMapClickListener(new OnMapClickListener() {
                public void onMapClick(LatLng latLng) {
                    WritableMap makeClickEventData = AirMapView.this.makeClickEventData(latLng);
                    makeClickEventData.putString("action", "press");
                    AirMapView.this.manager.pushEvent(AirMapView.this.context, this, "onPress", makeClickEventData);
                }
            });
            googleMap.setOnMapLongClickListener(new OnMapLongClickListener() {
                public void onMapLongClick(LatLng latLng) {
                    AirMapView.this.makeClickEventData(latLng).putString("action", "long-press");
                    AirMapView.this.manager.pushEvent(AirMapView.this.context, this, "onLongPress", AirMapView.this.makeClickEventData(latLng));
                }
            });
            googleMap.setOnCameraMoveStartedListener(new OnCameraMoveStartedListener() {
                public void onCameraMoveStarted(int i) {
                    AirMapView.this.cameraMoveReason = i;
                }
            });
            googleMap.setOnCameraMoveListener(new OnCameraMoveListener() {
                public void onCameraMove() {
                    LatLngBounds latLngBounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
                    AirMapView.this.cameraLastIdleBounds = null;
                    AirMapView.this.eventDispatcher.dispatchEvent(new RegionChangeEvent(AirMapView.this.getId(), latLngBounds, true));
                }
            });
            googleMap.setOnCameraIdleListener(new OnCameraIdleListener() {
                public void onCameraIdle() {
                    LatLngBounds latLngBounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
                    if (AirMapView.this.cameraMoveReason == 0) {
                        return;
                    }
                    if (AirMapView.this.cameraLastIdleBounds == null || LatLngBoundsUtils.BoundsAreDifferent(latLngBounds, AirMapView.this.cameraLastIdleBounds)) {
                        AirMapView.this.cameraLastIdleBounds = latLngBounds;
                        AirMapView.this.eventDispatcher.dispatchEvent(new RegionChangeEvent(AirMapView.this.getId(), latLngBounds, false));
                    }
                }
            });
            googleMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
                public void onMapLoaded() {
                    AirMapView.this.isMapLoaded = Boolean.valueOf(true);
                    AirMapView.this.cacheView();
                }
            });
            this.lifecycleListener = new LifecycleEventListener() {
                public void onHostResume() {
                    if (AirMapView.this.hasPermissions()) {
                        googleMap.setMyLocationEnabled(AirMapView.this.showUserLocation);
                    }
                    synchronized (AirMapView.this) {
                        if (!AirMapView.this.destroyed) {
                            AirMapView.this.onResume();
                        }
                        AirMapView.this.paused = false;
                    }
                }

                public void onHostPause() {
                    if (AirMapView.this.hasPermissions()) {
                        googleMap.setMyLocationEnabled(false);
                    }
                    synchronized (AirMapView.this) {
                        if (!AirMapView.this.destroyed) {
                            AirMapView.this.onPause();
                        }
                        AirMapView.this.paused = true;
                    }
                }

                public void onHostDestroy() {
                    AirMapView.this.doDestroy();
                }
            };
            this.context.addLifecycleEventListener(this.lifecycleListener);
        }
    }

    /* access modifiers changed from: private */
    public boolean hasPermissions() {
        if (PermissionChecker.checkSelfPermission(getContext(), PERMISSIONS[0]) == 0 || PermissionChecker.checkSelfPermission(getContext(), PERMISSIONS[1]) == 0) {
            return true;
        }
        return false;
    }

    public synchronized void doDestroy() {
        if (!this.destroyed) {
            this.destroyed = true;
            if (!(this.lifecycleListener == null || this.context == null)) {
                this.context.removeLifecycleEventListener(this.lifecycleListener);
                this.lifecycleListener = null;
            }
            if (!this.paused) {
                onPause();
                this.paused = true;
            }
            onDestroy();
        }
    }

    public void setInitialRegion(ReadableMap readableMap) {
        if (!this.initialRegionSet && readableMap != null) {
            setRegion(readableMap);
            this.initialRegionSet = true;
        }
    }

    public void setInitialCamera(ReadableMap readableMap) {
        if (!this.initialCameraSet && readableMap != null) {
            setCamera(readableMap);
            this.initialCameraSet = true;
        }
    }

    public void setRegion(ReadableMap readableMap) {
        ReadableMap readableMap2 = readableMap;
        if (readableMap2 != null) {
            Double valueOf = Double.valueOf(readableMap2.getDouble("longitude"));
            Double valueOf2 = Double.valueOf(readableMap2.getDouble("latitude"));
            Double valueOf3 = Double.valueOf(readableMap2.getDouble("longitudeDelta"));
            Double valueOf4 = Double.valueOf(readableMap2.getDouble("latitudeDelta"));
            LatLngBounds latLngBounds = new LatLngBounds(new LatLng(valueOf2.doubleValue() - (valueOf4.doubleValue() / 2.0d), valueOf.doubleValue() - (valueOf3.doubleValue() / 2.0d)), new LatLng(valueOf2.doubleValue() + (valueOf4.doubleValue() / 2.0d), valueOf.doubleValue() + (valueOf3.doubleValue() / 2.0d)));
            if (super.getHeight() <= 0 || super.getWidth() <= 0) {
                this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(valueOf2.doubleValue(), valueOf.doubleValue()), 10.0f));
                this.boundsToMove = latLngBounds;
            } else {
                this.map.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
                this.boundsToMove = null;
            }
        }
    }

    public void setCamera(ReadableMap readableMap) {
        if (readableMap != null) {
            Builder builder = new Builder();
            ReadableMap map2 = readableMap.getMap("center");
            if (map2 != null) {
                builder.target(new LatLng(Double.valueOf(map2.getDouble("latitude")).doubleValue(), Double.valueOf(map2.getDouble("longitude")).doubleValue()));
            }
            builder.tilt((float) readableMap.getDouble("pitch"));
            builder.bearing((float) readableMap.getDouble("heading"));
            builder.zoom((float) readableMap.getInt("zoom"));
            CameraUpdate newCameraPosition = CameraUpdateFactory.newCameraPosition(builder.build());
            if (super.getHeight() <= 0 || super.getWidth() <= 0) {
                this.cameraToSet = newCameraPosition;
            } else {
                this.map.moveCamera(newCameraPosition);
                this.cameraToSet = null;
            }
        }
    }

    public void setShowsUserLocation(boolean z) {
        this.showUserLocation = z;
        if (hasPermissions()) {
            this.map.setMyLocationEnabled(z);
        }
    }

    public void setShowsMyLocationButton(boolean z) {
        if (hasPermissions() || !z) {
            this.map.getUiSettings().setMyLocationButtonEnabled(z);
        }
    }

    public void setToolbarEnabled(boolean z) {
        if (hasPermissions() || !z) {
            this.map.getUiSettings().setMapToolbarEnabled(z);
        }
    }

    public void setCacheEnabled(boolean z) {
        this.cacheEnabled = z;
        cacheView();
    }

    public void enableMapLoading(boolean z) {
        if (z && !this.isMapLoaded.booleanValue()) {
            getMapLoadingLayoutView().setVisibility(0);
        }
    }

    public void setMoveOnMarkerPress(boolean z) {
        this.moveOnMarkerPress = z;
    }

    public void setLoadingBackgroundColor(Integer num) {
        this.loadingBackgroundColor = num;
        RelativeLayout relativeLayout = this.mapLoadingLayout;
        if (relativeLayout == null) {
            return;
        }
        if (num == null) {
            relativeLayout.setBackgroundColor(-1);
        } else {
            relativeLayout.setBackgroundColor(this.loadingBackgroundColor.intValue());
        }
    }

    public void setLoadingIndicatorColor(Integer num) {
        this.loadingIndicatorColor = num;
        if (this.mapLoadingProgressBar != null) {
            Integer valueOf = num == null ? Integer.valueOf(Color.parseColor("#606060")) : num;
            if (VERSION.SDK_INT >= 21) {
                ColorStateList valueOf2 = ColorStateList.valueOf(num.intValue());
                ColorStateList valueOf3 = ColorStateList.valueOf(num.intValue());
                ColorStateList valueOf4 = ColorStateList.valueOf(num.intValue());
                this.mapLoadingProgressBar.setProgressTintList(valueOf2);
                this.mapLoadingProgressBar.setSecondaryProgressTintList(valueOf3);
                this.mapLoadingProgressBar.setIndeterminateTintList(valueOf4);
                return;
            }
            Mode mode = Mode.SRC_IN;
            if (VERSION.SDK_INT <= 10) {
                mode = Mode.MULTIPLY;
            }
            if (this.mapLoadingProgressBar.getIndeterminateDrawable() != null) {
                this.mapLoadingProgressBar.getIndeterminateDrawable().setColorFilter(valueOf.intValue(), mode);
            }
            if (this.mapLoadingProgressBar.getProgressDrawable() != null) {
                this.mapLoadingProgressBar.getProgressDrawable().setColorFilter(valueOf.intValue(), mode);
            }
        }
    }

    public void setHandlePanDrag(boolean z) {
        this.handlePanDrag = z;
    }

    public void addFeature(View view, int i) {
        if (view instanceof AirMapMarker) {
            AirMapMarker airMapMarker = (AirMapMarker) view;
            airMapMarker.addToMap(this.map);
            this.features.add(i, airMapMarker);
            int visibility = airMapMarker.getVisibility();
            airMapMarker.setVisibility(4);
            ViewGroup viewGroup = (ViewGroup) airMapMarker.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(airMapMarker);
            }
            this.attacherGroup.addView(airMapMarker);
            airMapMarker.setVisibility(visibility);
            this.markerMap.put((Marker) airMapMarker.getFeature(), airMapMarker);
        } else if (view instanceof AirMapPolyline) {
            AirMapPolyline airMapPolyline = (AirMapPolyline) view;
            airMapPolyline.addToMap(this.map);
            this.features.add(i, airMapPolyline);
            this.polylineMap.put((Polyline) airMapPolyline.getFeature(), airMapPolyline);
        } else if (view instanceof AirMapPolygon) {
            AirMapPolygon airMapPolygon = (AirMapPolygon) view;
            airMapPolygon.addToMap(this.map);
            this.features.add(i, airMapPolygon);
            this.polygonMap.put((Polygon) airMapPolygon.getFeature(), airMapPolygon);
        } else if (view instanceof AirMapCircle) {
            AirMapCircle airMapCircle = (AirMapCircle) view;
            airMapCircle.addToMap(this.map);
            this.features.add(i, airMapCircle);
        } else if (view instanceof AirMapUrlTile) {
            AirMapUrlTile airMapUrlTile = (AirMapUrlTile) view;
            airMapUrlTile.addToMap(this.map);
            this.features.add(i, airMapUrlTile);
        } else if (view instanceof AirMapWMSTile) {
            AirMapWMSTile airMapWMSTile = (AirMapWMSTile) view;
            airMapWMSTile.addToMap(this.map);
            this.features.add(i, airMapWMSTile);
        } else if (view instanceof AirMapLocalTile) {
            AirMapLocalTile airMapLocalTile = (AirMapLocalTile) view;
            airMapLocalTile.addToMap(this.map);
            this.features.add(i, airMapLocalTile);
        } else if (view instanceof AirMapOverlay) {
            AirMapOverlay airMapOverlay = (AirMapOverlay) view;
            airMapOverlay.addToMap(this.map);
            this.features.add(i, airMapOverlay);
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup2 = (ViewGroup) view;
            for (int i2 = 0; i2 < viewGroup2.getChildCount(); i2++) {
                addFeature(viewGroup2.getChildAt(i2), i);
            }
        } else {
            addView(view, i);
        }
    }

    public int getFeatureCount() {
        return this.features.size();
    }

    public View getFeatureAt(int i) {
        return (View) this.features.get(i);
    }

    public void removeFeatureAt(int i) {
        AirMapFeature airMapFeature = (AirMapFeature) this.features.remove(i);
        if (airMapFeature instanceof AirMapMarker) {
            this.markerMap.remove(airMapFeature.getFeature());
        }
        airMapFeature.removeFromMap(this.map);
    }

    public WritableMap makeClickEventData(LatLng latLng) {
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        WritableNativeMap writableNativeMap2 = new WritableNativeMap();
        writableNativeMap2.putDouble("latitude", latLng.latitude);
        writableNativeMap2.putDouble("longitude", latLng.longitude);
        writableNativeMap.putMap("coordinate", writableNativeMap2);
        Point screenLocation = this.map.getProjection().toScreenLocation(latLng);
        WritableNativeMap writableNativeMap3 = new WritableNativeMap();
        writableNativeMap3.putDouble("x", (double) screenLocation.x);
        writableNativeMap3.putDouble("y", (double) screenLocation.y);
        writableNativeMap.putMap(ViewProps.POSITION, writableNativeMap3);
        return writableNativeMap;
    }

    public void updateExtraData(Object obj) {
        if (this.boundsToMove != null) {
            HashMap hashMap = (HashMap) obj;
            String str = "width";
            int intValue = hashMap.get(str) == null ? 0 : ((Float) hashMap.get(str)).intValue();
            String str2 = "height";
            int intValue2 = hashMap.get(str2) == null ? 0 : ((Float) hashMap.get(str2)).intValue();
            if (intValue <= 0 || intValue2 <= 0) {
                this.map.moveCamera(CameraUpdateFactory.newLatLngBounds(this.boundsToMove, 0));
            } else {
                this.map.moveCamera(CameraUpdateFactory.newLatLngBounds(this.boundsToMove, intValue, intValue2, 0));
            }
            this.boundsToMove = null;
            this.cameraToSet = null;
            return;
        }
        CameraUpdate cameraUpdate = this.cameraToSet;
        if (cameraUpdate != null) {
            this.map.moveCamera(cameraUpdate);
            this.cameraToSet = null;
        }
    }

    public void animateToCamera(ReadableMap readableMap, int i) {
        GoogleMap googleMap = this.map;
        if (googleMap != null) {
            Builder builder = new Builder(googleMap.getCameraPosition());
            String str = "zoom";
            if (readableMap.hasKey(str)) {
                builder.zoom((float) readableMap.getDouble(str));
            }
            String str2 = "heading";
            if (readableMap.hasKey(str2)) {
                builder.bearing((float) readableMap.getDouble(str2));
            }
            String str3 = "pitch";
            if (readableMap.hasKey(str3)) {
                builder.tilt((float) readableMap.getDouble(str3));
            }
            String str4 = "center";
            if (readableMap.hasKey(str4)) {
                ReadableMap map2 = readableMap.getMap(str4);
                builder.target(new LatLng(map2.getDouble("latitude"), map2.getDouble("longitude")));
            }
            CameraUpdate newCameraPosition = CameraUpdateFactory.newCameraPosition(builder.build());
            if (i <= 0) {
                this.map.moveCamera(newCameraPosition);
            } else {
                this.map.animateCamera(newCameraPosition, i, null);
            }
        }
    }

    public void animateToNavigation(LatLng latLng, float f, float f2, int i) {
        GoogleMap googleMap = this.map;
        if (googleMap != null) {
            this.map.animateCamera(CameraUpdateFactory.newCameraPosition(new Builder(googleMap.getCameraPosition()).bearing(f).tilt(f2).target(latLng).build()), i, null);
        }
    }

    public void animateToRegion(LatLngBounds latLngBounds, int i) {
        GoogleMap googleMap = this.map;
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0), i, null);
        }
    }

    public void animateToViewingAngle(float f, int i) {
        GoogleMap googleMap = this.map;
        if (googleMap != null) {
            this.map.animateCamera(CameraUpdateFactory.newCameraPosition(new Builder(googleMap.getCameraPosition()).tilt(f).build()), i, null);
        }
    }

    public void animateToBearing(float f, int i) {
        GoogleMap googleMap = this.map;
        if (googleMap != null) {
            this.map.animateCamera(CameraUpdateFactory.newCameraPosition(new Builder(googleMap.getCameraPosition()).bearing(f).build()), i, null);
        }
    }

    public void animateToCoordinate(LatLng latLng, int i) {
        GoogleMap googleMap = this.map;
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng), i, null);
        }
    }

    public void fitToElements(boolean z) {
        if (this.map != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            boolean z2 = false;
            for (AirMapFeature airMapFeature : this.features) {
                if (airMapFeature instanceof AirMapMarker) {
                    builder.include(((Marker) airMapFeature.getFeature()).getPosition());
                    z2 = true;
                }
            }
            if (z2) {
                CameraUpdate newLatLngBounds = CameraUpdateFactory.newLatLngBounds(builder.build(), 50);
                if (z) {
                    this.map.animateCamera(newLatLngBounds);
                } else {
                    this.map.moveCamera(newLatLngBounds);
                }
            }
        }
    }

    public void fitToSuppliedMarkers(ReadableArray readableArray, ReadableMap readableMap, boolean z) {
        if (this.map != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            String[] strArr = new String[readableArray.size()];
            boolean z2 = false;
            for (int i = 0; i < readableArray.size(); i++) {
                strArr[i] = readableArray.getString(i);
            }
            List asList = Arrays.asList(strArr);
            for (AirMapFeature airMapFeature : this.features) {
                if (airMapFeature instanceof AirMapMarker) {
                    String identifier = ((AirMapMarker) airMapFeature).getIdentifier();
                    Marker marker = (Marker) airMapFeature.getFeature();
                    if (asList.contains(identifier)) {
                        builder.include(marker.getPosition());
                        z2 = true;
                    }
                }
            }
            if (z2) {
                CameraUpdate newLatLngBounds = CameraUpdateFactory.newLatLngBounds(builder.build(), 50);
                if (readableMap != null) {
                    this.map.setPadding(readableMap.getInt(ViewProps.LEFT), readableMap.getInt(ViewProps.TOP), readableMap.getInt(ViewProps.RIGHT), readableMap.getInt(ViewProps.BOTTOM));
                }
                if (z) {
                    this.map.animateCamera(newLatLngBounds);
                } else {
                    this.map.moveCamera(newLatLngBounds);
                }
            }
        }
    }

    public void fitToCoordinates(ReadableArray readableArray, ReadableMap readableMap, boolean z) {
        if (this.map != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (int i = 0; i < readableArray.size(); i++) {
                ReadableMap map2 = readableArray.getMap(i);
                builder.include(new LatLng(Double.valueOf(map2.getDouble("latitude")).doubleValue(), Double.valueOf(map2.getDouble("longitude")).doubleValue()));
            }
            CameraUpdate newLatLngBounds = CameraUpdateFactory.newLatLngBounds(builder.build(), 50);
            if (readableMap != null) {
                this.map.setPadding(readableMap.getInt(ViewProps.LEFT), readableMap.getInt(ViewProps.TOP), readableMap.getInt(ViewProps.RIGHT), readableMap.getInt(ViewProps.BOTTOM));
            }
            if (z) {
                this.map.animateCamera(newLatLngBounds);
            } else {
                this.map.moveCamera(newLatLngBounds);
            }
            this.map.setPadding(0, 0, 0, 0);
        }
    }

    public double[][] getMapBoundaries() {
        LatLngBounds latLngBounds = this.map.getProjection().getVisibleRegion().latLngBounds;
        LatLng latLng = latLngBounds.northeast;
        LatLng latLng2 = latLngBounds.southwest;
        return new double[][]{new double[]{latLng.longitude, latLng.latitude}, new double[]{latLng2.longitude, latLng2.latitude}};
    }

    public void setMapBoundaries(ReadableMap readableMap, ReadableMap readableMap2) {
        if (this.map != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            String str = "latitude";
            String str2 = "longitude";
            builder.include(new LatLng(Double.valueOf(readableMap.getDouble(str)).doubleValue(), Double.valueOf(readableMap.getDouble(str2)).doubleValue()));
            builder.include(new LatLng(Double.valueOf(readableMap2.getDouble(str)).doubleValue(), Double.valueOf(readableMap2.getDouble(str2)).doubleValue()));
            this.map.setLatLngBoundsForCameraTarget(builder.build());
        }
    }

    public View getInfoWindow(Marker marker) {
        return getMarkerMap(marker).getCallout();
    }

    public View getInfoContents(Marker marker) {
        return getMarkerMap(marker).getInfoContents();
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        this.gestureDetector.onTouchEvent(motionEvent);
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        boolean z = false;
        if (actionMasked == 0) {
            ViewParent parent = getParent();
            GoogleMap googleMap = this.map;
            if (googleMap != null && googleMap.getUiSettings().isScrollGesturesEnabled()) {
                z = true;
            }
            parent.requestDisallowInterceptTouchEvent(z);
        } else if (actionMasked == 1) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        super.dispatchTouchEvent(motionEvent);
        return true;
    }

    public void onMarkerDragStart(Marker marker) {
        this.manager.pushEvent(this.context, this, "onMarkerDragStart", makeClickEventData(marker.getPosition()));
        this.manager.pushEvent(this.context, getMarkerMap(marker), "onDragStart", makeClickEventData(marker.getPosition()));
    }

    public void onMarkerDrag(Marker marker) {
        this.manager.pushEvent(this.context, this, "onMarkerDrag", makeClickEventData(marker.getPosition()));
        this.manager.pushEvent(this.context, getMarkerMap(marker), "onDrag", makeClickEventData(marker.getPosition()));
    }

    public void onMarkerDragEnd(Marker marker) {
        this.manager.pushEvent(this.context, this, "onMarkerDragEnd", makeClickEventData(marker.getPosition()));
        this.manager.pushEvent(this.context, getMarkerMap(marker), "onDragEnd", makeClickEventData(marker.getPosition()));
    }

    public void onPoiClick(PointOfInterest pointOfInterest) {
        WritableMap makeClickEventData = makeClickEventData(pointOfInterest.latLng);
        makeClickEventData.putString("placeId", pointOfInterest.placeId);
        makeClickEventData.putString("name", pointOfInterest.name);
        this.manager.pushEvent(this.context, this, "onPoiClick", makeClickEventData);
    }

    private ProgressBar getMapLoadingProgressBar() {
        if (this.mapLoadingProgressBar == null) {
            this.mapLoadingProgressBar = new ProgressBar(getContext());
            this.mapLoadingProgressBar.setIndeterminate(true);
        }
        Integer num = this.loadingIndicatorColor;
        if (num != null) {
            setLoadingIndicatorColor(num);
        }
        return this.mapLoadingProgressBar;
    }

    private RelativeLayout getMapLoadingLayoutView() {
        if (this.mapLoadingLayout == null) {
            this.mapLoadingLayout = new RelativeLayout(getContext());
            this.mapLoadingLayout.setBackgroundColor(-3355444);
            addView(this.mapLoadingLayout, new ViewGroup.LayoutParams(-1, -1));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(13);
            this.mapLoadingLayout.addView(getMapLoadingProgressBar(), layoutParams);
            this.mapLoadingLayout.setVisibility(4);
        }
        setLoadingBackgroundColor(this.loadingBackgroundColor);
        return this.mapLoadingLayout;
    }

    private ImageView getCacheImageView() {
        if (this.cacheImageView == null) {
            this.cacheImageView = new ImageView(getContext());
            addView(this.cacheImageView, new ViewGroup.LayoutParams(-1, -1));
            this.cacheImageView.setVisibility(4);
        }
        return this.cacheImageView;
    }

    private void removeCacheImageView() {
        ImageView imageView = this.cacheImageView;
        if (imageView != null) {
            ((ViewGroup) imageView.getParent()).removeView(this.cacheImageView);
            this.cacheImageView = null;
        }
    }

    private void removeMapLoadingProgressBar() {
        ProgressBar progressBar = this.mapLoadingProgressBar;
        if (progressBar != null) {
            ((ViewGroup) progressBar.getParent()).removeView(this.mapLoadingProgressBar);
            this.mapLoadingProgressBar = null;
        }
    }

    private void removeMapLoadingLayoutView() {
        removeMapLoadingProgressBar();
        RelativeLayout relativeLayout = this.mapLoadingLayout;
        if (relativeLayout != null) {
            ((ViewGroup) relativeLayout.getParent()).removeView(this.mapLoadingLayout);
            this.mapLoadingLayout = null;
        }
    }

    /* access modifiers changed from: private */
    public void cacheView() {
        if (this.cacheEnabled) {
            final ImageView cacheImageView2 = getCacheImageView();
            final RelativeLayout mapLoadingLayoutView = getMapLoadingLayoutView();
            cacheImageView2.setVisibility(4);
            mapLoadingLayoutView.setVisibility(0);
            if (this.isMapLoaded.booleanValue()) {
                this.map.snapshot(new SnapshotReadyCallback() {
                    public void onSnapshotReady(Bitmap bitmap) {
                        cacheImageView2.setImageBitmap(bitmap);
                        cacheImageView2.setVisibility(0);
                        mapLoadingLayoutView.setVisibility(4);
                    }
                });
                return;
            }
            return;
        }
        removeCacheImageView();
        if (this.isMapLoaded.booleanValue()) {
            removeMapLoadingLayoutView();
        }
    }

    public void onPanDrag(MotionEvent motionEvent) {
        this.manager.pushEvent(this.context, this, "onPanDrag", makeClickEventData(this.map.getProjection().fromScreenLocation(new Point((int) motionEvent.getX(), (int) motionEvent.getY()))));
    }

    public void setKmlSrc(String str) {
        String str2 = "name";
        String str3 = "description";
        try {
            InputStream inputStream = (InputStream) new FileUtil(this.context).execute(new String[]{str}).get();
            if (inputStream != null) {
                this.kmlLayer = new KmlLayer(this.map, inputStream, (Context) this.context);
                this.kmlLayer.addLayerToMap();
                WritableNativeMap writableNativeMap = new WritableNativeMap();
                WritableNativeArray writableNativeArray = new WritableNativeArray();
                String str4 = "onKmlReady";
                if (this.kmlLayer.getContainers() == null) {
                    this.manager.pushEvent(this.context, this, str4, writableNativeMap);
                    return;
                }
                KmlContainer kmlContainer = (KmlContainer) this.kmlLayer.getContainers().iterator().next();
                if (kmlContainer != null) {
                    if (kmlContainer.getContainers() != null) {
                        if (kmlContainer.getContainers().iterator().hasNext()) {
                            kmlContainer = (KmlContainer) kmlContainer.getContainers().iterator().next();
                        }
                        Integer valueOf = Integer.valueOf(0);
                        for (KmlPlacemark kmlPlacemark : kmlContainer.getPlacemarks()) {
                            MarkerOptions markerOptions = new MarkerOptions();
                            if (kmlPlacemark.getInlineStyle() != null) {
                                markerOptions = kmlPlacemark.getMarkerOptions();
                            } else {
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
                            }
                            LatLng latLng = (LatLng) kmlPlacemark.getGeometry().getGeometryObject();
                            String str5 = "";
                            String property = kmlPlacemark.hasProperty(str2) ? kmlPlacemark.getProperty(str2) : str5;
                            if (kmlPlacemark.hasProperty(str3)) {
                                str5 = kmlPlacemark.getProperty(str3);
                            }
                            markerOptions.position(latLng);
                            markerOptions.title(property);
                            markerOptions.snippet(str5);
                            String str6 = str2;
                            AirMapMarker airMapMarker = new AirMapMarker(this.context, markerOptions, this.manager.getMarkerManager());
                            if (kmlPlacemark.getInlineStyle() != null && kmlPlacemark.getInlineStyle().getIconUrl() != null) {
                                airMapMarker.setImage(kmlPlacemark.getInlineStyle().getIconUrl());
                            } else if (kmlContainer.getStyle(kmlPlacemark.getStyleId()) != null) {
                                airMapMarker.setImage(kmlContainer.getStyle(kmlPlacemark.getStyleId()).getIconUrl());
                            }
                            StringBuilder sb = new StringBuilder();
                            sb.append(property);
                            sb.append(" - ");
                            sb.append(valueOf);
                            String sb2 = sb.toString();
                            airMapMarker.setIdentifier(sb2);
                            Integer valueOf2 = Integer.valueOf(valueOf.intValue() + 1);
                            addFeature(airMapMarker, valueOf.intValue());
                            WritableMap makeClickEventData = makeClickEventData(latLng);
                            makeClickEventData.putString("id", sb2);
                            makeClickEventData.putString("title", property);
                            makeClickEventData.putString(str3, str5);
                            writableNativeArray.pushMap(makeClickEventData);
                            valueOf = valueOf2;
                            str2 = str6;
                        }
                        writableNativeMap.putArray("markers", writableNativeArray);
                        this.manager.pushEvent(this.context, this, str4, writableNativeMap);
                        return;
                    }
                }
                this.manager.pushEvent(this.context, this, str4, writableNativeMap);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (InterruptedException e3) {
            e3.printStackTrace();
        } catch (ExecutionException e4) {
            e4.printStackTrace();
        }
    }

    public void onIndoorBuildingFocused() {
        IndoorBuilding focusedBuilding = this.map.getFocusedBuilding();
        String str = "onIndoorBuildingFocused";
        String str2 = "IndoorBuilding";
        String str3 = "underground";
        String str4 = "activeLevelIndex";
        String str5 = "levels";
        int i = 0;
        if (focusedBuilding != null) {
            List<IndoorLevel> levels = focusedBuilding.getLevels();
            WritableArray createArray = Arguments.createArray();
            for (IndoorLevel indoorLevel : levels) {
                WritableMap createMap = Arguments.createMap();
                createMap.putInt("index", i);
                createMap.putString("name", indoorLevel.getName());
                createMap.putString("shortName", indoorLevel.getShortName());
                createArray.pushMap(createMap);
                i++;
            }
            WritableMap createMap2 = Arguments.createMap();
            WritableMap createMap3 = Arguments.createMap();
            createMap3.putArray(str5, createArray);
            createMap3.putInt(str4, focusedBuilding.getActiveLevelIndex());
            createMap3.putBoolean(str3, focusedBuilding.isUnderground());
            createMap2.putMap(str2, createMap3);
            this.manager.pushEvent(this.context, this, str, createMap2);
            return;
        }
        WritableMap createMap4 = Arguments.createMap();
        WritableArray createArray2 = Arguments.createArray();
        WritableMap createMap5 = Arguments.createMap();
        createMap5.putArray(str5, createArray2);
        createMap5.putInt(str4, 0);
        createMap5.putBoolean(str3, false);
        createMap4.putMap(str2, createMap5);
        this.manager.pushEvent(this.context, this, str, createMap4);
    }

    public void onIndoorLevelActivated(IndoorBuilding indoorBuilding) {
        if (indoorBuilding != null) {
            int activeLevelIndex = indoorBuilding.getActiveLevelIndex();
            if (activeLevelIndex >= 0 && activeLevelIndex < indoorBuilding.getLevels().size()) {
                IndoorLevel indoorLevel = (IndoorLevel) indoorBuilding.getLevels().get(activeLevelIndex);
                WritableMap createMap = Arguments.createMap();
                WritableMap createMap2 = Arguments.createMap();
                createMap2.putInt("activeLevelIndex", activeLevelIndex);
                createMap2.putString("name", indoorLevel.getName());
                createMap2.putString("shortName", indoorLevel.getShortName());
                createMap.putMap("IndoorLevel", createMap2);
                this.manager.pushEvent(this.context, this, "onIndoorLevelActivated", createMap);
            }
        }
    }

    public void setIndoorActiveLevelIndex(int i) {
        IndoorBuilding focusedBuilding = this.map.getFocusedBuilding();
        if (focusedBuilding != null && i >= 0 && i < focusedBuilding.getLevels().size()) {
            IndoorLevel indoorLevel = (IndoorLevel) focusedBuilding.getLevels().get(i);
            if (indoorLevel != null) {
                indoorLevel.activate();
            }
        }
    }

    /* access modifiers changed from: private */
    public AirMapMarker getMarkerMap(Marker marker) {
        AirMapMarker airMapMarker = (AirMapMarker) this.markerMap.get(marker);
        if (airMapMarker != null) {
            return airMapMarker;
        }
        Iterator it = this.markerMap.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Entry entry = (Entry) it.next();
            if (((Marker) entry.getKey()).getPosition().equals(marker.getPosition()) && ((Marker) entry.getKey()).getTitle().equals(marker.getTitle())) {
                airMapMarker = (AirMapMarker) entry.getValue();
                break;
            }
        }
        return airMapMarker;
    }
}
