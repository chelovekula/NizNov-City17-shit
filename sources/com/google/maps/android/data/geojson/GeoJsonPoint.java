package com.google.maps.android.data.geojson;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.Point;

public class GeoJsonPoint extends Point {
    public GeoJsonPoint(LatLng latLng) {
        super(latLng);
    }

    public String getType() {
        return getGeometryType();
    }

    public LatLng getCoordinates() {
        return getGeometryObject();
    }
}
