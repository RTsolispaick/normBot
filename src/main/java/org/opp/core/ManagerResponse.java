package org.opp.core;

import org.opp.bots.Bot;
import org.opp.data.models.Response;
import org.opp.data.models.User;

import java.util.List;

/**
 * Отвечает за отправку сообщений пользователям
 */
public class ManagerResponse {
    private final Manager manager;
    private Bot botTG;
    private Bot botVK;

    public ManagerResponse() {
        this.manager = new Manager();
    }

    /**
     * Устанавливет зарегестрированного бота TG с определённым у него методом {@link Bot#sendMessage(Long, String)}
     */
    public void setBotTG(Bot botTG) {
        this.botTG = botTG;
    }

    /**
     * Устанавливет зарегестрированного бота VK с определённым у него методом {@link Bot#sendMessage(Long, String)}
     */
    public void setBotVK(Bot botVK) {
        this.botVK = botVK;
    }

    /**
     * Контролирует отправку сообщений, полученных из логики бота
     * @param message сообщение пользователя
     * @param user пользователь
     */
    public void definitionOfResponse(String message, User user) {
        List<Response> responseUser = manager.chooseState(message, user);

        responseUser.forEach(i -> {
            switch (i.getPlatform()) {
                case TG -> botTG.sendMessage(i.getID(), i.getResponse());
                case VK -> botVK.sendMessage(i.getID(), i.getResponse());
            }
        });
    }
}