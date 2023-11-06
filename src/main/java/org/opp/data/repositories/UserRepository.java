package org.opp.data.repositories;

import org.opp.data.dao.UserDAO;
import org.opp.data.models.User;

import java.util.List;

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

}
