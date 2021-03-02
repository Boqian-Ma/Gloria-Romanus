package unsw.gloriaromanus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LoadGameController {
    
    @FXML
    private Button goToMainMenuView;
    private StartScreen startScreen;
    
    @FXML
    private Button goToLoadGameView;
    private LoadScreen LoadScreen;


    @FXML
    public void goToMainMenuView(ActionEvent event) {
        startScreen.start();
    }

    public void setStartScreen(StartScreen startScreen) {
        this.startScreen = startScreen;
    }

    @FXML
    public void goToLoadGameView(ActionEvent event) {
        LoadScreen.start();
    } 
    
}
