package org.opp.data.models;

import jakarta.persistence.*;
import org.opp.data.models.types.State;

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
    private State state;
    @Column(name = "word")
    private String word;
    @Column(name = "category")
    private String category;
    @Column(name = "game_view_of_the_word")
    private String gameViewOfTheWord;
    @Column(name = "word_from_excluded_letters")
    private String wordFromExcludedLetters;
    @Column(name = "numbers_of_lives")
    private Integer numberOfLives;
    @Column(name = "status_game")
    private Integer statusGame;
    @Column(name = "total_game")
    private Integer totalGame;
    @Column(name = "total_win")
    private Integer totalWin;
    @Column(name = "rating")
    private Integer ratingUser;

    /**
     * Конструкток пользователя
     */
    public User() {}

    public User(Long platform_id, String name) {
        this.state = State.IDLE;
        this.name = name;
        this.chat_id = platform_id;
        this.word = null;
        this.category = null;
        this.gameViewOfTheWord = null;
        this.wordFromExcludedLetters = null;
        this.numberOfLives = -1;
        this.statusGame = -1;
        this.totalWin = 0;
        this.totalGame = 0;
        this.ratingUser = 0;
    }

    public void setStateIdle(){
        this.state = State.IDLE;
        this.word = null;
        this.gameViewOfTheWord = null;
        this.wordFromExcludedLetters = null;
        this.numberOfLives = -1;
        this.statusGame = -1;
        this.ratingUser = this.totalWin * 3 - this.totalGame;
    }

    public void setStateGame(String word){
        this.state = State.GAME;
        this.word = word;
        this.gameViewOfTheWord = "_ ".repeat(word.length() - 1) + "_";
        this.wordFromExcludedLetters = "";
        this.numberOfLives = 10;
        this.statusGame = -1;
        this.totalGame += 1;
    }

    public State getState(){
        return state;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getCategory() {return category;}

    public void setCategory(String category) {this.category = category;}

    public String getGameViewOfTheWord() {
        return gameViewOfTheWord;
    }

    public void setGameViewOfTheWord(String gameViewOfTheWord) {
        this.gameViewOfTheWord = gameViewOfTheWord;
    }

    public String getWordFromExcludedLetters() {
        return wordFromExcludedLetters;
    }

    public void setWordFromExcludedLetters(String wordFromExcludedLetters) {
        this.wordFromExcludedLetters = wordFromExcludedLetters;
    }

    public Integer getNumberOfLives() {
        return numberOfLives;
    }

    public void setNumberOfLives(Integer numberOfLives) {
        this.numberOfLives = numberOfLives;
    }

    public Integer getStatusGame() {return statusGame;}

    public void setStatusGame(Integer statusGame) {this.statusGame = statusGame;}

    public Integer getTotalGame() {return totalGame;}

    public void setTotalGame(Integer totalGame) {this.statusGame = totalGame;}

    public Integer getTotalWin() {return totalWin;}

    public void setTotalWin(Integer totalWin) {this.totalWin = totalWin;}

    public Integer getRatingUser() {
        return ratingUser;
    }

    public void setRatingUser(Integer ratingUser) {
        this.ratingUser = ratingUser;
    }
}