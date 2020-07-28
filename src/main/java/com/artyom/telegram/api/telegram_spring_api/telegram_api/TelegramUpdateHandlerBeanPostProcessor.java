package com.artyom.telegram.api.telegram_spring_api.telegram_api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
@Component
public class TelegramUpdateHandlerBeanPostProcessor implements BeanPostProcessor, Ordered {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramUpdateHandlerBeanPostProcessor.class);

    private final BotApiMethodContainer container = BotApiMethodContainer.getInstanse();
    private final Map<String, Class> botControllerMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        var beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(BotController.class)) {
            botControllerMap.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(!botControllerMap.containsKey(beanName)) return bean;

        Arrays.stream(botControllerMap.get(beanName).getMethods())
                .filter(method -> method.isAnnotationPresent(BotRequestMapping.class))
                .forEach((Method method) -> generateController(bean, method));
        Arrays.stream(botControllerMap.get(beanName).getMethods())
                .filter(method -> method.isAnnotationPresent(BotRequestFilter.class))
                .forEach((Method method) -> generateFilter(bean, method));
        return bean;
    }

    private void generateFilter(Object bean, Method method) {
        BotController botController = bean.getClass().getAnnotation(BotController.class);
        BotRequestFilter botRequestFilter = method.getAnnotation(BotRequestFilter.class);

        Arrays.stream(botController.value()).forEach(controllerPath-> Arrays.stream(botRequestFilter.value()).forEach(methodPath->{
            String path = controllerPath + methodPath;
            Arrays.stream(botRequestFilter.method()).forEach(m->{
                BotApiMethodFilter filter = switch (m){
                    case MESSAGE -> createFilterUpdate2ApiMethod(bean, method);
                    case CALLBACK -> createFilterListForController(bean, method);
                    case INLINE -> createFilterUpdate2InlineMethod(bean, method);
                };
                try {
                    container.addBotFilter(path, filter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }));
    }

    private BotApiMethodFilter createFilterUpdate2InlineMethod(Object bean, Method method) {
        return new BotApiMethodFilter(bean, method) {
            @Override
            public boolean successUpdatePredicate(Update update) {
                return update!=null && update.hasMessage() && update.getMessage().hasText();
            }
        };
    }

    private BotApiMethodFilter createFilterListForController(Object bean, Method method) {
        return new BotApiMethodFilter(bean, method) {
            @Override
            public boolean successUpdatePredicate(Update update) {
                return update!=null && update.hasCallbackQuery() && update.getCallbackQuery().getData() != null;
            }
        };
    }

    private BotApiMethodFilter createFilterUpdate2ApiMethod(Object bean, Method method) {
        return new BotApiMethodFilter(bean, method) {
            @Override
            public boolean successUpdatePredicate(Update update) {
                return update!=null && update.hasInlineQuery() && update.getInlineQuery().getQuery() != null;
            }
        };
    }

    private void generateController(Object bean, Method method) {
        BotController botController = bean.getClass().getAnnotation(BotController.class);
        BotRequestMapping botRequestMapping = method.getAnnotation(BotRequestMapping.class);

        Arrays.stream(botController.value()).forEach(controllerPath-> Arrays.stream(botRequestMapping.value()).forEach(methodPath->{
            String path = controllerPath + methodPath;
            Arrays.stream(botRequestMapping.method()).forEach(m->{
                BotApiMethodController controller = switch (m){
                    case MESSAGE -> createControllerUpdate2ApiMethod(bean, method);
                    case CALLBACK -> createProcessListForController(bean, method);
                    case INLINE -> createControllerUpdate2InlineMethod(bean, method);
                };
                controller.setFiltered(botRequestMapping.filtered());
                try {
                    container.addBotController(path, controller);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }));
    }

    private BotApiMethodController createControllerUpdate2ApiMethod(Object bean, Method method){
        return new BotApiMethodController(bean, method) {
            @Override
            public boolean successUpdatePredicate(Update update) {
                return update!=null && update.hasMessage() && update.getMessage().hasText();
            }
        };
    }

    private BotApiMethodController createProcessListForController(Object bean, Method method){
        return new BotApiMethodController(bean, method) {
            @Override
            public boolean successUpdatePredicate(Update update) {
                return update!=null && update.hasCallbackQuery() && update.getCallbackQuery().getData() != null;
            }
        };
    }

    private BotApiMethodController createControllerUpdate2InlineMethod(Object bean, Method method){
        return new BotApiMethodController(bean, method) {
            @Override
            public boolean successUpdatePredicate(Update update) {
                return update!=null && update.hasInlineQuery() && update.getInlineQuery().getQuery() != null;
            }
        };
    }

    @Override
    public int getOrder() {
        return 100;
    }
}