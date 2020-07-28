package com.artyom.telegram.api.telegram_spring_api.telegram_api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BotApiMethodFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(BotApiMethodFilter.class);

    private final Object bean;
    private final Method method;
    private final Process processUpdate;

    public BotApiMethodFilter(Object bean, Method method) {
        this.bean = bean;
        this.method = method;

        processUpdate = this::processSingle;
    }

    public abstract boolean successUpdatePredicate(Update update);

    public boolean process(Update update) {
        if(!successUpdatePredicate(update)) return true;

        try {
            return processUpdate.accept(update);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("bad invoke method", e);
        }

        return true;
    }


    private boolean processSingle(Update update) throws InvocationTargetException, IllegalAccessException {
        return (boolean) method.invoke(bean, update);
    }

    private interface Process{
        boolean accept(Update update) throws InvocationTargetException, IllegalAccessException;
    }
}