package com.google.maps.android.data.geojson;

import android.util.Log;
import com.brentvatne.react.ReactVideoViewManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.data.Geometry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class GeoJsonParser {
    private static final String BOUNDING_BOX = "bbox";
    private static final String FEATURE = "Feature";
    private static final String FEATURE_COLLECTION = "FeatureCollection";
    private static final String FEATURE_COLLECTION_ARRAY = "features";
    private static final String FEATURE_GEOMETRY = "geometry";
    private static final String FEATURE_ID = "id";
    private static final String GEOMETRY_COLLECTION = "GeometryCollection";
    private static final String GEOMETRY_COLLECTION_ARRAY = "geometries";
    private static final String GEOMETRY_COORDINATES_ARRAY = "coordinates";
    private static final String LINESTRING = "LineString";
    private static final String LOG_TAG = "GeoJsonParser";
    private static final String MULTILINESTRING = "MultiLineString";
    private static final String MULTIPOINT = "MultiPoint";
    private static final String MULTIPOLYGON = "MultiPolygon";
    private static final String POINT = "Point";
    private static final String POLYGON = "Polygon";
    private static final String PROPERTIES = "properties";
    private LatLngBounds mBoundingBox = null;
    private final ArrayList<GeoJsonFeature> mGeoJsonFeatures = new ArrayList<>();
    private final JSONObject mGeoJsonFile;

    GeoJsonParser(JSONObject jSONObject) {
        this.mGeoJsonFile = jSONObject;
        parseGeoJson();
    }

    private static boolean isGeometry(String str) {
        return str.matches("Point|MultiPoint|LineString|MultiLineString|Polygon|MultiPolygon|GeometryCollection");
    }

    private static GeoJsonFeature parseFeature(JSONObject jSONObject) {
        String str = BOUNDING_BOX;
        String str2 = FEATURE_ID;
        String str3 = PROPERTIES;
        String str4 = FEATURE_GEOMETRY;
        HashMap hashMap = new HashMap();
        try {
            String string = jSONObject.has(str2) ? jSONObject.getString(str2) : null;
            LatLngBounds parseBoundingBox = jSONObject.has(str) ? parseBoundingBox(jSONObject.getJSONArray(str)) : null;
            Geometry parseGeometry = (!jSONObject.has(str4) || jSONObject.isNull(str4)) ? null : parseGeometry(jSONObject.getJSONObject(str4));
            if (jSONObject.has(str3) && !jSONObject.isNull(str3)) {
                hashMap = parseProperties(jSONObject.getJSONObject(str3));
            }
            return new GeoJsonFeature(parseGeometry, string, hashMap, parseBoundingBox);
        } catch (JSONException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append("Feature could not be successfully parsed ");
            sb.append(jSONObject.toString());
            Log.w(LOG_TAG, sb.toString());
            return null;
        }
    }

    private static LatLngBounds parseBoundingBox(JSONArray jSONArray) throws JSONException {
        return new LatLngBounds(new LatLng(jSONArray.getDouble(1), jSONArray.getDouble(0)), new LatLng(jSONArray.getDouble(3), jSONArray.getDouble(2)));
    }

    private static Geometry parseGeometry(JSONObject jSONObject) {
        JSONArray jSONArray;
        try {
            String string = jSONObject.getString(ReactVideoViewManager.PROP_SRC_TYPE);
            if (string.equals(GEOMETRY_COLLECTION)) {
                jSONArray = jSONObject.getJSONArray(GEOMETRY_COLLECTION_ARRAY);
            } else {
                if (isGeometry(string)) {
                    jSONArray = jSONObject.getJSONArray(GEOMETRY_COORDINATES_ARRAY);
                }
                return null;
            }
            return createGeometry(string, jSONArray);
        } catch (JSONException unused) {
        }
    }

    private static GeoJsonFeature parseGeometryToFeature(JSONObject jSONObject) {
        Geometry parseGeometry = parseGeometry(jSONObject);
        if (parseGeometry != null) {
            return new GeoJsonFeature(parseGeometry, null, new HashMap(), null);
        }
        Log.w(LOG_TAG, "Geometry could not be parsed");
        return null;
    }

    private static HashMap<String, String> parseProperties(JSONObject jSONObject) throws JSONException {
        HashMap<String, String> hashMap = new HashMap<>();
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            hashMap.put(str, jSONObject.isNull(str) ? null : jSONObject.getString(str));
        }
        return hashMap;
    }

    private static Geometry createGeometry(String str, JSONArray jSONArray) throws JSONException {
        if (str.equals(POINT)) {
            return createPoint(jSONArray);
        }
        if (str.equals(MULTIPOINT)) {
            return createMultiPoint(jSONArray);
        }
        if (str.equals(LINESTRING)) {
            return createLineString(jSONArray);
        }
        if (str.equals(MULTILINESTRING)) {
            return createMultiLineString(jSONArray);
        }
        if (str.equals("Polygon")) {
            return createPolygon(jSONArray);
        }
        if (str.equals(MULTIPOLYGON)) {
            return createMultiPolygon(jSONArray);
        }
        if (str.equals(GEOMETRY_COLLECTION)) {
            return createGeometryCollection(jSONArray);
        }
        return null;
    }

    private static GeoJsonPoint createPoint(JSONArray jSONArray) throws JSONException {
        return new GeoJsonPoint(parseCoordinate(jSONArray));
    }

    private static GeoJsonMultiPoint createMultiPoint(JSONArray jSONArray) throws JSONException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(createPoint(jSONArray.getJSONArray(i)));
        }
        return new GeoJsonMultiPoint(arrayList);
    }

    private static GeoJsonLineString createLineString(JSONArray jSONArray) throws JSONException {
        return new GeoJsonLineString(parseCoordinatesArray(jSONArray));
    }

    private static GeoJsonMultiLineString createMultiLineString(JSONArray jSONArray) throws JSONException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(createLineString(jSONArray.getJSONArray(i)));
        }
        return new GeoJsonMultiLineString(arrayList);
    }

    private static GeoJsonPolygon createPolygon(JSONArray jSONArray) throws JSONException {
        return new GeoJsonPolygon(parseCoordinatesArrays(jSONArray));
    }

    private static GeoJsonMultiPolygon createMultiPolygon(JSONArray jSONArray) throws JSONException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(createPolygon(jSONArray.getJSONArray(i)));
        }
        return new GeoJsonMultiPolygon(arrayList);
    }

    private static GeoJsonGeometryCollection createGeometryCollection(JSONArray jSONArray) throws JSONException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            Geometry parseGeometry = parseGeometry(jSONArray.getJSONObject(i));
            if (parseGeometry != null) {
                arrayList.add(parseGeometry);
            }
        }
        return new GeoJsonGeometryCollection(arrayList);
    }

    private static LatLng parseCoordinate(JSONArray jSONArray) throws JSONException {
        return new LatLng(jSONArray.getDouble(1), jSONArray.getDouble(0));
    }

    private static ArrayList<LatLng> parseCoordinatesArray(JSONArray jSONArray) throws JSONException {
        ArrayList<LatLng> arrayList = new ArrayList<>();
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(parseCoordinate(jSONArray.getJSONArray(i)));
        }
        return arrayList;
    }

    private static ArrayList<ArrayList<LatLng>> parseCoordinatesArrays(JSONArray jSONArray) throws JSONException {
        ArrayList<ArrayList<LatLng>> arrayList = new ArrayList<>();
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(parseCoordinatesArray(jSONArray.getJSONArray(i)));
        }
        return arrayList;
    }

    private void parseGeoJson() {
        String str = "GeoJSON file could not be parsed.";
        String str2 = LOG_TAG;
        try {
            String string = this.mGeoJsonFile.getString(ReactVideoViewManager.PROP_SRC_TYPE);
            if (string.equals(FEATURE)) {
                GeoJsonFeature parseFeature = parseFeature(this.mGeoJsonFile);
                if (parseFeature != null) {
                    this.mGeoJsonFeatures.add(parseFeature);
                }
            } else if (string.equals(FEATURE_COLLECTION)) {
                this.mGeoJsonFeatures.addAll(parseFeatureCollection(this.mGeoJsonFile));
            } else if (isGeometry(string)) {
                GeoJsonFeature parseGeometryToFeature = parseGeometryToFeature(this.mGeoJsonFile);
                if (parseGeometryToFeature != null) {
                    this.mGeoJsonFeatures.add(parseGeometryToFeature);
                }
            } else {
                Log.w(str2, str);
            }
        } catch (JSONException unused) {
            Log.w(str2, str);
        }
    }

    private ArrayList<GeoJsonFeature> parseFeatureCollection(JSONObject jSONObject) {
        String str = "Index of Feature in Feature Collection that could not be created: ";
        String str2 = BOUNDING_BOX;
        String str3 = LOG_TAG;
        ArrayList<GeoJsonFeature> arrayList = new ArrayList<>();
        try {
            JSONArray jSONArray = jSONObject.getJSONArray(FEATURE_COLLECTION_ARRAY);
            if (jSONObject.has(str2)) {
                this.mBoundingBox = parseBoundingBox(jSONObject.getJSONArray(str2));
            }
            for (int i = 0; i < jSONArray.length(); i++) {
                try {
                    JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                    if (jSONObject2.getString(ReactVideoViewManager.PROP_SRC_TYPE).equals(FEATURE)) {
                        GeoJsonFeature parseFeature = parseFeature(jSONObject2);
                        if (parseFeature != null) {
                            arrayList.add(parseFeature);
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append(str);
                            sb.append(i);
                            Log.w(str3, sb.toString());
                        }
                    }
                } catch (JSONException unused) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(i);
                    Log.w(str3, sb2.toString());
                }
            }
            return arrayList;
        } catch (JSONException unused2) {
            Log.w(str3, "Feature Collection could not be created.");
            return arrayList;
        }
    }

    /* access modifiers changed from: 0000 */
    public ArrayList<GeoJsonFeature> getFeatures() {
        return this.mGeoJsonFeatures;
    }

    /* access modifiers changed from: 0000 */
    public LatLngBounds getBoundingBox() {
        return this.mBoundingBox;
    }
}
