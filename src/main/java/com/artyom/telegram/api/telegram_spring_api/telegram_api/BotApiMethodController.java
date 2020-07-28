package com.artyom.telegram.api.telegram_spring_api.telegram_api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("ALL")
public abstract class BotApiMethodController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BotApiMethodController.class);

    private final Object bean;
    private final Method method;
    private final Process processUpdate;
    private boolean filtered;

    public BotApiMethodController(Object bean, Method method) {
        this.bean = bean;
        this.method = method;

        processUpdate = typeListReturnDetect() ? this::processList : this::processSingle;
    }

    public abstract boolean successUpdatePredicate(Update update);

    public List<BotApiMethod> process(Update update) {
        if(!successUpdatePredicate(update)) return null;

        try {
            return processUpdate.accept(update);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("bad invoke method", e);
        }

        return null;
    }

    boolean typeListReturnDetect() {
        return List.class.equals(method.getReturnType());
    }

    private List<BotApiMethod> processSingle(Update update) throws InvocationTargetException, IllegalAccessException {
        BotApiMethod botApiMethod = (BotApiMethod) method.invoke(bean, update);
        return botApiMethod != null ? Collections.singletonList(botApiMethod) : new ArrayList<>(0);
    }

    private List<BotApiMethod> processList(Update update) throws InvocationTargetException, IllegalAccessException {
        List<BotApiMethod> botApiMethods = (List<BotApiMethod>) method.invoke(bean, update);
        return botApiMethods != null ? botApiMethods : new ArrayList<>(0);
    }

    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
    }

    public boolean isFiltered() {
        return filtered;
    }


    private interface Process{
        List<BotApiMethod> accept(Update update) throws InvocationTargetException, IllegalAccessException;
    }
}