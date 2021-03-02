package unsw.gloriaromanus;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScreensFrameworkApplication extends Application {
    public static String mainMenuId = "main_menu"; // home page
    public static String mainMenuFile = "main_menu.fxml";

    public static String newGameId = "new_game";
    public static String newGameFile = "new_game.fxml";

    public static String loadGameId = "load_game";
    public static String loadGameFile = "load_game.fxml";

    public static String mainGameId = "main_game";
    public static String mainGameFile = "main.fxml";

    @Override
    public void start(Stage primaryStage) {
        ScreensController mainContainer = new ScreensController();

        mainContainer.loadScreen(ScreensFrameworkApplication.mainMenuId, ScreensFrameworkApplication.mainMenuFile);

        mainContainer.loadScreen(ScreensFrameworkApplication.newGameId, ScreensFrameworkApplication.newGameFile);

        mainContainer.loadScreen(ScreensFrameworkApplication.loadGameId, ScreensFrameworkApplication.loadGameFile);

        // set to main menu at the start
        mainContainer.setScreen(ScreensFrameworkApplication.mainMenuId);

        // create a group of secnes
        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        

        primaryStage.setTitle("Gloria Romanus");
        primaryStage.setWidth(1300);
        primaryStage.setHeight(800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
