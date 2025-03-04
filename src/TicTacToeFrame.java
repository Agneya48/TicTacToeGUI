import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

/**
 * @author Josh Hampton hamptojt@mail.uc.edu
 *
 * A simple GUI interface for Tic-Tac-Toe, built in Java Swing
 * Created as part the Tic Tac Toe refactoring assignment for Programming II with Tom Wulf Spring 2025
 */
public class TicTacToeFrame extends JFrame {

    private final int ROWS = 3;
    private final int COLS = 3;

    private int boardWidth = 600;
    private int boardHeight = 700;

    int playerXWins = 0;
    int playerOWins = 0;
    int ties = 0;
    int turns = 0;

    boolean gameOver = false;
    boolean tie = false;

    JOptionPane messageBox = new JOptionPane();

    JLabel headingTextLabel = new JLabel();
    JPanel textPanel = new JPanel();
    //top of window

    JLabel xWinsLabel = new JLabel();
    JLabel oWinsLabel = new JLabel();
    JLabel tiesLabel = new JLabel();
    JPanel scorePanel = new JPanel();
    //bottom of window

    JPanel boardPanel = new JPanel();
    JButton[][] board = new JButton[ROWS][COLS];
    private final String playerX = "X";
    private final String playerO = "O";
    String currentPlayer = "X";

    TicTacToeFrame() {

        createHeading();
        createScorePnl();
        createBoard();

        setTitle("TicTacToe");
        setSize(boardWidth, boardHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Constructs ~50px tall heading area where titles and persistent messages are displayed, including turn order
     */
    private void createHeading() {
        headingTextLabel.setBackground(Color.darkGray);
        headingTextLabel.setForeground(Color.white);
        headingTextLabel.setFont(new Font("Arial", Font.BOLD, 50));
        headingTextLabel.setHorizontalAlignment(JLabel.CENTER);
        headingTextLabel.setText("Tic-Tac-Toe");
        headingTextLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(headingTextLabel);
        add(textPanel, BorderLayout.NORTH);
    }

    /**
     * Score panel that keeps track of Wins for both players and ties
     */
    private void createScorePnl() {
        xWinsLabel.setBackground(Color.darkGray);
        oWinsLabel.setBackground(Color.darkGray);
        tiesLabel.setBackground(Color.darkGray);
        xWinsLabel.setForeground(Color.white);
        oWinsLabel.setForeground(Color.white);
        tiesLabel.setForeground(Color.white);
        xWinsLabel.setFont(new Font("Arial", Font.BOLD, 30));
        oWinsLabel.setFont(new Font("Arial", Font.BOLD, 30));
        tiesLabel.setFont(new Font("Arial", Font.BOLD, 30));
        xWinsLabel.setHorizontalAlignment(JLabel.CENTER);
        oWinsLabel.setHorizontalAlignment(JLabel.CENTER);
        tiesLabel.setHorizontalAlignment(JLabel.CENTER);
        xWinsLabel.setOpaque(true);
        oWinsLabel.setOpaque(true);
        tiesLabel.setOpaque(true);

        xWinsLabel.setText("X Wins: " + playerXWins);
        oWinsLabel.setText("O Wins: " + playerOWins);
        tiesLabel.setText("Ties: " + ties);

        scorePanel.setLayout(new GridLayout(1, 3));
        scorePanel.add(xWinsLabel);
        scorePanel.add(tiesLabel);
        scorePanel.add(oWinsLabel);
        scorePanel.setBorder(BorderFactory.createLineBorder(Color.darkGray, 4));
        add(scorePanel, BorderLayout.SOUTH);

    }

    /**
     * Builds the 3x3 TicTacToe board
     * ActionListeners on buttons trigger game logic
     */
    private void createBoard() {
        boardPanel.setLayout(new GridLayout(ROWS, COLS));
        boardPanel.setBackground(Color.darkGray);
        add(boardPanel);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                JButton tile = new JButton();
                board[row][col] = tile;
                boardPanel.add(tile);

                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Arial", Font.BOLD, 120));
                tile.setFocusable(false);

                tile.addActionListener((ActionEvent ae) -> playerMove(ae));
            }

        }
    }

    /**
     * Determines X and O placement and then checks for Wins and ties, past a certain number of moves
     * Majority of game resolution is ultimately nested in this method
     * @param ae ActionEvent from buttons.
     */
    private void playerMove(ActionEvent ae) {

        JButton tile = (JButton) ae.getSource();
        //check if clicked button is occupied
        if (tile.getText() == "") {
            //empty, so fill the tile
            tile.setText(currentPlayer);
            turns++;
            checkWinOrTie();
                if(!gameOver) {
                    //set player to O if currently X, otherwise remain X
                    currentPlayer = currentPlayer == playerX ? playerO : playerX;
                    headingTextLabel.setText(currentPlayer + "'s turn");
                }
        }
        //if occupied, send a message about an illegal move
        else if (tile.getText() == "X" || tile.getText() == "O") {
            if(gameOver == true || tie == true) {
                gameOverDialogue();
            }
            else {
                messageBox.showMessageDialog(boardPanel, "Square already taken!", "Illegal Move",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    /**
     * Checks for wins by calling methods for row, col, and diagonal wins.
     * Ties are checked by another more complicated function
     */
    private void checkWinOrTie() {
        if (!gameOver) {
            if (turns >= 3) {
                checkRowWin();
                checkColWin();
                checkDiagonalWin();

                if (gameOver) {
                    if (currentPlayer == "X") {
                        playerXWins++;
                        JOptionPane.showMessageDialog(boardPanel, "X wins!", "Victory",
                                JOptionPane.INFORMATION_MESSAGE);
                        xWinsLabel.setText("X Wins: " + playerXWins);
                    } else if (currentPlayer == "O") {
                        playerOWins++;
                        JOptionPane.showMessageDialog(boardPanel, "O wins!", "Victory",
                                JOptionPane.INFORMATION_MESSAGE);
                        oWinsLabel.setText("O Wins: " + playerOWins);
                    }
                    gameOverDialogue();
                    return;
                }

                checkTie();
            }
        }
    }

    private void checkRowWin() {
        for (int r = 0; r < ROWS; r++) {
            if (board[r][0].getText() == currentPlayer && board[r][1].getText() == currentPlayer
                    && board[r][2].getText() == currentPlayer) {

                for (int i = 0; i < COLS; i++) {
                    setWinner(board[r][i]);
                }
                gameOver = true;
                return;
            }
        }
    }

    private void checkColWin() {
        for (int c = 0; c < COLS; c++) {
            if (board[0][c].getText() == currentPlayer && board[1][c].getText() == currentPlayer
                    && board[2][c].getText() == currentPlayer) {

                for (int i = 0; i < ROWS; i++) {
                    setWinner(board[i][c]);
                }
                gameOver = true;
                return;
            }
        }
    }

    private void checkDiagonalWin() {
        if (board[0][0].getText() == currentPlayer && board[1][1].getText() == currentPlayer &&
                board[2][2].getText() == currentPlayer) {
            for (int i = 0; i < 3; i++) {
                setWinner(board[i][i]);
            }
        }
        else if (board[0][2].getText() == currentPlayer && board[1][1].getText() == currentPlayer &&
                board[2][0].getText() == currentPlayer) {
            setWinner(board[0][2]);
            setWinner(board[1][1]);
            setWinner(board[2][0]);
            gameOver = true;
        }
    }

    /**
     * Called by the check row, col, and diagonalWin methods. Adjusts graphical display of
     * buttons to highlight a win. This function is intended to be called multiple times
     * within a loop, as it only paints an individual tile at a time
     * @param tile with row and col indexes
     */
    private void setWinner(JButton tile) {
        if (currentPlayer == "X") {
            tile.setForeground(Color.blue);
        }
        else if (currentPlayer == "O") {
            tile.setForeground(Color.red);
        }
        tile.setBackground(Color.gray);
        headingTextLabel.setText(currentPlayer + " is the winner!");
        gameOver = true;

    }

    /**
     * Highlights tiles to emphasize a tie. Intended to be called on all
     * buttons through a nested for loop
     * @param tile with row and col indexes
     */
    private void setTie(JButton tile) {
        tile.setForeground(Color.orange);
        tile.setBackground(Color.gray);
        headingTextLabel.setText("Tie!");
    }


    /**
     * Checks whether the given list/vector of Jbuttons contains an X
     * @param vector list of buttons that are in a line
     * @return boolean true or false
     */
    private boolean vectorContainsX(JButton[] vector) {
        for (JButton b : vector) {
            if (b.getText() == "X") {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the given list/vector of JButtons contains an O
     * @param vector list of buttons in a line
     * @return boolean true or false
     */
    private boolean vectorContainsO (JButton[] vector) {
        for (JButton b : vector) {
            if (b.getText() == "O") {
                return true;
            }
        }
        return false;
    }

    /**
     * Brute force checks each vector so an earlier tie can be declared
     * If a vector contains both an X and an O, then we know it is no longer a possible solution
     * If all vectors contain both, then a tie is declared
     * Will also default to declaring a tie if turn 9 is reached
     */
    private void checkTie() {

        if (turns >= 9) { //must be a tie if all squares are filled; here as a failsafe
            gameOver = true;
            tie = true;
            ties++;
            //if there is a tie, grey all tiles
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    setTie(board[r][c]);
                }
            }
            tiesLabel.setText("Ties: " + ties);
            tieDialogue();
            return;
        }


        JButton[] vector1 = {board[0][0], board[0][1], board[0][2]};
        JButton[] vector2 = {board[1][0], board[1][1], board[1][2]};
        JButton[] vector3 = {board[2][0], board[2][1], board[2][2]};
        JButton[] vector4 = {board[0][0], board[1][0], board[2][0]};
        JButton[] vector5 = {board[0][1], board[1][1], board[2][1]};
        JButton[] vector6 = {board[0][2], board[1][2], board[2][2]};
        JButton[] vector7 = {board[0][0], board[1][1], board[2][2]};
        JButton[] vector8 = {board[0][2], board[1][1], board[2][0]};


        if ((vectorContainsX(vector1) && vectorContainsO(vector1))
                && (vectorContainsX(vector2) && vectorContainsO(vector2))
                && (vectorContainsX(vector3) && vectorContainsO(vector3))
                && (vectorContainsX(vector4) && vectorContainsO(vector4))
                && (vectorContainsX(vector5) && vectorContainsO(vector5))
                && (vectorContainsX(vector6) && vectorContainsO(vector6))
                && (vectorContainsX(vector7) && vectorContainsO(vector7))
                && (vectorContainsX(vector8) && vectorContainsO(vector8))) {
            gameOver = true;
            tie = true;
            ties++;
            tiesLabel.setText("Ties: " + ties);
            //if there is a tie, grey all tiles
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    setTie(board[r][c]);
                }
            }
            tieDialogue();
            return;
        }
    }

    /**
     * Triggers a JOptionPane asking if the user wants to reset the board and play again
     */
    private void gameOverDialogue() {
        int result = JOptionPane.showConfirmDialog(boardPanel,
                "Game Over. Play again?", "Game Over", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            resetGame();
        }
    }


    /**
     * Clears the board and resets to default, but keeps score
     */
    private void resetGame() {
        turns = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                board[r][c].setText("");
                board[r][c].setBackground(Color.darkGray);
                board[r][c].setForeground(Color.white);
            }
        }
        tie = false;
        gameOver = false;
        headingTextLabel.setText("Tic-Tac-Toe");
    }

    /**
     * JOptionPane dialogue specifically for game overs created by a tie
     */
    private void tieDialogue() {
        int result = JOptionPane.showConfirmDialog(boardPanel, "The game ends in a tie. Play again?",
                "Tie", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            resetGame();
        }
    }
}
