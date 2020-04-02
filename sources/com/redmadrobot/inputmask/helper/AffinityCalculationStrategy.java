package com.redmadrobot.inputmask.helper;

import com.redmadrobot.inputmask.model.CaretString;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0014\u0010\u000b\u001a\u00020\f*\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0002j\u0002\b\u000ej\u0002\b\u000f¨\u0006\u0010"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/helper/AffinityCalculationStrategy;", "", "(Ljava/lang/String;I)V", "calculateAffinityOfMask", "", "mask", "Lcom/redmadrobot/inputmask/helper/Mask;", "text", "Lcom/redmadrobot/inputmask/model/CaretString;", "autocomplete", "", "prefixIntersection", "", "another", "WHOLE_STRING", "PREFIX", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
/* compiled from: AffinityCalculationStrategy.kt */
public enum AffinityCalculationStrategy {
    WHOLE_STRING,
    PREFIX;

    @Metadata(mo15441bv = {1, 0, 2}, mo15444k = 3, mo15445mv = {1, 1, 9})
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0 = null;

        static {
            $EnumSwitchMapping$0 = new int[AffinityCalculationStrategy.values().length];
            $EnumSwitchMapping$0[AffinityCalculationStrategy.WHOLE_STRING.ordinal()] = 1;
            $EnumSwitchMapping$0[AffinityCalculationStrategy.PREFIX.ordinal()] = 2;
        }
    }

    public final int calculateAffinityOfMask(@NotNull Mask mask, @NotNull CaretString caretString, boolean z) {
        Intrinsics.checkParameterIsNotNull(mask, "mask");
        Intrinsics.checkParameterIsNotNull(caretString, "text");
        int i = WhenMappings.$EnumSwitchMapping$0[ordinal()];
        if (i == 1) {
            return mask.apply(new CaretString(caretString.getString(), caretString.getCaretPosition()), z).getAffinity();
        }
        if (i == 2) {
            return prefixIntersection(mask.apply(new CaretString(caretString.getString(), caretString.getCaretPosition()), z).getFormattedText().getString(), caretString.getString()).length();
        }
        throw new NoWhenBranchMatchedException();
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x004e  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0056  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.lang.String prefixIntersection(@org.jetbrains.annotations.NotNull java.lang.String r7, java.lang.String r8) {
        /*
            r6 = this;
            r0 = r7
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            int r0 = r0.length()
            r1 = 1
            r2 = 0
            if (r0 != 0) goto L_0x000d
            r0 = 1
            goto L_0x000e
        L_0x000d:
            r0 = 0
        L_0x000e:
            if (r0 != 0) goto L_0x005c
            r0 = r8
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            int r0 = r0.length()
            if (r0 != 0) goto L_0x001a
            goto L_0x001b
        L_0x001a:
            r1 = 0
        L_0x001b:
            if (r1 == 0) goto L_0x001e
            goto L_0x005c
        L_0x001e:
            r0 = 0
        L_0x001f:
            int r1 = r7.length()
            java.lang.String r3 = "null cannot be cast to non-null type java.lang.String"
            java.lang.String r4 = "(this as java.lang.Strin…ing(startIndex, endIndex)"
            if (r0 >= r1) goto L_0x004c
            int r1 = r8.length()
            if (r0 >= r1) goto L_0x004c
            char r1 = r7.charAt(r0)
            char r5 = r8.charAt(r0)
            if (r1 != r5) goto L_0x003c
            int r0 = r0 + 1
            goto L_0x001f
        L_0x003c:
            if (r7 == 0) goto L_0x0046
            java.lang.String r7 = r7.substring(r2, r0)
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r7, r4)
            return r7
        L_0x0046:
            kotlin.TypeCastException r7 = new kotlin.TypeCastException
            r7.<init>(r3)
            throw r7
        L_0x004c:
            if (r7 == 0) goto L_0x0056
            java.lang.String r7 = r7.substring(r2, r0)
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r7, r4)
            return r7
        L_0x0056:
            kotlin.TypeCastException r7 = new kotlin.TypeCastException
            r7.<init>(r3)
            throw r7
        L_0x005c:
            java.lang.String r7 = ""
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.redmadrobot.inputmask.helper.AffinityCalculationStrategy.prefixIntersection(java.lang.String, java.lang.String):java.lang.String");
    }
}
