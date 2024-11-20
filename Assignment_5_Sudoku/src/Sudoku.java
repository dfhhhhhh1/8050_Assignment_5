import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Sudoku {
    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private int[][] board;

    public Sudoku() {
        board = new int[SIZE][SIZE];
    }

    public void loadBoard(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;
            while ((line = reader.readLine())!= null) {
                String[] values = line.split("\\s+");
                for (int col = 0; col < SIZE; col++) {
                    board[row][col] = Integer.parseInt(values[col]);
                }
                row++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public void printBoard() {
        System.out.println(" -----------------------");
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (col == 0) {
                    System.out.print("| ");
                }
                System.out.print(board[row][col] + " ");
                if ((col + 1) % SUBGRID_SIZE == 0) {
                    System.out.print("| ");
                }
            }
            System.out.println();
            if ((row + 1) % SUBGRID_SIZE == 0) {
                System.out.println(" -----------------------");

            }
        }
    }

    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku();
        sudoku.loadBoard("sudoku.txt");
        sudoku.printBoard();
    }
}