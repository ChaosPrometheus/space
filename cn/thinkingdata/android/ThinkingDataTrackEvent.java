package cn.thinkingdata.android;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ThinkingDataTrackEvent {
    String appId() default "";

    String eventName() default "";

    String properties() default "{}";
}
