package com.google.maps.android.data.geojson;

import android.content.Context;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.data.Layer;
import com.google.maps.android.data.Layer.OnFeatureClickListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

public class GeoJsonLayer extends Layer {
    private LatLngBounds mBoundingBox;

    public interface GeoJsonOnFeatureClickListener extends OnFeatureClickListener {
    }

    public GeoJsonLayer(GoogleMap googleMap, JSONObject jSONObject) {
        if (jSONObject != null) {
            this.mBoundingBox = null;
            GeoJsonParser geoJsonParser = new GeoJsonParser(jSONObject);
            this.mBoundingBox = geoJsonParser.getBoundingBox();
            HashMap hashMap = new HashMap();
            Iterator it = geoJsonParser.getFeatures().iterator();
            while (it.hasNext()) {
                hashMap.put((GeoJsonFeature) it.next(), null);
            }
            storeRenderer(new GeoJsonRenderer(googleMap, hashMap));
            return;
        }
        throw new IllegalArgumentException("GeoJSON file cannot be null");
    }

    public GeoJsonLayer(GoogleMap googleMap, int i, Context context) throws IOException, JSONException {
        this(googleMap, createJsonFileObject(context.getResources().openRawResource(i)));
    }

    private static JSONObject createJsonFileObject(InputStream inputStream) throws IOException, JSONException {
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine != null) {
                sb.append(readLine);
            } else {
                bufferedReader.close();
                return new JSONObject(sb.toString());
            }
        }
    }

    public void addLayerToMap() {
        super.addGeoJsonToMap();
    }

    public Iterable<GeoJsonFeature> getFeatures() {
        return super.getFeatures();
    }

    public void addFeature(GeoJsonFeature geoJsonFeature) {
        if (geoJsonFeature != null) {
            super.addFeature(geoJsonFeature);
            return;
        }
        throw new IllegalArgumentException("Feature cannot be null");
    }

    public void removeFeature(GeoJsonFeature geoJsonFeature) {
        if (geoJsonFeature != null) {
            super.removeFeature(geoJsonFeature);
            return;
        }
        throw new IllegalArgumentException("Feature cannot be null");
    }

    public LatLngBounds getBoundingBox() {
        return this.mBoundingBox;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Collection{");
        sb.append("\n Bounding box=");
        sb.append(this.mBoundingBox);
        sb.append("\n}\n");
        return sb.toString();
    }
}
