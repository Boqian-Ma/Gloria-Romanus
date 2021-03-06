package unsw.gloriaromanus;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NewGameScreen {
    
    private Stage stage;
    private String title;
    private NewGameController controller;
    private Scene scene;

    public NewGameScreen(Stage stage) throws IOException {
        this.stage = stage;
        title = "New Game";

        controller = new NewGameController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("new_game.fxml"));
        loader.setController(controller);

        Parent root = loader.load();
        scene = new Scene(root, 800, 600);

    }

    public void start() {
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public NewGameController getController() {
        return controller;
    }

}
