package com.facebook.react.bridge.queue;

import android.os.Build.VERSION;
import androidx.annotation.Nullable;
import com.facebook.infer.annotation.Assertions;

public class ReactQueueConfigurationSpec {
    private static final long LEGACY_STACK_SIZE_BYTES = 2000000;
    private final MessageQueueThreadSpec mJSQueueThreadSpec;
    private final MessageQueueThreadSpec mNativeModulesQueueThreadSpec;

    public static class Builder {
        @Nullable
        private MessageQueueThreadSpec mJSQueueSpec;
        @Nullable
        private MessageQueueThreadSpec mNativeModulesQueueSpec;

        public Builder setNativeModulesQueueThreadSpec(MessageQueueThreadSpec messageQueueThreadSpec) {
            Assertions.assertCondition(this.mNativeModulesQueueSpec == null, "Setting native modules queue spec multiple times!");
            this.mNativeModulesQueueSpec = messageQueueThreadSpec;
            return this;
        }

        public Builder setJSQueueThreadSpec(MessageQueueThreadSpec messageQueueThreadSpec) {
            Assertions.assertCondition(this.mJSQueueSpec == null, "Setting JS queue multiple times!");
            this.mJSQueueSpec = messageQueueThreadSpec;
            return this;
        }

        public ReactQueueConfigurationSpec build() {
            return new ReactQueueConfigurationSpec((MessageQueueThreadSpec) Assertions.assertNotNull(this.mNativeModulesQueueSpec), (MessageQueueThreadSpec) Assertions.assertNotNull(this.mJSQueueSpec));
        }
    }

    private ReactQueueConfigurationSpec(MessageQueueThreadSpec messageQueueThreadSpec, MessageQueueThreadSpec messageQueueThreadSpec2) {
        this.mNativeModulesQueueThreadSpec = messageQueueThreadSpec;
        this.mJSQueueThreadSpec = messageQueueThreadSpec2;
    }

    public MessageQueueThreadSpec getNativeModulesQueueThreadSpec() {
        return this.mNativeModulesQueueThreadSpec;
    }

    public MessageQueueThreadSpec getJSQueueThreadSpec() {
        return this.mJSQueueThreadSpec;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ReactQueueConfigurationSpec createDefault() {
        MessageQueueThreadSpec messageQueueThreadSpec;
        String str = "native_modules";
        if (VERSION.SDK_INT < 21) {
            messageQueueThreadSpec = MessageQueueThreadSpec.newBackgroundThreadSpec(str, LEGACY_STACK_SIZE_BYTES);
        } else {
            messageQueueThreadSpec = MessageQueueThreadSpec.newBackgroundThreadSpec(str);
        }
        return builder().setJSQueueThreadSpec(MessageQueueThreadSpec.newBackgroundThreadSpec("js")).setNativeModulesQueueThreadSpec(messageQueueThreadSpec).build();
    }
}
