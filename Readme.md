#Telegram spring api

*Read this in other languages: [English](Readme.md), [Русский](Readme.Ru-ru.md)

This library help to create telegram boot with spring.

###Properties
+ Mandatory properties
    ```.properties
    telegram.bot-token=bot123456:ABC-DEF1234ghIkl-zyx57W2v1u123ew11
    telegram.user-name=example_bot
    telegram.web-hook-path=https://bot.example.com
    ```
+ Proxy oprions (not mandatory)
   ```.properties
  telegram.enable-proxy=true
  telegram.proxy-host=
  telegram.proxy-type=
  telegram.proxy-port=
  ```

###Normal commands

For Spring to recognize command handlers you need to put an annotation on the class containing them.
```java
@BotController
public class ExampleBotController{
}
```

Adding command handlers:
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
    
###Filters

Не понимаю что это за хрень
vvv

Если фильтр вернул false, то для обработки сообщений будет вызван метод у которого поле `filtered = true`(по умолчанию false), а текст сообщения совпадает с текстом фильтра.

If the filter returns ```false```, a method with ```filtered = true``` will be called

Иначе для обработки сообщений будет вызван метод у которого поле `filtered = false`(по умолчанию), а текст сообщения совпадает с текстом фильтра
```java
@BotRequestFilter(value = {"/command"})
public boolean command_filter(Update update){}

@BotRequestMapping(value = "/command", filtered = true)
public SendMessage startCommand(Update update) {}

@BotRequestMapping(value = "/command", filtered = false)
public SendMessage startCommand(Update update) {}
```

###Inline querries

For creating inline handlers you need to select method BotRequestMethod.INLINE
```java
@BotRequestMapping(value = "/start", method = BotRequestMethod.INLINE)
public BotApiMethod<?> startInline(Update update){}
```
