package kotlin.properties;

import kotlin.Metadata;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;

@Metadata(mo15441bv = {1, 0, 3}, mo15442d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J)\u0010\u0002\u001a\u00020\u00032\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u00052\u0006\u0010\u0006\u001a\u00028\u00002\u0006\u0010\u0007\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010\b¨\u0006\t"}, mo15443d2 = {"kotlin/properties/Delegates$vetoable$1", "Lkotlin/properties/ObservableProperty;", "beforeChange", "", "property", "Lkotlin/reflect/KProperty;", "oldValue", "newValue", "(Lkotlin/reflect/KProperty;Ljava/lang/Object;Ljava/lang/Object;)Z", "kotlin-stdlib"}, mo15444k = 1, mo15445mv = {1, 1, 15})
/* compiled from: Delegates.kt */
public final class Delegates$vetoable$1 extends ObservableProperty<T> {
    final /* synthetic */ Object $initialValue;
    final /* synthetic */ Function3 $onChange;

    public Delegates$vetoable$1(Function3 function3, Object obj, Object obj2) {
        this.$onChange = function3;
        this.$initialValue = obj;
        super(obj2);
    }

    /* access modifiers changed from: protected */
    public boolean beforeChange(@NotNull KProperty<?> kProperty, T t, T t2) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        return ((Boolean) this.$onChange.invoke(kProperty, t, t2)).booleanValue();
    }
}