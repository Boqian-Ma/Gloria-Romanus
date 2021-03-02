package unsw.gloriaromanus;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameScreen {

    private Stage stage;
    private String title;
    private GameScreenController controller;
    private Scene scene;

    public GameScreen(Stage stage) throws IOException {
        this.stage = stage;
        title = "Gloria Romanus";

        controller = new GameScreenController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        loader.setController(controller);

        Parent root = loader.load();
        scene = new Scene(root, 800, 600);


    }

    public void start() {
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

}
