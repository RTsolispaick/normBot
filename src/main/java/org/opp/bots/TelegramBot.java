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

public class TelegramBot extends TelegramLongPollingBot implements Bot {
    private String token;
    private String name;

    public TelegramBot(String name, String token) {
        this.name = name;
        this.token = token;
    }

    @Override
    public String getBotUsername() {
        return this.name;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message m = update.getMessage();
            if (m.hasText()) {
                String response = Logic.massageHandler(m.getText());
                sendMessage(m.getChatId(), response);
            }
        }
    }

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

    public static void launch() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TelegramBot("bot-username", "bot-token"));
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}