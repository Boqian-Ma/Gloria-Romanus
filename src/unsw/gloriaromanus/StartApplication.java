package unsw.gloriaromanus;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class StartApplication extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        StartScreen startScreen = new StartScreen(primaryStage);
        NewGameScreen newGameScreen = new NewGameScreen(primaryStage);
        LoadScreen loadGameScreen = new LoadScreen(primaryStage);

        startScreen.getController().setNewGameScreen(newGameScreen);
        startScreen.getController().setLoadGameScreen(loadGameScreen);
        newGameScreen.getController().setStartScreen(startScreen);
        loadGameScreen.getController().setStartScreen(startScreen);

        startScreen.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
