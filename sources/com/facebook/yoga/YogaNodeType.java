package com.facebook.yoga;

import com.facebook.proguard.annotations.DoNotStrip;

@DoNotStrip
public enum YogaNodeType {
    DEFAULT(0),
    TEXT(1);
    
    private final int mIntValue;

    private YogaNodeType(int i) {
        this.mIntValue = i;
    }

    public int intValue() {
        return this.mIntValue;
    }

    public static YogaNodeType fromInt(int i) {
        if (i == 0) {
            return DEFAULT;
        }
        if (i == 1) {
            return TEXT;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown enum value: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }
}
