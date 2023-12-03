package org.opp.data.models;

import jakarta.persistence.*;
import org.opp.data.models.types.Platform;
import org.opp.data.models.types.State;

import java.util.Objects;

/**
 * Класс пользователя
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "platform")
    @Enumerated(EnumType.STRING)
    private Platform platform;
    @Column(name = "name")
    private String name;
    @Column(name = "chat_id")
    private Long chat_id;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;
    @Column(name = "total_game_easy")
    private Integer totalGameEasy;
    @Column(name = "total_game_medium")
    private Integer totalGameMedium;
    @Column(name = "total_game_hard")
    private Integer totalGameHard;
    @Column(name = "total_win_easy")
    private Integer totalWinEasy;
    @Column(name = "total_win_medium")
    private Integer totalWinMedium;
    @Column(name = "total_win_hard")
    private Integer totalWinHard;
    @Column(name = "rating")
    private Integer ratingUser;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    private GameSession userGameSession;
    @Column(name = "index_in_session")
    private Integer indexInGameSession;

    /**
     * Пустой конструкток пользователя
     */
    public User() {}

    /**
     * Конструк пользователя
     * @param platform платформа, с которой обращается пользователь
     * @param chat_id идентификатор юзера на платформе
     * @param name Имя пользователя
     */
    public User(Platform platform, Long chat_id, String name) {
        this.state = State.IDLE;
        this.platform = platform;
        this.name = name;
        this.chat_id = chat_id;
        this.totalGameEasy = 0;
        this.totalGameMedium = 0;
        this.totalGameHard = 0;
        this.totalWinEasy = 0;
        this.totalWinMedium = 0;
        this.totalWinHard = 0;
        this.ratingUser = 0;
    }

    /**
     * Установливает состояние покоя и пересчитать рейтинг
     */
    public void setStateIdle() {
        this.state = State.IDLE;
        this.userGameSession = null;
        this.ratingUser = 5 * (this.totalWinHard * 2 - this.totalGameHard) +
                3 * (this.totalWinMedium * 2 - this.totalGameMedium) +
                (this.totalWinEasy * 2 - this.totalGameEasy);
    }

    /**
     * Устанавливет состояние игры и присоединяет пользователя к сессии
     */
    public void setStateGame(GameSession gameSession) {
        this.state = State.GAME;
        this.userGameSession = gameSession;
        this.indexInGameSession = gameSession.getLinkedUsers().size();
        this.userGameSession.addUser(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getChat_id() {
        return chat_id;
    }

    public State getState() {
        return state;
    }

    public Integer getTotalGameEasy() {
        return totalGameEasy;
    }

    public void setTotalGameEasy(Integer totalGameEasy) {
        this.totalGameEasy = totalGameEasy;
    }

    public Integer getTotalGameMedium() {
        return totalGameMedium;
    }

    public void setTotalGameMedium(Integer totalGameMedium) {
        this.totalGameMedium = totalGameMedium;
    }

    public Integer getTotalGameHard() {
        return totalGameHard;
    }

    public void setTotalGameHard(Integer totalGameHard) {
        this.totalGameHard = totalGameHard;
    }

    public Integer getTotalWinEasy() {
        return totalWinEasy;
    }

    public void setTotalWinEasy(Integer totalWinEasy) {
        this.totalWinEasy = totalWinEasy;
    }

    public Integer getTotalWinMedium() {
        return totalWinMedium;
    }

    public void setTotalWinMedium(Integer totalWinMedium) {
        this.totalWinMedium = totalWinMedium;
    }

    public Integer getTotalWinHard() {
        return totalWinHard;
    }

    public void setTotalWinHard(Integer totalWinHard) {
        this.totalWinHard = totalWinHard;
    }

    public Integer getRatingUser() {
        return ratingUser;
    }

    public Platform getPlatform() {
        return platform;
    }

    public GameSession getUserGameSession() {
        return userGameSession;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }
}
