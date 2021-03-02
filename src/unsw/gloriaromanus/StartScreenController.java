package unsw.gloriaromanus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartScreenController {
    
    @FXML
    private Button newGameButton;
    private NewGameScreen newGameScreen;
    
    @FXML
    private Button loadGameButton;
    private LoadScreen loadGameScreen;

    @FXML
    private void goToNewGameView(ActionEvent event) {
        newGameScreen.start();
    }

    public void setNewGameScreen(NewGameScreen newGameScreen) {
        this.newGameScreen = newGameScreen;
    }

    @FXML
    private void goToLoadGameView(ActionEvent event) {
        loadGameScreen.start();
    }

    public void setLoadGameScreen(LoadScreen loadGameScreen) {
        this.loadGameScreen = loadGameScreen;
    }

}
