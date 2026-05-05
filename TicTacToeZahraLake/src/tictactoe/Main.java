package tictactoe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Main {

    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[94m";
    private static final String PURPLE = "\u001B[95m";
    private static final Font FONT_ALICE_LARGE = new Font("Alice", Font.BOLD, 34);
    private static final Font FONT_ALICE_MEDIUM = new Font("Alice", Font.PLAIN, 24);
    private static final Font FONT_ALICE_BUTTON = new Font("Alice", Font.BOLD, 20);
    private static final Font FONT_ALICE_GAME_TITLE = new Font("Alice", Font.BOLD, 32);
    private static final Font FONT_ALICE_STATUS = new Font("Alice", Font.BOLD, 24);
    private static final Font FONT_ALICE_MARK = new Font("Alice", Font.BOLD, 72);
    private static final String RANKINGS_FILE = "player_rankings.csv";

    public static void main(String[] args) {
        startUiWelcome();
    }

    private static void startUiWelcome() {
        JFrame frame = new JFrame("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        root.setBackground(new Color(235, 232, 255));

        JLabel welcome = new JLabel(
                "<html><div style='text-align:center;'>Welcome to Tic-Tac-Toe.<br>Please put enter your name.</div></html>",
                SwingConstants.CENTER);
        welcome.setFont(FONT_ALICE_LARGE);
        welcome.setForeground(new Color(70, 80, 230));

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(235, 232, 255));
        centerPanel.setLayout(new GridLayout(4, 1, 8, 8));

        JLabel nameLabel = new JLabel("Player X Name:", SwingConstants.CENTER);
        nameLabel.setFont(FONT_ALICE_MEDIUM);
        nameLabel.setForeground(new Color(92, 52, 190));

        javax.swing.JTextField nameField = new javax.swing.JTextField();
        nameField.setFont(FONT_ALICE_MEDIUM);
        nameField.setForeground(Color.BLACK);
        nameField.setHorizontalAlignment(SwingConstants.CENTER);
        nameField.setPreferredSize(new Dimension(440, 56));
        nameField.setBackground(new Color(248, 246, 255));
        JLabel secondNameLabel = new JLabel("Player O Name:", SwingConstants.CENTER);
        secondNameLabel.setFont(FONT_ALICE_MEDIUM);
        secondNameLabel.setForeground(new Color(92, 52, 190));

        javax.swing.JTextField secondNameField = new javax.swing.JTextField("computer");
        secondNameField.setFont(FONT_ALICE_MEDIUM);
        secondNameField.setForeground(Color.BLACK);
        secondNameField.setHorizontalAlignment(SwingConstants.CENTER);
        secondNameField.setPreferredSize(new Dimension(440, 56));
        secondNameField.setBackground(new Color(248, 246, 255));
        secondNameField.setEditable(false);

        JLabel modeLabel = new JLabel("Players:", SwingConstants.CENTER);
        modeLabel.setFont(FONT_ALICE_MEDIUM);
        modeLabel.setForeground(new Color(92, 52, 190));

        JPanel modePanel = new JPanel();
        modePanel.setBackground(new Color(235, 232, 255));
        JButton onePlayerButton = new JButton("1 Player");
        JButton twoPlayerButton = new JButton("2 Players");
        onePlayerButton.setFont(FONT_ALICE_BUTTON);
        twoPlayerButton.setFont(FONT_ALICE_BUTTON);
        onePlayerButton.setBackground(new Color(120, 130, 255));
        onePlayerButton.setForeground(Color.WHITE);
        twoPlayerButton.setBackground(new Color(92, 52, 190));
        twoPlayerButton.setForeground(Color.WHITE);
        modePanel.add(onePlayerButton);
        modePanel.add(twoPlayerButton);

        final boolean[] twoPlayerMode = { false };
        Runnable applyModeStyles = () -> {
            if (twoPlayerMode[0]) {
                twoPlayerButton.setBackground(new Color(70, 35, 160));
                twoPlayerButton.setBorder(BorderFactory.createLineBorder(new Color(185, 155, 255), 3));
                onePlayerButton.setBackground(new Color(120, 130, 255));
                onePlayerButton.setBorder(BorderFactory.createLineBorder(new Color(120, 130, 255), 1));
                secondNameField.setEditable(true);
                secondNameField.setText("");
            } else {
                onePlayerButton.setBackground(new Color(80, 95, 235));
                onePlayerButton.setBorder(BorderFactory.createLineBorder(new Color(160, 190, 255), 3));
                twoPlayerButton.setBackground(new Color(92, 52, 190));
                twoPlayerButton.setBorder(BorderFactory.createLineBorder(new Color(92, 52, 190), 1));
                secondNameField.setEditable(false);
                secondNameField.setText("computer");
            }
        };
        onePlayerButton.addActionListener(e -> {
            twoPlayerMode[0] = false;
            applyModeStyles.run();
        });
        twoPlayerButton.addActionListener(e -> {
            twoPlayerMode[0] = true;
            applyModeStyles.run();
        });
        applyModeStyles.run();

        JLabel gameTypeLabel = new JLabel("Game Type:", SwingConstants.CENTER);
        gameTypeLabel.setFont(FONT_ALICE_MEDIUM);
        gameTypeLabel.setForeground(new Color(92, 52, 190));

        JPanel gameTypePanel = new JPanel();
        gameTypePanel.setBackground(new Color(235, 232, 255));
        JButton classicButton = new JButton("Classic");
        JButton ultimateButton = new JButton("Ultimate");
        classicButton.setFont(FONT_ALICE_BUTTON);
        ultimateButton.setFont(FONT_ALICE_BUTTON);
        classicButton.setForeground(Color.WHITE);
        ultimateButton.setForeground(Color.WHITE);
        gameTypePanel.add(classicButton);
        gameTypePanel.add(ultimateButton);

        final boolean[] ultimateMode = { false };
        Runnable applyGameTypeStyles = () -> {
            if (ultimateMode[0]) {
                ultimateButton.setBackground(new Color(70, 35, 160));
                ultimateButton.setBorder(BorderFactory.createLineBorder(new Color(185, 155, 255), 3));
                classicButton.setBackground(new Color(120, 130, 255));
                classicButton.setBorder(BorderFactory.createLineBorder(new Color(120, 130, 255), 1));
            } else {
                classicButton.setBackground(new Color(80, 95, 235));
                classicButton.setBorder(BorderFactory.createLineBorder(new Color(160, 190, 255), 3));
                ultimateButton.setBackground(new Color(92, 52, 190));
                ultimateButton.setBorder(BorderFactory.createLineBorder(new Color(92, 52, 190), 1));
            }
        };
        classicButton.addActionListener(e -> {
            ultimateMode[0] = false;
            applyGameTypeStyles.run();
        });
        ultimateButton.addActionListener(e -> {
            ultimateMode[0] = true;
            applyGameTypeStyles.run();
        });
        applyGameTypeStyles.run();

        centerPanel.add(nameLabel);
        centerPanel.add(nameField);
        centerPanel.add(secondNameLabel);
        centerPanel.add(secondNameField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(235, 232, 255));
        JButton startConsole = new JButton("Start Console");
        JButton startUiGame = new JButton("Start UI");
        startConsole.setFont(FONT_ALICE_BUTTON);
        startUiGame.setFont(FONT_ALICE_BUTTON);
        startConsole.setBackground(new Color(120, 130, 255));
        startConsole.setForeground(Color.WHITE);
        startUiGame.setBackground(new Color(150, 90, 220));
        startUiGame.setForeground(Color.WHITE);
        buttonPanel.add(startConsole);
        buttonPanel.add(startUiGame);
        buttonPanel.add(modeLabel);
        buttonPanel.add(modePanel);
        buttonPanel.add(gameTypeLabel);
        buttonPanel.add(gameTypePanel);

        startConsole.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter your name first.");
                return;
            }
            String secondPlayer = "zahra";
            if (twoPlayerMode[0]) {
                secondPlayer = secondNameField.getText().trim();
                if (secondPlayer.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter Player 2 name.");
                    return;
                }
            }
            frame.dispose();
            startConsoleMode(name, secondPlayer, ultimateMode[0]);
        });

        startUiGame.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter your name first.");
                return;
            }
            String secondPlayer = "zahra";
            if (twoPlayerMode[0]) {
                secondPlayer = secondNameField.getText().trim();
                if (secondPlayer.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter Player 2 name.");
                    return;
                }
            }
            frame.dispose();
            startUiMode(name, secondPlayer, ultimateMode[0]);
        });

        root.add(welcome, BorderLayout.NORTH);
        root.add(centerPanel, BorderLayout.CENTER);
        root.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(root);
        frame.pack();
        frame.setMinimumSize(new Dimension(900, 620));
        frame.setSize(980, 680);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void startConsoleMode(String playerName, String opponent, boolean ultimateMode) {
        Scanner in = new Scanner(System.in);
        printWelcomeBanner();
        System.out.println("Hi, " + playerName + "! Let's play Tic-Tac-Toe.");
        if (ultimateMode) {
            System.out.println("Ultimate mode is available in UI mode. Starting classic console mode.");
        }
        ensurePlayerExists(playerName);
        ensurePlayerExists(opponent);
        startConsoleGameLoop(in, playerName, opponent);
    }

    private static void printWelcomeBanner() {
        System.out.println(BLUE + "============================================================");
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
                playWhooshSound();

                if (isWin(board, current)) {
                    printBoard(board);
                    String winnerName = current == 'X' ? playerX : playerO;
                    if (winnerName.equalsIgnoreCase("computer")) {
                        playSadWompSound();
                    } else {
                        playPlayerWinSound();
                    }
                    System.out.println("🎉 " + winnerName + " wins!");
                    recordWin(winnerName);
                    printRankingsToConsole();
                    gameOver = true;
                } else if (isDraw(board)) {
                    printBoard(board);
                    System.out.println("🤝 Draw game!");
                    printRankingsToConsole();
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
                    symbol = PURPLE + "O" + RESET;
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

    private static void startUiMode(String playerName, String playerTwoName, boolean ultimateMode) {
        String playerX = playerName;
        String playerO = playerTwoName;
        ensurePlayerExists(playerX);
        ensurePlayerExists(playerO);
        if (ultimateMode) {
            startUltimateUiMode(playerX, playerO);
            return;
        }

        JFrame frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(12, 12));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(28, 22, 64));
        top.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("WELCOME TO TIC TAC TOE", SwingConstants.CENTER);
        title.setForeground(new Color(130, 175, 255));
        title.setFont(FONT_ALICE_GAME_TITLE);
        top.add(title, BorderLayout.NORTH);

        JLabel status = new JLabel(playerX + " (X) turn", SwingConstants.CENTER);
        status.setForeground(new Color(130, 175, 255));
        status.setFont(FONT_ALICE_STATUS);
        top.add(status, BorderLayout.SOUTH);

        // Dedicated JPanel container for the playable 3x3 grid.
        JPanel gameGridPanel = new JPanel(new GridLayout(3, 3, 8, 8));
        gameGridPanel.setBackground(new Color(28, 22, 64));
        gameGridPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        gameGridPanel.setPreferredSize(new Dimension(640, 640));

        JButton[][] cells = new JButton[3][3];
        char[][] state = new char[3][3];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                state[r][c] = 'E';
                JButton button = new JButton(" ");
                button.setFocusPainted(false);
                button.setFont(FONT_ALICE_MARK);
                button.setBackground(new Color(24, 18, 58));
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createLineBorder(new Color(95, 96, 201), 2));
                final int row = r;
                final int col = c;
                button.addActionListener(e -> {
                    if (state[row][col] != 'E') {
                        return;
                    }
                    char current = status.getText().contains("(X)") ? 'X' : 'O';
                    state[row][col] = current;
                    button.setText(String.valueOf(current));
                    button.setForeground(current == 'X' ? new Color(90, 170, 255) : new Color(183, 115, 255));
                    playWhooshSound();

                    if (isWin(state, current)) {
                        String winnerName = current == 'X' ? playerX : playerO;
                        if (winnerName.equalsIgnoreCase("computer")) {
                            playSadWompSound();
                        } else {
                            playPlayerWinSound();
                        }
                        status.setText(winnerName + " wins!");
                        status.setForeground(Color.GREEN);
                        recordWin(winnerName);
                        int choice = JOptionPane.showConfirmDialog(
                                frame,
                                "🎉 " + winnerName + " wins!\n\n" + buildRankingsText() + "\nPlay again?",
                                "Game Over",
                                JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            resetClassicBoard(cells, state, status, playerX);
                        }
                        return;
                    }
                    if (isDraw(state)) {
                        status.setText("Draw game");
                        status.setForeground(Color.ORANGE);
                        int choice = JOptionPane.showConfirmDialog(
                                frame,
                                "🤝 Draw game!\n\n" + buildRankingsText() + "\nPlay again?",
                                "Game Over",
                                JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            resetClassicBoard(cells, state, status, playerX);
                        }
                        return;
                    }

                    if (current == 'X') {
                        status.setText(playerO + " (O) turn");
                        status.setForeground(new Color(183, 115, 255));
                    } else {
                        status.setText(playerX + " (X) turn");
                        status.setForeground(new Color(90, 170, 255));
                    }
                });
                cells[r][c] = button;
                gameGridPanel.add(button);
            }
        }

        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(28, 22, 64));
        JButton restart = new JButton("Restart");
        restart.setFont(FONT_ALICE_BUTTON);
        restart.setBackground(new Color(92, 52, 190));
        restart.setForeground(Color.WHITE);
        restart.addActionListener(e -> resetClassicBoard(cells, state, status, playerX));
        bottom.add(restart);

        frame.add(top, BorderLayout.NORTH);
        frame.add(gameGridPanel, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);
        frame.getContentPane().setBackground(new Color(28, 22, 64));
        frame.pack();
        frame.setMinimumSize(new Dimension(900, 780));
        frame.setSize(980, 820);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void startUltimateUiMode(String playerX, String playerO) {
        JFrame frame = new JFrame("Ultimate Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(12, 12));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(28, 22, 64));
        top.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel title = new JLabel("ULTIMATE TIC TAC TOE", SwingConstants.CENTER);
        title.setForeground(new Color(130, 175, 255));
        title.setFont(FONT_ALICE_GAME_TITLE);
        top.add(title, BorderLayout.NORTH);

        JLabel status = new JLabel(playerX + " (X) turn - Any mini board", SwingConstants.CENTER);
        status.setForeground(new Color(130, 175, 255));
        status.setFont(FONT_ALICE_STATUS);
        top.add(status, BorderLayout.SOUTH);

        JPanel megaPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        megaPanel.setBackground(new Color(28, 22, 64));
        megaPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel[] miniPanels = new JPanel[9];
        JButton[][] buttons = new JButton[9][9];
        char[][] state = new char[9][9];
        char[] miniWinners = new char[9];
        for (int i = 0; i < 9; i++) {
            miniWinners[i] = 'E';
            JPanel mini = new JPanel(new GridLayout(3, 3, 4, 4));
            mini.setBackground(new Color(35, 28, 78));
            mini.setBorder(BorderFactory.createLineBorder(new Color(95, 96, 201), 2));
            miniPanels[i] = mini;
            for (int j = 0; j < 9; j++) {
                state[i][j] = 'E';
                JButton b = new JButton(" ");
                b.setFont(new Font("Alice", Font.BOLD, 28));
                b.setFocusPainted(false);
                b.setBackground(new Color(24, 18, 58));
                b.setForeground(Color.WHITE);
                b.setBorder(BorderFactory.createLineBorder(new Color(95, 96, 201), 1));
                final int miniIdx = i;
                final int cellIdx = j;
                b.addActionListener(e -> {
                    int activeMini = Integer.parseInt((String) frame.getRootPane().getClientProperty("activeMini"));
                    char current = ((String) frame.getRootPane().getClientProperty("currentPlayer")).charAt(0);
                    boolean gameOver = Boolean.TRUE.equals(frame.getRootPane().getClientProperty("gameOver"));
                    if (gameOver || miniWinners[miniIdx] != 'E' || state[miniIdx][cellIdx] != 'E') {
                        return;
                    }
                    if (activeMini != -1 && miniIdx != activeMini) {
                        status.setText("You must play in mini board " + (activeMini + 1));
                        return;
                    }

                    state[miniIdx][cellIdx] = current;
                    b.setText(String.valueOf(current));
                    b.setForeground(current == 'X' ? new Color(90, 170, 255) : new Color(183, 115, 255));
                    playWhooshSound();

                    char miniResult = evaluateMiniBoard(state[miniIdx]);
                    if (miniResult != 'E') {
                        miniWinners[miniIdx] = miniResult;
                        if (miniResult == 'X') {
                            miniPanels[miniIdx].setBorder(BorderFactory.createLineBorder(new Color(90, 170, 255), 4));
                        } else if (miniResult == 'O') {
                            miniPanels[miniIdx].setBorder(BorderFactory.createLineBorder(new Color(183, 115, 255), 4));
                        } else {
                            miniPanels[miniIdx].setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
                        }
                    }

                    char macroWinner = evaluateMacroBoard(miniWinners);
                    if (macroWinner == 'X' || macroWinner == 'O') {
                        String winnerName = macroWinner == 'X' ? playerX : playerO;
                        if (winnerName.equalsIgnoreCase("computer")) {
                            playSadWompSound();
                        } else {
                            playPlayerWinSound();
                        }
                        recordWin(winnerName);
                        frame.getRootPane().putClientProperty("gameOver", true);
                        status.setText(winnerName + " wins Ultimate!");
                        status.setForeground(Color.GREEN);
                        int choice = JOptionPane.showConfirmDialog(
                                frame,
                                "🏆 " + winnerName + " wins Ultimate!\n\n" + buildRankingsText() + "\nPlay again?",
                                "Game Over",
                                JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            resetUltimateBoard(frame, buttons, state, miniWinners, miniPanels, status, playerX);
                        }
                        return;
                    }
                    if (isMacroDraw(miniWinners)) {
                        frame.getRootPane().putClientProperty("gameOver", true);
                        status.setText("Ultimate draw game");
                        status.setForeground(Color.ORANGE);
                        int choice = JOptionPane.showConfirmDialog(
                                frame,
                                "🤝 Ultimate draw game!\n\n" + buildRankingsText() + "\nPlay again?",
                                "Game Over",
                                JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            resetUltimateBoard(frame, buttons, state, miniWinners, miniPanels, status, playerX);
                        }
                        return;
                    }

                    int nextMini = cellIdx;
                    if (miniWinners[nextMini] == 'E') {
                        activeMini = nextMini;
                    } else {
                        activeMini = -1;
                    }
                    frame.getRootPane().putClientProperty("activeMini", String.valueOf(activeMini));

                    char next = current == 'X' ? 'O' : 'X';
                    frame.getRootPane().putClientProperty("currentPlayer", String.valueOf(next));
                    String nextName = next == 'X' ? playerX : playerO;
                    if (activeMini == -1) {
                        status.setText(nextName + " (" + next + ") turn - Any mini board");
                    } else {
                        status.setText(nextName + " (" + next + ") turn - Mini board " + (activeMini + 1));
                    }
                    status.setForeground(next == 'X' ? new Color(90, 170, 255) : new Color(183, 115, 255));
                });
                buttons[i][j] = b;
                mini.add(b);
            }
            megaPanel.add(mini);
        }

        frame.getRootPane().putClientProperty("activeMini", "-1");
        frame.getRootPane().putClientProperty("currentPlayer", "X");
        frame.getRootPane().putClientProperty("gameOver", false);

        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(28, 22, 64));
        JButton restart = new JButton("Restart Ultimate");
        restart.setFont(FONT_ALICE_BUTTON);
        restart.setBackground(new Color(92, 52, 190));
        restart.setForeground(Color.WHITE);
        restart.addActionListener(
                e -> resetUltimateBoard(frame, buttons, state, miniWinners, miniPanels, status, playerX));
        bottom.add(restart);

        frame.add(top, BorderLayout.NORTH);
        frame.add(megaPanel, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);
        frame.getContentPane().setBackground(new Color(28, 22, 64));
        frame.setMinimumSize(new Dimension(1050, 920));
        frame.setSize(1120, 980);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static char evaluateMiniBoard(char[] miniCells) {
        int[][] lines = {
                { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 },
                { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
                { 0, 4, 8 }, { 2, 4, 6 }
        };
        for (int[] line : lines) {
            char a = miniCells[line[0]];
            if (a != 'E' && a == miniCells[line[1]] && a == miniCells[line[2]]) {
                return a;
            }
        }
        for (char c : miniCells) {
            if (c == 'E') {
                return 'E';
            }
        }
        return 'D';
    }

    private static char evaluateMacroBoard(char[] miniWinners) {
        int[][] lines = {
                { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 },
                { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
                { 0, 4, 8 }, { 2, 4, 6 }
        };
        for (int[] line : lines) {
            char a = miniWinners[line[0]];
            if ((a == 'X' || a == 'O') && a == miniWinners[line[1]] && a == miniWinners[line[2]]) {
                return a;
            }
        }
        return 'E';
    }

    private static boolean isMacroDraw(char[] miniWinners) {
        for (char c : miniWinners) {
            if (c == 'E') {
                return false;
            }
        }
        return evaluateMacroBoard(miniWinners) == 'E';
    }

    private static void playPlayerWinSound() {
        playTone(784, 150, 0.35); // G5
        playTone(988, 150, 0.35); // B5
        playTone(1175, 260, 0.35); // D6
    }

    private static void playSadWompSound() {
        playTone(330, 220, 0.40);
        playTone(247, 220, 0.40);
        playTone(196, 320, 0.40);
    }

    private static void playWhooshSound() {
        playTone(420, 55, 0.25);
        playTone(560, 55, 0.25);
        playTone(760, 70, 0.25);
    }

    private static void playTone(int hz, int ms, double volume) {
        final float sampleRate = 44100f;
        final int numSamples = (int) ((ms / 1000.0) * sampleRate);
        byte[] buffer = new byte[numSamples * 2];

        for (int i = 0; i < numSamples; i++) {
            double angle = 2.0 * Math.PI * i * hz / sampleRate;
            short sample = (short) (Math.sin(angle) * Short.MAX_VALUE * volume);
            buffer[2 * i] = (byte) (sample & 0xFF);
            buffer[2 * i + 1] = (byte) ((sample >> 8) & 0xFF);
        }

        AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
        try (SourceDataLine line = AudioSystem.getSourceDataLine(format)) {
            line.open(format);
            line.start();
            line.write(buffer, 0, buffer.length);
            line.drain();
            line.stop();
        } catch (LineUnavailableException ex) {
            // Keep gameplay running even if audio output is unavailable.
        }
    }

    private static void resetClassicBoard(JButton[][] cells, char[][] state, JLabel status, String playerX) {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                state[r][c] = 'E';
                cells[r][c].setText(" ");
                cells[r][c].setForeground(Color.WHITE);
            }
        }
        status.setText(playerX + " (X) turn");
        status.setForeground(new Color(90, 170, 255));
    }

    private static void resetUltimateBoard(
            JFrame frame,
            JButton[][] buttons,
            char[][] state,
            char[] miniWinners,
            JPanel[] miniPanels,
            JLabel status,
            String playerX) {
        for (int i = 0; i < 9; i++) {
            miniWinners[i] = 'E';
            miniPanels[i].setBorder(BorderFactory.createLineBorder(new Color(95, 96, 201), 2));
            for (int j = 0; j < 9; j++) {
                state[i][j] = 'E';
                buttons[i][j].setText(" ");
                buttons[i][j].setForeground(Color.WHITE);
            }
        }
        frame.getRootPane().putClientProperty("activeMini", "-1");
        frame.getRootPane().putClientProperty("currentPlayer", "X");
        frame.getRootPane().putClientProperty("gameOver", false);
        status.setText(playerX + " (X) turn - Any mini board");
        status.setForeground(new Color(90, 170, 255));
    }

    private static void ensurePlayerExists(String name) {
        Map<String, Integer> scores = loadRankings();
        if (!scores.containsKey(name)) {
            scores.put(name, 0);
            saveRankings(scores);
        }
    }

    private static void recordWin(String winnerName) {
        Map<String, Integer> scores = loadRankings();
        scores.put(winnerName, scores.getOrDefault(winnerName, 0) + 1);
        saveRankings(scores);
    }

    private static Map<String, Integer> loadRankings() {
        Map<String, Integer> scores = new LinkedHashMap<>();
        try {
            File file = new File(RANKINGS_FILE);
            if (!file.exists()) {
                return scores;
            }
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty() || !line.contains(",")) {
                    continue;
                }
                String[] parts = line.split(",", 2);
                String name = parts[0].trim();
                int wins = Integer.parseInt(parts[1].trim());
                scores.put(name, wins);
            }
            scanner.close();
        } catch (Exception ex) {
            // Ignore malformed file rows and continue gameplay.
        }
        return scores;
    }

    private static void saveRankings(Map<String, Integer> scores) {
        try {
            FileWriter writer = new FileWriter(RANKINGS_FILE);
            for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + System.lineSeparator());
            }
            writer.close();
        } catch (Exception ex) {
            // Ignore write failures to avoid interrupting gameplay.
        }
    }

    private static String buildRankingsText() {
        Map<String, Integer> scores = loadRankings();
        List<Map.Entry<String, Integer>> rankingList = new ArrayList<>(scores.entrySet());
        Collections.sort(rankingList, Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed()
                .thenComparing(Map.Entry::getKey, String.CASE_INSENSITIVE_ORDER));

        StringBuilder sb = new StringBuilder("Rankings:\n");
        int rank = 1;
        for (Map.Entry<String, Integer> entry : rankingList) {
            sb.append(rank).append(". ").append(entry.getKey()).append(" - ").append(entry.getValue()).append(" wins\n");
            rank++;
        }
        if (rankingList.isEmpty()) {
            sb.append("No players yet.");
        }
        return sb.toString();
    }

    private static void printRankingsToConsole() {
        System.out.println();
        System.out.println(buildRankingsText());
    }
}