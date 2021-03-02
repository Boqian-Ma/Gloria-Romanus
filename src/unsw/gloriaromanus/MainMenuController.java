package unsw.gloriaromanus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainMenuController implements ControlledScreen {
    ScreensController myController;

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    @FXML
    private void goToNewGameView(ActionEvent event) {
        myController.setScreen(ScreensFrameworkApplication.newGameId);
    }

    @FXML
    private void goToLoadGameView(ActionEvent event) {
        myController.setScreen(ScreensFrameworkApplication.loadGameId);
    }

}


