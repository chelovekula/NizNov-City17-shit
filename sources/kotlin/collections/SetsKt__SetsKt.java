package kotlin.collections;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo15441bv = {1, 0, 3}, mo15442d1 = {"\u00000\n\u0000\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\b\u0005\u001a\u0012\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002\u001a\u001f\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0004j\b\u0012\u0004\u0012\u0002H\u0002`\u0005\"\u0004\b\u0000\u0010\u0002H\b\u001a5\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0004j\b\u0012\u0004\u0012\u0002H\u0002`\u0005\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002¢\u0006\u0002\u0010\b\u001a\u001f\u0010\t\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\nj\b\u0012\u0004\u0012\u0002H\u0002`\u000b\"\u0004\b\u0000\u0010\u0002H\b\u001a5\u0010\t\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\nj\b\u0012\u0004\u0012\u0002H\u0002`\u000b\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002¢\u0006\u0002\u0010\f\u001a\u0015\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u00020\u000e\"\u0004\b\u0000\u0010\u0002H\b\u001a+\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002¢\u0006\u0002\u0010\u000f\u001a\u0015\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002H\b\u001a+\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002¢\u0006\u0002\u0010\u000f\u001a\u001e\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0000\u001a!\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0004\u0012\u0002H\u0002\u0018\u00010\u0001H\b¨\u0006\u0013"}, mo15443d2 = {"emptySet", "", "T", "hashSetOf", "Ljava/util/HashSet;", "Lkotlin/collections/HashSet;", "elements", "", "([Ljava/lang/Object;)Ljava/util/HashSet;", "linkedSetOf", "Ljava/util/LinkedHashSet;", "Lkotlin/collections/LinkedHashSet;", "([Ljava/lang/Object;)Ljava/util/LinkedHashSet;", "mutableSetOf", "", "([Ljava/lang/Object;)Ljava/util/Set;", "setOf", "optimizeReadOnlySet", "orEmpty", "kotlin-stdlib"}, mo15444k = 5, mo15445mv = {1, 1, 15}, mo15447xi = 1, mo15448xs = "kotlin/collections/SetsKt")
/* compiled from: Sets.kt */
class SetsKt__SetsKt extends SetsKt__SetsJVMKt {
    @NotNull
    public static final <T> Set<T> emptySet() {
        return EmptySet.INSTANCE;
    }

    @NotNull
    public static final <T> Set<T> setOf(@NotNull T... tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return tArr.length > 0 ? ArraysKt.toSet(tArr) : SetsKt.emptySet();
    }

    @InlineOnly
    private static final <T> Set<T> setOf() {
        return SetsKt.emptySet();
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> Set<T> mutableSetOf() {
        return new LinkedHashSet<>();
    }

    @NotNull
    public static final <T> Set<T> mutableSetOf(@NotNull T... tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return (Set) ArraysKt.toCollection(tArr, new LinkedHashSet(MapsKt.mapCapacity(tArr.length)));
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> HashSet<T> hashSetOf() {
        return new HashSet<>();
    }

    @NotNull
    public static final <T> HashSet<T> hashSetOf(@NotNull T... tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return (HashSet) ArraysKt.toCollection(tArr, new HashSet(MapsKt.mapCapacity(tArr.length)));
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> LinkedHashSet<T> linkedSetOf() {
        return new LinkedHashSet<>();
    }

    @NotNull
    public static final <T> LinkedHashSet<T> linkedSetOf(@NotNull T... tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return (LinkedHashSet) ArraysKt.toCollection(tArr, new LinkedHashSet(MapsKt.mapCapacity(tArr.length)));
    }

    @InlineOnly
    private static final <T> Set<T> orEmpty(@Nullable Set<? extends T> set) {
        return set != null ? set : SetsKt.emptySet();
    }

    @NotNull
    public static final <T> Set<T> optimizeReadOnlySet(@NotNull Set<? extends T> set) {
        Intrinsics.checkParameterIsNotNull(set, "$this$optimizeReadOnlySet");
        int size = set.size();
        if (size == 0) {
            return SetsKt.emptySet();
        }
        if (size != 1) {
            return set;
        }
        return SetsKt.setOf(set.iterator().next());
    }
}
