package com.artyom.telegram.api.telegram_spring_api.telegram_api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.artyom.telegram.api.telegram_spring_api.telegram_api.BotRequestMethod.CALLBACK;
import static com.artyom.telegram.api.telegram_spring_api.telegram_api.BotRequestMethod.MESSAGE;


@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BotRequestMapping {
    String[] value() default {""};
    BotRequestMethod[] method() default {MESSAGE, CALLBACK};
    boolean filtered() default false;
}