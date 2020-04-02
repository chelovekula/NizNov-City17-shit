package com.redmadrobot.inputmask.helper;

import com.redmadrobot.inputmask.helper.Compiler.FormatError;
import com.redmadrobot.inputmask.model.CaretString;
import com.redmadrobot.inputmask.model.Notation;
import com.redmadrobot.inputmask.model.State;
import com.redmadrobot.inputmask.model.state.EOLState;
import com.redmadrobot.inputmask.model.state.FixedState;
import com.redmadrobot.inputmask.model.state.FreeState;
import com.redmadrobot.inputmask.model.state.OptionalValueState;
import com.redmadrobot.inputmask.model.state.OptionalValueState.StateType;
import com.redmadrobot.inputmask.model.state.OptionalValueState.StateType.AlphaNumeric;
import com.redmadrobot.inputmask.model.state.OptionalValueState.StateType.Custom;
import com.redmadrobot.inputmask.model.state.OptionalValueState.StateType.Literal;
import com.redmadrobot.inputmask.model.state.OptionalValueState.StateType.Numeric;
import com.redmadrobot.inputmask.model.state.ValueState;
import com.redmadrobot.inputmask.model.state.ValueState.StateType.Ellipsis;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\u0018\u0000 \u001a2\u00020\u0001:\u0002\u001a\u001bB\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006¢\u0006\u0002\u0010\bJ\u0006\u0010\u000b\u001a\u00020\fJ\u0006\u0010\r\u001a\u00020\fJ\u001a\u0010\u000e\u001a\u00020\u00032\b\u0010\u000f\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0010\u001a\u00020\u0003H\u0002J\u0016\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\u0010\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u000f\u001a\u00020\nH\u0002J\u0006\u0010\u0010\u001a\u00020\u0003J\u0006\u0010\u0018\u001a\u00020\fJ\u0006\u0010\u0019\u001a\u00020\fR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000¨\u0006\u001c"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/helper/Mask;", "", "format", "", "(Ljava/lang/String;)V", "customNotations", "", "Lcom/redmadrobot/inputmask/model/Notation;", "(Ljava/lang/String;Ljava/util/List;)V", "initialState", "Lcom/redmadrobot/inputmask/model/State;", "acceptableTextLength", "", "acceptableValueLength", "appendPlaceholder", "state", "placeholder", "apply", "Lcom/redmadrobot/inputmask/helper/Mask$Result;", "text", "Lcom/redmadrobot/inputmask/model/CaretString;", "autocomplete", "", "noMandatoryCharactersLeftAfterState", "totalTextLength", "totalValueLength", "Factory", "Result", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
/* compiled from: Mask.kt */
public final class Mask {
    public static final Factory Factory = new Factory(null);
    /* access modifiers changed from: private */
    public static final Map<String, Mask> cache = new HashMap();
    private final List<Notation> customNotations;
    private final State initialState;

    @Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001c\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00052\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fJ\u001c\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\n\u001a\u00020\u00052\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fR \u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\u0010"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/helper/Mask$Factory;", "", "()V", "cache", "", "", "Lcom/redmadrobot/inputmask/helper/Mask;", "getCache", "()Ljava/util/Map;", "getOrCreate", "format", "customNotations", "", "Lcom/redmadrobot/inputmask/model/Notation;", "isValid", "", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
    /* compiled from: Mask.kt */
    public static final class Factory {
        private Factory() {
        }

        public /* synthetic */ Factory(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final Map<String, Mask> getCache() {
            return Mask.cache;
        }

        @NotNull
        public final Mask getOrCreate(@NotNull String str, @NotNull List<Notation> list) {
            Intrinsics.checkParameterIsNotNull(str, "format");
            Intrinsics.checkParameterIsNotNull(list, "customNotations");
            Factory factory = this;
            Mask mask = (Mask) factory.getCache().get(str);
            if (mask != null) {
                return mask;
            }
            Mask mask2 = new Mask(str, list);
            factory.getCache().put(str, mask2);
            return mask2;
        }

        public final boolean isValid(@NotNull String str, @NotNull List<Notation> list) {
            Intrinsics.checkParameterIsNotNull(str, "format");
            Intrinsics.checkParameterIsNotNull(list, "customNotations");
            try {
                new Mask(str, list);
                return true;
            } catch (FormatError unused) {
                return false;
            }
        }
    }

    @Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012¨\u0006\u0013"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/helper/Mask$Result;", "", "formattedText", "Lcom/redmadrobot/inputmask/model/CaretString;", "extractedValue", "", "affinity", "", "complete", "", "(Lcom/redmadrobot/inputmask/model/CaretString;Ljava/lang/String;IZ)V", "getAffinity", "()I", "getComplete", "()Z", "getExtractedValue", "()Ljava/lang/String;", "getFormattedText", "()Lcom/redmadrobot/inputmask/model/CaretString;", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
    /* compiled from: Mask.kt */
    public static final class Result {
        private final int affinity;
        private final boolean complete;
        @NotNull
        private final String extractedValue;
        @NotNull
        private final CaretString formattedText;

        public Result(@NotNull CaretString caretString, @NotNull String str, int i, boolean z) {
            Intrinsics.checkParameterIsNotNull(caretString, "formattedText");
            Intrinsics.checkParameterIsNotNull(str, "extractedValue");
            this.formattedText = caretString;
            this.extractedValue = str;
            this.affinity = i;
            this.complete = z;
        }

        @NotNull
        public final CaretString getFormattedText() {
            return this.formattedText;
        }

        @NotNull
        public final String getExtractedValue() {
            return this.extractedValue;
        }

        public final int getAffinity() {
            return this.affinity;
        }

        public final boolean getComplete() {
            return this.complete;
        }
    }

    public Mask(@NotNull String str, @NotNull List<Notation> list) {
        Intrinsics.checkParameterIsNotNull(str, "format");
        Intrinsics.checkParameterIsNotNull(list, "customNotations");
        this.customNotations = list;
        this.initialState = new Compiler(this.customNotations).compile(str);
    }

    public Mask(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "format");
        this(str, CollectionsKt.emptyList());
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r12v2 */
    /* JADX WARNING: type inference failed for: r6v0 */
    /* JADX WARNING: type inference failed for: r12v3, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r6v1, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r12v4, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r6v2, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r12v6, types: [java.lang.Character] */
    /* JADX WARNING: type inference failed for: r12v7, types: [java.lang.Object] */
    /* JADX WARNING: type inference failed for: r12v8, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r6v3, types: [java.lang.Character] */
    /* JADX WARNING: type inference failed for: r6v4, types: [java.lang.Object] */
    /* JADX WARNING: type inference failed for: r6v5, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r6v6 */
    /* JADX WARNING: type inference failed for: r12v9 */
    /* JADX WARNING: type inference failed for: r12v10 */
    /* JADX WARNING: type inference failed for: r6v7 */
    /* JADX WARNING: type inference failed for: r12v11, types: [java.lang.Character] */
    /* JADX WARNING: type inference failed for: r12v12, types: [java.lang.Object] */
    /* JADX WARNING: type inference failed for: r12v13, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r6v8, types: [java.lang.Character] */
    /* JADX WARNING: type inference failed for: r6v9, types: [java.lang.Object] */
    /* JADX WARNING: type inference failed for: r6v10, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r6v11 */
    /* JADX WARNING: type inference failed for: r12v14 */
    /* JADX WARNING: type inference failed for: r12v15 */
    /* JADX WARNING: type inference failed for: r12v16 */
    /* JADX WARNING: type inference failed for: r12v17 */
    /* JADX WARNING: type inference failed for: r6v12 */
    /* JADX WARNING: type inference failed for: r6v13 */
    /* JADX WARNING: type inference failed for: r12v18 */
    /* JADX WARNING: type inference failed for: r6v14 */
    /* JADX WARNING: type inference failed for: r12v19 */
    /* JADX WARNING: type inference failed for: r6v15 */
    /* JADX WARNING: type inference failed for: r12v20 */
    /* JADX WARNING: type inference failed for: r12v21 */
    /* JADX WARNING: type inference failed for: r6v16 */
    /* JADX WARNING: type inference failed for: r6v17 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r12v10
      assigns: []
      uses: []
      mth insns count: 90
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 16 */
    @org.jetbrains.annotations.NotNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.redmadrobot.inputmask.helper.Mask.Result apply(@org.jetbrains.annotations.NotNull com.redmadrobot.inputmask.model.CaretString r12, boolean r13) {
        /*
            r11 = this;
            java.lang.String r0 = "text"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r12, r0)
            com.redmadrobot.inputmask.helper.CaretStringIterator r0 = new com.redmadrobot.inputmask.helper.CaretStringIterator
            r1 = 0
            r2 = 2
            r3 = 0
            r0.<init>(r12, r1, r2, r3)
            int r12 = r12.getCaretPosition()
            com.redmadrobot.inputmask.model.State r2 = r11.initialState
            boolean r3 = r0.beforeCaret()
            java.lang.Character r4 = r0.next()
            java.lang.String r5 = ""
            r1 = r12
            r7 = r3
            r12 = r5
            r6 = r12
            r3 = 0
        L_0x0022:
            if (r4 == 0) goto L_0x0094
            char r8 = r4.charValue()
            com.redmadrobot.inputmask.model.Next r8 = r2.accept(r8)
            if (r8 == 0) goto L_0x007e
            com.redmadrobot.inputmask.model.State r2 = r8.getState()
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            r9.append(r12)
            java.lang.Character r12 = r8.getInsert()
            if (r12 == 0) goto L_0x0041
            goto L_0x0042
        L_0x0041:
            r12 = r5
        L_0x0042:
            r9.append(r12)
            java.lang.String r12 = r9.toString()
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            r9.append(r6)
            java.lang.Character r6 = r8.getValue()
            if (r6 == 0) goto L_0x0058
            goto L_0x0059
        L_0x0058:
            r6 = r5
        L_0x0059:
            r9.append(r6)
            java.lang.String r6 = r9.toString()
            boolean r9 = r8.getPass()
            if (r9 == 0) goto L_0x0071
            boolean r7 = r0.beforeCaret()
            java.lang.Character r4 = r0.next()
            int r3 = r3 + 1
            goto L_0x0022
        L_0x0071:
            if (r7 == 0) goto L_0x007b
            java.lang.Character r8 = r8.getInsert()
            if (r8 == 0) goto L_0x007b
            int r1 = r1 + 1
        L_0x007b:
            int r3 = r3 + -1
            goto L_0x0022
        L_0x007e:
            boolean r4 = r0.beforeCaret()
            if (r4 == 0) goto L_0x0086
            int r1 = r1 + -1
        L_0x0086:
            boolean r4 = r0.beforeCaret()
            java.lang.Character r7 = r0.next()
            int r3 = r3 + -1
            r10 = r7
            r7 = r4
            r4 = r10
            goto L_0x0022
        L_0x0094:
            if (r13 == 0) goto L_0x00d9
            if (r7 == 0) goto L_0x00d9
            com.redmadrobot.inputmask.model.Next r0 = r2.autocomplete()
            if (r0 == 0) goto L_0x00d9
            com.redmadrobot.inputmask.model.State r2 = r0.getState()
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r12)
            java.lang.Character r12 = r0.getInsert()
            if (r12 == 0) goto L_0x00b1
            goto L_0x00b2
        L_0x00b1:
            r12 = r5
        L_0x00b2:
            r4.append(r12)
            java.lang.String r12 = r4.toString()
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r6)
            java.lang.Character r6 = r0.getValue()
            if (r6 == 0) goto L_0x00c8
            goto L_0x00c9
        L_0x00c8:
            r6 = r5
        L_0x00c9:
            r4.append(r6)
            java.lang.String r6 = r4.toString()
            java.lang.Character r0 = r0.getInsert()
            if (r0 == 0) goto L_0x0094
            int r1 = r1 + 1
            goto L_0x0094
        L_0x00d9:
            com.redmadrobot.inputmask.helper.Mask$Result r13 = new com.redmadrobot.inputmask.helper.Mask$Result
            com.redmadrobot.inputmask.model.CaretString r0 = new com.redmadrobot.inputmask.model.CaretString
            r0.<init>(r12, r1)
            boolean r12 = r11.noMandatoryCharactersLeftAfterState(r2)
            r13.<init>(r0, r6, r3, r12)
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.redmadrobot.inputmask.helper.Mask.apply(com.redmadrobot.inputmask.model.CaretString, boolean):com.redmadrobot.inputmask.helper.Mask$Result");
    }

    @NotNull
    public final String placeholder() {
        return appendPlaceholder(this.initialState, "");
    }

    public final int acceptableTextLength() {
        State state = this.initialState;
        int i = 0;
        while (state != null && !(state instanceof EOLState)) {
            if ((state instanceof FixedState) || (state instanceof FreeState) || (state instanceof ValueState)) {
                i++;
            }
            state = state.getChild();
        }
        return i;
    }

    public final int totalTextLength() {
        State state = this.initialState;
        int i = 0;
        while (state != null && !(state instanceof EOLState)) {
            if ((state instanceof FixedState) || (state instanceof FreeState) || (state instanceof ValueState) || (state instanceof OptionalValueState)) {
                i++;
            }
            state = state.getChild();
        }
        return i;
    }

    public final int acceptableValueLength() {
        State state = this.initialState;
        int i = 0;
        while (state != null && !(state instanceof EOLState)) {
            if ((state instanceof FixedState) || (state instanceof ValueState)) {
                i++;
            }
            state = state.getChild();
        }
        return i;
    }

    public final int totalValueLength() {
        State state = this.initialState;
        int i = 0;
        while (state != null && !(state instanceof EOLState)) {
            if ((state instanceof FixedState) || (state instanceof ValueState) || (state instanceof OptionalValueState)) {
                i++;
            }
            state = state.getChild();
        }
        return i;
    }

    private final String appendPlaceholder(State state, String str) {
        String str2;
        if (state == null || (state instanceof EOLState)) {
            return str;
        }
        if (state instanceof FixedState) {
            State child = state.getChild();
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(((FixedState) state).getOwnCharacter());
            return appendPlaceholder(child, sb.toString());
        } else if (state instanceof FreeState) {
            State child2 = state.getChild();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(((FreeState) state).getOwnCharacter());
            return appendPlaceholder(child2, sb2.toString());
        } else {
            String str3 = "0";
            String str4 = "a";
            String str5 = "-";
            if (state instanceof OptionalValueState) {
                OptionalValueState optionalValueState = (OptionalValueState) state;
                StateType type = optionalValueState.getType();
                if (type instanceof AlphaNumeric) {
                    State child3 = state.getChild();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str);
                    sb3.append(str5);
                    str2 = appendPlaceholder(child3, sb3.toString());
                } else if (type instanceof Literal) {
                    State child4 = state.getChild();
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(str);
                    sb4.append(str4);
                    str2 = appendPlaceholder(child4, sb4.toString());
                } else if (type instanceof Numeric) {
                    State child5 = state.getChild();
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(str);
                    sb5.append(str3);
                    str2 = appendPlaceholder(child5, sb5.toString());
                } else if (type instanceof Custom) {
                    State child6 = state.getChild();
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(str);
                    sb6.append(((Custom) optionalValueState.getType()).getCharacter());
                    str2 = appendPlaceholder(child6, sb6.toString());
                } else {
                    throw new NoWhenBranchMatchedException();
                }
                return str2;
            } else if (!(state instanceof ValueState)) {
                return str;
            } else {
                ValueState valueState = (ValueState) state;
                ValueState.StateType type2 = valueState.getType();
                if (type2 instanceof ValueState.StateType.AlphaNumeric) {
                    State child7 = state.getChild();
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(str);
                    sb7.append(str5);
                    str = appendPlaceholder(child7, sb7.toString());
                } else if (type2 instanceof ValueState.StateType.Literal) {
                    State child8 = state.getChild();
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(str);
                    sb8.append(str4);
                    str = appendPlaceholder(child8, sb8.toString());
                } else if (type2 instanceof ValueState.StateType.Numeric) {
                    State child9 = state.getChild();
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(str);
                    sb9.append(str3);
                    str = appendPlaceholder(child9, sb9.toString());
                } else if (!(type2 instanceof Ellipsis)) {
                    if (type2 instanceof ValueState.StateType.Custom) {
                        State child10 = state.getChild();
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append(str);
                        sb10.append(((ValueState.StateType.Custom) valueState.getType()).getCharacter());
                        str = appendPlaceholder(child10, sb10.toString());
                    } else {
                        throw new NoWhenBranchMatchedException();
                    }
                }
                return str;
            }
        }
    }

    private final boolean noMandatoryCharactersLeftAfterState(State state) {
        boolean z;
        if (state instanceof EOLState) {
            z = true;
        } else if (state instanceof ValueState) {
            return ((ValueState) state).isElliptical();
        } else {
            if (state instanceof FixedState) {
                z = false;
            } else {
                z = noMandatoryCharactersLeftAfterState(state.nextState());
            }
        }
        return z;
    }
}
