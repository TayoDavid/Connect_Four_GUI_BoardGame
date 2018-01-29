package boardGame;

import javafx.fxml.FXMLLoader;

public class Board {

    GameBoardController gameBoardController;
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gameBoard.fxml"));

    int[][] board = new int[6][7];

    public Board() {
        board = new int[][]{
                {0, 0, 0, 0, 0, 0, 0,},
                {0, 0, 0, 0, 0, 0, 0,},
                {0, 0, 0, 0, 0, 0, 0,},
                {0, 0, 0, 0, 0, 0, 0,},
                {0, 0, 0, 0, 0, 0, 0,},
                {0, 0, 0, 0, 0, 0, 0,},
        };
    }

    // This checks the top row.
    public boolean isLegalMove(int column) {
        return board[0][column] == 0;
    }

    public void undoMove(int column) {
        for (int i = 0; i <= 5; ++i) {
            if (board[i][column] != 0) {
                board[i][column] = 0;
                break;
            }
        }
    }

    //Printing the board
    public void displayBoard() {
        System.out.println();
        for (int i = 0; i <= 5; ++i) {
            for (int j = 0; j <= 6; ++j) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

