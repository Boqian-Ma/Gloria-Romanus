package unsw.gloriaromanus;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;


public class NewGameController {

    public static Stage stage;

    @FXML
    private Button goToMainMenuView;
    private StartScreen startScreen;
    
    @FXML
    private Button goToMainGameView;
    private GameScreen gameScreen;

    @FXML
    private TextField filename;
    @FXML
    private Label p1Label;
    @FXML
    private ChoiceBox p1choiceBox;
    @FXML
    private Label p2Label;
    @FXML
    private ChoiceBox p2choiceBox;

    @FXML
    private Label new_game_message;


    public void initialize() {
        p1choiceBox.getItems().addAll("Roman", "Carthaginian", "Gaul", "Celtic Briton", "Spanish", "Numidian",
                "Egyptian", "Seleucid Empire", "Pontu", "Amenian", "Parthian", "Germanic", "Greek City State",
                "Macedonian", "Tracian", "Dacian");
        p2choiceBox.getItems().addAll("Roman", "Carthaginian", "Gaul", "Celtic Briton", "Spanish", "Numidian",
                "Egyptian", "Seleucid Empire", "Pontu", "Amenian", "Parthian", "Germanic", "Greek City State",
                "Macedonian", "Tracian", "Dacian");
    }

    @FXML
    public void goToMainMenuView(ActionEvent event) {
        startScreen.start();
    }

    public void setStartScreen(StartScreen startScreen) {
        this.startScreen = startScreen;
    }

    @FXML
    private void goToMainGameView(ActionEvent event) throws IOException {
        if (filename.getText() == null || filename.getText().equals("")) {
            this.new_game_message.setText("Please enter a game name");
        } else if (p1choiceBox.getValue() == null || p2choiceBox.getValue() == null) {
            this.new_game_message.setText("Please select some provinces.");
        } else if (!p1choiceBox.getValue().equals(p2choiceBox.getValue())) {


            GameScreenController.folderName = filename.getText();
            GameScreenController.newGameFaction1 = p1choiceBox.getValue().toString();
            GameScreenController.newGameFaction2 = p2choiceBox.getValue().toString();
                        
            GameScreen gameScreen = new GameScreen(startScreen.getStage());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            GameScreenController controller = new GameScreenController();
            loader.setController(controller);
            gameScreen.start();

        } else {
            this.new_game_message.setText("Please select distinct provinces.");
        }
    }

        // add game log to list of saved games

    public void setMainGame(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }
}


