package org.opp.bots;


import org.opp.core.Bot;
import org.opp.core.Logic;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Класс телеграмм бота
 */
public class TelegramBot extends TelegramLongPollingBot implements Bot {
    private String token;
    private String name;
    private Logic telegramLogic = new Logic();

    /**
     * Конструктор ТГ бота
     * @param name
     * @param token
     */
    public TelegramBot(String name, String token) {
        this.name = name;
        this.token = token;
    }

    /**
     * метод для получения имени бота
     * @return
     */
    @Override
    public String getBotUsername() {
        return this.name;
    }
    /**
     * метод для получения токена бота
     * @return
     */
    @Override
    public String getBotToken() {
        return this.token;
    }

    /**
     * реакция на сообщения пользователя
     * @param update
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message m = update.getMessage();
            if (m.hasText()) {
                String response = telegramLogic.massageHandler(m.getText());
                sendMessage(m.getChatId(), response);
            }
        }
    }

    /**
     * Отправка сообщений
     * @param id
     * @param message
     */
    @Override
    public void sendMessage(Long id, String message) {
        SendMessage msg = SendMessage.builder()
                .chatId(id.toString())
                .text(message)
                .build();

        try {
            execute(msg).getMessageId();
        } catch (TelegramApiException e) {
            System.err.println(e);
        }
    }

    /**
     * Запуск бота
     */
    public static void launch() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TelegramBot("bot-username", "bot-token"));
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
