package tictactoe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class Main {

    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[94m";
    private static final String RED = "\u001B[91m";
    private static final String GREEN = "\u001B[92m";

    public static void main(String[] args) {
        startUiWelcome();
    }

    private static void startUiWelcome() {
        JFrame frame = new JFrame("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        root.setBackground(Color.WHITE);

        JLabel welcome = new JLabel(
                "<html><div style='text-align:center;'>Welcome to Tic-Tac-Toe.<br>Please put enter your name.</div></html>",
                SwingConstants.CENTER);
        welcome.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        welcome.setForeground(new Color(0, 150, 0));

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new GridLayout(2, 1, 8, 8));

        JLabel nameLabel = new JLabel("Name:", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        nameLabel.setForeground(Color.BLACK);

        javax.swing.JTextField nameField = new javax.swing.JTextField();
        nameField.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        nameField.setForeground(Color.BLACK);
        nameField.setHorizontalAlignment(SwingConstants.CENTER);
        nameField.setPreferredSize(new Dimension(320, 40));

        centerPanel.add(nameLabel);
        centerPanel.add(nameField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton startConsole = new JButton("Start Console");
        JButton startUiGame = new JButton("Start UI");
        buttonPanel.add(startConsole);
        buttonPanel.add(startUiGame);

        startConsole.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter your name first.");
                return;
            }
            frame.dispose();
            startConsoleMode(name);
        });

        startUiGame.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter your name first.");
                return;
            }
            frame.dispose();
            startUiMode(name);
        });

        root.add(welcome, BorderLayout.NORTH);
        root.add(centerPanel, BorderLayout.CENTER);
        root.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(root);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void startConsoleMode(String playerName) {
        Scanner in = new Scanner(System.in);
        printWelcomeBanner();
        System.out.println("Hi, " + playerName + "! Let's play Tic-Tac-Toe.");
        String opponent = "Computer";
        startConsoleGameLoop(in, playerName, opponent);
    }

    private static void printWelcomeBanner() {
        System.out.println(GREEN + "============================================================");
        System.out.println(" Welcome to Tic-Tac-Toe. Please put enter your name.");
        System.out.println("============================================================" + RESET);
    }

    private static void startConsoleGameLoop(Scanner in, String playerX, String playerO) {
        boolean playAgain = true;
        while (playAgain) {
            char[][] board = new char[3][3];
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    board[r][c] = 'E';
                }
            }

            char current = 'X';
            boolean gameOver = false;

            while (!gameOver) {
                printBoard(board);
                if (current == 'X') {
                    System.out.println("✨ " + playerX + "'s turn (X)");
                } else {
                    System.out.println("💨 " + playerO + "'s turn (O)");
                }

                int row = askForInt(in, "Row (0-2): ");
                int col = askForInt(in, "Col (0-2): ");

                if (row < 0 || row > 2 || col < 0 || col > 2) {
                    System.out.println("Try again: row/col must be between 0 and 2.");
                    continue;
                }
                if (board[row][col] != 'E') {
                    System.out.println("That cell is taken. Pick another.");
                    continue;
                }

                board[row][col] = current;

                if (isWin(board, current)) {
                    printBoard(board);
                    String winnerName = current == 'X' ? playerX : playerO;
                    System.out.println("🎉 " + winnerName + " wins!");
                    gameOver = true;
                } else if (isDraw(board)) {
                    printBoard(board);
                    System.out.println("🤝 Draw game!");
                    gameOver = true;
                } else {
                    current = current == 'X' ? 'O' : 'X';
                }
            }

            System.out.print("Play again? (y/n): ");
            playAgain = in.nextLine().trim().equalsIgnoreCase("y");
        }
    }

    private static int askForInt(Scanner in, String prompt) {
        System.out.print(prompt);
        String value = in.nextLine().trim();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private static void printBoard(char[][] board) {
        System.out.println();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                char value = board[r][c];
                String symbol;
                if (value == 'X') {
                    symbol = BLUE + "X" + RESET;
                } else if (value == 'O') {
                    symbol = RED + "O" + RESET;
                } else {
                    symbol = " ";
                }

                System.out.print(" " + symbol + " ");
                if (c < 2) {
                    System.out.print("|");
                }
            }
            System.out.println();
            if (r < 2) {
                System.out.println("---+---+---");
            }
        }
        System.out.println();
    }

    private static boolean isWin(char[][] board, char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player)
                || (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    private static boolean isDraw(char[][] board) {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == 'E') {
                    return false;
                }
            }
        }
        return true;
    }

    private static void startUiMode(String playerName) {
        String playerX = playerName;
        String playerO = "Computer";

        JFrame frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(12, 12));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.BLACK);
        top.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("WELCOME TO TIC TAC TOE", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        top.add(title, BorderLayout.NORTH);

        JLabel status = new JLabel(playerX + " (X) turn", SwingConstants.CENTER);
        status.setForeground(new Color(120, 190, 255));
        status.setFont(new Font("SansSerif", Font.BOLD, 18));
        top.add(status, BorderLayout.SOUTH);

        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        boardPanel.setBackground(Color.BLACK);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        boardPanel.setPreferredSize(new Dimension(450, 450));

        JButton[][] cells = new JButton[3][3];
        char[][] state = new char[3][3];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                state[r][c] = 'E';
                JButton button = new JButton(" ");
                button.setFocusPainted(false);
                button.setFont(new Font("SansSerif", Font.BOLD, 54));
                button.setBackground(Color.BLACK);
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 2));
                final int row = r;
                final int col = c;
                button.addActionListener(e -> {
                    if (state[row][col] != 'E') {
                        return;
                    }
                    char current = status.getText().contains("(X)") ? 'X' : 'O';
                    state[row][col] = current;
                    button.setText(String.valueOf(current));
                    button.setForeground(current == 'X' ? new Color(56, 149, 255) : new Color(255, 72, 72));

                    if (isWin(state, current)) {
                        String winnerName = current == 'X' ? playerX : playerO;
                        status.setText(winnerName + " wins!");
                        status.setForeground(Color.GREEN);
                        JOptionPane.showMessageDialog(frame, "🎉 " + winnerName + " wins!");
                        return;
                    }
                    if (isDraw(state)) {
                        status.setText("Draw game");
                        status.setForeground(Color.ORANGE);
                        JOptionPane.showMessageDialog(frame, "🤝 Draw game!");
                        return;
                    }

                    if (current == 'X') {
                        status.setText(playerO + " (O) turn");
                        status.setForeground(new Color(255, 72, 72));
                    } else {
                        status.setText(playerX + " (X) turn");
                        status.setForeground(new Color(56, 149, 255));
                    }
                });
                cells[r][c] = button;
                boardPanel.add(button);
            }
        }

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.BLACK);
        JButton restart = new JButton("Restart");
        restart.addActionListener(e -> {
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    state[r][c] = 'E';
                    cells[r][c].setText(" ");
                    cells[r][c].setForeground(Color.WHITE);
                }
            }
            status.setText(playerX + " (X) turn");
            status.setForeground(new Color(120, 190, 255));
        });
        bottom.add(restart);

        frame.add(top, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}