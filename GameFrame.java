package com.example.guessing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameFrame extends JFrame {

    private final JComboBox<GameEngine.Difficulty> difficultyBox =
            new JComboBox<>(GameEngine.Difficulty.values());

    private final JTextField guessField = new JTextField();
    private final JLabel rangeLabel = new JLabel();
    private final JLabel attemptLabel = new JLabel();
    private final JLabel resultLabel = new JLabel("Make your first guess!");
    private final JLabel scoreLabel = new JLabel("Rounds completed: 0");
    private final JTextArea historyArea = new JTextArea();

    private final List<String> roundHistory = new ArrayList<>();

    private GameEngine game;
    private int roundNumber = 1;
    private boolean roundWon = false;

    public GameFrame() {
        setTitle("Number Guessing Game");
        setSize(700, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        game = new GameEngine(GameEngine.Difficulty.MEDIUM);

        buildUI();
        updateLabels();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("NUMBER GUESSING GAME", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 27));
        root.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(15, 15));

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
        gamePanel.setBorder(BorderFactory.createTitledBorder("Current Round"));

        JLabel difficultyText = new JLabel("Difficulty:");
        difficultyText.setFont(new Font("SansSerif", Font.BOLD, 15));

        difficultyBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        difficultyBox.addActionListener(e -> changeDifficulty());

        rangeLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        attemptLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        gamePanel.add(difficultyText);
        gamePanel.add(Box.createVerticalStrut(7));
        gamePanel.add(difficultyBox);
        gamePanel.add(Box.createVerticalStrut(15));
        gamePanel.add(rangeLabel);
        gamePanel.add(Box.createVerticalStrut(7));
        gamePanel.add(attemptLabel);
        gamePanel.add(Box.createVerticalStrut(20));

        JLabel guessText = new JLabel("Enter your guess:");
        guessText.setFont(new Font("SansSerif", Font.BOLD, 15));
        gamePanel.add(guessText);
        gamePanel.add(Box.createVerticalStrut(7));

        guessField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        guessField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        guessField.addActionListener(e -> submitGuess());
        gamePanel.add(guessField);

        gamePanel.add(Box.createVerticalStrut(15));

        JButton guessButton = new JButton("Submit Guess");
        guessButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        guessButton.addActionListener(e -> submitGuess());
        gamePanel.add(guessButton);

        gamePanel.add(Box.createVerticalStrut(15));

        resultLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gamePanel.add(resultLabel);

        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        playAgainButton.addActionListener(e -> startNewRound());
        gamePanel.add(Box.createVerticalStrut(15));
        gamePanel.add(playAgainButton);

        center.add(gamePanel, BorderLayout.CENTER);

        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);
        historyArea.setBorder(BorderFactory.createTitledBorder("Score / Round History"));

        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setPreferredSize(new Dimension(310, 0));
        historyPanel.add(new JScrollPane(historyArea), BorderLayout.CENTER);
        historyPanel.add(scoreLabel, BorderLayout.SOUTH);

        center.add(historyPanel, BorderLayout.EAST);

        root.add(center, BorderLayout.CENTER);

        JLabel footer = new JLabel(
                "Guess the secret number before you run out of attempts.",
                SwingConstants.CENTER
        );
        footer.setFont(new Font("SansSerif", Font.ITALIC, 12));
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void submitGuess() {
        if (game.isFinished()) {
            JOptionPane.showMessageDialog(
                    this,
                    "This round has ended. Click Play Again to start a new round.",
                    "Round Finished",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        String input = guessField.getText().trim();

        if (input.isEmpty()) {
            showError("Please enter a number.");
            return;
        }

        if (!input.matches("\\d+")) {
            showError("Invalid input. Please enter a whole number only.");
            return;
        }

        int guess;

        try {
            guess = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            showError("The number is too large.");
            return;
        }

        if (guess < game.getMinimum() || guess > game.getMaximum()) {
            showError(
                    "Please enter a number between "
                            + game.getMinimum() + " and " + game.getMaximum() + "."
            );
            return;
        }

        GameEngine.GuessResult result = game.makeGuess(guess);
        guessField.selectAll();

        switch (result) {
            case TOO_HIGH -> {
                resultLabel.setText("Too High!");
                resultLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

                if (game.isFinished()) {
                    roundLost();
                }
            }
            case TOO_LOW -> {
                resultLabel.setText("Too Low!");
                resultLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

                if (game.isFinished()) {
                    roundLost();
                }
            }
            case CORRECT -> {
                resultLabel.setText("Correct!");
                resultLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
                roundWon = true;
                finishRound();
            }
            case OUT_OF_RANGE -> showError("Guess is outside the allowed range.");
        }

        updateLabels();
    }

    private void roundLost() {
        resultLabel.setText("You Lost! Number was " + game.getSecretNumber());
        finishRound();
    }

    private void finishRound() {
        String outcome = roundWon ? "won" : "lost";
        String entry = String.format(
                "Round %d — guessed in %d attempt%s — %s",
                roundNumber,
                game.getAttempts(),
                game.getAttempts() == 1 ? "" : "s",
                outcome
        );

        roundHistory.add(entry);
        refreshHistory();

        JOptionPane.showMessageDialog(
                this,
                roundWon
                        ? "Correct! You guessed the number in "
                            + game.getAttempts() + " attempt(s)."
                        : "You Lost! The correct number was "
                            + game.getSecretNumber() + ".",
                "Round " + roundNumber,
                roundWon
                        ? JOptionPane.INFORMATION_MESSAGE
                        : JOptionPane.WARNING_MESSAGE
        );
    }

    private void startNewRound() {
        roundNumber++;
        roundWon = false;

        GameEngine.Difficulty difficulty =
                (GameEngine.Difficulty) difficultyBox.getSelectedItem();

        game.startNewRound(difficulty);
        guessField.setText("");
        resultLabel.setText("New round started. Make your guess!");
        updateLabels();
    }

    private void changeDifficulty() {
        GameEngine.Difficulty difficulty =
                (GameEngine.Difficulty) difficultyBox.getSelectedItem();

        if (game != null && !game.isFinished() && game.getAttempts() > 0) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Changing difficulty will start a new round. Continue?",
                    "Change Difficulty",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice != JOptionPane.YES_OPTION) {
                difficultyBox.setSelectedItem(game.getDifficulty());
                return;
            }
        }

        roundNumber++;
        roundWon = false;
        game.startNewRound(difficulty);
        guessField.setText("");
        resultLabel.setText("Difficulty changed. New round started!");
        updateLabels();
    }

    private void updateLabels() {
        rangeLabel.setText(
                "Range: " + game.getMinimum() + " to " + game.getMaximum()
        );

        attemptLabel.setText(
                "Attempts: " + game.getAttempts()
                        + " / " + game.getMaxAttempts()
        );

        scoreLabel.setText(
                "Rounds completed: " + roundHistory.size()
        );
    }

    private void refreshHistory() {
        StringBuilder builder = new StringBuilder();

        for (String entry : roundHistory) {
            builder.append(entry).append("\n");
        }

        historyArea.setText(builder.toString());
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
