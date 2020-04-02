package com.facebook.common.internal;

public class Suppliers {
    public static final Supplier<Boolean> BOOLEAN_FALSE = new Supplier<Boolean>() {
        public Boolean get() {
            return Boolean.valueOf(false);
        }
    };
    public static final Supplier<Boolean> BOOLEAN_TRUE = new Supplier<Boolean>() {
        public Boolean get() {
            return Boolean.valueOf(true);
        }
    };

    /* renamed from: of */
    public static <T> Supplier<T> m29of(final T t) {
        return new Supplier<T>() {
            public T get() {
                return t;
            }
        };
    }
}
