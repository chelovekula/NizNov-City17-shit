package kotlin.collections.unsigned;

import kotlin.Metadata;
import kotlin.UIntArray;
import kotlin.collections.UIntIterator;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

@Metadata(mo15441bv = {1, 0, 3}, mo15442d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo15443d2 = {"<anonymous>", "Lkotlin/collections/UIntIterator;", "invoke"}, mo15444k = 3, mo15445mv = {1, 1, 15})
/* compiled from: _UArrays.kt */
final class UArraysKt___UArraysKt$withIndex$1 extends Lambda implements Function0<UIntIterator> {
    final /* synthetic */ int[] $this_withIndex;

    UArraysKt___UArraysKt$withIndex$1(int[] iArr) {
        this.$this_withIndex = iArr;
        super(0);
    }

    @NotNull
    public final UIntIterator invoke() {
        return UIntArray.m323iteratorimpl(this.$this_withIndex);
    }
}
