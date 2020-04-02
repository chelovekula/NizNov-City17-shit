package com.google.android.gms.common.internal;

import android.net.Uri;
import android.net.Uri.Builder;
import com.facebook.common.util.UriUtil;
import com.google.android.gms.common.annotation.KeepForSdk;

@KeepForSdk
public final class ResourceUtils {
    private static final Uri zzet = new Builder().scheme(UriUtil.QUALIFIED_RESOURCE_SCHEME).authority("com.google.android.gms").appendPath("drawable").build();

    private ResourceUtils() {
    }
}
