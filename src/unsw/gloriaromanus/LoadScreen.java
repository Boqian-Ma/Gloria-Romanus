package unsw.gloriaromanus;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoadScreen {
    
    private Stage stage;
    private String title;
    private LoadGameController controller;
    private Scene scene;

    public LoadScreen(Stage stage) throws IOException {
        this.stage = stage;
        title = "Load";

        controller = new LoadGameController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("load_game.fxml"));
        loader.setController(controller);

        Parent root = loader.load();
        scene = new Scene(root, 700, 600);
    }

    public void start() {
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public LoadGameController getController() {
        return controller;
    }

}
