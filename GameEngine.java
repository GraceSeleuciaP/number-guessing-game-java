package com.example.guessing;

import java.util.Random;

public class GameEngine {

    public enum Difficulty {
        EASY("Easy", 1, 50, 10),
        MEDIUM("Medium", 1, 100, 7),
        HARD("Hard", 1, 200, 5);

        private final String name;
        private final int minimum;
        private final int maximum;
        private final int maxAttempts;

        Difficulty(String name, int minimum, int maximum, int maxAttempts) {
            this.name = name;
            this.minimum = minimum;
            this.maximum = maximum;
            this.maxAttempts = maxAttempts;
        }

        public String getName() {
            return name;
        }

        public int getMinimum() {
            return minimum;
        }

        public int getMaximum() {
            return maximum;
        }

        public int getMaxAttempts() {
            return maxAttempts;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum GuessResult {
        TOO_LOW,
        TOO_HIGH,
        CORRECT,
        OUT_OF_RANGE
    }

    private final Random random = new Random();

    private Difficulty difficulty;
    private int secretNumber;
    private int attempts;
    private boolean finished;

    public GameEngine(Difficulty difficulty) {
        startNewRound(difficulty);
    }

    public void startNewRound(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.secretNumber = random.nextInt(
                difficulty.getMaximum() - difficulty.getMinimum() + 1
        ) + difficulty.getMinimum();
        this.attempts = 0;
        this.finished = false;
    }

    public GuessResult makeGuess(int guess) {
        if (finished) {
            return GuessResult.OUT_OF_RANGE;
        }

        if (guess < difficulty.getMinimum() || guess > difficulty.getMaximum()) {
            return GuessResult.OUT_OF_RANGE;
        }

        attempts++;

        if (guess == secretNumber) {
            finished = true;
            return GuessResult.CORRECT;
        }

        if (attempts >= difficulty.getMaxAttempts()) {
            finished = true;
            return guess < secretNumber
                    ? GuessResult.TOO_LOW
                    : GuessResult.TOO_HIGH;
        }

        return guess < secretNumber
                ? GuessResult.TOO_LOW
                : GuessResult.TOO_HIGH;
    }

    public boolean isLost() {
        return finished && attempts >= difficulty.getMaxAttempts()
                && !wasCorrect();
    }

    private boolean wasCorrect() {
        // A finished round is considered won when the last submitted guess
        // equals the secret number.
        return false;
    }

    public int getSecretNumber() {
        return secretNumber;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getMaxAttempts() {
        return difficulty.getMaxAttempts();
    }

    public int getMinimum() {
        return difficulty.getMinimum();
    }

    public int getMaximum() {
        return difficulty.getMaximum();
    }

    public boolean isFinished() {
        return finished;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
