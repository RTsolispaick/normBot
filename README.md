# normBot

Hangman Game bot

## Task 4

Добавление категорий в хранилище слов (теперь пользователю будет выводиться категория, к которой относиться слово, как ориентир при угадывании).  Реализовать уровни сложности(Easy - 15 жизней, Medium - 10 жизней, Hard - 5 жизней).

## Commands

* /start - beginning of work
* /rules - if you need rules
* /game - start game  
  /easy - 15 attempts are given  
  /medium - 10 attempts are given  
  /hard - 5 attempts are given  
* /stop - stop game
* /top - top 5 players
* /stats - your stats

## How to launch  

1. Just use:
```
git clone https://github.com/RTsolispaick/normBot.git
```
2. Enter your token and bot name in Config class and add database info (host, port, name, user, pass).

3. And run.
  
## Пример уровней сложности и нового storage с категориями:  
П: /game  
Б: Выбери уровень сложности!  
    /easy - 15 попыток  
    /medium - 10 попыток  
    /hard - 5 попыток  
П: /ultrahard  
Б: Я тебя не понял! Напиши ещё раз!  
П: /easy  
Б: Давай сыграем!  
     Тема: животные с хвостами и говорящие “мяу”.  
     Я загадал слово: _ _ _.  
     У тебя осталось 15 попыток!   


