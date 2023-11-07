# normBot

Hangman Game bot

## Task 3

Обновить архитектуру проекта для внедрения базы данных.  Добавление команды /stats и /top, 
которые будут выводить статистику пользователя (количество сыгранных игр, количество побед
и поражений) и рейтинг игроков по количеству выигранных игр.

## Commands

* /start - beginning of work
* /rules - if you need rules
* /game - start game
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

## Example of using a bot

П: /StArT  
Б: Привет! Давай сыграем в виселицу! Если ты согласен напиши /game.  
П: /game
Б: Давай сыграем!
Тема: спорт
Я загадал слово: _ _ _ _ _ _.
У тебя осталось 10 попыток!

Пример команд /stats и /top:
П: /stats
Б: Количество сыгранных игр: 1337
Количество побед: 666
П: /top
Б: Топ:
1. Артём = 1337
2. Артур = 10
3. Дима = 0
4. Ярик = -10
5. Костя = -1337

    Твой рейтинг: 1337 

