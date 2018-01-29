package boardGame;

import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GameBoardController {
    @FXML
    protected AnchorPane boardPane;
    @FXML
    protected Label player1Name;
    @FXML
    protected JFXButton player1_colour;
    @FXML
    protected Label player2Name;
    @FXML
    protected JFXButton player2_colour;
    @FXML
    protected Label lblComment;
    @FXML
    protected JFXButton btnHomeScreen;
    @FXML
    private JFXButton btnPlayOrNew;
    @FXML
    private Label currentPlayer;
    @FXML
    private GridPane boardGrid;

    private Board board = new Board();
    private int nextMoveLocation = -1;
    private int maxDepth = 9;
    private int player = 2;

    // This method starts the game.
    @SuppressWarnings("unlikely-arg-type")
	public void play(ActionEvent event) throws IOException {
        ObservableList<Node> buttonsInGrid = boardGrid.getChildren();
        if (btnPlayOrNew.getText() != "NEW") {
            boardPane.setDisable(false);
            if (player2Name.equals("Computer")) humanPlayerMove(0);
            else humanPlayerMove(player);
            btnPlayOrNew.setText("NEW");
        } else {
            board = new Board();
            for (Node node : buttonsInGrid) {
                node.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 25px");
            }
            if (player2Name.equals("Computer")) humanPlayerMove(0);
            else humanPlayerMove(2);
            boardPane.setDisable(true);
            currentPlayer.setText("");
            btnPlayOrNew.setText("PLAY");
        }
    }

    // This method handles the human to human turns
    public void humanVsHuman(int player, int move) {
        if (!board.isLegalMove(move)) {
            lblComment.setText("No empty hole in this column. Try another column!");
            return;
        }

        int gameResult;

        if (player == 2) {
            placeMove(board, move, player);
            setHoleColour();
            gameResult = gameResult(board);
            if (gameResult == 2) {
                setHoleColour();
                currentPlayer.setText(player1Name.getText() + " Wins");
                boardPane.setDisable(true);
                boardPane.setDisable(true);
            }

            player = 1;

        } else {
            placeMove(board, move, player);
            setHoleColour();
            player = 2;
            gameResult = gameResult(board);
            if (gameResult == 1) {
                currentPlayer.setText(player2Name.getText() + " Win");
                boardPane.setDisable(true);
            } else if (gameResult == 0) {
                currentPlayer.setText("Draw!");
                boardPane.setDisable(true);
            }
        }
        if (gameResult != 1 && gameResult != 2)
            humanPlayerMove(player);
    }

    // This method handle the human vs computer turns
    private void humanVsComputer(int move) {
        if (!board.isLegalMove(move)) {
            lblComment.setText("No empty hole in this column. Try another column!");
            return;
        }

        placeMove(board, move, 2);
        int gameResult = gameResult(board);

        if (gameResult == 2) {
            setHoleColour();
            currentPlayer.setText(player1Name.getText() + " Wins");
            boardPane.setDisable(true);
            boardPane.setDisable(true);
        } else {
            placeMove(board, getAIMove(), 1);
            setHoleColour();
            gameResult = gameResult(board);
            if (gameResult == 1) {
                currentPlayer.setText("Computer Win");
                boardPane.setDisable(true);
            } else if (gameResult == 0) {
                currentPlayer.setText("Draw!");
                boardPane.setDisable(true);
            } else humanPlayerMove(0);
        }
    }

    // This method handles the event that should happen when the human players click a column button.
    public void columnButtonClicked(ActionEvent event) {
        if (lblComment.getText() != null) lblComment.setText("");
        String columnButtonPressed = (((Control) event.getSource()).getId());
        String lastChar = "" + columnButtonPressed.charAt(columnButtonPressed.length() - 1);
        int currentPlayerMove = Integer.valueOf(lastChar);

        String player2 = player2Name.getText();
        if (player2.equalsIgnoreCase("Computer")) humanVsComputer(currentPlayerMove);
        else {
            humanVsHuman(player, currentPlayerMove);
        }
    }

    // This method sets the colour
    public void setHoleColour() {
        ObservableList<Node> buttonsInGrid = boardGrid.getChildren();
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                if (board.board[i][j] != 0) {
                    if (board.board[i][j] == 1) {
                        for (Node node : buttonsInGrid) {
                            if (node.getId().equals("btncol" + j + "_" + i)) {
                                node.setStyle("-fx-background-color: " + ChoosePlayerController.getPlayer2Colour() + "; -fx-background-radius: 25px");
                            }
                        }
                    } else {
                        for (Node node : buttonsInGrid) {
                            if (node.getId().equals("btncol" + j + "_" + i)) {
                                node.setStyle("-fx-background-color: " + ChoosePlayerController.getPlayer1Colour() + "; -fx-background-radius: 25px");
                            }
                        }
                    }

                }
            }
        }
    }

    //Opponent's turn
    public void humanPlayerMove(int player) {
        if (player == 0)
            currentPlayer.setText(player1Name.getText() + "'s Turn!");
        else {
            this.player = player;
            if (player == 2)
                currentPlayer.setText(player1Name.getText() + "'s Turn!");
            else
                currentPlayer.setText(player2Name.getText() + "'s Turn!");
        }
    }

    //Game Result
    public int gameResult(Board b) {
        int aiScore = 0, humanScore = 0;
        for (int i = 5; i >= 0; --i) {
            for (int j = 0; j <= 6; ++j) {
                if (b.board[i][j] == 0) continue;

                //Checking cells to the right
                if (j <= 3) {
                    for (int k = 0; k < 4; ++k) {
                        if (b.board[i][j + k] == 1) aiScore++;
                        else if (b.board[i][j + k] == 2) humanScore++;
                        else break;
                    }
                    if (aiScore == 4) return 1;
                    else if (humanScore == 4) return 2;
                    aiScore = 0;
                    humanScore = 0;
                }

                //Checking cells up
                if (i >= 3) {
                    for (int k = 0; k < 4; ++k) {
                        if (b.board[i - k][j] == 1) aiScore++;
                        else if (b.board[i - k][j] == 2) humanScore++;
                        else break;
                    }
                    if (aiScore == 4) return 1;
                    else if (humanScore == 4) return 2;
                    aiScore = 0;
                    humanScore = 0;
                }

                //Checking diagonal up-right
                if (j <= 3 && i >= 3) {
                    for (int k = 0; k < 4; ++k) {
                        if (b.board[i - k][j + k] == 1) aiScore++;
                        else if (b.board[i - k][j + k] == 2) humanScore++;
                        else break;
                    }
                    if (aiScore == 4) return 1;
                    else if (humanScore == 4) return 2;
                    aiScore = 0;
                    humanScore = 0;
                }

                //Checking diagonal up-left
                if (j >= 3 && i >= 3) {
                    for (int k = 0; k < 4; ++k) {
                        if (b.board[i - k][j - k] == 1) aiScore++;
                        else if (b.board[i - k][j - k] == 2) humanScore++;
                        else break;
                    }
                    if (aiScore == 4) return 1;
                    else if (humanScore == 4) return 2;
                    aiScore = 0;
                    humanScore = 0;
                }
            }
        }

        for (int j = 0; j < 7; ++j) {
            //Game has not ended yet
            if (b.board[0][j] == 0) return -1;
        }
        //Game draw!
        return 0;
    }

    int calculateScore(int aiScore, int moreMoves) {
        int moveScore = 4 - moreMoves;
        if (aiScore == 0) return 0;
        else if (aiScore == 1) return 1 * moveScore;
        else if (aiScore == 2) return 10 * moveScore;
        else if (aiScore == 3) return 100 * moveScore;
        else return 1000;
    }

    // Evaluate board favourableness for AI
    public int evaluateBoard(Board b) {

        int aiScore = 1;
        int score = 0;
        int blanks = 0;
        int k = 0, moreMoves = 0;
        for (int i = 5; i >= 0; --i) {
            for (int j = 0; j <= 6; ++j) {

                if (b.board[i][j] == 0 || b.board[i][j] == 2) continue;

                if (j <= 3) {
                    for (k = 1; k < 4; ++k) {
                        if (b.board[i][j + k] == 1) aiScore++;
                        else if (b.board[i][j + k] == 2) {
                            aiScore = 0;
                            blanks = 0;
                            break;
                        } else blanks++;
                    }

                    moreMoves = 0;
                    if (blanks > 0)
                        for (int c = 1; c < 4; ++c) {
                            int column = j + c;
                            for (int m = i; m <= 5; m++) {
                                if (b.board[m][column] == 0) moreMoves++;
                                else break;
                            }
                        }

                    if (moreMoves != 0) score += calculateScore(aiScore, moreMoves);
                    aiScore = 1;
                    blanks = 0;
                }

                if (i >= 3) {
                    for (k = 1; k < 4; ++k) {
                        if (b.board[i - k][j] == 1) aiScore++;
                        else if (b.board[i - k][j] == 2) {
                            aiScore = 0;
                            break;
                        }
                    }
                    moreMoves = 0;

                    if (aiScore > 0) {
                        int column = j;
                        for (int m = (i - k + 1); m <= (i - 1); m++) {
                            if (b.board[m][column] == 0) moreMoves++;
                            else break;
                        }
                    }
                    if (moreMoves != 0) score += calculateScore(aiScore, moreMoves);
                    aiScore = 1;
                    blanks = 0;
                }

                if (j >= 3) {
                    for (k = 1; k < 4; ++k) {
                        if (b.board[i][j - k] == 1) aiScore++;
                        else if (b.board[i][j - k] == 2) {
                            aiScore = 0;
                            blanks = 0;
                            break;
                        } else blanks++;
                    }
                    moreMoves = 0;
                    if (blanks > 0)
                        for (int c = 1; c < 4; ++c) {
                            int column = j - c;
                            for (int m = i; m <= 5; m++) {
                                if (b.board[m][column] == 0) moreMoves++;
                                else break;
                            }
                        }

                    if (moreMoves != 0) score += calculateScore(aiScore, moreMoves);
                    aiScore = 1;
                    blanks = 0;
                }

                if (j <= 3 && i >= 3) {
                    for (k = 1; k < 4; ++k) {
                        if (b.board[i - k][j + k] == 1) aiScore++;
                        else if (b.board[i - k][j + k] == 2) {
                            aiScore = 0;
                            blanks = 0;
                            break;
                        } else blanks++;
                    }
                    moreMoves = 0;
                    if (blanks > 0) {
                        for (int c = 1; c < 4; ++c) {
                            int column = j + c, row = i - c;
                            for (int m = row; m <= 5; ++m) {
                                if (b.board[m][column] == 0) moreMoves++;
                                else if (b.board[m][column] == 1) ;
                                else break;
                            }
                        }
                        if (moreMoves != 0) score += calculateScore(aiScore, moreMoves);
                        aiScore = 1;
                        blanks = 0;
                    }
                }

                if (i >= 3 && j >= 3) {
                    for (k = 1; k < 4; ++k) {
                        if (b.board[i - k][j - k] == 1) aiScore++;
                        else if (b.board[i - k][j - k] == 2) {
                            aiScore = 0;
                            blanks = 0;
                            break;
                        } else blanks++;
                    }
                    moreMoves = 0;
                    if (blanks > 0) {
                        for (int c = 1; c < 4; ++c) {
                            int column = j - c, row = i - c;
                            for (int m = row; m <= 5; ++m) {
                                if (b.board[m][column] == 0) moreMoves++;
                                else if (b.board[m][column] == 1) ;
                                else break;
                            }
                        }
                        if (moreMoves != 0) score += calculateScore(aiScore, moreMoves);
                        aiScore = 1;
                        blanks = 0;
                    }
                }
            }
        }
        return score;
    }

    public int minimax(int depth, int turn, int alpha, int beta) {

        if (beta <= alpha) {
            if (turn == 1) return Integer.MAX_VALUE;
            else return Integer.MIN_VALUE;
        }
        int gameResult = gameResult(board);

        if (gameResult == 1) return Integer.MAX_VALUE / 2;
        else if (gameResult == 2) return Integer.MIN_VALUE / 2;
        else if (gameResult == 0) return 0;

        if (depth == maxDepth) return evaluateBoard(board);

        int maxScore = Integer.MIN_VALUE, minScore = Integer.MAX_VALUE;

        for (int j = 0; j <= 6; ++j) {

            int currentScore = 0;

            if (!board.isLegalMove(j)) continue;

            if (turn == 1) {
                placeMove(board, j, 1);
                currentScore = minimax(depth + 1, 2, alpha, beta);

                if (depth == 0) {
                    if (currentScore > maxScore) nextMoveLocation = j;
                    if (currentScore == Integer.MAX_VALUE / 2) {
                        board.undoMove(j);
                        break;
                    }
                }

                maxScore = Math.max(currentScore, maxScore);

                alpha = Math.max(currentScore, alpha);
            } else if (turn == 2) {
                placeMove(board, j, 2);
                currentScore = minimax(depth + 1, 1, alpha, beta);
                minScore = Math.min(currentScore, minScore);

                beta = Math.min(currentScore, beta);
            }
            board.undoMove(j);
            if (currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE) break;

            currentPlayer.setText("Computer's Turn");
        }
        return turn == 1 ? maxScore : minScore;
    }

    // This method gets the Computer's (AI) player1Move
    public int getAIMove() {
        currentPlayer.setText("Computer's Turn");
        nextMoveLocation = -1;
        minimax(0, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return nextMoveLocation;
    }

    //Placing a Move on the board
    public boolean placeMove(Board board, int column, int player) {
        if (!board.isLegalMove(column)) {
            return false;
        }
        for (int i = 5; i >= 0; --i) {
            if (board.board[i][column] == 0) {
                board.board[i][column] = (byte) player;
                return true;
            }
        }
        return false;
    }

    public void gotoHomeScreen(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage.setTitle("Home");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

        Stage closeBoard = (Stage) btnHomeScreen.getScene().getWindow();
        closeBoard.close();
    }

    public void exit(ActionEvent event) {
        System.exit(-1);
    }
}