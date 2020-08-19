#Telegram spring api

*Read this in other languages:* [English](Readme.md), [Русский](Readme.Ru-ru.md)

Данная библиотека позволяет создавать ботов с помощью Spring.

###Настройки
+ Обязательные настройки
    ```.properties
    telegram.bot-token=bot123456:ABC-DEF1234ghIkl-zyx57W2v1u123ew11
    telegram.user-name=example_bot
    telegram.web-hook-path=https://bot.example.com
    ```
+ Настройки прокси не обязательно
   ```.properties
  telegram.enable-proxy=true
  telegram.proxy-host=
  telegram.proxy-type=
  telegram.proxy-port=
  ```

###Стандартные команды:
Что бы Spring распознал обработчики команд нужно указать что класс, где они находятся являться обработчиком сообщений бота.
```java
@BotController
public class ExampleBotController{
}
```

Для добавления обработчика команд:
+   ```java
    @BotRequestMapping("/command")
    public BotApiMethod<?> command(Update update){}
    ```
+   ```java
    @BotRequestMapping({"/command", "/command1"})
    public BotApiMethod<?> command(Update update){}
    ```
+   ```java
    @BotRequestMapping(value = {"/command", "/command1"})
    public BotApiMethod<?> command(Update update){}
    ```
    
###Фильтры

Если фильтр вернул false, то для обработки сообщений будет вызван метод у которого поле `filtered = true`(по умолчанию false), а текст сообщения совпадает с текстом фильтра.

Иначе для обработки сообщений будет вызван метод у которого поле `filtered = false`(по умолчанию), а текст сообщения совпадает с текстом фильтра
```java
@BotRequestFilter(value = {"/command"})
public boolean command_filter(Update update){}

@BotRequestMapping(value = "/command", filtered = true)
public SendMessage startCommand(Update update) {}

@BotRequestMapping(value = "/command", filtered = false)
public SendMessage startCommand(Update update) {}
```

###Инлайн запросы

Для создания обработчика инлайн запросов необходимо указать метод обработки BotRequestMethod.INLINE
```java
@BotRequestMapping(value = "/start", method = BotRequestMethod.INLINE)
public BotApiMethod<?> startInline(Update update){}
```
