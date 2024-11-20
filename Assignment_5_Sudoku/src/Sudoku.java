import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//GUI implementation piece
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Sudoku {
    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private int[][] board;

    private JTextField[][] fields;
    private JFrame frame;

    private int stepCounter = 0;

    public Sudoku() {
        board = new int[SIZE][SIZE];

        fields = new JTextField[SIZE][SIZE];
        frame = new JFrame("Sudoku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create GUI format
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JPanel subgridPanel = new JPanel(new GridLayout(3, 3));
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        int indexRow = i * 3 + row;
                        int indexCol = j * 3 + col;
                        fields[indexRow][indexCol] = new JTextField(2);
                        fields[indexRow][indexCol].setFont(new Font("Arial", Font.BOLD, 24));

                        fields[indexRow][indexCol].setHorizontalAlignment(JTextField.CENTER);
                        subgridPanel.add(fields[indexRow][indexCol]);
                    }
                }
                boardPanel.add(subgridPanel);
            }
        }

        frame.add(boardPanel, BorderLayout.CENTER);

        loadBoard("sudoku.txt");

        //Add button, Swingworker is being used for updates
        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        solve();
                        printBoard();
                        return null;
                    }
                };
                worker.execute();
            }
        });
        frame.add(solveButton, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);

    }

    //Load from the file sudoku.txt
    public void loadBoard(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;
            while ((line = reader.readLine())!= null) {
                String[] values = line.split("\\s+");
                for (int col = 0; col < SIZE; col++) {
                    board[row][col] = Integer.parseInt(values[col]);
                    if(board[row][col] == 0)
                    {
                        fields[row][col].setText("");
                    }
                    else {
                        fields[row][col].setText(String.valueOf(board[row][col]));
                    }
                }
                row++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public void solve () {
        if(solveSodoku(0,0)) {
            System.out.println("Sodoku Solved");
            printBoard();
        }
        else {
            System.out.println("Sodoku Unsolved, no solution found.");
        }
    }


    public boolean solveSodoku (int row, int col) {
        //System.out.println("Running solveSodoku row: " + row + ", col: " + col);

        if(row == SIZE) {
            return true;

        }
        if(col == SIZE)
        {
            return solveSodoku(row+1, 0);
        }
        if(board[row][col] != 0) {
            return solveSodoku(row, col + 1);
        }
        for(int num=1; num <= SIZE; num++) {
            if(isSafe(row, col, num)) {
                board[row][col] = num;
                fields[row][col].setText(String.valueOf(board[row][col]));

                //System.out.println("Trying row: " + row + ", col: " + col + ", num: " + num);
                //printBoard();

                stepCounter++;
                if (stepCounter % 100 == 0) {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }


                if (solveSodoku(row, col + 1)) {
                    stepCounter = 0; //Reset stepCounter to 0

                    return true;
                }
                board[row][col] = 0;
                fields[row][col].setText("");

                //System.out.println("Backtracking... row: " + row + ", col: " + col + ", num: " + num);
            }
        }
        return false;
    }

    private boolean isSafe (int row, int col, int num) {
        //Check the row for dupes
        for(int i = 0; i < SIZE; i++) {
            if(board[row][i] == num) {
                return false;
            }
        }

        //Check the column of tile
        for(int i = 0; i < SIZE; i++) {
            if(board[i][col] == num) {
                return false;
            }
        }

        //Check sub grid not done
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //

        return true;
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
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }


    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku();
        //sudoku.loadBoard("sudoku.txt");
        //sudoku.solve();
        //sudoku.printBoard();
    }
}