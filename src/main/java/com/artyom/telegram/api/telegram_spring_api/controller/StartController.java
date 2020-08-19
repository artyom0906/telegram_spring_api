package com.artyom.telegram.api.telegram_spring_api.controller;

import com.artyom.telegram.api.telegram_spring_api.telegram_api.BotController;
import com.artyom.telegram.api.telegram_spring_api.telegram_api.BotRequestFilter;
import com.artyom.telegram.api.telegram_spring_api.telegram_api.BotRequestMapping;
import com.artyom.telegram.api.telegram_spring_api.telegram_api.BotRequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@BotController
public class StartController {
    Logger logger = LoggerFactory.getLogger(StartController.class);

    @BotRequestFilter(value = "/start")
    public boolean isAdmin(Update update){
        //test if telegram name equals "robot_artyom"
        return update.getMessage().getFrom().getUserName().equalsIgnoreCase("robot_artyom");
    }
    @BotRequestMapping("/test")
    public BotApiMethod<?> test(Update u){
        return null;
    }
    //if filter return false method with filtered field equals true will executed. default value false
    @BotRequestMapping(value = "/start", filtered = true)
    public SendMessage startCommand(Update update) {
        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText("hi!");
    }

    @BotRequestMapping(value = "/start", method = BotRequestMethod.INLINE)
    public BotApiMethod<?> startInline(Update update){
        List<InlineQueryResult> results = new ArrayList<>();
            InlineQueryResultArticle r1 = new InlineQueryResultArticle()
                    .setId(UUID.randomUUID().toString())
                    .setDescription("bla bla bla")
                    .setInputMessageContent(new InputTextMessageContent().setMessageText("message"))
                    .setTitle("test");
            results.add(r1);
        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setInlineQueryId(update.getInlineQuery().getId());
        answerInlineQuery.setResults(results);
        return answerInlineQuery;
    }

    //if filter return true method with filtered field equals false will executed. default value false
    @BotRequestMapping("/start")
    public SendMessage start1Command(Update update) {
        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText("hi artyom!");
    }

    @BotRequestMapping
    public SendMessage anyRequest(Update update){
        return null;
    }
}
