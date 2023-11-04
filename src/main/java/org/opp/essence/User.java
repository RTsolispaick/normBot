package org.opp.essence;

import org.opp.core.State;

/**
 * Класс пользователя
 */
public class User {
    private State state;
    private String word;
    private StringBuilder gameViewOfTheWord;
    private StringBuilder wordFromExcludedLetters;
    private int numberOfLives;
    private int statusGame;

    /**
     * Конструкток пользователя
     */
    public User() {
        this.state = State.IDLE;
    }

    public void setStateIdle(){
        this.state = State.IDLE;
        this.word = null;
        this.gameViewOfTheWord = null;
        this.wordFromExcludedLetters = null;
        this.numberOfLives = 0;
        this.statusGame = -1;
    }

    public void setStateGame(String word){
        this.state = State.GAME;
        this.word = word;
        this.gameViewOfTheWord = new StringBuilder("_ ".repeat(word.length() - 1)).append("_");
        this.wordFromExcludedLetters = new StringBuilder();
        this.numberOfLives = 10;
        this.statusGame = -1;
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

    public StringBuilder getGameViewOfTheWord() {
        return gameViewOfTheWord;
    }

    public void setGameViewOfTheWord(StringBuilder gameViewOfTheWord) {
        this.gameViewOfTheWord = gameViewOfTheWord;
    }

    public StringBuilder getWordFromExcludedLetters() {
        return wordFromExcludedLetters;
    }

    public void setWordFromExcludedLetters(StringBuilder wordFromExcludedLetters) {
        this.wordFromExcludedLetters = wordFromExcludedLetters;
    }

    public int getNumberOfLives() {
        return numberOfLives;
    }

    public void setNumberOfLives(int numberOfLives) {
        this.numberOfLives = numberOfLives;
    }

    public int getStatusGame() {return statusGame;}

    public void setStatusGame(int statusGame) {this.statusGame = statusGame;}
}