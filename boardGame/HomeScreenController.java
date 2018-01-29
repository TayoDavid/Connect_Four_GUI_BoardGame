package boardGame;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeScreenController {

    static String gameOption;
    ChoosePlayerController choosePlayerController;
    @FXML
    private JFXButton humanVsHuman;
    @FXML
    private JFXButton humanVsComputer;

    @FXML
    public void humanVsHuman(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("choosePlayer.fxml"));
        stage.setTitle("Player Configuration");
        stage.setScene(new Scene(root));
        stage.show();
        stage.setResizable(false);

        gameOption = "Human vs Human";

        Stage homeScreen = (Stage) humanVsHuman.getScene().getWindow();
        homeScreen.close();
    }

    @FXML
    public void humanVsComputer(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("choosePlayer.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("Player Configuration");
        stage.setScene(new Scene(root));
        stage.show();
        stage.setResizable(false);
        gameOption = "Human vs Computer";

        Stage homeScreen = (Stage) humanVsComputer.getScene().getWindow();
        homeScreen.close();

        // Disable Player 2 pane and set computer information.
        choosePlayerController = fxmlLoader.getController();
        choosePlayerController.txtPlayer2.setText("Computer");
        String colour = "Computer Selects colour RED!";
        choosePlayerController.computerColourDisplay.setText(colour);
        choosePlayerController.player2Pane.setDisable(true);
        choosePlayerController.btnPlayer1_red.setDisable(true);
        choosePlayerController.setPlayer2Colour("red");
    }
}
