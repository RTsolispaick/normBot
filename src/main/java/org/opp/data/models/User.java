package org.opp.data.models;

import jakarta.persistence.*;
import org.opp.data.models.types.Difficult;
import org.opp.data.models.types.State;
import org.opp.data.models.types.StatusGame;

/**
 * Класс пользователя
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    private Game userGame;

    /**
     * Пустой конструкток пользователя
     */
    public User() {}

    /**
     * Конструк пользователя
     * @param chat_id идентификатор юзера на платформе
     * @param name Имя пользователя
     */
    public User(Long chat_id, String name) {
        this.state = State.IDLE;
        this.name = name;
        this.chat_id = chat_id;
        this.totalGameEasy = 0;
        this.totalGameMedium = 0;
        this.totalGameHard = 0;
        this.totalWinEasy = 0;
        this.totalWinMedium = 0;
        this.totalWinHard = 0;
        this.ratingUser = 0;
        this.userGame = new Game();
    }

    /**
     * геттер для получения текущего состояния игры
     * @return состояние игры пользователя
     */
    public StatusGame getStatusGame(){
        return this.userGame.getStatusGame();
    }

    public Difficult getDifficultGame() {
        return this.userGame.getDifficult();
    }

    /**
     * сеттер для того, чтобы задать состояние покоя и пересчитать рейтинг пользователя
     */
    public void setStateIdle() {
        this.state = State.IDLE;
        this.ratingUser = 5 * (this.totalWinHard * 2 - this.totalGameHard) +
                3 * (this.totalWinMedium * 2 - this.totalGameMedium) +
                (this.totalWinEasy * 2 - this.totalGameEasy);
    }

    /**
     * Сеттер, чтобы задать состояние игры и инициализировать игру по слову
     * @param word слово для игры
     */
    public void setStateGame(Word word) {
        this.state = State.GAME;
        this.userGame.initGame(word);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void setChat_id(Long chat_id) {
        this.chat_id = chat_id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    public void setRatingUser(Integer ratingUser) {
        this.ratingUser = ratingUser;
    }

    public Game getUserGame() {
        return userGame;
    }

    public void setUserGame(Game userGame) {
        this.userGame = userGame;
    }
}
