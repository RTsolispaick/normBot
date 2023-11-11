package org.opp.data.models;

import jakarta.persistence.*;

/**
 * Класс слов
 */
@Entity
@Table(name = "words")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "word")
    private String word;
    @Column(name = "category")
    private String category;

    public Word() {}

    /**
     * Конструктор для тестов
     * @param word слово
     * @param category категория
     */
    public Word(String word, String category){
        this.word = word;
        this.category = category;
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
}
