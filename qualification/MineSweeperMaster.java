/**
 * @author Kenny He (he.scu2013@gmail.com, she@scu.edu)
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MineSweeperMaster {

    final static char COVERED = '*';
    final static char UNCOVERED = '.';
    final static char FIRSTCLICK = 'c';
    

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
            
            for (int caseNo = 1; caseNo <= cases; caseNo++) {
                // get test case data
                String[] vars = in.readLine().split("\\s");
                r = Integer.parseInt(vars[0]);
                c = Integer.parseInt(vars[1]);
                m = Integer.parseInt(vars[2]);
                int remains = r * c - m;
                
                System.out.format("Case #%d:\n", caseNo);
                
                char[][] board = null;

                if (m == 0) {
                    board = initBoard(r, c, UNCOVERED);
                    board[0][0] = FIRSTCLICK;
                } else if (remains == 1) {
                    board = initBoard(r, c, UNCOVERED);
                    board[0][0] = FIRSTCLICK;
                } else if (r == 1) {
                    board = uncoverByMatrix(1, c, 1, remains);
                } else if (c == 1) {
                    board = uncoverByMatrix(r, 1, remains, 1);
                } else if (remains == 2 || remains == 3 || remains == 5 || remains == 7) {
                    // impossible
                } else if (r == 2) {
                    if (m % 2 == 1) {
                        // impossible
                    } else {
                        board = uncoverByMatrix(2, c, 2, remains / 2);
                    }
                } else if (c == 2) {
                    if (m % 2 == 1) {
                        // impossible
                    } else {
                        board = uncoverByMatrix(r, 2, remains / 2, 2);
                    }
/*                } else if (remains == 4 || remains == 6 || remains == 8 || remains == 9) {
                    int rows = 2, cols = remains / 2;
                    if (remains == 9) {
                        rows = 3; cols = 3;
                    }
                    board = uncoverByMatrix(r, c, rows, cols);*/
                } else {
                    board = rowFill(r, c, m, remains);
                }
                if (board == null) {
                    System.out.println("Impossible");
                } else {
                    printResults(board, r, c);
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

    
    /**
     * Fill the mines by rows, from top to down, row by row, and line by line. 
     * If can solve with one click, then return a board. Else return null.
     * Since those cases of cannot fill have been listed above, definitely we can fill.
     * @param r
     * @param c
     * @param m
     * @param remains
     * @return the board
     */
    private static char[][] rowFill(int r, int c, int m, int remains) {
        int rs = m / c;
        int cs = m % c;
        char[][] board = null;
        if (rs < r - 2 && cs < c - 1) {
            board = uncoverByLineFill(r, c, rs, cs);
        } else if (rs < r - 3 && cs == c - 1) {
            board = uncoverByLineFill(r, c, rs, cs);
            board[rs + 1][0] = COVERED;
            board[rs][cs - 1] = UNCOVERED;
        } else if (rs == r - 3 && cs == c - 1) {
            board = uncoverByLineFill(r, c, rs, cs);
            board[r - 2][0] = COVERED;
            board[r - 1][0] = COVERED;
            board[rs][cs - 1] = UNCOVERED;
            board[rs][cs - 2] = UNCOVERED;
        } else { // (rs >= r - 2)
            if (remains % 2 == 0) {
                board = uncoverByMatrix(r, c, 2, remains / 2);
            } else {
                board = uncoverByMatrix(r, c, 2, (remains - 3) / 2);
                board[2][0] = UNCOVERED;
                board[2][1] = UNCOVERED;
                board[2][2] = UNCOVERED;
            }
        }
        
        return board;
    }

    
    /**
     * Create a rows * cols board and fill all its cells with a specific character.
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
     * Generate a rows * cols gaming board, make m * n columns uncovered
     * @param rows The count of rows of the gaming board
     * @param cols The count of columns of the gaming board
     * @param m The count of rows uncovered
     * @param n The count of columns uncovered
     * @return The board
     */
    public static char[][] uncoverByMatrix(int rows, int cols, int m, int n) {
        char[][]board = initBoard(rows, cols, COVERED);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = UNCOVERED;
            }
        }
        board[0][0] = FIRSTCLICK;
        return board;
    }
    

    /**
     * Generate a rows * cols gaming board, cover the first m rows, and the first n cols of (m+1)th row
     * @param rows The count of rows of the gaming board
     * @param cols The count of columns of the gaming board
     * @param m The count of rows covered
     * @param n The count of columns covered in next row
     * @return The board
     */
    public static char[][] uncoverByLineFill(int rows, int cols, int m, int n) {
        char[][]board = initBoard(rows, cols, COVERED);
        for (int j = n; j < cols; j++) {
            board[m][j] = UNCOVERED;
        }

        for (int i = m + 1; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = UNCOVERED;
            }
        }

        board[rows-1][cols-1] = FIRSTCLICK;
        return board;
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
