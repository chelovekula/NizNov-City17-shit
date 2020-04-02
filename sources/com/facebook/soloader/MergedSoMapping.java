package com.facebook.soloader;

import javax.annotation.Nullable;

class MergedSoMapping {
    @Nullable
    static String mapLibName(String str) {
        return null;
    }

    MergedSoMapping() {
    }

    static void invokeJniOnload(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown library: ");
        sb.append(str);
        throw new IllegalArgumentException(sb.toString());
    }
}
