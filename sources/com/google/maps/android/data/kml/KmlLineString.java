package com.google.maps.android.data.kml;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.LineString;
import java.util.ArrayList;

public class KmlLineString extends LineString {
    public KmlLineString(ArrayList<LatLng> arrayList) {
        super(arrayList);
    }

    public ArrayList<LatLng> getGeometryObject() {
        return new ArrayList<>(super.getGeometryObject());
    }
}
