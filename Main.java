package poppyfanboy.snakegame;

/**
 * Class "Main"
 * Starting point of the application
 * Creates the initial window and loads the scene with the game
 *
 * @author PoppyFanboy
 */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.fxml.*;

public class Main extends Application {
    public static final int WINDOW_WIDTH = 640;
    public static final int WINDOW_HEIGHT = 480;
    public static final int GAME_WIDTH = 405;
    public static final int GAME_HEIGHT = 405;
    
    public static final String HOME = "src/poppyfanboy/snakegame/";

    private static Stage stage;

    @Override
    public void start(Stage aStage) {
        stage = aStage;
        aStage.setTitle("Snake Game | by PoppyFanboy");

        try (FileInputStream fxmlStream = new FileInputStream(HOME + "gui/FXMLGameWindow.fxml")) {
            FXMLLoader loader = new FXMLLoader();
            Scene scene = (Scene) loader.load(fxmlStream);
            aStage.setScene(scene);
            aStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            aStage.close();
            return;
        }
    }

    public static Stage getStage() {
        return stage;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}