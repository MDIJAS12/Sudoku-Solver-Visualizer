import javax.swing.*;
import java.awt.*;

public class SudokuVisualizer extends JFrame {
    private static final int SIZE = 9;
    private JTextField[][] cells = new JTextField[SIZE][SIZE];
    private int[][] board = {
            { 1, 0, 0, 4, 8, 9, 0, 0, 6 },
            { 7, 3, 0, 0, 0, 0, 0, 4, 0 },
            { 0, 0, 0, 0, 0, 1, 2, 9, 5 },
            { 0, 0, 7, 1, 2, 0, 6, 0, 0 },
            { 5, 0, 0, 7, 0, 3, 0, 0, 8 },
            { 0, 0, 6, 0, 9, 5, 7, 0, 0 },
            { 9, 1, 4, 6, 0, 0, 0, 0, 0 },
            { 0, 2, 0, 0, 0, 0, 0, 3, 7 },
            { 8, 0, 0, 5, 1, 2, 0, 0, 4 }
    };

    public SudokuVisualizer() {
        setTitle("Sudoku Solver");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel sudokuPanel = new JPanel();
        sudokuPanel.setLayout(new GridLayout(3, 3, 5, 5));

        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 3; blockCol++) {
                JPanel blockPanel = new JPanel();
                blockPanel.setLayout(new GridLayout(3, 3));
                blockPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                for (int row = blockRow * 3; row < blockRow * 3 + 3; row++) {
                    for (int col = blockCol * 3; col < blockCol * 3 + 3; col++) {
                        cells[row][col] = new JTextField();
                        cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                        if (board[row][col] != 0) {
                            cells[row][col].setText(String.valueOf(board[row][col]));
                            cells[row][col].setEditable(false);
                            cells[row][col].setBackground(Color.LIGHT_GRAY);
                        }
                        blockPanel.add(cells[row][col]);
                    }
                }
                sudokuPanel.add(blockPanel);
            }
        }

        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(e -> solveSudoku());
        add(sudokuPanel, BorderLayout.CENTER);
        add(solveButton, BorderLayout.SOUTH);
    }

    private void solveSudoku() {
        SudokuSolver solver = new SudokuSolver(cells);
        new Thread(() -> {
            if (solver.solveSudoku(board)) {
                JOptionPane.showMessageDialog(this, "Sudoku Solved!");
            } else {
                JOptionPane.showMessageDialog(this, "No solution exists");
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SudokuVisualizer frame = new SudokuVisualizer();
            frame.setVisible(true);
        });
    }
}

class SudokuSolver {
    private static final int SIZE = 9;
    private static final int EMPTY = 0;
    private JTextField[][] cells;

    public SudokuSolver(JTextField[][] cells) {
        this.cells = cells;
    }

    public boolean solveSudoku(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == EMPTY) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            updateUI(row, col, num);
                            if (solveSudoku(board)) {
                                return true;
                            } else {
                                board[row][col] = EMPTY;
                                updateUI(row, col, EMPTY);
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num
                    || board[row - row % 3 + i / 3][col - col % 3 + i % 3] == num) {
                return false;
            }
        }
        return true;
    }

    private void updateUI(int row, int col, int num) {
        SwingUtilities.invokeLater(() -> cells[row][col].setText(num == EMPTY ? "" : String.valueOf(num)));
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
