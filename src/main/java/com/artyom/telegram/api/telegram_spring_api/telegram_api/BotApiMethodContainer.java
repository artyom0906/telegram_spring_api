package com.artyom.telegram.api.telegram_spring_api.telegram_api;

import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static com.google.common.collect.ArrayListMultimap.create;

public class BotApiMethodContainer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BotApiMethodContainer.class);


    private Multimap<String, BotApiMethodController> controllerMultimap;
    private Multimap<String, BotApiMethodFilter> filterMultimap;

    public static BotApiMethodContainer getInstanse() {
        return Holder.INST;
    }

    public void addBotController(String path, BotApiMethodController controller) {
        controllerMultimap.put(path, controller);
    }
    public void addBotFilter(String path, BotApiMethodFilter filter) {
        filterMultimap.put(path, filter);
    }

    public Collection<BotApiMethodController> getBotApiMethodController(String path){
        return controllerMultimap.get(path);
    }

    public Collection<BotApiMethodFilter> getBotApiFilterController(String path){
        return filterMultimap.get(path);
    }

    private BotApiMethodContainer(){
        controllerMultimap = create();
        filterMultimap = create();
    }

    public Multimap<String, BotApiMethodController> getControllerMap(){
        return controllerMultimap;
    }
    public Multimap<String, BotApiMethodFilter> getFilterMultimap(){
        return filterMultimap;
    }

    private static class Holder{
        final static BotApiMethodContainer INST = new BotApiMethodContainer();
    }
}