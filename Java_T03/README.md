# Java_T03 — Tic-Tac-Toe Web Application

Веб-приложение "Крестики-нолики" на Spring Boot с REST API.

## Технологии
- Java 17
- Spring Boot 3.x
- Gradle
- REST API

## Архитектура
src/tictac/
├── datasource/ # Репозиторий (хранение игр в памяти)
├── domain/ # Бизнес-логика (Game, GameBoard, GameService)
├── web/ # Контроллеры и DTO
└── di/ # Конфигурация Spring

text

## API Endpoints

| Метод | URL | Описание |
|-------|-----|----------|
| POST | `/api/games` | Создать новую игру |
| GET | `/api/games/{id}` | Получить состояние игры |
| POST | `/api/games/{id}/moves` | Сделать ход |
| GET | `/api/games/{id}/status` | Статус игры |

## Запуск
```bash
cd src/tictac
./gradlew bootRun
После запуска приложение доступно по адресу: http://localhost:8080

Пример запроса (создание игры)
bash
curl -X POST http://localhost:8080/api/games
Цели проекта
Научиться разрабатывать REST API на Spring Boot

Освоить трёхуровневую архитектуру

Понять внедрение зависимостей (IoC/DI)

Подготовиться к более сложным проектам
