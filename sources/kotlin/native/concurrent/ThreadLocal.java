package kotlin.native.concurrent;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import kotlin.Metadata;
import kotlin.OptionalExpectation;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.Retention;

@Target({ElementType.TYPE})
@kotlin.annotation.Target(allowedTargets = {AnnotationTarget.PROPERTY, AnnotationTarget.CLASS})
@Metadata(mo15441bv = {1, 0, 3}, mo15442d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\"\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002"}, mo15443d2 = {"Lkotlin/native/concurrent/ThreadLocal;", "", "kotlin-stdlib"}, mo15444k = 1, mo15445mv = {1, 1, 15})
@OptionalExpectation
@Retention(AnnotationRetention.BINARY)
@java.lang.annotation.Retention(RetentionPolicy.CLASS)
/* compiled from: NativeAnnotationsH.kt */
@interface ThreadLocal {
}
