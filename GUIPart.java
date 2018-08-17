package poppyfanboy.snakegame;

/**
 * Class "GUIPart"
 * Implements GUI part of the "Snake" game
 * (including menus, additional windows and other GUI stuff)
 *
 * @author PoppyFanboy
 */

// GUI events
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
// GUI elements
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
// GUI (general stuff)
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyEvent;

public class GUIPart extends Application {
    private Stage stage;
    private static SnakeGame game;

    public static final int WINDOW_WIDTH = 1024;
    public static final int WINDOW_HEIGHT = 768;
    public static final int GAME_WIDTH = 600;
    public static final int GAME_HEIGHT = 600;

    public void start(Stage aStage) {
        stage = aStage;
        stage.setTitle("Snake Game | by PoppyFanboy");

        Pane root = new Pane();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                game.handleKey(event.getCode());
            }
        });

        MenuBar menuBar = new MenuBar();
        final Menu menu1 = new Menu("Game");
        final Menu menu2 = new Menu("Reference");

        MenuItem menuNewGame = new MenuItem("New Game");
        MenuItem menuScore = new MenuItem("Scoreboard");
        MenuItem menuParam = new MenuItem("Parameters");
        MenuItem menuExit = new MenuItem("Exit");
        menuExit.setOnAction(new MenuActionHandler());
        menuNewGame.setOnAction(new MenuActionHandler());

        menu1.getItems().addAll(menuNewGame, new SeparatorMenuItem(), menuScore,
                menuParam, new SeparatorMenuItem(), menuExit);

        MenuItem menuReference = new MenuItem("Show Reference");
        MenuItem menuAbout = new MenuItem("About");
        menu2.getItems().addAll(menuReference, new SeparatorMenuItem(), menuAbout);

        menuBar.getMenus().addAll(menu1, menu2);
        root.getChildren().add(menuBar);

        stage.show();
        game = new SnakeGame(root);
    }

    public static void main(String[] args) {
        launch(args);
        game.stop();
    }

    class MenuActionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent mouseEvent) {
            if (mouseEvent.getTarget() instanceof MenuItem) {
                MenuItem target = (MenuItem) mouseEvent.getTarget();
                if (target.getText() == "Exit") {
                    game.stop();
                    stage.close();
                } else if (target.getText() == "New Game") {
                    game.stop();
                    game.start();
                }
            }
        }
    }
}
