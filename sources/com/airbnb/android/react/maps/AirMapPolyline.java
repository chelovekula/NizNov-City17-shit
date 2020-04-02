package com.airbnb.android.react.maps;

import android.content.Context;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import java.util.ArrayList;
import java.util.List;

public class AirMapPolyline extends AirMapFeature {
    private int color;
    private List<LatLng> coordinates;
    private boolean geodesic;
    private Cap lineCap = new RoundCap();
    private List<PatternItem> pattern;
    private ReadableArray patternValues;
    private Polyline polyline;
    private PolylineOptions polylineOptions;
    private boolean tappable;
    private float width;
    private float zIndex;

    public AirMapPolyline(Context context) {
        super(context);
    }

    public void setCoordinates(ReadableArray readableArray) {
        this.coordinates = new ArrayList(readableArray.size());
        for (int i = 0; i < readableArray.size(); i++) {
            ReadableMap map = readableArray.getMap(i);
            this.coordinates.add(i, new LatLng(map.getDouble("latitude"), map.getDouble("longitude")));
        }
        Polyline polyline2 = this.polyline;
        if (polyline2 != null) {
            polyline2.setPoints(this.coordinates);
        }
    }

    public void setColor(int i) {
        this.color = i;
        Polyline polyline2 = this.polyline;
        if (polyline2 != null) {
            polyline2.setColor(i);
        }
    }

    public void setWidth(float f) {
        this.width = f;
        Polyline polyline2 = this.polyline;
        if (polyline2 != null) {
            polyline2.setWidth(f);
        }
    }

    public void setZIndex(float f) {
        this.zIndex = f;
        Polyline polyline2 = this.polyline;
        if (polyline2 != null) {
            polyline2.setZIndex(f);
        }
    }

    public void setTappable(boolean z) {
        this.tappable = z;
        Polyline polyline2 = this.polyline;
        if (polyline2 != null) {
            polyline2.setClickable(this.tappable);
        }
    }

    public void setGeodesic(boolean z) {
        this.geodesic = z;
        Polyline polyline2 = this.polyline;
        if (polyline2 != null) {
            polyline2.setGeodesic(z);
        }
    }

    public void setLineCap(Cap cap) {
        this.lineCap = cap;
        Polyline polyline2 = this.polyline;
        if (polyline2 != null) {
            polyline2.setStartCap(cap);
            this.polyline.setEndCap(cap);
        }
        applyPattern();
    }

    public void setLineDashPattern(ReadableArray readableArray) {
        this.patternValues = readableArray;
        applyPattern();
    }

    private void applyPattern() {
        Object obj;
        ReadableArray readableArray = this.patternValues;
        if (readableArray != null) {
            this.pattern = new ArrayList(readableArray.size());
            for (int i = 0; i < this.patternValues.size(); i++) {
                float f = (float) this.patternValues.getDouble(i);
                if (i % 2 != 0) {
                    this.pattern.add(new Gap(f));
                } else {
                    if (this.lineCap instanceof RoundCap) {
                        obj = new Dot();
                    } else {
                        obj = new Dash(f);
                    }
                    this.pattern.add(obj);
                }
            }
            Polyline polyline2 = this.polyline;
            if (polyline2 != null) {
                polyline2.setPattern(this.pattern);
            }
        }
    }

    public PolylineOptions getPolylineOptions() {
        if (this.polylineOptions == null) {
            this.polylineOptions = createPolylineOptions();
        }
        return this.polylineOptions;
    }

    private PolylineOptions createPolylineOptions() {
        PolylineOptions polylineOptions2 = new PolylineOptions();
        polylineOptions2.addAll(this.coordinates);
        polylineOptions2.color(this.color);
        polylineOptions2.width(this.width);
        polylineOptions2.geodesic(this.geodesic);
        polylineOptions2.zIndex(this.zIndex);
        polylineOptions2.startCap(this.lineCap);
        polylineOptions2.endCap(this.lineCap);
        polylineOptions2.pattern(this.pattern);
        return polylineOptions2;
    }

    public Object getFeature() {
        return this.polyline;
    }

    public void addToMap(GoogleMap googleMap) {
        this.polyline = googleMap.addPolyline(getPolylineOptions());
        this.polyline.setClickable(this.tappable);
    }

    public void removeFromMap(GoogleMap googleMap) {
        this.polyline.remove();
    }
}
