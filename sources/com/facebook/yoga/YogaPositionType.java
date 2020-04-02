package com.facebook.yoga;

import com.facebook.proguard.annotations.DoNotStrip;

@DoNotStrip
public enum YogaPositionType {
    RELATIVE(0),
    ABSOLUTE(1);
    
    private final int mIntValue;

    private YogaPositionType(int i) {
        this.mIntValue = i;
    }

    public int intValue() {
        return this.mIntValue;
    }

    public static YogaPositionType fromInt(int i) {
        if (i == 0) {
            return RELATIVE;
        }
        if (i == 1) {
            return ABSOLUTE;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown enum value: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }
}
