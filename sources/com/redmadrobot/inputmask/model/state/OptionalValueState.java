package com.redmadrobot.inputmask.model.state;

import com.brentvatne.react.ReactVideoViewManager;
import com.redmadrobot.inputmask.model.Next;
import com.redmadrobot.inputmask.model.State;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\u0010B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0012\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u000bH\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0016R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0011"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/model/state/OptionalValueState;", "Lcom/redmadrobot/inputmask/model/State;", "child", "type", "Lcom/redmadrobot/inputmask/model/state/OptionalValueState$StateType;", "(Lcom/redmadrobot/inputmask/model/State;Lcom/redmadrobot/inputmask/model/state/OptionalValueState$StateType;)V", "getType", "()Lcom/redmadrobot/inputmask/model/state/OptionalValueState$StateType;", "accept", "Lcom/redmadrobot/inputmask/model/Next;", "character", "", "accepts", "", "toString", "", "StateType", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
/* compiled from: OptionalValueState.kt */
public final class OptionalValueState extends State {
    @NotNull
    private final StateType type;

    @Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0004\u0003\u0004\u0005\u0006B\u0007\b\u0002¢\u0006\u0002\u0010\u0002\u0001\u0004\u0007\b\t\n¨\u0006\u000b"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/model/state/OptionalValueState$StateType;", "", "()V", "AlphaNumeric", "Custom", "Literal", "Numeric", "Lcom/redmadrobot/inputmask/model/state/OptionalValueState$StateType$Numeric;", "Lcom/redmadrobot/inputmask/model/state/OptionalValueState$StateType$Literal;", "Lcom/redmadrobot/inputmask/model/state/OptionalValueState$StateType$AlphaNumeric;", "Lcom/redmadrobot/inputmask/model/state/OptionalValueState$StateType$Custom;", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
    /* compiled from: OptionalValueState.kt */
    public static abstract class StateType {

        @Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/model/state/OptionalValueState$StateType$AlphaNumeric;", "Lcom/redmadrobot/inputmask/model/state/OptionalValueState$StateType;", "()V", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
        /* compiled from: OptionalValueState.kt */
        public static final class AlphaNumeric extends StateType {
            public AlphaNumeric() {
                super(null);
            }
        }

        @Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u000b"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/model/state/OptionalValueState$StateType$Custom;", "Lcom/redmadrobot/inputmask/model/state/OptionalValueState$StateType;", "character", "", "characterSet", "", "(CLjava/lang/String;)V", "getCharacter", "()C", "getCharacterSet", "()Ljava/lang/String;", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
        /* compiled from: OptionalValueState.kt */
        public static final class Custom extends StateType {
            private final char character;
            @NotNull
            private final String characterSet;

            public Custom(char c, @NotNull String str) {
                Intrinsics.checkParameterIsNotNull(str, "characterSet");
                super(null);
                this.character = c;
                this.characterSet = str;
            }

            public final char getCharacter() {
                return this.character;
            }

            @NotNull
            public final String getCharacterSet() {
                return this.characterSet;
            }
        }

        @Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/model/state/OptionalValueState$StateType$Literal;", "Lcom/redmadrobot/inputmask/model/state/OptionalValueState$StateType;", "()V", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
        /* compiled from: OptionalValueState.kt */
        public static final class Literal extends StateType {
            public Literal() {
                super(null);
            }
        }

        @Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/model/state/OptionalValueState$StateType$Numeric;", "Lcom/redmadrobot/inputmask/model/state/OptionalValueState$StateType;", "()V", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
        /* compiled from: OptionalValueState.kt */
        public static final class Numeric extends StateType {
            public Numeric() {
                super(null);
            }
        }

        private StateType() {
        }

        public /* synthetic */ StateType(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public OptionalValueState(@NotNull State state, @NotNull StateType stateType) {
        Intrinsics.checkParameterIsNotNull(state, "child");
        Intrinsics.checkParameterIsNotNull(stateType, ReactVideoViewManager.PROP_SRC_TYPE);
        super(state);
        this.type = stateType;
    }

    @NotNull
    public final StateType getType() {
        return this.type;
    }

    private final boolean accepts(char c) {
        StateType stateType = this.type;
        if (stateType instanceof Numeric) {
            return Character.isDigit(c);
        }
        if (stateType instanceof Literal) {
            return Character.isLetter(c);
        }
        if (stateType instanceof AlphaNumeric) {
            return Character.isLetterOrDigit(c);
        }
        if (stateType instanceof Custom) {
            return StringsKt.contains$default((CharSequence) ((Custom) stateType).getCharacterSet(), c, false, 2, (Object) null);
        }
        throw new NoWhenBranchMatchedException();
    }

    @Nullable
    public Next accept(char c) {
        if (accepts(c)) {
            return new Next(nextState(), Character.valueOf(c), true, Character.valueOf(c));
        }
        return new Next(nextState(), null, false, null);
    }

    @NotNull
    public String toString() {
        StateType stateType = this.type;
        String str = "null";
        if (stateType instanceof Literal) {
            StringBuilder sb = new StringBuilder();
            sb.append("[a] -> ");
            if (getChild() != null) {
                str = getChild().toString();
            }
            sb.append(str);
            return sb.toString();
        } else if (stateType instanceof Numeric) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("[9] -> ");
            if (getChild() != null) {
                str = getChild().toString();
            }
            sb2.append(str);
            return sb2.toString();
        } else if (stateType instanceof AlphaNumeric) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("[-] -> ");
            if (getChild() != null) {
                str = getChild().toString();
            }
            sb3.append(str);
            return sb3.toString();
        } else if (stateType instanceof Custom) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("[");
            sb4.append(((Custom) this.type).getCharacter());
            sb4.append("] -> ");
            if (getChild() != null) {
                str = getChild().toString();
            }
            sb4.append(str);
            return sb4.toString();
        } else {
            throw new NoWhenBranchMatchedException();
        }
    }
}
