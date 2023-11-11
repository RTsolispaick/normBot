package org.opp.data.repositories;

import org.opp.data.dao.UserDAO;
import org.opp.data.models.User;

import java.util.List;

/**
 * Репозиторий, который позволяет совершать операции над базой данных
 */
public class UserRepository {
    private final UserDAO userDAO;

    public UserRepository() {
        this.userDAO = new UserDAO();
    }
    public User findByChatID(Long chat_id) {return userDAO.findByChatId(chat_id);}

    public void save(User user) {
        userDAO.save(user);
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
    public User login(Long chat_id, String name){
        User user = userDAO.findByChatId(chat_id);
        if (user == null){
            user = new User(chat_id, name);
            userDAO.save(user);
        }
        if(!user.getName().equals(name)){
            user.setName(name);
            userDAO.update(user);
        }
        return user;
    }

}
