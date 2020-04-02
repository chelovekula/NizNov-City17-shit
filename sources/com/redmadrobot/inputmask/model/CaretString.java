package com.redmadrobot.inputmask.model;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u000b"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/model/CaretString;", "", "string", "", "caretPosition", "", "(Ljava/lang/String;I)V", "getCaretPosition", "()I", "getString", "()Ljava/lang/String;", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
/* compiled from: CaretString.kt */
public final class CaretString {
    private final int caretPosition;
    @NotNull
    private final String string;

    public CaretString(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "string");
        this.string = str;
        this.caretPosition = i;
    }

    public final int getCaretPosition() {
        return this.caretPosition;
    }

    @NotNull
    public final String getString() {
        return this.string;
    }
}
