package unsw.gloriaromanus;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartScreen {
    
    private Stage stage;
    private String title;
    private StartScreenController controller;
    private Scene scene;

    public StartScreen(Stage stage) throws IOException {
        this.stage = stage;
        title = "Start Screen";

        controller = new StartScreenController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
        loader.setController(controller);

        Parent root = loader.load();
        scene = new Scene(root, 800, 600);
    }

    public Stage getStage() {
        return this.stage;
    }

    public void start() {
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public StartScreenController getController() {
        return controller;
    }

}
