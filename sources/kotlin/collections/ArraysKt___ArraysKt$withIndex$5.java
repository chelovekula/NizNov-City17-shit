package kotlin.collections;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.ArrayIteratorsKt;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

@Metadata(mo15441bv = {1, 0, 3}, mo15442d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo15443d2 = {"<anonymous>", "Lkotlin/collections/LongIterator;", "invoke"}, mo15444k = 3, mo15445mv = {1, 1, 15})
/* compiled from: _Arrays.kt */
final class ArraysKt___ArraysKt$withIndex$5 extends Lambda implements Function0<LongIterator> {
    final /* synthetic */ long[] $this_withIndex;

    ArraysKt___ArraysKt$withIndex$5(long[] jArr) {
        this.$this_withIndex = jArr;
        super(0);
    }

    @NotNull
    public final LongIterator invoke() {
        return ArrayIteratorsKt.iterator(this.$this_withIndex);
    }
}