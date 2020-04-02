package com.redmadrobot.inputmask;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy;
import com.redmadrobot.inputmask.helper.Mask;
import com.redmadrobot.inputmask.helper.Mask.Result;
import com.redmadrobot.inputmask.model.CaretString;
import com.redmadrobot.inputmask.model.Notation;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IndexedValue;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0016\u0018\u0000 \\2\u00020\u00012\u00020\u0002:\u0002\\]B\u0017\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007B!\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\nB+\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\u0001\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\fB3\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\u0001\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\u000fB%\b\u0016\u0012\u0006\u0010\u0010\u001a\u00020\u0004\u0012\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00040\u0012\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0013B/\b\u0016\u0012\u0006\u0010\u0010\u001a\u00020\u0004\u0012\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00040\u0012\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\u0014B9\b\u0016\u0012\u0006\u0010\u0010\u001a\u00020\u0004\u0012\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00040\u0012\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\u0001\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\u0015BA\b\u0016\u0012\u0006\u0010\u0010\u001a\u00020\u0004\u0012\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00040\u0012\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\u0001\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\u0016BI\b\u0016\u0012\u0006\u0010\u0010\u001a\u00020\u0004\u0012\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00040\u0012\u0012\u0006\u0010\u0017\u001a\u00020\u0018\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\u0001\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\u0019Ba\u0012\u0006\u0010\u0010\u001a\u00020\u0004\u0012\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00040\u0012\u0012\u000e\b\u0002\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0012\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u0012\b\b\u0002\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\u001cJ\u0006\u0010?\u001a\u00020+J\u0006\u0010@\u001a\u00020+J\u0012\u0010A\u001a\u00020B2\b\u0010C\u001a\u0004\u0018\u00010DH\u0016J*\u0010E\u001a\u00020B2\b\u0010F\u001a\u0004\u0018\u00010G2\u0006\u0010H\u001a\u00020+2\u0006\u0010I\u001a\u00020+2\u0006\u0010J\u001a\u00020+H\u0016J(\u0010K\u001a\u00020+2\u0006\u0010L\u001a\u0002082\u0006\u0010M\u001a\u00020\u00042\u0006\u0010*\u001a\u00020+2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u001a\u0010N\u001a\u00020B2\b\u0010O\u001a\u0004\u0018\u00010P2\u0006\u0010Q\u001a\u00020\u000eH\u0016J(\u0010R\u001a\u00020B2\u0006\u0010M\u001a\u00020G2\u0006\u0010S\u001a\u00020+2\u0006\u0010T\u001a\u00020+2\u0006\u0010U\u001a\u00020+H\u0016J \u0010V\u001a\u0002082\u0006\u0010M\u001a\u00020\u00042\u0006\u0010*\u001a\u00020+2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u0006\u0010W\u001a\u00020\u0004J\u0012\u0010X\u001a\u0004\u0018\u00010Y2\u0006\u0010M\u001a\u00020\u0004H\u0016J\u0018\u0010X\u001a\u00020Y2\u0006\u0010M\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0006\u0010Z\u001a\u00020+J\u0006\u0010[\u001a\u00020+R \u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00040\u0012X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u001a\u0010\u0017\u001a\u00020\u0018X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\u000e\u0010%\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010'\"\u0004\b(\u0010)R\u000e\u0010*\u001a\u00020+X\u000e¢\u0006\u0002\n\u0000R \u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0012X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b,\u0010\u001e\"\u0004\b-\u0010 R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060.X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0001X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b/\u00100\"\u0004\b1\u00102R\u001a\u0010\u0010\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b3\u00104\"\u0004\b5\u00106R\u0014\u00107\u001a\u0002088BX\u0004¢\u0006\u0006\u001a\u0004\b9\u0010:R\u001c\u0010\b\u001a\u0004\u0018\u00010\tX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b;\u0010<\"\u0004\b=\u0010>¨\u0006^"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/MaskedTextChangedListener;", "Landroid/text/TextWatcher;", "Landroid/view/View$OnFocusChangeListener;", "format", "", "field", "Landroid/widget/EditText;", "(Ljava/lang/String;Landroid/widget/EditText;)V", "valueListener", "Lcom/redmadrobot/inputmask/MaskedTextChangedListener$ValueListener;", "(Ljava/lang/String;Landroid/widget/EditText;Lcom/redmadrobot/inputmask/MaskedTextChangedListener$ValueListener;)V", "listener", "(Ljava/lang/String;Landroid/widget/EditText;Landroid/text/TextWatcher;Lcom/redmadrobot/inputmask/MaskedTextChangedListener$ValueListener;)V", "autocomplete", "", "(Ljava/lang/String;ZLandroid/widget/EditText;Landroid/text/TextWatcher;Lcom/redmadrobot/inputmask/MaskedTextChangedListener$ValueListener;)V", "primaryFormat", "affineFormats", "", "(Ljava/lang/String;Ljava/util/List;Landroid/widget/EditText;)V", "(Ljava/lang/String;Ljava/util/List;Landroid/widget/EditText;Lcom/redmadrobot/inputmask/MaskedTextChangedListener$ValueListener;)V", "(Ljava/lang/String;Ljava/util/List;Landroid/widget/EditText;Landroid/text/TextWatcher;Lcom/redmadrobot/inputmask/MaskedTextChangedListener$ValueListener;)V", "(Ljava/lang/String;Ljava/util/List;ZLandroid/widget/EditText;Landroid/text/TextWatcher;Lcom/redmadrobot/inputmask/MaskedTextChangedListener$ValueListener;)V", "affinityCalculationStrategy", "Lcom/redmadrobot/inputmask/helper/AffinityCalculationStrategy;", "(Ljava/lang/String;Ljava/util/List;Lcom/redmadrobot/inputmask/helper/AffinityCalculationStrategy;ZLandroid/widget/EditText;Landroid/text/TextWatcher;Lcom/redmadrobot/inputmask/MaskedTextChangedListener$ValueListener;)V", "customNotations", "Lcom/redmadrobot/inputmask/model/Notation;", "(Ljava/lang/String;Ljava/util/List;Ljava/util/List;Lcom/redmadrobot/inputmask/helper/AffinityCalculationStrategy;ZLandroid/widget/EditText;Landroid/text/TextWatcher;Lcom/redmadrobot/inputmask/MaskedTextChangedListener$ValueListener;)V", "getAffineFormats", "()Ljava/util/List;", "setAffineFormats", "(Ljava/util/List;)V", "getAffinityCalculationStrategy", "()Lcom/redmadrobot/inputmask/helper/AffinityCalculationStrategy;", "setAffinityCalculationStrategy", "(Lcom/redmadrobot/inputmask/helper/AffinityCalculationStrategy;)V", "afterText", "getAutocomplete", "()Z", "setAutocomplete", "(Z)V", "caretPosition", "", "getCustomNotations", "setCustomNotations", "Ljava/lang/ref/WeakReference;", "getListener", "()Landroid/text/TextWatcher;", "setListener", "(Landroid/text/TextWatcher;)V", "getPrimaryFormat", "()Ljava/lang/String;", "setPrimaryFormat", "(Ljava/lang/String;)V", "primaryMask", "Lcom/redmadrobot/inputmask/helper/Mask;", "getPrimaryMask", "()Lcom/redmadrobot/inputmask/helper/Mask;", "getValueListener", "()Lcom/redmadrobot/inputmask/MaskedTextChangedListener$ValueListener;", "setValueListener", "(Lcom/redmadrobot/inputmask/MaskedTextChangedListener$ValueListener;)V", "acceptableTextLength", "acceptableValueLength", "afterTextChanged", "", "edit", "Landroid/text/Editable;", "beforeTextChanged", "p0", "", "p1", "p2", "p3", "calculateAffinity", "mask", "text", "onFocusChange", "view", "Landroid/view/View;", "hasFocus", "onTextChanged", "cursorPosition", "before", "count", "pickMask", "placeholder", "setText", "Lcom/redmadrobot/inputmask/helper/Mask$Result;", "totalTextLength", "totalValueLength", "Companion", "ValueListener", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
/* compiled from: MaskedTextChangedListener.kt */
public class MaskedTextChangedListener implements TextWatcher, OnFocusChangeListener {
    public static final Companion Companion = new Companion(null);
    @NotNull
    private List<String> affineFormats;
    @NotNull
    private AffinityCalculationStrategy affinityCalculationStrategy;
    private String afterText;
    private boolean autocomplete;
    private int caretPosition;
    @NotNull
    private List<Notation> customNotations;
    private final WeakReference<EditText> field;
    @Nullable
    private TextWatcher listener;
    @NotNull
    private String primaryFormat;
    @Nullable
    private ValueListener valueListener;

    @Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\"\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\nJ<\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\f2\b\b\u0002\u0010\r\u001a\u00020\u000e2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\nJb\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\f2\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\f2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u0011\u001a\u00020\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00142\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n¨\u0006\u0015"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/MaskedTextChangedListener$Companion;", "", "()V", "installOn", "Lcom/redmadrobot/inputmask/MaskedTextChangedListener;", "editText", "Landroid/widget/EditText;", "primaryFormat", "", "valueListener", "Lcom/redmadrobot/inputmask/MaskedTextChangedListener$ValueListener;", "affineFormats", "", "affinityCalculationStrategy", "Lcom/redmadrobot/inputmask/helper/AffinityCalculationStrategy;", "customNotations", "Lcom/redmadrobot/inputmask/model/Notation;", "autocomplete", "", "listener", "Landroid/text/TextWatcher;", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
    /* compiled from: MaskedTextChangedListener.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @NotNull
        public static /* bridge */ /* synthetic */ MaskedTextChangedListener installOn$default(Companion companion, EditText editText, String str, ValueListener valueListener, int i, Object obj) {
            if ((i & 4) != 0) {
                valueListener = null;
            }
            return companion.installOn(editText, str, valueListener);
        }

        @NotNull
        public final MaskedTextChangedListener installOn(@NotNull EditText editText, @NotNull String str, @Nullable ValueListener valueListener) {
            Intrinsics.checkParameterIsNotNull(editText, "editText");
            Intrinsics.checkParameterIsNotNull(str, "primaryFormat");
            return installOn(editText, str, CollectionsKt.emptyList(), AffinityCalculationStrategy.WHOLE_STRING, valueListener);
        }

        @NotNull
        public static /* bridge */ /* synthetic */ MaskedTextChangedListener installOn$default(Companion companion, EditText editText, String str, List list, AffinityCalculationStrategy affinityCalculationStrategy, ValueListener valueListener, int i, Object obj) {
            if ((i & 4) != 0) {
                list = CollectionsKt.emptyList();
            }
            List list2 = list;
            if ((i & 8) != 0) {
                affinityCalculationStrategy = AffinityCalculationStrategy.WHOLE_STRING;
            }
            AffinityCalculationStrategy affinityCalculationStrategy2 = affinityCalculationStrategy;
            if ((i & 16) != 0) {
                valueListener = null;
            }
            return companion.installOn(editText, str, list2, affinityCalculationStrategy2, valueListener);
        }

        @NotNull
        public final MaskedTextChangedListener installOn(@NotNull EditText editText, @NotNull String str, @NotNull List<String> list, @NotNull AffinityCalculationStrategy affinityCalculationStrategy, @Nullable ValueListener valueListener) {
            Intrinsics.checkParameterIsNotNull(editText, "editText");
            Intrinsics.checkParameterIsNotNull(str, "primaryFormat");
            Intrinsics.checkParameterIsNotNull(list, "affineFormats");
            Intrinsics.checkParameterIsNotNull(affinityCalculationStrategy, "affinityCalculationStrategy");
            return installOn(editText, str, list, CollectionsKt.emptyList(), affinityCalculationStrategy, true, null, valueListener);
        }

        @NotNull
        public static /* bridge */ /* synthetic */ MaskedTextChangedListener installOn$default(Companion companion, EditText editText, String str, List list, List list2, AffinityCalculationStrategy affinityCalculationStrategy, boolean z, TextWatcher textWatcher, ValueListener valueListener, int i, Object obj) {
            int i2 = i;
            return companion.installOn(editText, str, (i2 & 4) != 0 ? CollectionsKt.emptyList() : list, (i2 & 8) != 0 ? CollectionsKt.emptyList() : list2, (i2 & 16) != 0 ? AffinityCalculationStrategy.WHOLE_STRING : affinityCalculationStrategy, (i2 & 32) != 0 ? true : z, (i2 & 64) != 0 ? null : textWatcher, (i2 & 128) != 0 ? null : valueListener);
        }

        @NotNull
        public final MaskedTextChangedListener installOn(@NotNull EditText editText, @NotNull String str, @NotNull List<String> list, @NotNull List<Notation> list2, @NotNull AffinityCalculationStrategy affinityCalculationStrategy, boolean z, @Nullable TextWatcher textWatcher, @Nullable ValueListener valueListener) {
            EditText editText2 = editText;
            Intrinsics.checkParameterIsNotNull(editText, "editText");
            String str2 = str;
            Intrinsics.checkParameterIsNotNull(str, "primaryFormat");
            List<String> list3 = list;
            Intrinsics.checkParameterIsNotNull(list, "affineFormats");
            List<Notation> list4 = list2;
            Intrinsics.checkParameterIsNotNull(list2, "customNotations");
            AffinityCalculationStrategy affinityCalculationStrategy2 = affinityCalculationStrategy;
            Intrinsics.checkParameterIsNotNull(affinityCalculationStrategy2, "affinityCalculationStrategy");
            MaskedTextChangedListener maskedTextChangedListener = new MaskedTextChangedListener(str2, list3, list4, affinityCalculationStrategy2, z, editText, textWatcher, valueListener);
            editText.addTextChangedListener(maskedTextChangedListener);
            editText.setOnFocusChangeListener(maskedTextChangedListener);
            return maskedTextChangedListener;
        }
    }

    @Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H&¨\u0006\t"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/MaskedTextChangedListener$ValueListener;", "", "onTextChanged", "", "maskFilled", "", "extractedValue", "", "formattedValue", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
    /* compiled from: MaskedTextChangedListener.kt */
    public interface ValueListener {
        void onTextChanged(boolean z, @NotNull String str, @NotNull String str2);
    }

    public MaskedTextChangedListener(@NotNull String str, @NotNull List<String> list, @NotNull List<Notation> list2, @NotNull AffinityCalculationStrategy affinityCalculationStrategy2, boolean z, @NotNull EditText editText, @Nullable TextWatcher textWatcher, @Nullable ValueListener valueListener2) {
        Intrinsics.checkParameterIsNotNull(str, "primaryFormat");
        Intrinsics.checkParameterIsNotNull(list, "affineFormats");
        Intrinsics.checkParameterIsNotNull(list2, "customNotations");
        Intrinsics.checkParameterIsNotNull(affinityCalculationStrategy2, "affinityCalculationStrategy");
        Intrinsics.checkParameterIsNotNull(editText, "field");
        this.primaryFormat = str;
        this.affineFormats = list;
        this.customNotations = list2;
        this.affinityCalculationStrategy = affinityCalculationStrategy2;
        this.autocomplete = z;
        this.listener = textWatcher;
        this.valueListener = valueListener2;
        this.afterText = "";
        this.field = new WeakReference<>(editText);
    }

    @NotNull
    public final String getPrimaryFormat() {
        return this.primaryFormat;
    }

    public final void setPrimaryFormat(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.primaryFormat = str;
    }

    public /* synthetic */ MaskedTextChangedListener(String str, List list, List list2, AffinityCalculationStrategy affinityCalculationStrategy2, boolean z, EditText editText, TextWatcher textWatcher, ValueListener valueListener2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        int i2 = i;
        this(str, (i2 & 2) != 0 ? CollectionsKt.emptyList() : list, (i2 & 4) != 0 ? CollectionsKt.emptyList() : list2, (i2 & 8) != 0 ? AffinityCalculationStrategy.WHOLE_STRING : affinityCalculationStrategy2, (i2 & 16) != 0 ? true : z, editText, (i2 & 64) != 0 ? null : textWatcher, (i2 & 128) != 0 ? null : valueListener2);
    }

    @NotNull
    public final List<String> getAffineFormats() {
        return this.affineFormats;
    }

    public final void setAffineFormats(@NotNull List<String> list) {
        Intrinsics.checkParameterIsNotNull(list, "<set-?>");
        this.affineFormats = list;
    }

    @NotNull
    public final List<Notation> getCustomNotations() {
        return this.customNotations;
    }

    public final void setCustomNotations(@NotNull List<Notation> list) {
        Intrinsics.checkParameterIsNotNull(list, "<set-?>");
        this.customNotations = list;
    }

    @NotNull
    public final AffinityCalculationStrategy getAffinityCalculationStrategy() {
        return this.affinityCalculationStrategy;
    }

    public final void setAffinityCalculationStrategy(@NotNull AffinityCalculationStrategy affinityCalculationStrategy2) {
        Intrinsics.checkParameterIsNotNull(affinityCalculationStrategy2, "<set-?>");
        this.affinityCalculationStrategy = affinityCalculationStrategy2;
    }

    public final boolean getAutocomplete() {
        return this.autocomplete;
    }

    public final void setAutocomplete(boolean z) {
        this.autocomplete = z;
    }

    @Nullable
    public final TextWatcher getListener() {
        return this.listener;
    }

    public final void setListener(@Nullable TextWatcher textWatcher) {
        this.listener = textWatcher;
    }

    @Nullable
    public final ValueListener getValueListener() {
        return this.valueListener;
    }

    public final void setValueListener(@Nullable ValueListener valueListener2) {
        this.valueListener = valueListener2;
    }

    private final Mask getPrimaryMask() {
        return Mask.Factory.getOrCreate(this.primaryFormat, this.customNotations);
    }

    public MaskedTextChangedListener(@NotNull String str, @NotNull EditText editText) {
        Intrinsics.checkParameterIsNotNull(str, "format");
        Intrinsics.checkParameterIsNotNull(editText, "field");
        this(str, editText, (ValueListener) null);
    }

    public MaskedTextChangedListener(@NotNull String str, @NotNull EditText editText, @Nullable ValueListener valueListener2) {
        Intrinsics.checkParameterIsNotNull(str, "format");
        Intrinsics.checkParameterIsNotNull(editText, "field");
        this(str, editText, (TextWatcher) null, valueListener2);
    }

    public MaskedTextChangedListener(@NotNull String str, @NotNull EditText editText, @Nullable TextWatcher textWatcher, @Nullable ValueListener valueListener2) {
        Intrinsics.checkParameterIsNotNull(str, "format");
        Intrinsics.checkParameterIsNotNull(editText, "field");
        this(str, true, editText, textWatcher, valueListener2);
    }

    public MaskedTextChangedListener(@NotNull String str, boolean z, @NotNull EditText editText, @Nullable TextWatcher textWatcher, @Nullable ValueListener valueListener2) {
        Intrinsics.checkParameterIsNotNull(str, "format");
        Intrinsics.checkParameterIsNotNull(editText, "field");
        this(str, CollectionsKt.emptyList(), CollectionsKt.emptyList(), AffinityCalculationStrategy.WHOLE_STRING, z, editText, textWatcher, valueListener2);
    }

    public MaskedTextChangedListener(@NotNull String str, @NotNull List<String> list, @NotNull EditText editText) {
        Intrinsics.checkParameterIsNotNull(str, "primaryFormat");
        Intrinsics.checkParameterIsNotNull(list, "affineFormats");
        Intrinsics.checkParameterIsNotNull(editText, "field");
        this(str, list, editText, (ValueListener) null);
    }

    public MaskedTextChangedListener(@NotNull String str, @NotNull List<String> list, @NotNull EditText editText, @Nullable ValueListener valueListener2) {
        Intrinsics.checkParameterIsNotNull(str, "primaryFormat");
        Intrinsics.checkParameterIsNotNull(list, "affineFormats");
        Intrinsics.checkParameterIsNotNull(editText, "field");
        this(str, list, editText, (TextWatcher) null, valueListener2);
    }

    public MaskedTextChangedListener(@NotNull String str, @NotNull List<String> list, @NotNull EditText editText, @Nullable TextWatcher textWatcher, @Nullable ValueListener valueListener2) {
        Intrinsics.checkParameterIsNotNull(str, "primaryFormat");
        Intrinsics.checkParameterIsNotNull(list, "affineFormats");
        Intrinsics.checkParameterIsNotNull(editText, "field");
        this(str, list, true, editText, textWatcher, valueListener2);
    }

    public MaskedTextChangedListener(@NotNull String str, @NotNull List<String> list, boolean z, @NotNull EditText editText, @Nullable TextWatcher textWatcher, @Nullable ValueListener valueListener2) {
        Intrinsics.checkParameterIsNotNull(str, "primaryFormat");
        Intrinsics.checkParameterIsNotNull(list, "affineFormats");
        Intrinsics.checkParameterIsNotNull(editText, "field");
        this(str, list, AffinityCalculationStrategy.WHOLE_STRING, z, editText, textWatcher, valueListener2);
    }

    public MaskedTextChangedListener(@NotNull String str, @NotNull List<String> list, @NotNull AffinityCalculationStrategy affinityCalculationStrategy2, boolean z, @NotNull EditText editText, @Nullable TextWatcher textWatcher, @Nullable ValueListener valueListener2) {
        String str2 = str;
        Intrinsics.checkParameterIsNotNull(str, "primaryFormat");
        List<String> list2 = list;
        Intrinsics.checkParameterIsNotNull(list, "affineFormats");
        AffinityCalculationStrategy affinityCalculationStrategy3 = affinityCalculationStrategy2;
        Intrinsics.checkParameterIsNotNull(affinityCalculationStrategy2, "affinityCalculationStrategy");
        EditText editText2 = editText;
        Intrinsics.checkParameterIsNotNull(editText, "field");
        this(str2, list2, CollectionsKt.emptyList(), affinityCalculationStrategy3, z, editText2, textWatcher, valueListener2);
    }

    @Nullable
    public Result setText(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "text");
        EditText editText = (EditText) this.field.get();
        if (editText == null) {
            return null;
        }
        Intrinsics.checkExpressionValueIsNotNull(editText, "it");
        Result text = setText(str, editText);
        this.afterText = text.getFormattedText().getString();
        this.caretPosition = text.getFormattedText().getCaretPosition();
        ValueListener valueListener2 = this.valueListener;
        if (valueListener2 != null) {
            valueListener2.onTextChanged(text.getComplete(), text.getExtractedValue(), this.afterText);
        }
        return text;
    }

    @NotNull
    public Result setText(@NotNull String str, @NotNull EditText editText) {
        Intrinsics.checkParameterIsNotNull(str, "text");
        Intrinsics.checkParameterIsNotNull(editText, "field");
        Result apply = pickMask(str, str.length(), this.autocomplete).apply(new CaretString(str, str.length()), this.autocomplete);
        editText.setText(apply.getFormattedText().getString());
        editText.setSelection(apply.getFormattedText().getCaretPosition());
        return apply;
    }

    @NotNull
    public final String placeholder() {
        return getPrimaryMask().placeholder();
    }

    public final int acceptableTextLength() {
        return getPrimaryMask().acceptableTextLength();
    }

    public final int totalTextLength() {
        return getPrimaryMask().totalTextLength();
    }

    public final int acceptableValueLength() {
        return getPrimaryMask().acceptableValueLength();
    }

    public final int totalValueLength() {
        return getPrimaryMask().totalValueLength();
    }

    public void afterTextChanged(@Nullable Editable editable) {
        EditText editText = (EditText) this.field.get();
        if (editText != null) {
            editText.removeTextChangedListener(this);
        }
        if (editable != null) {
            editable.replace(0, editable.length(), this.afterText);
        }
        EditText editText2 = (EditText) this.field.get();
        if (editText2 != null) {
            editText2.setSelection(this.caretPosition);
        }
        EditText editText3 = (EditText) this.field.get();
        if (editText3 != null) {
            editText3.addTextChangedListener(this);
        }
        TextWatcher textWatcher = this.listener;
        if (textWatcher != null) {
            textWatcher.afterTextChanged(editable);
        }
    }

    public void beforeTextChanged(@Nullable CharSequence charSequence, int i, int i2, int i3) {
        TextWatcher textWatcher = this.listener;
        if (textWatcher != null) {
            textWatcher.beforeTextChanged(charSequence, i, i2, i3);
        }
    }

    public void onTextChanged(@NotNull CharSequence charSequence, int i, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(charSequence, "text");
        boolean z = true;
        boolean z2 = i2 > 0 && i3 == 0;
        int i4 = z2 ? i : i3 + i;
        Mask pickMask = pickMask(charSequence.toString(), i4, this.autocomplete && !z2);
        CaretString caretString = new CaretString(charSequence.toString(), i4);
        if (!this.autocomplete || z2) {
            z = false;
        }
        Result apply = pickMask.apply(caretString, z);
        this.afterText = apply.getFormattedText().getString();
        if (!z2) {
            i = apply.getFormattedText().getCaretPosition();
        }
        this.caretPosition = i;
        ValueListener valueListener2 = this.valueListener;
        if (valueListener2 != null) {
            valueListener2.onTextChanged(apply.getComplete(), apply.getExtractedValue(), this.afterText);
        }
    }

    public void onFocusChange(@Nullable View view, boolean z) {
        String str;
        if (this.autocomplete && z) {
            EditText editText = (EditText) this.field.get();
            Editable editable = null;
            CharSequence text = editText != null ? editText.getText() : null;
            if (text == null) {
                Intrinsics.throwNpe();
            }
            if (text.length() == 0) {
                str = "";
            } else {
                EditText editText2 = (EditText) this.field.get();
                if (editText2 != null) {
                    editable = editText2.getText();
                }
                str = String.valueOf(editable);
            }
            Result apply = pickMask(str, str.length(), this.autocomplete).apply(new CaretString(str, str.length()), this.autocomplete);
            this.afterText = apply.getFormattedText().getString();
            this.caretPosition = apply.getFormattedText().getCaretPosition();
            EditText editText3 = (EditText) this.field.get();
            if (editText3 != null) {
                editText3.setText(this.afterText);
            }
            EditText editText4 = (EditText) this.field.get();
            if (editText4 != null) {
                editText4.setSelection(apply.getFormattedText().getCaretPosition());
            }
            ValueListener valueListener2 = this.valueListener;
            if (valueListener2 != null) {
                valueListener2.onTextChanged(apply.getComplete(), apply.getExtractedValue(), this.afterText);
            }
        }
    }

    private final Mask pickMask(String str, int i, boolean z) {
        if (this.affineFormats.isEmpty()) {
            return getPrimaryMask();
        }
        int calculateAffinity = calculateAffinity(getPrimaryMask(), str, i, z);
        List arrayList = new ArrayList();
        for (String orCreate : this.affineFormats) {
            Mask orCreate2 = Mask.Factory.getOrCreate(orCreate, this.customNotations);
            arrayList.add(new MaskedTextChangedListener$pickMask$MaskAffinity(orCreate2, calculateAffinity(orCreate2, str, i, z)));
        }
        if (arrayList.size() > 1) {
            CollectionsKt.sortWith(arrayList, new MaskedTextChangedListener$pickMask$$inlined$sortByDescending$1());
        }
        int i2 = -1;
        Iterator it = CollectionsKt.withIndex(arrayList).iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            IndexedValue indexedValue = (IndexedValue) it.next();
            int component1 = indexedValue.component1();
            if (calculateAffinity >= ((MaskedTextChangedListener$pickMask$MaskAffinity) indexedValue.component2()).getAffinity()) {
                i2 = component1;
                break;
            }
        }
        if (i2 >= 0) {
            arrayList.add(i2, new MaskedTextChangedListener$pickMask$MaskAffinity(getPrimaryMask(), calculateAffinity));
        } else {
            arrayList.add(new MaskedTextChangedListener$pickMask$MaskAffinity(getPrimaryMask(), calculateAffinity));
        }
        return ((MaskedTextChangedListener$pickMask$MaskAffinity) CollectionsKt.first(arrayList)).getMask();
    }

    private final int calculateAffinity(Mask mask, String str, int i, boolean z) {
        return this.affinityCalculationStrategy.calculateAffinityOfMask(mask, new CaretString(str, i), z);
    }
}
