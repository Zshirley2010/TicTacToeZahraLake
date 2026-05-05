package tictactoe;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Board {
    // holds game play data in an instance variable
    private char[][] grid;

    // holds game play data in a CSV file
    private String filename;

    // non-default constructor - [5 points]
    public Board(String filename) {
        this.filename = filename;
        if (Board.isValidBoardFile(filename)) {
            grid = new char[3][3];
            loadBoardFromFile();
        }
    }

    // loads the grid with the file contents - [5 points]
    public void loadBoardFromFile() {
        if (grid == null) {
            return;
        }
        try {
            File file = new File("src/tictactoe/" + this.filename);
            Scanner scanner = new Scanner(file);
            int row = 0;
            while (scanner.hasNextLine() && row < 3) {
                String line = scanner.nextLine().trim();
                String[] parts = line.split(",");
                if (parts.length != 3) {
                    scanner.close();
                    return;
                }
                for (int col = 0; col < 3; col++) {
                    grid[row][col] = parts[col].trim().charAt(0);
                }
                row++;
            }
            scanner.close();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    /**
     * Static validation used by the constructor before {@code this.filename} is relied on
     * for instance state.
     */
    public static boolean isValidBoardFile(String filename) {
        try {
            File file = new File("src/tictactoe/" + filename);
            Scanner scanner = new Scanner(file);
            int xCount = 0;
            int oCount = 0;
            int lines = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                lines++;
                if (!line.matches("[EXO], [EXO], [EXO]")) {
                    scanner.close();
                    return false;
                }
                if (line.charAt(0) == 'X') {
                    xCount++;
                }
                if (line.charAt(4) == 'X') {
                    xCount++;
                }
                if (line.charAt(8) == 'X') {
                    xCount++;
                }
                if (line.charAt(0) == 'O') {
                    oCount++;
                }
                if (line.charAt(4) == 'O') {
                    oCount++;
                }
                if (line.charAt(8) == 'O') {
                    oCount++;
                }
            }
            scanner.close();
            if (lines != 3) {
                return false;
            }
            return xCount == oCount || xCount == oCount + 1;
        } catch (Exception error) {
            error.printStackTrace();
            return false;
        }
    }

    // valid if it resembles a 3x3 board that contains only E, X, O
    public boolean isValidBoardFile() {
        return isValidBoardFile(this.filename);
    }

    // saves the grid to the file in the proper format (CSV)
    public void saveBoardToFile() {
        if (grid == null) {
            return;
        }
        try {
            File file = new File("src/tictactoe/" + this.filename);
            FileWriter writer = new FileWriter(file);
            StringBuilder boardContents = new StringBuilder();
            for (int row = 0; row < grid.length; row++) {
                boardContents.append(grid[row][0]).append(", ").append(grid[row][1]).append(", ").append(grid[row][2]);
                if (row < 2) {
                    boardContents.append("\n");
                }
            }
            writer.write(boardContents.toString());
            writer.close();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    /*** These are the methods used to test those above ***/
    // prints the current grid
    public void printGrid() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                System.out.print(grid[row][col]);
            }
        }
    }

    // create a random board
    public void createRandomBoard() {
        char[] options = { 'E', 'X', 'O' };
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                int index = (int) (Math.random() * options.length);
                grid[row][col] = options[index];
            }
        }
        this.saveBoardToFile();
    }

    // clears the grid by placing E in every cell
    public void clearBoard() {
        char[][] clearBoard = { { 'E', 'E', 'E' }, { 'E', 'E', 'E' }, { 'E', 'E', 'E' } };
        this.grid = clearBoard;
        this.saveBoardToFile();
    }

    public char getCell(int row, int col) {
        return grid[row][col];
    }

    public void setCell(int row, int col, char value) {
        this.grid[row][col] = value;
        this.saveBoardToFile();
    }

    public char[][] getGrid() {
        return grid;
    }

    public void setGrid(char[][] grid) {
        this.grid = grid;
        this.saveBoardToFile();
    }

    public static void main(String args[]) {
        Board b = new Board("board.csv");
        System.out.println(b.isValidBoardFile());
        b.createRandomBoard();
        b.printGrid();
        b.saveBoardToFile();
        b.loadBoardFromFile();
        System.out.println();
        b.printGrid();
    }
}
