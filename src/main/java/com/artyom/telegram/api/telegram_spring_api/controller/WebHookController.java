package com.artyom.telegram.api.telegram_spring_api.controller;

import com.artyom.telegram.api.telegram_spring_api.bot.ApiTelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebHookController {
    private final ApiTelegramBot telegramBot;
    Logger logger = LoggerFactory.getLogger(WebHookController.class);

    public WebHookController(ApiTelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceive(@RequestBody Update update){
        return telegramBot.onWebhookUpdateReceived(update);
    }
}
