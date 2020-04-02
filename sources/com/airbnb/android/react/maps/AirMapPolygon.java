package com.airbnb.android.react.maps;

import android.content.Context;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import java.util.ArrayList;
import java.util.List;

public class AirMapPolygon extends AirMapFeature {
    private List<LatLng> coordinates;
    private int fillColor;
    private boolean geodesic;
    private List<List<LatLng>> holes;
    private Polygon polygon;
    private PolygonOptions polygonOptions;
    private int strokeColor;
    private float strokeWidth;
    private boolean tappable;
    private float zIndex;

    public AirMapPolygon(Context context) {
        super(context);
    }

    public void setCoordinates(ReadableArray readableArray) {
        this.coordinates = new ArrayList(readableArray.size());
        for (int i = 0; i < readableArray.size(); i++) {
            ReadableMap map = readableArray.getMap(i);
            this.coordinates.add(i, new LatLng(map.getDouble("latitude"), map.getDouble("longitude")));
        }
        Polygon polygon2 = this.polygon;
        if (polygon2 != null) {
            polygon2.setPoints(this.coordinates);
        }
    }

    public void setHoles(ReadableArray readableArray) {
        if (readableArray != null) {
            this.holes = new ArrayList(readableArray.size());
            for (int i = 0; i < readableArray.size(); i++) {
                ReadableArray array = readableArray.getArray(i);
                if (array.size() >= 3) {
                    ArrayList arrayList = new ArrayList();
                    for (int i2 = 0; i2 < array.size(); i2++) {
                        ReadableMap map = array.getMap(i2);
                        arrayList.add(new LatLng(map.getDouble("latitude"), map.getDouble("longitude")));
                    }
                    if (arrayList.size() == 3) {
                        arrayList.add(arrayList.get(0));
                    }
                    this.holes.add(arrayList);
                }
            }
            Polygon polygon2 = this.polygon;
            if (polygon2 != null) {
                polygon2.setHoles(this.holes);
            }
        }
    }

    public void setFillColor(int i) {
        this.fillColor = i;
        Polygon polygon2 = this.polygon;
        if (polygon2 != null) {
            polygon2.setFillColor(i);
        }
    }

    public void setStrokeColor(int i) {
        this.strokeColor = i;
        Polygon polygon2 = this.polygon;
        if (polygon2 != null) {
            polygon2.setStrokeColor(i);
        }
    }

    public void setStrokeWidth(float f) {
        this.strokeWidth = f;
        Polygon polygon2 = this.polygon;
        if (polygon2 != null) {
            polygon2.setStrokeWidth(f);
        }
    }

    public void setTappable(boolean z) {
        this.tappable = z;
        Polygon polygon2 = this.polygon;
        if (polygon2 != null) {
            polygon2.setClickable(this.tappable);
        }
    }

    public void setGeodesic(boolean z) {
        this.geodesic = z;
        Polygon polygon2 = this.polygon;
        if (polygon2 != null) {
            polygon2.setGeodesic(z);
        }
    }

    public void setZIndex(float f) {
        this.zIndex = f;
        Polygon polygon2 = this.polygon;
        if (polygon2 != null) {
            polygon2.setZIndex(f);
        }
    }

    public PolygonOptions getPolygonOptions() {
        if (this.polygonOptions == null) {
            this.polygonOptions = createPolygonOptions();
        }
        return this.polygonOptions;
    }

    private PolygonOptions createPolygonOptions() {
        PolygonOptions polygonOptions2 = new PolygonOptions();
        polygonOptions2.addAll(this.coordinates);
        polygonOptions2.fillColor(this.fillColor);
        polygonOptions2.strokeColor(this.strokeColor);
        polygonOptions2.strokeWidth(this.strokeWidth);
        polygonOptions2.geodesic(this.geodesic);
        polygonOptions2.zIndex(this.zIndex);
        if (this.holes != null) {
            for (int i = 0; i < this.holes.size(); i++) {
                polygonOptions2.addHole((Iterable) this.holes.get(i));
            }
        }
        return polygonOptions2;
    }

    public Object getFeature() {
        return this.polygon;
    }

    public void addToMap(GoogleMap googleMap) {
        this.polygon = googleMap.addPolygon(getPolygonOptions());
        this.polygon.setClickable(this.tappable);
    }

    public void removeFromMap(GoogleMap googleMap) {
        this.polygon.remove();
    }
}
