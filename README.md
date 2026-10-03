# Task 2 — Number Guessing Game

Java Swing GUI implementation of the assignment's Number Guessing Game.

## Features
- Random number generated between 1 and 100
- Guess input using JTextField
- "Too High", "Too Low", and "Correct!" responses
- Visible attempt counter
- Maximum 7 attempts
- "You Lost!" message after the limit, revealing the number
- Play Again button
- Score/history across multiple rounds
- Displays `Round X — guessed in Y attempts`
- Difficulty levels:
  - Easy: 1–50, 10 attempts
  - Medium: 1–100, 7 attempts
  - Hard: 1–200, 5 attempts
- Input validation for non-numeric, decimal, empty, and out-of-range input

## Requirements
- JDK 17 or newer
- VS Code
- Extension Pack for Java
- Maven

## Run in VS Code

Open this folder in VS Code, then use the terminal:

```bash
mvn clean compile
mvn exec:java
```

You can also run `Main.java` directly using VS Code's Java Run button.

## Project structure

```text
NumberGuessingGame/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            └── com/example/guessing/
                ├── Main.java
                ├── GameFrame.java
                └── GameEngine.java
```
