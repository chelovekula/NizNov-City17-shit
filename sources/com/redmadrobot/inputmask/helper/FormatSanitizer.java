package com.redmadrobot.inputmask.helper;

import com.redmadrobot.inputmask.helper.Compiler.FormatError;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u001c\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00060\bH\u0002J\u0016\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00060\b2\u0006\u0010\u000b\u001a\u00020\u0006H\u0002J\u0010\u0010\f\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006H\u0007J\u001c\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00060\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00060\bH\u0002¨\u0006\u000e"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/helper/FormatSanitizer;", "", "()V", "checkOpenBraces", "", "string", "", "divideBlocksWithMixedCharacters", "", "blocks", "getFormatBlocks", "formatString", "sanitize", "sortFormatBlocks", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
/* compiled from: FormatSanitizer.kt */
public final class FormatSanitizer {
    @NotNull
    public final String sanitize(@NotNull String str) throws FormatError {
        Intrinsics.checkParameterIsNotNull(str, "formatString");
        checkOpenBraces(str);
        return CollectionsKt.joinToString$default(sortFormatBlocks(divideBlocksWithMixedCharacters(getFormatBlocks(str))), "", null, null, 0, null, null, 62, null);
    }

    private final List<String> getFormatBlocks(String str) {
        List<String> arrayList = new ArrayList<>();
        if (str != null) {
            char[] charArray = str.toCharArray();
            Intrinsics.checkExpressionValueIsNotNull(charArray, "(this as java.lang.String).toCharArray()");
            int length = charArray.length;
            String str2 = "";
            boolean z = false;
            String str3 = str2;
            int i = 0;
            boolean z2 = false;
            while (true) {
                boolean z3 = true;
                if (i >= length) {
                    break;
                }
                char c = charArray[i];
                if ('\\' != c || z2) {
                    if (('[' == c || '{' == c) && !z2) {
                        if (str3.length() <= 0) {
                            z3 = false;
                        }
                        if (z3) {
                            arrayList.add(str3);
                        }
                        str3 = str2;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(str3);
                    sb.append(c);
                    str3 = sb.toString();
                    if ((']' == c || '}' == c) && !z2) {
                        arrayList.add(str3);
                        str3 = str2;
                    }
                    z2 = false;
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str3);
                    sb2.append(c);
                    str3 = sb2.toString();
                    z2 = true;
                }
                i++;
            }
            if (str3.length() == 0) {
                z = true;
            }
            if (!z) {
                arrayList.add(str3);
            }
            return arrayList;
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
    }

    private final List<String> divideBlocksWithMixedCharacters(List<String> list) {
        Iterator it;
        int i;
        String str;
        int i2;
        Iterator it2;
        Object obj;
        String str2;
        Object obj2;
        List<String> arrayList = new ArrayList<>();
        Iterator it3 = list.iterator();
        while (it3.hasNext()) {
            String str3 = (String) it3.next();
            String str4 = "[";
            Object obj3 = null;
            if (StringsKt.startsWith$default(str3, str4, false, 2, null)) {
                int length = str3.length();
                String str5 = "";
                int i3 = 0;
                while (true) {
                    if (i3 >= length) {
                        break;
                    }
                    char charAt = str3.charAt(i3);
                    if (charAt != '[') {
                        if (charAt == ']' && !StringsKt.endsWith$default(str5, "\\", false, 2, obj3)) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(str5);
                            sb.append(charAt);
                            arrayList.add(sb.toString());
                            break;
                        }
                        String str6 = "_";
                        String str7 = "-";
                        String str8 = "a";
                        String str9 = "A";
                        String str10 = "]";
                        if (charAt == '0' || charAt == '9') {
                            CharSequence charSequence = str5;
                            it2 = it3;
                            i2 = length;
                            if (StringsKt.contains$default(charSequence, (CharSequence) str9, false, 2, (Object) null) || StringsKt.contains$default(charSequence, (CharSequence) str8, false, 2, (Object) null) || StringsKt.contains$default(charSequence, (CharSequence) str7, false, 2, (Object) null)) {
                                str = str3;
                                obj = null;
                                i = i3;
                            } else if (StringsKt.contains$default(charSequence, (CharSequence) str6, false, 2, (Object) null)) {
                                str = str3;
                                i = i3;
                                obj = null;
                            }
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(str5);
                            sb2.append(str10);
                            arrayList.add(sb2.toString());
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(str4);
                            sb3.append(charAt);
                            str2 = sb3.toString();
                            str5 = str2;
                        } else {
                            it2 = it3;
                            i2 = length;
                        }
                        String str11 = "9";
                        String str12 = "0";
                        if (charAt == 'A' || charAt == 'a') {
                            CharSequence charSequence2 = str5;
                            str = str3;
                            i = i3;
                            if (StringsKt.contains$default(charSequence2, (CharSequence) str12, false, 2, (Object) null) || StringsKt.contains$default(charSequence2, (CharSequence) str11, false, 2, (Object) null) || StringsKt.contains$default(charSequence2, (CharSequence) str7, false, 2, (Object) null)) {
                                obj2 = null;
                            } else if (StringsKt.contains$default(charSequence2, (CharSequence) str6, false, 2, (Object) null)) {
                                obj2 = null;
                            }
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(str5);
                            sb4.append(str10);
                            arrayList.add(sb4.toString());
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append(str4);
                            sb5.append(charAt);
                            str2 = sb5.toString();
                            str5 = str2;
                        } else {
                            str = str3;
                            i = i3;
                        }
                        if (charAt == '-' || charAt == '_') {
                            CharSequence charSequence3 = str5;
                            obj = null;
                            if (StringsKt.contains$default(charSequence3, (CharSequence) str12, false, 2, (Object) null) || StringsKt.contains$default(charSequence3, (CharSequence) str11, false, 2, (Object) null) || StringsKt.contains$default(charSequence3, (CharSequence) str9, false, 2, (Object) null) || StringsKt.contains$default(charSequence3, (CharSequence) str8, false, 2, (Object) null)) {
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append(str5);
                                sb6.append(str10);
                                arrayList.add(sb6.toString());
                                StringBuilder sb7 = new StringBuilder();
                                sb7.append(str4);
                                sb7.append(charAt);
                                str2 = sb7.toString();
                                str5 = str2;
                            }
                        } else {
                            obj = null;
                        }
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append(str5);
                        sb8.append(charAt);
                        str2 = sb8.toString();
                        str5 = str2;
                    } else {
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append(str5);
                        sb9.append(charAt);
                        str5 = sb9.toString();
                        it2 = it3;
                        str = str3;
                        obj = obj3;
                        i2 = length;
                        i = i3;
                    }
                    i3 = i + 1;
                    obj3 = obj;
                    it3 = it2;
                    length = i2;
                    str3 = str;
                }
                it = it3;
            } else {
                it = it3;
                arrayList.add(str3);
            }
            it3 = it;
        }
        return arrayList;
    }

    private final List<String> sortFormatBlocks(List<String> list) {
        String str;
        List<String> arrayList = new ArrayList<>();
        for (String str2 : list) {
            String str3 = "[";
            if (StringsKt.startsWith$default(str2, str3, false, 2, null)) {
                CharSequence charSequence = str2;
                String str4 = "null cannot be cast to non-null type java.lang.String";
                String str5 = "]";
                String str6 = "";
                String str7 = "(this as java.lang.String).toCharArray()";
                if (StringsKt.contains$default(charSequence, (CharSequence) "0", false, 2, (Object) null) || StringsKt.contains$default(charSequence, (CharSequence) "9", false, 2, (Object) null)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str3);
                    String replace$default = StringsKt.replace$default(StringsKt.replace$default(str2, "[", "", false, 4, (Object) null), "]", "", false, 4, (Object) null);
                    if (replace$default != null) {
                        char[] charArray = replace$default.toCharArray();
                        Intrinsics.checkExpressionValueIsNotNull(charArray, str7);
                        sb.append(CollectionsKt.joinToString$default(ArraysKt.sorted(charArray), str6, null, null, 0, null, null, 62, null));
                        sb.append(str5);
                        str = sb.toString();
                    } else {
                        throw new TypeCastException(str4);
                    }
                } else if (StringsKt.contains$default(charSequence, (CharSequence) "a", false, 2, (Object) null) || StringsKt.contains$default(charSequence, (CharSequence) "A", false, 2, (Object) null)) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str3);
                    String replace$default2 = StringsKt.replace$default(StringsKt.replace$default(str2, "[", "", false, 4, (Object) null), "]", "", false, 4, (Object) null);
                    if (replace$default2 != null) {
                        char[] charArray2 = replace$default2.toCharArray();
                        Intrinsics.checkExpressionValueIsNotNull(charArray2, str7);
                        sb2.append(CollectionsKt.joinToString$default(ArraysKt.sorted(charArray2), str6, null, null, 0, null, null, 62, null));
                        sb2.append(str5);
                        str = sb2.toString();
                    } else {
                        throw new TypeCastException(str4);
                    }
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str3);
                    String replace$default3 = StringsKt.replace$default(StringsKt.replace$default(StringsKt.replace$default(StringsKt.replace$default(str2, "[", "", false, 4, (Object) null), "]", "", false, 4, (Object) null), "_", "A", false, 4, (Object) null), "-", "a", false, 4, (Object) null);
                    if (replace$default3 != null) {
                        char[] charArray3 = replace$default3.toCharArray();
                        Intrinsics.checkExpressionValueIsNotNull(charArray3, str7);
                        sb3.append(CollectionsKt.joinToString$default(ArraysKt.sorted(charArray3), str6, null, null, 0, null, null, 62, null));
                        sb3.append(str5);
                        str = StringsKt.replace$default(StringsKt.replace$default(sb3.toString(), "A", "_", false, 4, (Object) null), "a", "-", false, 4, (Object) null);
                    } else {
                        throw new TypeCastException(str4);
                    }
                }
                str2 = str;
            }
            arrayList.add(str2);
        }
        return arrayList;
    }

    private final void checkOpenBraces(String str) {
        if (str != null) {
            char[] charArray = str.toCharArray();
            Intrinsics.checkExpressionValueIsNotNull(charArray, "(this as java.lang.String).toCharArray()");
            boolean z = false;
            boolean z2 = false;
            boolean z3 = false;
            for (char c : charArray) {
                if ('\\' == c) {
                    z = !z;
                } else {
                    boolean z4 = true;
                    if ('[' == c) {
                        if (!z2) {
                            z2 = !z;
                        } else {
                            throw new FormatError();
                        }
                    }
                    if (']' == c && !z) {
                        z2 = false;
                    }
                    if ('{' == c) {
                        if (!z3) {
                            if (z) {
                                z4 = false;
                            }
                            z3 = z4;
                        } else {
                            throw new FormatError();
                        }
                    }
                    if ('}' == c && !z) {
                        z3 = false;
                    }
                    z = false;
                }
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
    }
}
