package org.opp.bots;


import org.opp.core.Manager;
import org.opp.essence.User;
import org.opp.utils.Config;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс телеграмм бота
 */
public class TelegramBot extends TelegramLongPollingBot implements Bot {
    private final String token;
    private final String name;
    private final Map<Long, User> db;
    private final Manager telegramManager;

    /**
     * Конструктор ТГ бота
     *
     * @param name имя бота
     * @param token токен
     */
    public TelegramBot(String name, String token) {
        this.name = name;
        this.token = token;
        db = new HashMap<>();
        telegramManager = new Manager();
    }


    @Override
    public String getBotUsername() {
        return this.name;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    /**
     * Приём и обработка сообщений пользователя
     *
     * @param update - получение новых действий пользователя
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message m = update.getMessage();
            if (m.hasText()) {
                if (!db.containsKey(m.getChatId())) {
                    db.put(m.getChatId(), new User());
                }
                String response = telegramManager.chooseState(m.getText().toLowerCase(), db.get(m.getChatId()));
                sendMessage(m.getChatId(), response);
            }
        }
    }

    /**
     * Отправка сообщений
     *
     * @param id - id пользователя
     * @param message - текст
     */
    @Override
    public void sendMessage(Long id, String message) {
        SendMessage msg = SendMessage.builder()
                .chatId(id.toString())
                .text(message)
                .build();

        try {
            execute(msg);
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
            botsApi.registerBot(new TelegramBot(Config.getName(), Config.getToken()));
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
