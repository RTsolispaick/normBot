package org.opp.core.handler;

import org.opp.data.models.User;
import org.opp.data.repositories.UserRepository;

import java.util.List;

/**
 * Этот класс отвечает за обработку информации получаемой от юзера
 */
public class IdleHandler {
    private final UserRepository userRepository;
    public IdleHandler() {this.userRepository = new UserRepository();}

    /**
     * Конструктор класса для тестирования функциональности класса
     * @param repository замоканный репозиторий для корректного теста функциональности класса
     */
    public IdleHandler(UserRepository repository) {this.userRepository = repository;}

    /**
     * Обрабатывает сообщения юзера, содержащие текстовое сообщение, и формирует ответ на него
     *
     * @param message содержит текст сообщения
     * @return возращает ответ на сообщение
     */
    public String getAnswer(String message, User user) {
        switch (message) {
            case "/start" -> {
                return """
                        Привет! Давай сыграем в виселицу!
                        Если ты не знаком с правилами напиши /rules.
                                            
                        Доступные команды:
                        /game - начать игру
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
            case "/stop" -> {
                return """
                        Игра остановлена!
                        Напиши /game, если хочешь сыграть ещё раз""";
            }
            case "/stats" -> {
                return "Количество побед: " + user.getTotalWin() + "\n" +
                        "Количество игр: " + user.getTotalGame();
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