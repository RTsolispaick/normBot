package org.opp.data.models;

import jakarta.persistence.*;
import org.opp.data.models.types.Difficult;
import org.opp.data.models.types.StatusGame;

/**
 * Класс игры
 */
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "word")
    private String word;
    @Column(name = "category")
    private String category;
    @Column(name = "word_game_view")
    private String gameViewOfTheWord;
    @Column(name = "word_from_excl")
    private String wordFromExcludedLetters;
    @Column(name = "numbers_of_live")
    private Integer numberOfLives;
    @Column(name = "status_game")
    @Enumerated(EnumType.STRING)
    private StatusGame statusGame;
    @Column(name = "difficult")
    @Enumerated(EnumType.STRING)
    private Difficult difficult;

    public Game() {}

    /**
     * Инициализация игры
     * @param word слово для игры в висилицу
     */
    public void initGame(Word word){
        this.word = word.getWord();
        this.category = word.getCategory();
        this.gameViewOfTheWord = "_ ".repeat(word.getWord().length() - 1) + "_";
        this.wordFromExcludedLetters = "";
        this.statusGame = StatusGame.STARTGAME;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

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

    public StatusGame getStatusGame() {
        return statusGame;
    }

    public void setStatusGame(StatusGame statusGame) {
        this.statusGame = statusGame;
    }

    public Difficult getDifficult() {
        return difficult;
    }

    public void setDifficult(Difficult difficult) {
        this.difficult = difficult;
    }
}
