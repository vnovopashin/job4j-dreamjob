

## job4j_dreamJob

![logo](images/logo.jpg)

Приложение представляет сайт для размещения вакансий.
### Основные возможности сайта
На сайте размещаются объявления о вакансиях, кадровики публикуют вакансии,
кандидаты публикуют резюме.
Объявление состоит из:

- названия вакансии/резюме
- описания вакансии/резюме
- даты публикации
- фотографии

___
### Стек технологий
- Java 17
- Spring boot
- Maven 3.8
- Thymeleaf
- Liquibase 4.15
- Junit 5
- Postgres 14
___
### Требования к окружению
- Java 17
- Maven 3.8
- Postgres 14
___
### Запуск проекта

Для запуска приложения необходима база данных
(команда ниже создает базу данных с названием dreamjob)
```
create database dreamjob
```
#### Варианты запуска приложения

С использованием командной строки и параметрами по умолчанию
(db=dreamjob, user=postgres, password=password, port=8081)
```
mvn spring-boot:run
```
С использованием собственных настроек базы данных и порта
```
mvn spring-boot:run -Dspring-boot.run.arguments=--db=your_database,--user=your_user,--password=your_password,--port=your_port
 ```

Компиляция в jar файл
```
mvn install
```
Запуск jar файла с параметрами по умолчанию
(db=dreamjob, user=postgres, password=password, port=8081)
```
java -jar target/job4j-dreamjob-1.0-SNAPSHOT.jar
```
Запуска jar файла с использованием собственных настроек базы данных и порта
```
java -jar target/job4j-dreamjob-1.0-SNAPSHOT.jar --db=your_database,--user=your_user,--password=your_password,--port=your_port
```

___
### Скриншоты приложения
    Главная страница
![main](images/main.jpg)
_________________________________

    Страница регистрации
![registration](images/registration.jpg)
_________________________________

    Страница входа
![login](images/login.jpg)
_________________________________

    Страница с вакансиями
![vacancies](images/vacancies.jpg)
_________________________________

    Страница с кандидатами
![candidates](images/candidates.jpg)
_________________________________

    Страница создания вакансии
![create_vacancy](images/create_vacancy.jpg)
_________________________________

    Страница создания резюме
![create_resume](images/create_resume.jpg)
_________________________________

    Страница редактирования вакансии
![edit_vacancy](images/edit_vacancy.jpg)
_________________________________

    Страница редактирования резюме
![edit_resume](images/edit_resume.jpg)
_________________________________

___
### Контакты

[GitHub.com](https://github.com/vnovopashin)