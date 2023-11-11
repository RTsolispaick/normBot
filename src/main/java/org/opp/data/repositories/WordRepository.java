package org.opp.data.repositories;

import org.opp.data.dao.WordDAO;
import org.opp.data.models.Word;

/**
 * Репозиторий, который позволяет совершать операции над базой данных слов
 */
public class WordRepository {
    private final WordDAO wordDAO;
    public WordRepository() {
        this.wordDAO = new WordDAO();
    }

    /**
     * Геттер для получения случайного слова
     * @return случайное слово
     */
    public Word getRandomWord() {return wordDAO.getRandomWord();}
}
