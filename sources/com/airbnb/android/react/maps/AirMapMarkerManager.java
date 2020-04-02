package com.airbnb.android.react.maps;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.view.View;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nullable;

public class AirMapMarkerManager extends ViewGroupManager<AirMapMarker> {
    private static final int ANIMATE_MARKER_TO_COORDINATE = 3;
    private static final int HIDE_INFO_WINDOW = 2;
    private static final int REDRAW = 4;
    private static final int SHOW_INFO_WINDOW = 1;
    private Map<String, AirMapMarkerSharedIcon> sharedIcons = new ConcurrentHashMap();

    public static class AirMapMarkerSharedIcon {
        private Bitmap bitmap;
        private BitmapDescriptor iconBitmapDescriptor;
        private boolean loadImageStarted = false;
        private Map<AirMapMarker, Boolean> markers = new WeakHashMap();

        public synchronized boolean shouldLoadImage() {
            if (this.loadImageStarted) {
                return false;
            }
            this.loadImageStarted = true;
            return true;
        }

        public synchronized void addMarker(AirMapMarker airMapMarker) {
            this.markers.put(airMapMarker, Boolean.valueOf(true));
            if (this.iconBitmapDescriptor != null) {
                airMapMarker.setIconBitmapDescriptor(this.iconBitmapDescriptor, this.bitmap);
            }
        }

        public synchronized void removeMarker(AirMapMarker airMapMarker) {
            this.markers.remove(airMapMarker);
        }

        public synchronized boolean hasMarker() {
            return this.markers.isEmpty();
        }

        public synchronized void updateIcon(BitmapDescriptor bitmapDescriptor, Bitmap bitmap2) {
            this.iconBitmapDescriptor = bitmapDescriptor;
            this.bitmap = bitmap2.copy(Config.ARGB_8888, true);
            if (!this.markers.isEmpty()) {
                for (Entry entry : this.markers.entrySet()) {
                    if (entry.getKey() != null) {
                        ((AirMapMarker) entry.getKey()).setIconBitmapDescriptor(bitmapDescriptor, bitmap2);
                    }
                }
            }
        }
    }

    public String getName() {
        return "AIRMapMarker";
    }

    public AirMapMarkerSharedIcon getSharedIcon(String str) {
        AirMapMarkerSharedIcon airMapMarkerSharedIcon = (AirMapMarkerSharedIcon) this.sharedIcons.get(str);
        if (airMapMarkerSharedIcon == null) {
            synchronized (this) {
                airMapMarkerSharedIcon = (AirMapMarkerSharedIcon) this.sharedIcons.get(str);
                if (airMapMarkerSharedIcon == null) {
                    airMapMarkerSharedIcon = new AirMapMarkerSharedIcon();
                    this.sharedIcons.put(str, airMapMarkerSharedIcon);
                }
            }
        }
        return airMapMarkerSharedIcon;
    }

    public void removeSharedIconIfEmpty(String str) {
        AirMapMarkerSharedIcon airMapMarkerSharedIcon = (AirMapMarkerSharedIcon) this.sharedIcons.get(str);
        if (airMapMarkerSharedIcon != null && !airMapMarkerSharedIcon.hasMarker()) {
            synchronized (this) {
                AirMapMarkerSharedIcon airMapMarkerSharedIcon2 = (AirMapMarkerSharedIcon) this.sharedIcons.get(str);
                if (airMapMarkerSharedIcon2 != null && !airMapMarkerSharedIcon2.hasMarker()) {
                    this.sharedIcons.remove(str);
                }
            }
        }
    }

    public AirMapMarker createViewInstance(ThemedReactContext themedReactContext) {
        return new AirMapMarker(themedReactContext, this);
    }

    @ReactProp(name = "coordinate")
    public void setCoordinate(AirMapMarker airMapMarker, ReadableMap readableMap) {
        airMapMarker.setCoordinate(readableMap);
    }

    @ReactProp(name = "title")
    public void setTitle(AirMapMarker airMapMarker, String str) {
        airMapMarker.setTitle(str);
    }

    @ReactProp(name = "identifier")
    public void setIdentifier(AirMapMarker airMapMarker, String str) {
        airMapMarker.setIdentifier(str);
    }

    @ReactProp(name = "description")
    public void setDescription(AirMapMarker airMapMarker, String str) {
        airMapMarker.setSnippet(str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:6:0x0013  */
    @com.facebook.react.uimanager.annotations.ReactProp(name = "anchor")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setAnchor(com.airbnb.android.react.maps.AirMapMarker r5, com.facebook.react.bridge.ReadableMap r6) {
        /*
            r4 = this;
            if (r6 == 0) goto L_0x000f
            java.lang.String r0 = "x"
            boolean r1 = r6.hasKey(r0)
            if (r1 == 0) goto L_0x000f
            double r0 = r6.getDouble(r0)
            goto L_0x0011
        L_0x000f:
            r0 = 4602678819172646912(0x3fe0000000000000, double:0.5)
        L_0x0011:
            if (r6 == 0) goto L_0x0020
            java.lang.String r2 = "y"
            boolean r3 = r6.hasKey(r2)
            if (r3 == 0) goto L_0x0020
            double r2 = r6.getDouble(r2)
            goto L_0x0022
        L_0x0020:
            r2 = 4607182418800017408(0x3ff0000000000000, double:1.0)
        L_0x0022:
            r5.setAnchor(r0, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.android.react.maps.AirMapMarkerManager.setAnchor(com.airbnb.android.react.maps.AirMapMarker, com.facebook.react.bridge.ReadableMap):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:6:0x0013  */
    @com.facebook.react.uimanager.annotations.ReactProp(name = "calloutAnchor")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setCalloutAnchor(com.airbnb.android.react.maps.AirMapMarker r5, com.facebook.react.bridge.ReadableMap r6) {
        /*
            r4 = this;
            if (r6 == 0) goto L_0x000f
            java.lang.String r0 = "x"
            boolean r1 = r6.hasKey(r0)
            if (r1 == 0) goto L_0x000f
            double r0 = r6.getDouble(r0)
            goto L_0x0011
        L_0x000f:
            r0 = 4602678819172646912(0x3fe0000000000000, double:0.5)
        L_0x0011:
            if (r6 == 0) goto L_0x0020
            java.lang.String r2 = "y"
            boolean r3 = r6.hasKey(r2)
            if (r3 == 0) goto L_0x0020
            double r2 = r6.getDouble(r2)
            goto L_0x0022
        L_0x0020:
            r2 = 0
        L_0x0022:
            r5.setCalloutAnchor(r0, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.android.react.maps.AirMapMarkerManager.setCalloutAnchor(com.airbnb.android.react.maps.AirMapMarker, com.facebook.react.bridge.ReadableMap):void");
    }

    @ReactProp(name = "image")
    public void setImage(AirMapMarker airMapMarker, @Nullable String str) {
        airMapMarker.setImage(str);
    }

    @ReactProp(name = "icon")
    public void setIcon(AirMapMarker airMapMarker, @Nullable String str) {
        airMapMarker.setImage(str);
    }

    @ReactProp(customType = "Color", defaultInt = -65536, name = "pinColor")
    public void setPinColor(AirMapMarker airMapMarker, int i) {
        float[] fArr = new float[3];
        Color.colorToHSV(i, fArr);
        airMapMarker.setMarkerHue(fArr[0]);
    }

    @ReactProp(defaultFloat = 0.0f, name = "rotation")
    public void setMarkerRotation(AirMapMarker airMapMarker, float f) {
        airMapMarker.setRotation(f);
    }

    @ReactProp(defaultBoolean = false, name = "flat")
    public void setFlat(AirMapMarker airMapMarker, boolean z) {
        airMapMarker.setFlat(z);
    }

    @ReactProp(defaultBoolean = false, name = "draggable")
    public void setDraggable(AirMapMarker airMapMarker, boolean z) {
        airMapMarker.setDraggable(z);
    }

    @ReactProp(defaultFloat = 0.0f, name = "zIndex")
    public void setZIndex(AirMapMarker airMapMarker, float f) {
        super.setZIndex(airMapMarker, f);
        airMapMarker.setZIndex(Math.round(f));
    }

    @ReactProp(defaultFloat = 1.0f, name = "opacity")
    public void setOpacity(AirMapMarker airMapMarker, float f) {
        super.setOpacity(airMapMarker, f);
        airMapMarker.setOpacity(f);
    }

    @ReactProp(defaultBoolean = true, name = "tracksViewChanges")
    public void setTracksViewChanges(AirMapMarker airMapMarker, boolean z) {
        airMapMarker.setTracksViewChanges(z);
    }

    public void addView(AirMapMarker airMapMarker, View view, int i) {
        if (view instanceof AirMapCallout) {
            airMapMarker.setCalloutView((AirMapCallout) view);
            return;
        }
        super.addView(airMapMarker, view, i);
        airMapMarker.update(true);
    }

    public void removeViewAt(AirMapMarker airMapMarker, int i) {
        super.removeViewAt(airMapMarker, i);
        airMapMarker.update(true);
    }

    @Nullable
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.m128of("showCallout", Integer.valueOf(1), "hideCallout", Integer.valueOf(2), "animateMarkerToCoordinate", Integer.valueOf(3), "redraw", Integer.valueOf(4));
    }

    public void receiveCommand(AirMapMarker airMapMarker, int i, @Nullable ReadableArray readableArray) {
        if (i == 1) {
            ((Marker) airMapMarker.getFeature()).showInfoWindow();
        } else if (i == 2) {
            ((Marker) airMapMarker.getFeature()).hideInfoWindow();
        } else if (i == 3) {
            ReadableMap map = readableArray.getMap(0);
            airMapMarker.animateToCoodinate(new LatLng(Double.valueOf(map.getDouble("latitude")).doubleValue(), Double.valueOf(map.getDouble("longitude")).doubleValue()), Integer.valueOf(readableArray.getInt(1)));
        } else if (i == 4) {
            airMapMarker.updateMarkerIcon();
        }
    }

    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        String str = "registrationName";
        String str2 = "onDragStart";
        String str3 = "onDrag";
        String str4 = "onDragEnd";
        String str5 = "onPress";
        String str6 = "onCalloutPress";
        Map of = MapBuilder.m129of(str5, MapBuilder.m125of(str, "onPress"), str6, MapBuilder.m125of(str, "onCalloutPress"), "onDragStart", MapBuilder.m125of(str, str2), "onDrag", MapBuilder.m125of(str, str3), "onDragEnd", MapBuilder.m125of(str, str4));
        of.putAll(MapBuilder.m127of("onDragStart", MapBuilder.m125of(str, str2), "onDrag", MapBuilder.m125of(str, str3), "onDragEnd", MapBuilder.m125of(str, str4)));
        return of;
    }

    public LayoutShadowNode createShadowNodeInstance() {
        return new SizeReportingShadowNode();
    }

    public void updateExtraData(AirMapMarker airMapMarker, Object obj) {
        HashMap hashMap = (HashMap) obj;
        airMapMarker.update((int) ((Float) hashMap.get("width")).floatValue(), (int) ((Float) hashMap.get("height")).floatValue());
    }
}
