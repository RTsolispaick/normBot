# normBot

Hangman Game bot

## Commands

* /start - beginning of work
* /help - if you need help
* /game - start game
* /stop - stop game

## How to launch  

1. Just use:
```
git clone https://github.com/RTsolispaick/normBot.git
```
2. Enter your token and bot name in Config class.

3. And run.

## Example of using a bot

П: /StAtT  
Б: Привет! Давай сыграем в виселицу! Если ты согласен напиши /game.  
П: /game  
Б: Давай сыграем! Я загадал слово: т _ _ _ _ с.  
У тебя осталось 10 попыток!  
П: ASDADA  
Б: Тебе нужно вывести одну букву на русском языке!  
П: F  
Б: Мы не в Англии!  
П: Н  
Б: Правильно! т _ н н _ с. Исключили:  
П: ф  
Б: Не угадал, попробуй ещё раз! т _ н н _ с. Исключили: ф  
У тебя осталось 9 попыток!  
П: а  
Б: Не угадал, попробуй ещё раз! т _ н н _ с. Исключили: фа  
У тебя осталось 8 попыток!  
П: Е  
Б: Правильно! т е н н _ с. Исключили: фа  
П: и  
Б: Ты угадал! Молодец! Если хочешь сыграть ещё напиши /game.  