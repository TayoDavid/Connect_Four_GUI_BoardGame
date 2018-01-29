package boardGame;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class ChoosePlayerController {
    GameBoardController gameBoardController;
    private String player1Name;
    private String player2Name;
    private static String player1Colour;
    private static String player2Colour;

    @FXML
    protected Pane player2Pane;
    @FXML
    protected JFXButton btnPlayer1_red;
    @FXML
    protected JFXTextField txtPlayer2;
    @FXML
    protected Label computerColourDisplay;
    @FXML
    private JFXTextField txtPlayer1;
    @FXML
    private JFXButton btnPlayer1_yellow;
    @FXML
    private JFXButton btnPlayer1_green;
    @FXML
    private JFXButton btnPlayer2_red;
    @FXML
    private JFXButton btnPlayer2_yellow;
    @FXML
    private JFXButton btnPlayer2_green;
    @FXML
    private JFXButton btnGoBack;
    @FXML
    private JFXButton gotoGame;
    @FXML
    private Label lblComment;

    public static String getPlayer1Colour() {
        return player1Colour;
    }

    public void setPlayer1Colour(String player1Colour) {
        ChoosePlayerController.player1Colour = player1Colour;
    }

    public static String getPlayer2Colour() {
        return player2Colour;
    }

    public void setPlayer2Colour(String player2Colour) {
        ChoosePlayerController.player2Colour = player2Colour;
    }

    @FXML
    protected void startGame(ActionEvent event) throws IOException {
        player1Name = txtPlayer1.getText().trim();
        player2Name = txtPlayer2.getText().trim();

        boolean player1IsEmpty = player1Name == null || player1Name.isEmpty();
        boolean player2IsEmpty = player2Name == null || player2Name.isEmpty();

        if (!player1IsEmpty && player1Colour != null && !player2IsEmpty && player2Colour != null) {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gameBoard.fxml"));
            Parent root = fxmlLoader.load();
            stage.setTitle("Connect Four: " + HomeScreenController.gameOption);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setResizable(false);

            Stage playerconfigWindow = (Stage) gotoGame.getScene().getWindow();
            playerconfigWindow.close();

            // Get the controller class for the gameBoard.
            gameBoardController = fxmlLoader.getController();
            gameBoardController.boardPane.setDisable(true);
            gameBoardController.player1Name.setText(player1Name);
            gameBoardController.player1_colour.setStyle("-fx-background-color: " + getPlayer1Colour());
            gameBoardController.player2Name.setText(player2Name);
            gameBoardController.player2_colour.setStyle("-fx-background-color: " + getPlayer2Colour());

        } else {
            if (!player2IsEmpty && player2Colour != null)
                lblComment.setText("Choose a name and a color.");
            else {
                lblComment.setText("Choose a name and a color.");
                computerColourDisplay.setText("Choose a name and a color!");
            }
        }
    }

    public void goBackToHomeScreen (ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("home.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("Home Screen");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

        Stage homeStage = (Stage) btnGoBack.getScene().getWindow();
        homeStage.close();
    }

    @FXML
    private void chosenColor(ActionEvent event) {
        String colour = ((Control) event.getSource()).getId();
        if (colour == btnPlayer1_yellow.getId()) {
            setPlayer1Colour("yellow");
            btnPlayer1_green.setDisable(true);
            btnPlayer1_red.setDisable(true);
            btnPlayer2_yellow.setDisable(true);
        } else if (colour == btnPlayer1_green.getId()) {
            setPlayer1Colour("green");
            btnPlayer1_yellow.setDisable(true);
            btnPlayer1_red.setDisable(true);
            btnPlayer2_green.setDisable(true);
        } else if (colour == btnPlayer1_red.getId()) {
            setPlayer1Colour("red");
            btnPlayer1_green.setDisable(true);
            btnPlayer1_yellow.setDisable(true);
            btnPlayer2_red.setDisable(true);
        } else if (colour == btnPlayer2_green.getId()) {
            setPlayer2Colour("green");
            btnPlayer2_yellow.setDisable(true);
            btnPlayer2_red.setDisable(true);
            btnPlayer1_green.setDisable(true);
        } else if (colour == btnPlayer2_yellow.getId()) {
            setPlayer2Colour("yellow");
            btnPlayer2_green.setDisable(true);
            btnPlayer2_red.setDisable(true);
            btnPlayer1_yellow.setDisable(true);
        } else if (colour == btnPlayer2_red.getId()) {
            setPlayer2Colour("red");
            btnPlayer2_yellow.setDisable(true);
            btnPlayer2_green.setDisable(true);
            btnPlayer1_red.setDisable(true);
        }
    }
}
