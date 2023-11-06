package org.opp.essence;

import org.opp.core.State;

/**
 * Класс пользователя
 */
public class User {
    private Integer id;
    private Long platform_id;
    private State state;
    private String word;
    private String category;
    private String gameViewOfTheWord;
    private String wordFromExcludedLetters;
    private Integer numberOfLives;
    private Integer statusGame;
    private Integer totalGame;
    private Integer totalWin;
    private Integer raitingUser;

    /**
     * Конструкток пользователя
     */
    public User() {
        this.state = State.IDLE;
        this.word = null;
        this.category = null;
        this.gameViewOfTheWord = null;
        this.wordFromExcludedLetters = null;
        this.numberOfLives = -1;
        this.statusGame = -1;
        this.totalWin = 0;
        this.totalGame = 0;
        this.raitingUser = 0;
    }

    public User(Long platform_id) {
        this.state = State.IDLE;
        this.platform_id = platform_id;
        this.word = null;
        this.category = null;
        this.gameViewOfTheWord = null;
        this.wordFromExcludedLetters = null;
        this.numberOfLives = -1;
        this.statusGame = -1;
        this.totalWin = 0;
        this.totalGame = 0;
        this.raitingUser = 0;
    }

    public void setStateIdle(){
        this.state = State.IDLE;
        this.word = null;
        this.gameViewOfTheWord = null;
        this.wordFromExcludedLetters = null;
        this.numberOfLives = -1;
        this.statusGame = -1;
        this.raitingUser = this.totalWin * 3 - this.totalGame;
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

    public Integer getRaitingUser() {
        return raitingUser;
    }

    public void setRaitingUser(Integer raitingUser) {
        this.raitingUser = raitingUser;
    }
}