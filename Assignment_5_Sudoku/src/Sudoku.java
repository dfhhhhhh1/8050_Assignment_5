import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Sudoku {
    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private int[][] board;

    private JTextField[][] fields;
    private JFrame frame;

    public Sudoku() {
        board = new int[SIZE][SIZE];

        fields = new JTextField[SIZE][SIZE];
        frame = new JFrame("Sudoku Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the GUI
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Outer border
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JPanel subgridPanel = new JPanel(new GridLayout(3, 3));
                subgridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Sub-grid border
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        int indexRow = i * 3 + row;
                        int indexCol = j * 3 + col;
                        fields[indexRow][indexCol] = new JTextField(2);
                        fields[indexRow][indexCol].setFont(new Font("Arial", Font.BOLD, 24));
                        fields[indexRow][indexCol].setHorizontalAlignment(JTextField.CENTER);
                        fields[indexRow][indexCol].setBackground(Color.LIGHT_GRAY); // Set background color
                        subgridPanel.add(fields[indexRow][indexCol]);
                    }
                }
                boardPanel.add(subgridPanel);
            }
        }

        frame.add(boardPanel, BorderLayout.CENTER);

        loadBoard("sudoku.txt");

        // Solve button
        JButton solveButton = new JButton("Solve");
        solveButton.setFont(new Font("Arial", Font.BOLD, 16));
        solveButton.addActionListener(e -> {
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    if (solveSodoku(0, 0)) {
                        System.out.println("Sudoku Solved!");
                    } else {
                        System.out.println("No solution found.");
                    }
                    return null;
                }
            };
            worker.execute();
        });
        frame.add(solveButton, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    public void loadBoard(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("\\s+");
                for (int col = 0; col < SIZE; col++) {
                    board[row][col] = Integer.parseInt(values[col]);
                    if (board[row][col] == 0) {
                        fields[row][col].setText("");
                    } else {
                        fields[row][col].setText(String.valueOf(board[row][col]));
                        fields[row][col].setEditable(false); // Make pre-filled cells non-editable
                        fields[row][col].setBackground(Color.WHITE);
                    }
                }
                row++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public boolean solveSodoku(int row, int col) {
        if (row == SIZE) {
            printBoard(); // Print the final solution
            return true;
        }
        if (col == SIZE) {
            return solveSodoku(row + 1, 0);
        }
        if (board[row][col] != 0) {
            return solveSodoku(row, col + 1);
        }
        for (int num = 1; num <= SIZE; num++) {
            if (isSafe(row, col, num)) {
                board[row][col] = num;
                fields[row][col].setText(String.valueOf(num));
                printBoard(); // Print the board after placing a number
                if (solveSodoku(row, col + 1)) {
                    return true;
                }
                board[row][col] = 0;
                fields[row][col].setText("");
                printBoard(); // Print the board after backtracking
            }
        }
        return false;
    }

    private boolean isSafe(int row, int col, int num) {
        // Check row
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num) {
                return false;
            }
        }
        // Check column
        for (int i = 0; i < SIZE; i++) {
            if (board[i][col] == num) {
                return false;
            }
        }
        // Check sub-grid
        int subGridRowStart = (row / 3) * 3;
        int subGridColStart = (col / 3) * 3;
        for (int i = 0; i < SUBGRID_SIZE; i++) {
            for (int j = 0; j < SUBGRID_SIZE; j++) {
                if (board[subGridRowStart + i][subGridColStart + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public void printBoard() {
        System.out.println(" -----------------------");
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (col == 0) {
                    System.out.print("| ");
                }
                System.out.print((board[row][col] == 0 ? "." : board[row][col]) + " ");
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
        SwingUtilities.invokeLater(Sudoku::new);
    }
}
