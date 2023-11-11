package org.opp.data.repositories;

import org.opp.data.dao.WordDAO;
import org.opp.data.models.Word;

public class WordRepository {
    private final WordDAO wordDAO;
    public WordRepository() {
        this.wordDAO = new WordDAO();
    }
    public Word getRandomWord() {return wordDAO.getRandomWord();}
}
