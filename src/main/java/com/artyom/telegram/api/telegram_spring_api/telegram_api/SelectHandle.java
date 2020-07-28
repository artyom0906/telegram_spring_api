package com.artyom.telegram.api.telegram_spring_api.telegram_api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class SelectHandle {
    private static BotApiMethodContainer container = BotApiMethodContainer.getInstanse();
    private final static Logger logger = LoggerFactory.getLogger(SelectHandle.class);

    public static Collection<BotApiMethodController> getHandle(Update update) {
        String path;
        Collection<BotApiMethodController> controllers = null;

        if (update.hasMessage() && update.getMessage().hasText()) {
            path = update.getMessage().getText().split(" ")[0].trim();
            controllers = getControllers(update, path);
        } else if (update.hasCallbackQuery()) {
            path = update.getCallbackQuery().getData().split("/")[1].trim();
            controllers = getControllers(update, path);
        } else if (update.hasInlineQuery()) {
            path = update.getInlineQuery().getQuery().split(" ")[0].trim();
            controllers = getControllers(update, path);
        }
        return controllers;
    }

    private static Collection<BotApiMethodController> getControllers(Update update, String path){
        Collection<BotApiMethodController> controllers;
        if(filter(path, update)) {
            controllers = container.getControllerMap().get(path).stream().filter(botApiMethodController -> botApiMethodController.successUpdatePredicate(update) && !botApiMethodController.isFiltered()).collect(Collectors.toSet());
            logger.info("not filtered");
        }else {
            logger.info("filtered");
            controllers = container.getControllerMap().get(path).stream().filter(botApiMethodController -> botApiMethodController.successUpdatePredicate(update) && botApiMethodController.isFiltered()).collect(Collectors.toSet());
        }
        try {
            if (controllers.isEmpty()) controllers = container.getControllerMap().get("");
        }catch (Exception e){
            return null;
        }
        return controllers;
    }

    private static boolean filter(String path, Update update){
        AtomicBoolean isOk = new AtomicBoolean(true);
        Collection<BotApiMethodFilter> filters;
        filters = container.getFilterMultimap().get(path);
        if(filters.isEmpty()) return true;
        filters.forEach(filter -> {
            if (isOk.get()) isOk.set(filter.process(update));
        });
        return isOk.get();
    }
}
