package org.opp.core.handler;

import org.opp.data.models.User;
import org.opp.data.repositories.UserRepository;

import java.util.List;

/**
 * Отвечает за определение ответа, несвязанного с игровым процессом.
 */
public class IdleHandler {
    private final UserRepository userRepository;

    public IdleHandler() {
        this.userRepository = new UserRepository();
    }

    /**
     * Конструктор, используемый для назначения UserRepository с необходимыми свойствами.
     * @param userRepository замоканный объект класса UserRepository
     */
    public IdleHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Реализует внеигровую логику. Определяет ответ пользователю на его сообщения вне игры.
     * @param message сообщение пользователя
     * @param user объект содержащий данные пользователя и игровую сессию, к которой он принадлежит
     * @return ответ на сообщение пользователя
     */
    public String getResponse(String message, User user) {
        switch (message) {
            case "/start" -> {
                return """
                        Привет! Давай сыграем в виселицу!
                        Если ты не знаком с правилами напиши /rules.
                                            
                        Доступные команды:
                        /game - начать однопользовательску игру
                        /multiplayer - cыграть с другим человеком
                        /stats - твоя статистика
                        /top - топ игроков""";
            }
            case "/rules" -> {
                return """
                        Бот загадывает слово. Cлово должно быть именем существительным, нарицательным в именительном падеже единственного числа, либо множественного числа при отсутствии у слова формы единственного числа.
                                            
                        Игрок предлагает букву, которая может входить в это слово. Если такая буква есть в слове, то бот пишет её над соответствующими этой букве чертами — столько раз, сколько она встречается в слове. Если такой буквы нет, то у игрока отнимается одна из жизней.
                                            
                        Конец игры наступает, либо когда пользователь отгадывает всё слово, либо когда у пользователя заканчиваются буквы.
                                            
                        Теперь, когда ты узнал правила, можещь написать /game для начала игры.""";
            }
            case "/stats" -> {
                return "Количество отгаданных слов:\n" +
                        "Easy = " + user.getTotalWinEasy() + "\n" +
                        "Medium = " + user.getTotalWinMedium() + "\n" +
                        "Hard = " + user.getTotalWinHard() + "\n\n" +
                        "Количество неотгаданных слов:\n" +
                        "Easy = " + (user.getTotalGameEasy() - user.getTotalWinEasy()) + "\n" +
                        "Medium = " + (user.getTotalGameMedium() - user.getTotalWinMedium()) + "\n" +
                        "Hard = " + (user.getTotalGameHard() - user.getTotalWinHard()) + "\n\n" +
                        "Количество игр:\n" +
                        "Easy = " + user.getTotalGameEasy() + "\n" +
                        "Medium = " + user.getTotalGameMedium() + "\n" +
                        "Hard = " + user.getTotalGameHard() + "\n\n" +
                        "Всего игр: " + (user.getTotalGameHard() + user.getTotalGameMedium() + user.getTotalGameEasy());
            }
            case "/top" -> {
                List<User> listUser = userRepository.findTop5();
                StringBuilder response = new StringBuilder("Top:\n");
                for (int i = 0; i < listUser.size(); i++) {
                    response.append((i + 1)).append(". ").append(listUser.get(i).getName())
                            .append(" = ").append(listUser.get(i).getRatingUser()).append("\n");
                }
                response.append("\nТвой рейтинг: ").append(user.getRatingUser());
                return response.toString();
            }
        }
        return "Я тебя не понял. Попробуй написать ещё раз!";
    }
}