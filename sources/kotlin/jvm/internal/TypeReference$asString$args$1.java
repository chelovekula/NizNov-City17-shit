package kotlin.jvm.internal;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.reflect.KTypeProjection;
import org.jetbrains.annotations.NotNull;

@Metadata(mo15441bv = {1, 0, 3}, mo15442d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo15443d2 = {"<anonymous>", "", "it", "Lkotlin/reflect/KTypeProjection;", "invoke"}, mo15444k = 3, mo15445mv = {1, 1, 15})
/* compiled from: TypeReference.kt */
final class TypeReference$asString$args$1 extends Lambda implements Function1<KTypeProjection, String> {
    final /* synthetic */ TypeReference this$0;

    TypeReference$asString$args$1(TypeReference typeReference) {
        this.this$0 = typeReference;
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull KTypeProjection kTypeProjection) {
        Intrinsics.checkParameterIsNotNull(kTypeProjection, "it");
        return this.this$0.asString(kTypeProjection);
    }
}
