package org.opp.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Класс реализует доступ и выбор слова для игры
 */
public class StorageWord {
    private final List<String> DICTIONARY = new ArrayList<>();
    private final Random RANDOM = new Random();

    /**
     * Конструктор класса, в котором происходит инициализация DICTIONARY словами из файла word.txt
     */
    public StorageWord() {
        String line;
        try (InputStream is = StorageWord.class.getClassLoader().getResourceAsStream("word.txt")) {
            if (is == null) throw new NullPointerException("File not found: word.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is), 13000);
            while ((line = br.readLine()) != null) DICTIONARY.add(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод wordChoice() реализует случайный выбор слова из словаря для игры
     * @return возращает выбранное слово
     */
    public String wordChoice() {
        return DICTIONARY.get(RANDOM.nextInt(0, DICTIONARY.size()));
    }
}