package org.opp;

import api.longpoll.bots.exceptions.VkApiException;
import org.opp.bots.TelegramBot;
import org.opp.bots.VKBot;
import org.opp.core.ManagerResponse;
import org.opp.utils.Config;

/**
 *Класс Main
 */
public class Main {
    public static void main(String[] args) throws VkApiException {
        Config cfg = new Config();

        ManagerResponse managerResponse = new ManagerResponse();
        TelegramBot.launch(cfg.getName(), cfg.getTokenTG(), managerResponse);
        new VKBot(cfg.getTokenVK(), managerResponse).startPolling();
    }
}