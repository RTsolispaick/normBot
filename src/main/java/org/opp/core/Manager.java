package org.opp.core;

import org.opp.core.handler.GameHandler;
import org.opp.core.handler.IdleHandler;
import org.opp.data.StorageWord;
import org.opp.data.models.User;
import org.opp.data.repositories.UserRepository;

/**
     * Управление состоянием игры
     */

public class Manager {
    private final IdleHandler idleHandler;
    private final GameHandler gameHandler;
    private final StorageWord storageWord;

    private final UserRepository userRepository;

    public Manager() {
        this.gameHandler = new GameHandler();
        this.idleHandler = new IdleHandler();
        this.storageWord = new StorageWord();
        this.userRepository = new UserRepository();
    }

    /**
     * Конструктор класса для тестирования функциональности класса
     * @param userRepository замоканный репозиторий для корректного теста функциональности класса
     */
    public Manager(UserRepository userRepository) {
        this.gameHandler = new GameHandler();
        this.idleHandler = new IdleHandler();
        this.storageWord = new StorageWord();
        this.userRepository = userRepository;
    }

    /**
     * Обработка сообщений в зависимости от состояния пользователя
     * @param message сообщение
     * @param chat_id пользователь
     * @return Ответ на сообщение
     */
    public String chooseState(String message, Long chat_id, String name) {
        User user = getUser(chat_id, name);

        String response = null;
        switch (user.getState()) {
            case IDLE -> {
                if (message.equals("/game")) {
                    user.setStateGame(storageWord.wordChoice());
                    response = gameHandler.getAnswer(message, user);
                } else if (message.equals("/stop")) {
                    response = "Вы не находитесь в игре!\nЕсли хотите начать игру напишите /game";
                } else {
                    response = idleHandler.getAnswer(message, user);
                }
            }
            case GAME -> {
                if (message.equals("/stop")) {
                    user.setStateIdle();
                    response = idleHandler.getAnswer(message, user);
                } else if (message.equals("/game")) {
                    response = "Игра и так идёт!\nЕсли хотите остановить игру напишите /stop";
                } else {
                    response = gameHandler.getAnswer(message, user);

                    if (user.getStatusGame() == 1) {
                        user.setStateIdle();
                    } else if (user.getStatusGame() == 2){
                        user.setTotalWin(user.getTotalWin() + 1);
                        user.setStateIdle();
                    }
                }
            }
        }

        userRepository.update(user);
        return response;
    }

    /**
     * Метод предоставляющий объект User по id пользователя на платформе и проверяющее
     * имя пользователя на изменение
     * @param chat_id id пользователя на платформе
     * @param name имя пользователя
     * @return объект User, принадлежащий пользователю с данным id
     */
    public User getUser(Long chat_id, String name) {
        User user = userRepository.findByChatID(chat_id);
        if (user == null) {
            user = new User(chat_id, name);
            userRepository.save(user);
        }
        if (!user.getName().equals(name)) {
            user.setName(name);
            userRepository.update(user);
        }

        return user;
    }
}