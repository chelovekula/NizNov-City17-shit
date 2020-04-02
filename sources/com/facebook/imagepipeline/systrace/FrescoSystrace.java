package com.facebook.imagepipeline.systrace;

import javax.annotation.Nullable;

public class FrescoSystrace {
    public static final ArgsBuilder NO_OP_ARGS_BUILDER = new NoOpArgsBuilder();
    @Nullable
    private static volatile Systrace sInstance = null;

    public interface ArgsBuilder {
        ArgsBuilder arg(String str, double d);

        ArgsBuilder arg(String str, int i);

        ArgsBuilder arg(String str, long j);

        ArgsBuilder arg(String str, Object obj);

        void flush();
    }

    private static final class NoOpArgsBuilder implements ArgsBuilder {
        public ArgsBuilder arg(String str, double d) {
            return this;
        }

        public ArgsBuilder arg(String str, int i) {
            return this;
        }

        public ArgsBuilder arg(String str, long j) {
            return this;
        }

        public ArgsBuilder arg(String str, Object obj) {
            return this;
        }

        public void flush() {
        }

        private NoOpArgsBuilder() {
        }
    }

    public interface Systrace {
        void beginSection(String str);

        ArgsBuilder beginSectionWithArgs(String str);

        void endSection();

        boolean isTracing();
    }

    private FrescoSystrace() {
    }

    public static void provide(Systrace systrace) {
        sInstance = systrace;
    }

    public static void beginSection(String str) {
        getInstance().beginSection(str);
    }

    public static ArgsBuilder beginSectionWithArgs(String str) {
        return getInstance().beginSectionWithArgs(str);
    }

    public static void endSection() {
        getInstance().endSection();
    }

    public static boolean isTracing() {
        return getInstance().isTracing();
    }

    private static Systrace getInstance() {
        if (sInstance == null) {
            synchronized (FrescoSystrace.class) {
                if (sInstance == null) {
                    sInstance = new DefaultFrescoSystrace();
                }
            }
        }
        return sInstance;
    }
}
