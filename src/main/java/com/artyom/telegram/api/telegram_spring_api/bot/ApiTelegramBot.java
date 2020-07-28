package com.artyom.telegram.api.telegram_spring_api.bot;

import com.artyom.telegram.api.telegram_spring_api.telegram_api.SelectHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SuppressWarnings("ALL")
public class ApiTelegramBot extends TelegramWebhookBot {
    Logger logger = LoggerFactory.getLogger(ApiTelegramBot.class);
    private String webHookPath;
    private String botUserName;
    private String botToken;

    public ApiTelegramBot(DefaultBotOptions botOptions) {
        super(botOptions);
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        var handlers = SelectHandle.getHandle(update);
        if (handlers != null) {
            handlers.forEach(botApiMethodController -> {
                if (botApiMethodController == null) return;
                var answers = botApiMethodController.process(update);
                if (answers != null) {
                    BotApiMethod res = answers.stream().findFirst().orElse(null);
                    if (res != null) {
                        try {
                            execute(res);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        return null;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

}