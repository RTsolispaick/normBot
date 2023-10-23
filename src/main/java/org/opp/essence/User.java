package org.opp.essence;

import org.opp.core.State;

import java.util.List;
import java.util.Map;

/**
 * Класс пользователя
 */
public class User {
    private State state;
    private String word;
    private StringBuilder gameViewOfTheWord;
    private StringBuilder wordFromExcludedLetters;
    private int numberOfLives;
    private Map<Character, List<Integer>> mapOfIndexesForEachLetter;

    /**
     * Конструкток пользователя
     */
    public User() {
        this.state = State.IDLE;
    }

    public void setStateIdle(){
        this.state = State.IDLE;
        word = null;
        gameViewOfTheWord = null;
        numberOfLives = 0;
        mapOfIndexesForEachLetter = null;
        wordFromExcludedLetters = null;
    }

    public void setStateGame(){
        this.state = State.GAME;
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

    public Map<Character, List<Integer>> getMapOfIndexesForEachLetter() {
        return mapOfIndexesForEachLetter;
    }

    public void setMapOfIndexesForEachLetter(Map<Character, List<Integer>> mapOfIndexesForEachLetter) {
        this.mapOfIndexesForEachLetter = mapOfIndexesForEachLetter;
    }
}