package org.opp.data.models;

import jakarta.persistence.*;
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
    @Column(name = "total_game")
    private Integer totalGame;
    @Column(name = "total_win")
    private Integer totalWin;
    @Column(name = "rating")
    private Integer ratingUser;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    private Game userGame;

    /**
     * Конструкток пользователя
     */
    public User() {
    }


    public User(Long platform_id, String name) {
        this.state = State.IDLE;
        this.name = name;
        this.chat_id = platform_id;
        this.totalWin = 0;
        this.totalGame = 0;
        this.ratingUser = 0;
        this.userGame = new Game();
    }
    public StatusGame getStatusGame(){
        return this.userGame.getStatusGame();
    }

    public void setStateIdle() {
        this.state = State.IDLE;
        this.ratingUser = this.totalWin * 3 - this.totalGame;
    }

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

    public Integer getTotalGame() {
        return totalGame;
    }

    public void setTotalGame(Integer totalGame) {
        this.totalGame = totalGame;
    }

    public Integer getTotalWin() {
        return totalWin;
    }

    public void setTotalWin(Integer totalWin) {
        this.totalWin = totalWin;
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
