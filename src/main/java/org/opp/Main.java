package org.opp;

import org.opp.bots.TelegramBot;
import org.opp.utils.Config;

/**
 *Класс Main
 */
public class Main {
    public static void main(String[] args){
        Config cfg = new Config();
        /**
         * Запуск ТГ бота
         */
        TelegramBot.launch(cfg.getName(), cfg.getToken());
    }
}
