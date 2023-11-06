package org.opp.core.handler;

import org.opp.data.models.User;
import org.opp.data.repositories.UserRepository;

import java.util.List;
import java.util.Locale;

/**
 * Этот класс отвечает за обработку информации получаемой от юзера
 */
public class IdleHandler {
    private UserRepository userRepository;

    public IdleHandler() {this.userRepository = new UserRepository();}

    /**
     * Обрабатывает сообщения юзера, содержащие текстовое сообщение, и формирует ответ на него
     *
     * @param message содержит текст сообщения
     * @return возращает ответ на сообщение
     */
    public String getAnswer(String message, User user) {
        switch (message) {
            case "/start": return "Привет! Давай сыграем в виселицу!\n" +
                    "Если ты согласен напиши /game. Если ты не знаком с правилами напиши /rules.";
            case "/rules": return "Бот загадывает слово — пишет две крайние буквы и отмечает места для остальных букв. " +
                    "Cлово должно быть именем существительным, нарицательным в именительном падеже единственного числа, " +
                    "либо множественного числа при отсутствии у слова формы единственного числа." + "\n\n" +
                    "Игрок предлагает букву, которая может входить в это слово. " +
                    "Если такая буква есть в слове, то бот пишет её над соответствующими этой букве чертами — столько раз, сколько она встречается в слове. " +
                    "Если такой буквы нет, то у игрока отнимается одна из жизней." + "\n\n" +
                    "Конец игры наступает, либо когда пользователь отгадывает всё слово, либо когда у пользователя заканчиваются буквы." + "\n\n" +
                    "Теперь, когда ты узнал правила, можещь написать /game для начала игры.";
            case "/stop": return "Игра остановлена!\n" +
                    "Напиши /game, если хочешь сыграть ещё раз";
            case "/stats": return "Количество побед: " + user.getTotalWin() + "\n" +
                    "Количество игр: " + user.getTotalGame();
            case "/top":
                List<User> listUser = userRepository.findTop5();

                StringBuilder response = new StringBuilder("Top: \n");

                for (int i = 0; i < listUser.size(); i++) {
                    response.append((i + 1) + ". " + listUser.get(i).getName() + " = " + listUser.get(i).getRatingUser() + "\n");
                }
                response.append("\nТвой рейтинг: " + user.getRatingUser());

                return response.toString();
            default: return "Я тебя не понял. Попробуй написать ещё раз!";
        }
    }
}