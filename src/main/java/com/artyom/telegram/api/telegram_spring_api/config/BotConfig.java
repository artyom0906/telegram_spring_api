package com.artyom.telegram.api.telegram_spring_api.config;

import com.artyom.telegram.api.telegram_spring_api.bot.ApiTelegramBot;
import com.artyom.telegram.api.telegram_spring_api.exceptions.BotConfigurationException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

@Data
@Configuration
@EnableConfigurationProperties(BotConfigData.class)
public class BotConfig {

    Logger logger = LoggerFactory.getLogger(BotConfig.class);
    private final BotConfigData data;

    @Autowired
    public BotConfig(BotConfigData data){
        logger.info(data.getBotToken());
        this.data = data;}


    @Bean
    public ApiTelegramBot mFTelegramBot() throws BotConfigurationException {

        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);

        if(data.isEnableProxy()) {
            options.setProxyHost(data.getProxyHost());
            options.setProxyPort(data.getProxyPort());
            options.setProxyType(data.getProxyType());
        }
        logger.info(String.valueOf(data.getWebHookPath()));
        logger.info(String.valueOf(data.getBotToken()));
        logger.info(String.valueOf(data.getUserName()));

        if(data.getUserName().isEmpty() || data.getBotToken().isEmpty() || data.getWebHookPath().isEmpty())
            throw new BotConfigurationException("one or more configuration params are empty");

        ApiTelegramBot apiTelegramBot = new ApiTelegramBot(options);
        apiTelegramBot.setBotUserName(data.getUserName());
        apiTelegramBot.setBotToken(data.getBotToken());
        apiTelegramBot.setWebHookPath(data.getWebHookPath());

        return apiTelegramBot;
    }

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
