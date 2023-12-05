package org.opp.data.models;

import jakarta.persistence.*;
import org.opp.data.models.types.StateSession;
import org.opp.data.models.types.TypeGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс игровой сессии
 */
@Entity
@Table(name = "game_session")
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "type_game")
    @Enumerated(EnumType.STRING)
    private TypeGame typeGame;
    @Column(name = "state_multi_game")
    @Enumerated(EnumType.STRING)
    private StateSession stateSession;
    @Column(name = "index_play")
    private Integer indexPlayUser;
    @OneToMany(mappedBy = "userGameSession", fetch = FetchType.EAGER)
    @OrderBy("indexInGameSession ASC")
    private List<User> linkedUsers;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    private Game game;

    public GameSession() {}

    /**
     * Создание игровой сессии
     * @param typeGame тип сессии
     */
    public GameSession(TypeGame typeGame) {
        this.typeGame = typeGame;
        this.stateSession = StateSession.INIT_SESSION;
        this.indexPlayUser = 0;
        this.linkedUsers = new ArrayList<>();
        this.game = new Game();
    }

    public TypeGame getTypeGame() {
        return this.typeGame;
    }

    public List<User> getLinkedUsers() {
        return this.linkedUsers;
    }

    public Game getGame() {
        return this.game;
    }

    public StateSession getStateSession() {
        return stateSession;
    }

    public void setStateSession(StateSession stateSession) {
        this.stateSession = stateSession;
    }

    /**
     * Подключение пользователя к сессии
     */
    public void addUser(User user) {
        this.linkedUsers.add(user);
    }

    /**
     * Возвращает играющего пользователя
     */
    public User getPlayUser() {
        return this.linkedUsers.get(indexPlayUser);
    }

    /**
     * Возаращает неиграющего пользователя
     */
    public User getDontPlayUser() {
        return this.linkedUsers.get((indexPlayUser == 0) ? 1 : 0);
    }

    /**
     * Возвращает второго пользователя относительно переданного пользователя
     */
    public User getAnotherUser(User user) {
        if (getPlayUser().equals(user))
            return getDontPlayUser();
        return getPlayUser();
    }

    /**
     * Передаём очередь загадывания слова
     * @return true - успешная передача очереди хода
     */
    public Boolean nextPlayGame() {
        if (this.indexPlayUser == 1)
            return Boolean.FALSE;

        this.indexPlayUser += 1;
        return Boolean.TRUE;
    }
}