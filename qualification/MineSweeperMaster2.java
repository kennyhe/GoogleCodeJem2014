/**
 * @author Kenny He (he.scu2013@gmail.com, she@scu.edu)
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MineSweeperMaster2 {

    final static char COVERED = '*';
    final static char UNCOVERED = '.';
    final static char FIRSTCLICK = 'c';
    
    /* The rows, columns, and count of mines passed in */
    int rows, cols, mines;
    int cells;  // cells = rows * cols
    char[][] board = null;
    int countCovered;

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please add the file name as the first parameter");
        }
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(args[0]));
            int cases = Integer.parseInt(in.readLine());
            
            int r, c, m;
            char[][] board = null;
            MineSweeperMaster2 game = new MineSweeperMaster2();
            
            for (int caseNo = 1; caseNo <= cases; caseNo++) {
                // get test case data
                String[] vars = in.readLine().split("\\s");
                r = Integer.parseInt(vars[0]);
                c = Integer.parseInt(vars[1]);
                m = Integer.parseInt(vars[2]);
                int remains = r * c - m;
                
                System.out.format("Case #%d:\n", caseNo);

                if (m == 0) {
                    printFull(r, c, '.');
                    continue;
                } else if (m == r * c - 1) {
                    printFull(r, c, '*');
                    continue;
                } else if (r < 3 && c < 3) {
                    System.out.println("Impossible");
                    continue;
                } else if (r == 1) {
                    printSingleRow(c, m);
                    continue;
                } else if (c == 1) {
                    printSingleCol(r, m);
                    continue;
                } else if ((board = rowFill(r, c, m)) != null) {
                    printResults(board, r, c);
                    continue;
                } else if (r != c && (board = colFill(r, c, m)) != null) {
                    printResults(board, r, c);
                    continue;
                } else if ((board = rcFill(r, c, m)) != null) {
                    printResults(board, r, c);
                    continue;
                } else if ((board = rcFill2(r, c, m)) != null) {
                    printResults(board, r, c);
                    continue;
                } else if (remains == 2 || remains == 3 || remains == 5 || remains == 7) {
                    System.out.println("Impossible");
                    continue;
                } else if ((board = fillBy9Cells(r, c, m)) != null) {
                    // Need to implement fillBy9Cell
                    printResults(board, r, c);
                    continue;
                } else {
                    System.out.println("Need to check");
                    // ToDo: Try to work out a dynamic solution.
                    // brute force
                    // game.getBruteForceSolution(r, c, m);
                }
            }
            
            in.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    
    private static char[][] fillBy9Cells(int r, int c, int m) {
        if (m % 9 != 0)
            return null;
        
        int remains = r * c - m;
        int cubes = m / 9;
        
        return null;
    }

    

    /**
     * Fill the mines by rows, from top to down, row by row, and line by line. 
     * If can solve with one click, then return a board. Else return null.
     * @param r
     * @param c
     * @param m
     * @return the board if can fill.
     */
    private static char[][] rowFill(int r, int c, int m) {
        int rs = m / c;
        int cs = m % c;
        if ((rs == r - 2 && cs == 0) || (rs < r - 2 && cs < c - 1)) {
            char[][] board = initBoard(r, c, COVERED);
            for (int j = cs; j < c; j++) {
                board[rs][j] = UNCOVERED;
            }

            for (int i = rs + 1; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    board[i][j] = UNCOVERED;
                }
            }

            board[r-1][c-1] = FIRSTCLICK;
            return board;
        } else if (rs >= r - 2) {
            int remain = r * c - m;
            if (remain >= 4 && remain % 2 == 0) {
                char[][] board = initBoard(r, c, COVERED);
                for (int i = r -2; i < r; i++) {
                    for (int j = c - remain/2; j < c; j++) {
                        board[i][j] = UNCOVERED;
                    }
                }
                board[r-1][c-1] = FIRSTCLICK;
                return board;
            }
        }
        
        return null;
    }

    
    

    /**
     * Fill the mines by columns, from left to right, column by column. 
     * If can solve with one click, then return a board. Else return null.
     * @param r
     * @param c
     * @param m
     * @return the board if can fill.
     */
    private static char[][] colFill(int r, int c, int m) {
        char[][] board = rowFill(c, r, m);
        if (board != null) {
            // generate a transposed board and return
            char[][] board1 = new char[r][c];
            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    board1[i][j] = board[j][i];
                }
            }
            return board1;
        }
        return null;
    }

    
    /**
     * Fill the rows and column with at the same time and make the mines to form a matrix
     * @param r
     * @param c
     * @param m
     * @return the board if can fill, else null
     */
    private static char[][] rcFill(int r, int c, int m) {
        for (int rs = 1; rs <= r - 2; rs++) {
            for (int cs = 1; cs <= c -2; cs++) {
                if (rs * cs == m) { // can form
                    char[][] board = initBoard(r, c, COVERED);
                    for (int i = 0; i < rs; i++) {
                        for (int j = 0; j < cs; j++) {
                            board[i][j] = UNCOVERED;
                        }
                    }
                    board[r-1][c-1] = FIRSTCLICK;
                    return board;
                }
            }
        }
        return null;
    }

    
    private static char[][] rcFill2(int r, int c, int m) {
        int remains = r * c - m;
        for (int rs = 1; rs <= r - 2; rs++) {
            for (int cs = 1; cs <= c -2; cs++) {
                if (rs * cs == remains) { // can form
                    char[][] board = initBoard(r, c, UNCOVERED);
                    for (int i = 0; i < rs; i++) {
                        for (int j = 0; j < cs; j++) {
                            board[i][j] = COVERED;
                        }
                    }
                    board[0][0] = FIRSTCLICK;
                    return board;
                }
            }
        }
        return null;
    }

    
    /**
     * Create a board and fill all its cells with a specific character.
     * @param rows
     * @param cols
     * @param ch The char to be filled into the board
     * @return The board
     */
    private static char[][] initBoard(int rows, int cols, char ch) {
        char[][] board = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = ch;
            }
        }
        return board;
    }
    
    

    /**
     * Brute Force solution
     * @param R
     * @param C
     * @param m
     */
    public void getBruteForceSolution(int R, int C, int m) {
        this.cells = R * C;
        this.mines = m;
        this.rows = R;
        this.cols = C;
        
        // Initialize the board
        this.board = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = '.';
            }
        }
        
        // Run brute force algorithm, to create the board, and try to "click and open"
        if (! fillBoard(m - 1, -1)) {
            System.out.println("Impossible");
        }
    }
    
    
    /**
     * Recursively fill the mines into the board, and test. Once found a solution, return true.
     * @param remains The remained mines after this iteration
     * @param prevPosition The last mine placed.
     * @return If got a solution, return true. Else false.
     */
    private boolean fillBoard(int remains, int prevPosition) {
        for (int i = prevPosition + 1; i < cells - remains; i++) {
            int r = i / cols;
            int c = i - r * cols;
            
            // try to place the mine into this cell
            board[r][c] = '*';
            
            // If all the mines have been placed into the board, do a test.
            if (remains == 0) {
                // Check the board
                if (canSolve()) {
                    // If problem solved, then print the board and return true.
                    printResults(board, rows, cols);
                    return true;
                }
            } else {
                // If there are mines to be placed, recursively call this function to place the mines, and test
                if (fillBoard(remains - 1, i)) {
                    return true;
                }
            }
            
            // Restore the cell
            board[r][c] = '.';
        }
        return false;
    }
    
    /**
     * Check whether the current filled board can be solved. If yes, print the board and the first click.
     * @return true if solvable. Else false.
     */
    private boolean canSolve() {
        int[][] cboard = new int[rows][cols];
        
        // Update the mine counts.
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == '*') {
                    // Update the count in the adjacent 8 cells
                    if (i > 0) {
                        if (j > 0) {
                            cboard[i-1][j-1]++;
                        }
                        cboard[i-1][j]++;
                        if (j < cols - 1) {
                            cboard[i-1][j+1]++;
                        }
                    }
                    if (i < rows - 1) {
                        if (j > 0) {
                            cboard[i+1][j-1]++;
                        }
                        cboard[i+1][j]++;
                        if (j < cols - 1) {
                            cboard[i+1][j+1]++;
                        }
                    }
                    if (j > 0) {
                        cboard[i][j-1]++;
                    }
                    if (j < cols - 1) {
                        cboard[i][j+1]++;
                    }
                }
            }
        }

        // Find an 0 cell, keep their coordinations
        int zeroX = -1, zeroY = -1;
        for (int i = 0; i < rows; i++) {
            if (zeroX > 0)
                break;
            
            for (int j = 0; j < cols; j++) {
                if (cboard[i][j] == 0) {
                    zeroX = i; zeroY = j;
                    break;
                }
            }
        }
        
        // Couldn't found an "0" cell
        if (zeroX == -1)
            return false;
        
        // Uncover the numbers of "0" cell and the 8 cells around it.
        uncover(zeroX, zeroY, cboard);
        
        // count the uncovered cells
        int unCoveredCount = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (cboard[i][j] == UNCOVERED) {
                    unCoveredCount++;
                }
            }
        }
        
        // If all cells without mines have been uncovered, the problem solved! Bingo!
        if (unCoveredCount == cells - mines) {
            board[zeroX][zeroY] = 'c';
            return true;
        }
        
        return false;
    }
    
    
    
    
    /**
     * Uncover the "0" cells and their neighbors
     * @param row the row index
     * @param column the column index
     * @param numboard the number board
     */
    private void uncover(int row, int column, int[][] numboard) {
        // Check whether current cell is "0" cell
        boolean isZeroCell = (numboard[row][column] == 0);
        
        // Mark the cell as uncovered
        numboard[row][column] = UNCOVERED;
        if (isZeroCell) {
            // Uncover the neighbors of a "0" cell
            if (row > 0) {
                if (column > 0) {
                    uncover(row-1, column-1, numboard);
                }
                uncover(row-1, column, numboard);
                if (column < cols - 1) {
                    uncover(row-1, column+1, numboard);
                }
            }
            if (row < rows - 1) {
                if (column > 0) {
                    uncover(row+1, column-1, numboard);
                }
                uncover(row+1, column, numboard);
                if (column < cols - 1) {
                    uncover(row+1, column+1, numboard);
                }
            }
            if (column > 0) {
                uncover(row, column-1, numboard);
            }
            if (column < cols - 1) {
                uncover(row, column+1, numboard);
            }
        }
    }

    
    /**
     * Print a board with all empty or all mines except one not filled
     * @param rows the count of rows of the game board
     * @param columns the count of the columns of the game board
     * @param ch '*' to print all filled mines, or '.' to print an empty board
     */
    public static void printFull(int rows, int columns, char ch) {
        System.out.print('c');
        for (int j = 1; j < columns; j++) {
            System.out.print(ch);
        }
        System.out.println();
        for (int i = 1; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(ch);
            }
            System.out.println();
        }
    }


    /**
     * Print a gaming board with single row
     * @param cols The count of columns of the gaming board
     * @param mines The count of mines
     */
    public static void printSingleRow(int cols, int mines) {
        System.out.print('c');
        int separator = cols - mines;
        for (int j = 1; j < separator; j++) {
            System.out.print('.');
        }
        for (int j = separator; j < cols; j++) {
            System.out.print('*');
        }
        System.out.println();
    }


    /**
     * Print a gaming board with single column
     * @param rows The count of rows of the gaming board
     * @param mines The count of mines
     */
    public static void printSingleCol(int rows, int mines) {
        System.out.println('c');
        int separator = rows - mines;
        for (int j = 1; j < separator; j++) {
            System.out.println('.');
        }
        for (int j = separator; j < rows; j++) {
            System.out.println('*');
        }
    }
    
    /**
     * Print the gaming board
     * @param board The gaming board array
     * @param rows The count of rows of the gaming board 
     * @param cols The count of columns of the gaming board
     */
    public static void printResults(char[][] board, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }
    
}
