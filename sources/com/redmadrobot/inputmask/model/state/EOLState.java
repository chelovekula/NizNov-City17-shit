package com.redmadrobot.inputmask.model.state;

import com.redmadrobot.inputmask.model.Next;
import com.redmadrobot.inputmask.model.State;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\t"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/model/state/EOLState;", "Lcom/redmadrobot/inputmask/model/State;", "()V", "accept", "Lcom/redmadrobot/inputmask/model/Next;", "character", "", "toString", "", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
/* compiled from: EOLState.kt */
public final class EOLState extends State {
    @Nullable
    public Next accept(char c) {
        return null;
    }

    @NotNull
    public String toString() {
        return "EOL";
    }

    public EOLState() {
        super(null);
    }
}
