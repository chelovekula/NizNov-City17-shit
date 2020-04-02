package com.google.maps.android.data.geojson;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BiMultiMap<K> extends HashMap<K, Object> {
    private final Map<Object, K> mValuesToKeys = new HashMap();

    public void putAll(Map<? extends K, ?> map) {
        for (Entry entry : map.entrySet()) {
            put((K) entry.getKey(), entry.getValue());
        }
    }

    public Object put(K k, Object obj) {
        this.mValuesToKeys.put(obj, k);
        return super.put(k, obj);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<java.lang.Object>, for r5v0, types: [java.util.Collection<java.lang.Object>, java.util.Collection, java.lang.Object] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object put(K r4, java.util.Collection<java.lang.Object> r5) {
        /*
            r3 = this;
            java.util.Iterator r0 = r5.iterator()
        L_0x0004:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0014
            java.lang.Object r1 = r0.next()
            java.util.Map<java.lang.Object, K> r2 = r3.mValuesToKeys
            r2.put(r1, r4)
            goto L_0x0004
        L_0x0014:
            java.lang.Object r4 = super.put(r4, r5)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.maps.android.data.geojson.BiMultiMap.put(java.lang.Object, java.util.Collection):java.lang.Object");
    }

    public Object remove(Object obj) {
        Object remove = super.remove(obj);
        if (remove instanceof Collection) {
            for (Object remove2 : (Collection) remove) {
                this.mValuesToKeys.remove(remove2);
            }
        } else {
            this.mValuesToKeys.remove(remove);
        }
        return remove;
    }

    public void clear() {
        super.clear();
        this.mValuesToKeys.clear();
    }

    public BiMultiMap<K> clone() {
        BiMultiMap<K> biMultiMap = new BiMultiMap<>();
        biMultiMap.putAll((Map) super.clone());
        return biMultiMap;
    }

    public K getKey(Object obj) {
        return this.mValuesToKeys.get(obj);
    }
}
