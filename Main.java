package com.example.guessing;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameFrame().setVisible(true);
        });
    }
}
