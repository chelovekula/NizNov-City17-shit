package com.redmadrobot.inputmask.helper;

import com.redmadrobot.inputmask.model.CaretString;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\f\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010\u0007\u001a\u00020\bJ\r\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\u0002\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u000e¢\u0006\u0002\n\u0000¨\u0006\f"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/helper/CaretStringIterator;", "", "caretString", "Lcom/redmadrobot/inputmask/model/CaretString;", "currentIndex", "", "(Lcom/redmadrobot/inputmask/model/CaretString;I)V", "beforeCaret", "", "next", "", "()Ljava/lang/Character;", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
/* compiled from: CaretStringIterator.kt */
public final class CaretStringIterator {
    private final CaretString caretString;
    private int currentIndex;

    public CaretStringIterator(@NotNull CaretString caretString2, int i) {
        Intrinsics.checkParameterIsNotNull(caretString2, "caretString");
        this.caretString = caretString2;
        this.currentIndex = i;
    }

    public /* synthetic */ CaretStringIterator(CaretString caretString2, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        this(caretString2, i);
    }

    public final boolean beforeCaret() {
        return this.currentIndex <= this.caretString.getCaretPosition() || (this.currentIndex == 0 && this.caretString.getCaretPosition() == 0);
    }

    @Nullable
    public final Character next() {
        if (this.currentIndex >= this.caretString.getString().length()) {
            return null;
        }
        String string = this.caretString.getString();
        if (string != null) {
            char[] charArray = string.toCharArray();
            Intrinsics.checkExpressionValueIsNotNull(charArray, "(this as java.lang.String).toCharArray()");
            int i = this.currentIndex;
            char c = charArray[i];
            this.currentIndex = i + 1;
            return Character.valueOf(c);
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
    }
}
