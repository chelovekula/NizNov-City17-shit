package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.ranges.IntRange;
import org.jetbrains.annotations.NotNull;

@Metadata(mo15441bv = {1, 0, 3}, mo15442d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo15443d2 = {"<anonymous>", "", "it", "Lkotlin/ranges/IntRange;", "invoke"}, mo15444k = 3, mo15445mv = {1, 1, 15})
/* compiled from: Strings.kt */
final class StringsKt__StringsKt$splitToSequence$2 extends Lambda implements Function1<IntRange, String> {
    final /* synthetic */ CharSequence $this_splitToSequence;

    StringsKt__StringsKt$splitToSequence$2(CharSequence charSequence) {
        this.$this_splitToSequence = charSequence;
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull IntRange intRange) {
        Intrinsics.checkParameterIsNotNull(intRange, "it");
        return StringsKt.substring(this.$this_splitToSequence, intRange);
    }
}
