package com.facebook.react.bridge;

import android.util.JsonWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JsonWriterHelper {
    public static void value(JsonWriter jsonWriter, Object obj) throws IOException {
        if (obj instanceof Map) {
            mapValue(jsonWriter, (Map) obj);
        } else if (obj instanceof List) {
            listValue(jsonWriter, (List) obj);
        } else if (obj instanceof ReadableMap) {
            readableMapValue(jsonWriter, (ReadableMap) obj);
        } else if (obj instanceof ReadableArray) {
            readableArrayValue(jsonWriter, (ReadableArray) obj);
        } else if (obj instanceof Dynamic) {
            dynamicValue(jsonWriter, (Dynamic) obj);
        } else {
            objectValue(jsonWriter, obj);
        }
    }

    private static void dynamicValue(JsonWriter jsonWriter, Dynamic dynamic) throws IOException {
        switch (dynamic.getType()) {
            case Null:
                jsonWriter.nullValue();
                return;
            case Boolean:
                jsonWriter.value(dynamic.asBoolean());
                return;
            case Number:
                jsonWriter.value(dynamic.asDouble());
                return;
            case String:
                jsonWriter.value(dynamic.asString());
                return;
            case Map:
                readableMapValue(jsonWriter, dynamic.asMap());
                return;
            case Array:
                readableArrayValue(jsonWriter, dynamic.asArray());
                return;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown data type: ");
                sb.append(dynamic.getType());
                throw new IllegalArgumentException(sb.toString());
        }
    }

    private static void readableMapValue(JsonWriter jsonWriter, ReadableMap readableMap) throws IOException {
        jsonWriter.beginObject();
        try {
            ReadableMapKeySetIterator keySetIterator = readableMap.keySetIterator();
            while (keySetIterator.hasNextKey()) {
                String nextKey = keySetIterator.nextKey();
                jsonWriter.name(nextKey);
                switch (readableMap.getType(nextKey)) {
                    case Null:
                        jsonWriter.nullValue();
                        break;
                    case Boolean:
                        jsonWriter.value(readableMap.getBoolean(nextKey));
                        break;
                    case Number:
                        jsonWriter.value(readableMap.getDouble(nextKey));
                        break;
                    case String:
                        jsonWriter.value(readableMap.getString(nextKey));
                        break;
                    case Map:
                        readableMapValue(jsonWriter, readableMap.getMap(nextKey));
                        break;
                    case Array:
                        readableArrayValue(jsonWriter, readableMap.getArray(nextKey));
                        break;
                    default:
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unknown data type: ");
                        sb.append(readableMap.getType(nextKey));
                        throw new IllegalArgumentException(sb.toString());
                }
            }
        } finally {
            jsonWriter.endObject();
        }
    }

    public static void readableArrayValue(JsonWriter jsonWriter, ReadableArray readableArray) throws IOException {
        jsonWriter.beginArray();
        int i = 0;
        while (i < readableArray.size()) {
            try {
                switch (readableArray.getType(i)) {
                    case Null:
                        jsonWriter.nullValue();
                        break;
                    case Boolean:
                        jsonWriter.value(readableArray.getBoolean(i));
                        break;
                    case Number:
                        jsonWriter.value(readableArray.getDouble(i));
                        break;
                    case String:
                        jsonWriter.value(readableArray.getString(i));
                        break;
                    case Map:
                        readableMapValue(jsonWriter, readableArray.getMap(i));
                        break;
                    case Array:
                        readableArrayValue(jsonWriter, readableArray.getArray(i));
                        break;
                    default:
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unknown data type: ");
                        sb.append(readableArray.getType(i));
                        throw new IllegalArgumentException(sb.toString());
                }
                i++;
            } finally {
                jsonWriter.endArray();
            }
        }
    }

    private static void mapValue(JsonWriter jsonWriter, Map<?, ?> map) throws IOException {
        jsonWriter.beginObject();
        for (Entry entry : map.entrySet()) {
            jsonWriter.name(entry.getKey().toString());
            value(jsonWriter, entry.getValue());
        }
        jsonWriter.endObject();
    }

    private static void listValue(JsonWriter jsonWriter, List<?> list) throws IOException {
        jsonWriter.beginArray();
        for (Object objectValue : list) {
            objectValue(jsonWriter, objectValue);
        }
        jsonWriter.endArray();
    }

    private static void objectValue(JsonWriter jsonWriter, Object obj) throws IOException {
        if (obj == null) {
            jsonWriter.nullValue();
        } else if (obj instanceof String) {
            jsonWriter.value((String) obj);
        } else if (obj instanceof Number) {
            jsonWriter.value((Number) obj);
        } else if (obj instanceof Boolean) {
            jsonWriter.value(((Boolean) obj).booleanValue());
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Unknown value: ");
            sb.append(obj);
            throw new IllegalArgumentException(sb.toString());
        }
    }
}
