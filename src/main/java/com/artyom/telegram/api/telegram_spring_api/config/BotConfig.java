package com.artyom.telegram.api.telegram_spring_api.config;

import com.artyom.telegram.api.telegram_spring_api.bot.ApiTelegramBot;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegram")
public class BotConfig {
    private String webHookPath;
    private String userName;
    private String botToken;

    private DefaultBotOptions.ProxyType proxyType;
    private String proxyHost;
    private int proxyPort;
    private boolean enableProxy = false;

    @Bean
    public ApiTelegramBot mFTelegramBot(){
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);

        if(enableProxy) {
            options.setProxyHost(proxyHost);
            options.setProxyPort(proxyPort);
            options.setProxyType(proxyType);
        }
        ApiTelegramBot apiTelegramBot = new ApiTelegramBot(options);
        apiTelegramBot.setBotUserName(userName);
        apiTelegramBot.setBotToken(botToken);
        apiTelegramBot.setWebHookPath(webHookPath);

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
