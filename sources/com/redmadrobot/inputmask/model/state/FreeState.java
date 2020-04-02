package com.redmadrobot.inputmask.model.state;

import com.redmadrobot.inputmask.model.Next;
import com.redmadrobot.inputmask.model.State;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\f\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0012\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u0004H\u0016J\n\u0010\u000b\u001a\u0004\u0018\u00010\tH\u0016J\b\u0010\f\u001a\u00020\rH\u0016R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u000e"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/model/state/FreeState;", "Lcom/redmadrobot/inputmask/model/State;", "child", "ownCharacter", "", "(Lcom/redmadrobot/inputmask/model/State;C)V", "getOwnCharacter", "()C", "accept", "Lcom/redmadrobot/inputmask/model/Next;", "character", "autocomplete", "toString", "", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
/* compiled from: FreeState.kt */
public final class FreeState extends State {
    private final char ownCharacter;

    public FreeState(@NotNull State state, char c) {
        Intrinsics.checkParameterIsNotNull(state, "child");
        super(state);
        this.ownCharacter = c;
    }

    public final char getOwnCharacter() {
        return this.ownCharacter;
    }

    @Nullable
    public Next accept(char c) {
        if (this.ownCharacter == c) {
            return new Next(nextState(), Character.valueOf(c), true, null);
        }
        return new Next(nextState(), Character.valueOf(this.ownCharacter), false, null);
    }

    @Nullable
    public Next autocomplete() {
        return new Next(nextState(), Character.valueOf(this.ownCharacter), false, null);
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this.ownCharacter);
        sb.append(" -> ");
        sb.append(getChild() == null ? "null" : getChild().toString());
        return sb.toString();
    }
}
