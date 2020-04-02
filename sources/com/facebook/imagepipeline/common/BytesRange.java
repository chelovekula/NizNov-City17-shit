package com.facebook.imagepipeline.common;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.util.HashCodeUtil;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
public class BytesRange {
    public static final int TO_END_OF_CONTENT = Integer.MAX_VALUE;
    @Nullable
    private static Pattern sHeaderParsingRegEx;
    public final int from;

    /* renamed from: to */
    public final int f33to;

    public BytesRange(int i, int i2) {
        this.from = i;
        this.f33to = i2;
    }

    public String toHttpRangeHeaderValue() {
        return String.format(null, "bytes=%s-%s", new Object[]{valueOrEmpty(this.from), valueOrEmpty(this.f33to)});
    }

    public boolean contains(@Nullable BytesRange bytesRange) {
        boolean z = false;
        if (bytesRange == null) {
            return false;
        }
        if (this.from <= bytesRange.from && this.f33to >= bytesRange.f33to) {
            z = true;
        }
        return z;
    }

    public String toString() {
        return String.format(null, "%s-%s", new Object[]{valueOrEmpty(this.from), valueOrEmpty(this.f33to)});
    }

    private static String valueOrEmpty(int i) {
        return i == Integer.MAX_VALUE ? "" : Integer.toString(i);
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BytesRange)) {
            return false;
        }
        BytesRange bytesRange = (BytesRange) obj;
        if (!(this.from == bytesRange.from && this.f33to == bytesRange.f33to)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return HashCodeUtil.hashCode(this.from, this.f33to);
    }

    public static BytesRange from(int i) {
        Preconditions.checkArgument(i >= 0);
        return new BytesRange(i, Integer.MAX_VALUE);
    }

    public static BytesRange toMax(int i) {
        Preconditions.checkArgument(i > 0);
        return new BytesRange(0, i);
    }

    @Nullable
    public static BytesRange fromContentRangeHeader(@Nullable String str) throws IllegalArgumentException {
        if (str == null) {
            return null;
        }
        if (sHeaderParsingRegEx == null) {
            sHeaderParsingRegEx = Pattern.compile("[-/ ]");
        }
        try {
            String[] split = sHeaderParsingRegEx.split(str);
            Preconditions.checkArgument(split.length == 4);
            Preconditions.checkArgument(split[0].equals("bytes"));
            int parseInt = Integer.parseInt(split[1]);
            int parseInt2 = Integer.parseInt(split[2]);
            int parseInt3 = Integer.parseInt(split[3]);
            Preconditions.checkArgument(parseInt2 > parseInt);
            Preconditions.checkArgument(parseInt3 > parseInt2);
            if (parseInt2 < parseInt3 - 1) {
                return new BytesRange(parseInt, parseInt2);
            }
            return new BytesRange(parseInt, Integer.MAX_VALUE);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(null, "Invalid Content-Range header value: \"%s\"", new Object[]{str}), e);
        }
    }
}
