package org.opp.data.repositories;

import org.opp.data.dao.UserDAO;
import org.opp.data.models.User;
import org.opp.data.models.types.Platform;

import java.util.List;

/**
 * Репозиторий, который позволяет совершать операции над базой данных
 */
public class UserRepository {
    private final UserDAO userDAO;

    public UserRepository() {
        this.userDAO = new UserDAO();
    }

    public void update(User user) {
        userDAO.update(user);
    }

    public List<User> findTop5() {
        return userDAO.findTop5();
    }

    /**
     * Логирование пользователя
     * @param chat_id - id на платформе
     * @param name имя пользователя
     * @return пользователя
     */
    public User login(Platform platform, Long chat_id, String name){
        User user = userDAO.findByChatId(chat_id);
        if (user == null){
            user = new User(platform, chat_id, name);
            userDAO.save(user);
        }
        if(!user.getName().equals(name)){
            user.setName(name);
            userDAO.update(user);
        }
        return user;
    }

}