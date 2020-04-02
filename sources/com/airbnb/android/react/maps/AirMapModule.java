package com.airbnb.android.react.maps;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Point;
import android.net.Uri;
import android.util.Base64;
import android.util.DisplayMetrics;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.facebook.react.uimanager.UIBlock;
import com.facebook.react.uimanager.UIManagerModule;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

public class AirMapModule extends ReactContextBaseJavaModule {
    private static final String SNAPSHOT_FORMAT_JPG = "jpg";
    private static final String SNAPSHOT_FORMAT_PNG = "png";
    private static final String SNAPSHOT_RESULT_BASE64 = "base64";
    private static final String SNAPSHOT_RESULT_FILE = "file";

    public String getName() {
        return "AirMapModule";
    }

    public AirMapModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    public Map<String, Object> getConstants() {
        HashMap hashMap = new HashMap();
        hashMap.put("legalNotice", "This license information is displayed in Settings > Google > Open Source on any device running Google Play services.");
        return hashMap;
    }

    public Activity getActivity() {
        return getCurrentActivity();
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }

    @ReactMethod
    public void takeSnapshot(int i, ReadableMap readableMap, Promise promise) {
        int i2;
        ReadableMap readableMap2 = readableMap;
        final ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        String str = "format";
        boolean hasKey = readableMap2.hasKey(str);
        String str2 = SNAPSHOT_FORMAT_PNG;
        final String string = hasKey ? readableMap2.getString(str) : str2;
        CompressFormat compressFormat = string.equals(str2) ? CompressFormat.PNG : string.equals(SNAPSHOT_FORMAT_JPG) ? CompressFormat.JPEG : null;
        final CompressFormat compressFormat2 = compressFormat;
        String str3 = "quality";
        final double d = readableMap2.hasKey(str3) ? readableMap2.getDouble(str3) : 1.0d;
        DisplayMetrics displayMetrics = reactApplicationContext.getResources().getDisplayMetrics();
        String str4 = "width";
        int i3 = 0;
        if (readableMap2.hasKey(str4)) {
            double d2 = (double) displayMetrics.density;
            double d3 = readableMap2.getDouble(str4);
            Double.isNaN(d2);
            i2 = (int) (d2 * d3);
        } else {
            i2 = 0;
        }
        Integer valueOf = Integer.valueOf(i2);
        String str5 = "height";
        if (readableMap2.hasKey(str5)) {
            double d4 = (double) displayMetrics.density;
            double d5 = readableMap2.getDouble(str5);
            Double.isNaN(d4);
            i3 = (int) (d4 * d5);
        }
        Integer valueOf2 = Integer.valueOf(i3);
        String str6 = "result";
        UIManagerModule uIManagerModule = (UIManagerModule) reactApplicationContext.getNativeModule(UIManagerModule.class);
        final int i4 = i;
        final Promise promise2 = promise;
        final Integer num = valueOf;
        final Integer num2 = valueOf2;
        final String string2 = readableMap2.hasKey(str6) ? readableMap2.getString(str6) : "file";
        C04351 r0 = new UIBlock() {
            public void execute(NativeViewHierarchyManager nativeViewHierarchyManager) {
                AirMapView airMapView = (AirMapView) nativeViewHierarchyManager.resolveView(i4);
                if (airMapView == null) {
                    promise2.reject("AirMapView not found");
                } else if (airMapView.map == null) {
                    promise2.reject("AirMapView.map is not valid");
                } else {
                    airMapView.map.snapshot(new SnapshotReadyCallback() {
                        public void onSnapshotReady(@Nullable Bitmap bitmap) {
                            if (bitmap == null) {
                                promise2.reject("Failed to generate bitmap, snapshot = null");
                                return;
                            }
                            if (!(num.intValue() == 0 || num2.intValue() == 0 || (num.intValue() == bitmap.getWidth() && num2.intValue() == bitmap.getHeight()))) {
                                bitmap = Bitmap.createScaledBitmap(bitmap, num.intValue(), num2.intValue(), true);
                            }
                            if (string2.equals("file")) {
                                String str = "AirMapSnapshot";
                                try {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(".");
                                    sb.append(string);
                                    File createTempFile = File.createTempFile(str, sb.toString(), reactApplicationContext.getCacheDir());
                                    FileOutputStream fileOutputStream = new FileOutputStream(createTempFile);
                                    bitmap.compress(compressFormat2, (int) (d * 100.0d), fileOutputStream);
                                    AirMapModule.closeQuietly(fileOutputStream);
                                    promise2.resolve(Uri.fromFile(createTempFile).toString());
                                } catch (Exception e) {
                                    promise2.reject((Throwable) e);
                                }
                            } else if (string2.equals("base64")) {
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(compressFormat2, (int) (d * 100.0d), byteArrayOutputStream);
                                AirMapModule.closeQuietly(byteArrayOutputStream);
                                promise2.resolve(Base64.encodeToString(byteArrayOutputStream.toByteArray(), 2));
                            }
                        }
                    });
                }
            }
        };
        uIManagerModule.addUIBlock(r0);
    }

    @ReactMethod
    public void getCamera(final int i, final Promise promise) {
        ((UIManagerModule) getReactApplicationContext().getNativeModule(UIManagerModule.class)).addUIBlock(new UIBlock() {
            public void execute(NativeViewHierarchyManager nativeViewHierarchyManager) {
                AirMapView airMapView = (AirMapView) nativeViewHierarchyManager.resolveView(i);
                if (airMapView == null) {
                    promise.reject("AirMapView not found");
                } else if (airMapView.map == null) {
                    promise.reject("AirMapView.map is not valid");
                } else {
                    CameraPosition cameraPosition = airMapView.map.getCameraPosition();
                    WritableNativeMap writableNativeMap = new WritableNativeMap();
                    writableNativeMap.putDouble("latitude", cameraPosition.target.latitude);
                    writableNativeMap.putDouble("longitude", cameraPosition.target.longitude);
                    WritableNativeMap writableNativeMap2 = new WritableNativeMap();
                    writableNativeMap2.putMap("center", writableNativeMap);
                    writableNativeMap2.putDouble("heading", (double) cameraPosition.bearing);
                    writableNativeMap2.putDouble("zoom", (double) cameraPosition.zoom);
                    writableNativeMap2.putDouble("pitch", (double) cameraPosition.tilt);
                    promise.resolve(writableNativeMap2);
                }
            }
        });
    }

    @ReactMethod
    public void pointForCoordinate(int i, ReadableMap readableMap, Promise promise) {
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        final double d = (double) reactApplicationContext.getResources().getDisplayMetrics().density;
        String str = "latitude";
        double d2 = 0.0d;
        double d3 = readableMap.hasKey(str) ? readableMap.getDouble(str) : 0.0d;
        String str2 = "longitude";
        if (readableMap.hasKey(str2)) {
            d2 = readableMap.getDouble(str2);
        }
        final LatLng latLng = new LatLng(d3, d2);
        UIManagerModule uIManagerModule = (UIManagerModule) reactApplicationContext.getNativeModule(UIManagerModule.class);
        final int i2 = i;
        final Promise promise2 = promise;
        C04383 r2 = new UIBlock() {
            public void execute(NativeViewHierarchyManager nativeViewHierarchyManager) {
                AirMapView airMapView = (AirMapView) nativeViewHierarchyManager.resolveView(i2);
                if (airMapView == null) {
                    promise2.reject("AirMapView not found");
                } else if (airMapView.map == null) {
                    promise2.reject("AirMapView.map is not valid");
                } else {
                    Point screenLocation = airMapView.map.getProjection().toScreenLocation(latLng);
                    WritableNativeMap writableNativeMap = new WritableNativeMap();
                    double d = (double) screenLocation.x;
                    double d2 = d;
                    Double.isNaN(d);
                    writableNativeMap.putDouble("x", d / d2);
                    double d3 = (double) screenLocation.y;
                    double d4 = d;
                    Double.isNaN(d3);
                    writableNativeMap.putDouble("y", d3 / d4);
                    promise2.resolve(writableNativeMap);
                }
            }
        };
        uIManagerModule.addUIBlock(r2);
    }

    @ReactMethod
    public void coordinateForPoint(final int i, ReadableMap readableMap, final Promise promise) {
        int i2;
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        double d = (double) reactApplicationContext.getResources().getDisplayMetrics().density;
        String str = "x";
        int i3 = 0;
        if (readableMap.hasKey(str)) {
            double d2 = readableMap.getDouble(str);
            Double.isNaN(d);
            i2 = (int) (d2 * d);
        } else {
            i2 = 0;
        }
        String str2 = "y";
        if (readableMap.hasKey(str2)) {
            double d3 = readableMap.getDouble(str2);
            Double.isNaN(d);
            i3 = (int) (d3 * d);
        }
        final Point point = new Point(i2, i3);
        ((UIManagerModule) reactApplicationContext.getNativeModule(UIManagerModule.class)).addUIBlock(new UIBlock() {
            public void execute(NativeViewHierarchyManager nativeViewHierarchyManager) {
                AirMapView airMapView = (AirMapView) nativeViewHierarchyManager.resolveView(i);
                if (airMapView == null) {
                    promise.reject("AirMapView not found");
                } else if (airMapView.map == null) {
                    promise.reject("AirMapView.map is not valid");
                } else {
                    LatLng fromScreenLocation = airMapView.map.getProjection().fromScreenLocation(point);
                    WritableNativeMap writableNativeMap = new WritableNativeMap();
                    writableNativeMap.putDouble("latitude", fromScreenLocation.latitude);
                    writableNativeMap.putDouble("longitude", fromScreenLocation.longitude);
                    promise.resolve(writableNativeMap);
                }
            }
        });
    }

    @ReactMethod
    public void getMapBoundaries(final int i, final Promise promise) {
        ((UIManagerModule) getReactApplicationContext().getNativeModule(UIManagerModule.class)).addUIBlock(new UIBlock() {
            public void execute(NativeViewHierarchyManager nativeViewHierarchyManager) {
                AirMapView airMapView = (AirMapView) nativeViewHierarchyManager.resolveView(i);
                if (airMapView == null) {
                    promise.reject("AirMapView not found");
                } else if (airMapView.map == null) {
                    promise.reject("AirMapView.map is not valid");
                } else {
                    double[][] mapBoundaries = airMapView.getMapBoundaries();
                    WritableNativeMap writableNativeMap = new WritableNativeMap();
                    WritableNativeMap writableNativeMap2 = new WritableNativeMap();
                    WritableNativeMap writableNativeMap3 = new WritableNativeMap();
                    String str = "longitude";
                    writableNativeMap2.putDouble(str, mapBoundaries[0][0]);
                    String str2 = "latitude";
                    writableNativeMap2.putDouble(str2, mapBoundaries[0][1]);
                    writableNativeMap3.putDouble(str, mapBoundaries[1][0]);
                    writableNativeMap3.putDouble(str2, mapBoundaries[1][1]);
                    writableNativeMap.putMap("northEast", writableNativeMap2);
                    writableNativeMap.putMap("southWest", writableNativeMap3);
                    promise.resolve(writableNativeMap);
                }
            }
        });
    }
}
