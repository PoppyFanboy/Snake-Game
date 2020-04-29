package poppyfanboy.snakegame;

/**
 * Class "Main"
 * Starting point of the application
 * Creates the initial window and loads the scene with the game
 *
 * @author PoppyFanboy
 */

import java.io.InputStream;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.fxml.*;

import poppyfanboy.snakegame.gui.GameWindowController;
import poppyfanboy.snakegame.gui.NewHighscoreWindowController;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Snake Game | by PoppyFanboy");

        try (InputStream fxmlStream = NewHighscoreWindowController.class
                .getResourceAsStream("/gui/FXMLGameWindow.fxml")) {
            FXMLLoader loader = new FXMLLoader();
            Scene scene = loader.load(fxmlStream);
            stage.setScene(scene);
            stage.show();

            GameWindowController controller = loader.getController();
            controller.start(stage);
        } catch (IOException ex) {
            ex.printStackTrace();
            stage.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
