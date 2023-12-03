package org.opp;

import org.opp.bots.TelegramBot;
import org.opp.core.ManagerResponse;
import org.opp.utils.Config;

/**
 *Класс Main
 */
public class Main {
    public static void main(String[] args) {
        Config cfg = new Config();

        ManagerResponse managerResponse = new ManagerResponse();
        TelegramBot.launch(cfg.getName(), cfg.getTokenTG(), managerResponse);
    }
}