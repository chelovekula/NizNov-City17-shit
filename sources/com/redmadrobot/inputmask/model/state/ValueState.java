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

@Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\u0015B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004B\u0019\b\u0016\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0001\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J\u0012\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010\u0011\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010\u0012\u001a\u00020\u0001H\u0016J\b\u0010\u0013\u001a\u00020\u0014H\u0016R\u0011\u0010\b\u001a\u00020\t8F¢\u0006\u0006\u001a\u0004\b\b\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u0016"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/model/state/ValueState;", "Lcom/redmadrobot/inputmask/model/State;", "inheritedType", "Lcom/redmadrobot/inputmask/model/state/ValueState$StateType;", "(Lcom/redmadrobot/inputmask/model/state/ValueState$StateType;)V", "child", "type", "(Lcom/redmadrobot/inputmask/model/State;Lcom/redmadrobot/inputmask/model/state/ValueState$StateType;)V", "isElliptical", "", "()Z", "getType", "()Lcom/redmadrobot/inputmask/model/state/ValueState$StateType;", "accept", "Lcom/redmadrobot/inputmask/model/Next;", "character", "", "accepts", "nextState", "toString", "", "StateType", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
/* compiled from: ValueState.kt */
public final class ValueState extends State {
    @NotNull
    private final StateType type;

    @Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0005\u0003\u0004\u0005\u0006\u0007B\u0007\b\u0002¢\u0006\u0002\u0010\u0002\u0001\u0005\b\t\n\u000b\f¨\u0006\r"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/model/state/ValueState$StateType;", "", "()V", "AlphaNumeric", "Custom", "Ellipsis", "Literal", "Numeric", "Lcom/redmadrobot/inputmask/model/state/ValueState$StateType$Numeric;", "Lcom/redmadrobot/inputmask/model/state/ValueState$StateType$Literal;", "Lcom/redmadrobot/inputmask/model/state/ValueState$StateType$AlphaNumeric;", "Lcom/redmadrobot/inputmask/model/state/ValueState$StateType$Ellipsis;", "Lcom/redmadrobot/inputmask/model/state/ValueState$StateType$Custom;", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
    /* compiled from: ValueState.kt */
    public static abstract class StateType {

        @Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/model/state/ValueState$StateType$AlphaNumeric;", "Lcom/redmadrobot/inputmask/model/state/ValueState$StateType;", "()V", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
        /* compiled from: ValueState.kt */
        public static final class AlphaNumeric extends StateType {
            public AlphaNumeric() {
                super(null);
            }
        }

        @Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u000b"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/model/state/ValueState$StateType$Custom;", "Lcom/redmadrobot/inputmask/model/state/ValueState$StateType;", "character", "", "characterSet", "", "(CLjava/lang/String;)V", "getCharacter", "()C", "getCharacterSet", "()Ljava/lang/String;", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
        /* compiled from: ValueState.kt */
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

        @Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001¢\u0006\u0002\u0010\u0003R\u0011\u0010\u0002\u001a\u00020\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0005¨\u0006\u0006"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/model/state/ValueState$StateType$Ellipsis;", "Lcom/redmadrobot/inputmask/model/state/ValueState$StateType;", "inheritedType", "(Lcom/redmadrobot/inputmask/model/state/ValueState$StateType;)V", "getInheritedType", "()Lcom/redmadrobot/inputmask/model/state/ValueState$StateType;", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
        /* compiled from: ValueState.kt */
        public static final class Ellipsis extends StateType {
            @NotNull
            private final StateType inheritedType;

            public Ellipsis(@NotNull StateType stateType) {
                Intrinsics.checkParameterIsNotNull(stateType, "inheritedType");
                super(null);
                this.inheritedType = stateType;
            }

            @NotNull
            public final StateType getInheritedType() {
                return this.inheritedType;
            }
        }

        @Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/model/state/ValueState$StateType$Literal;", "Lcom/redmadrobot/inputmask/model/state/ValueState$StateType;", "()V", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
        /* compiled from: ValueState.kt */
        public static final class Literal extends StateType {
            public Literal() {
                super(null);
            }
        }

        @Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/model/state/ValueState$StateType$Numeric;", "Lcom/redmadrobot/inputmask/model/state/ValueState$StateType;", "()V", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
        /* compiled from: ValueState.kt */
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

    @NotNull
    public final StateType getType() {
        return this.type;
    }

    public ValueState(@NotNull StateType stateType) {
        Intrinsics.checkParameterIsNotNull(stateType, "inheritedType");
        super(null);
        this.type = new Ellipsis(stateType);
    }

    public ValueState(@Nullable State state, @NotNull StateType stateType) {
        Intrinsics.checkParameterIsNotNull(stateType, ReactVideoViewManager.PROP_SRC_TYPE);
        super(state);
        this.type = stateType;
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
        if (stateType instanceof Ellipsis) {
            StateType inheritedType = ((Ellipsis) stateType).getInheritedType();
            if (inheritedType instanceof Numeric) {
                return Character.isDigit(c);
            }
            if (inheritedType instanceof Literal) {
                return Character.isLetter(c);
            }
            if (inheritedType instanceof AlphaNumeric) {
                return Character.isLetterOrDigit(c);
            }
            return false;
        } else if (stateType instanceof Custom) {
            return StringsKt.contains$default((CharSequence) ((Custom) stateType).getCharacterSet(), c, false, 2, (Object) null);
        } else {
            throw new NoWhenBranchMatchedException();
        }
    }

    @Nullable
    public Next accept(char c) {
        if (!accepts(c)) {
            return null;
        }
        return new Next(nextState(), Character.valueOf(c), true, Character.valueOf(c));
    }

    public final boolean isElliptical() {
        return this.type instanceof Ellipsis;
    }

    @NotNull
    public State nextState() {
        if (this.type instanceof Ellipsis) {
            return this;
        }
        return super.nextState();
    }

    @NotNull
    public String toString() {
        StateType stateType = this.type;
        String str = "null";
        if (stateType instanceof Literal) {
            StringBuilder sb = new StringBuilder();
            sb.append("[A] -> ");
            if (getChild() != null) {
                str = getChild().toString();
            }
            sb.append(str);
            return sb.toString();
        } else if (stateType instanceof Numeric) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("[0] -> ");
            if (getChild() != null) {
                str = getChild().toString();
            }
            sb2.append(str);
            return sb2.toString();
        } else if (stateType instanceof AlphaNumeric) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("[_] -> ");
            if (getChild() != null) {
                str = getChild().toString();
            }
            sb3.append(str);
            return sb3.toString();
        } else if (stateType instanceof Ellipsis) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("[…] -> ");
            if (getChild() != null) {
                str = getChild().toString();
            }
            sb4.append(str);
            return sb4.toString();
        } else if (stateType instanceof Custom) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("[");
            sb5.append(((Custom) this.type).getCharacter());
            sb5.append("] -> ");
            if (getChild() != null) {
                str = getChild().toString();
            }
            sb5.append(str);
            return sb5.toString();
        } else {
            throw new NoWhenBranchMatchedException();
        }
    }
}
