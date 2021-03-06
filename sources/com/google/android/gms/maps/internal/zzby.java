package com.google.android.gms.maps.internal;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import com.facebook.react.uimanager.ViewProps;

public final class zzby {
    public static void zza(@Nullable Bundle bundle, @Nullable Bundle bundle2) {
        if (bundle != null && bundle2 != null) {
            String str = "MapOptions";
            Parcelable zza = zza(bundle, str);
            if (zza != null) {
                zza(bundle2, str, zza);
            }
            String str2 = "StreetViewPanoramaOptions";
            Parcelable zza2 = zza(bundle, str2);
            if (zza2 != null) {
                zza(bundle2, str2, zza2);
            }
            String str3 = "camera";
            Parcelable zza3 = zza(bundle, str3);
            if (zza3 != null) {
                zza(bundle2, str3, zza3);
            }
            String str4 = ViewProps.POSITION;
            if (bundle.containsKey(str4)) {
                bundle2.putString(str4, bundle.getString(str4));
            }
            String str5 = "com.google.android.wearable.compat.extra.LOWBIT_AMBIENT";
            if (bundle.containsKey(str5)) {
                bundle2.putBoolean(str5, bundle.getBoolean(str5, false));
            }
        }
    }

    private static <T extends Parcelable> T zza(@Nullable Bundle bundle, String str) {
        if (bundle == null) {
            return null;
        }
        bundle.setClassLoader(zzby.class.getClassLoader());
        Bundle bundle2 = bundle.getBundle("map_state");
        if (bundle2 == null) {
            return null;
        }
        bundle2.setClassLoader(zzby.class.getClassLoader());
        return bundle2.getParcelable(str);
    }

    public static void zza(Bundle bundle, String str, Parcelable parcelable) {
        bundle.setClassLoader(zzby.class.getClassLoader());
        String str2 = "map_state";
        Bundle bundle2 = bundle.getBundle(str2);
        if (bundle2 == null) {
            bundle2 = new Bundle();
        }
        bundle2.setClassLoader(zzby.class.getClassLoader());
        bundle2.putParcelable(str, parcelable);
        bundle.putBundle(str2, bundle2);
    }

    private zzby() {
    }
}
