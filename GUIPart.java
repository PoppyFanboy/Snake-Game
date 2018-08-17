package poppyfanboy.snakegame;

/**
 * Class "GUIPart"
 * Implements GUI part of the "Snake" game
 * (including menus, additional windows and other GUI stuff)
 *
 * @author PoppyFanboy
 */

// GUI events
import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
// GUI elements
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import javafx.scene.control.*;
import javafx.geometry.Orientation;
// GUI (general stuff)
import javafx.application.Application;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class GUIPart extends Application {
    private Stage stage;
    private SnakeGame game;

    public static final int WINDOW_WIDTH = 640;
    public static final int WINDOW_HEIGHT = 480;
    public static final int GAME_WIDTH = 405;
    public static final int GAME_HEIGHT = 405;
    
    private static final String HOME = "src/poppyfanboy/snakegame/";

    @Override
    public void start(Stage aStage) {
        stage = aStage;
        stage.setTitle("Snake Game | by PoppyFanboy");
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                game.stop();
            }
        });

        Pane root = new Pane();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);

        
        stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                game.handleKey(event.getCode());
            }
        });

        MenuBar menuBar = new MenuBar();
        final Menu menu1 = new Menu("Game");
        final Menu menu2 = new Menu("Reference");
        
        menu1.setOnAction(new MenuActionHandler());
        menu2.setOnAction(new MenuActionHandler());
        
        MenuItem menuNewGame = new MenuItem("New Game");
        MenuItem menuPause = new MenuItem("Pause (P)");
        MenuItem menuScore = new MenuItem("Scoreboard");
        MenuItem menuParam = new MenuItem("Parameters");
        MenuItem menuExit = new MenuItem("Exit");

        menu1.getItems().addAll(menuNewGame, menuPause, new SeparatorMenuItem(), menuScore, menuParam, new SeparatorMenuItem(), menuExit);

        MenuItem menuReference = new MenuItem("Show Reference");
        MenuItem menuAbout = new MenuItem("About");
        
        menu2.getItems().addAll(menuReference, new SeparatorMenuItem(), menuAbout);

        menuBar.getMenus().addAll(menu1, menu2);
        root.getChildren().add(menuBar);

        stage.show();
        game = new SnakeGame(root);
    }
    
    // bugs:
    //  - long nicks are not supported
    //  - whitespace among the nick leads to the unhandled exception
    private static Score[] getScoreBoard(int n) throws IllegalArgumentException {
        try {
            Scanner in = new Scanner(Paths.get(HOME + "Scoreboard.tab"));
            while (in.hasNext()) {
                String[] tag = in.nextLine().split(" ");
                System.out.println(Arrays.toString(tag));
                
                if (tag[0].equals("@board") && Integer.valueOf(tag[1]) == n) {
                    int length = Integer.valueOf(tag[2]);
                    Score[] scores = new Score[length];
                    for (int i = 0; i < length; i++) {
                        String line = in.nextLine();
                        scores[i] = new Score(line.split(" ")[0], Integer.valueOf(line.split(" ")[1]));
                    }
                    in.close();
                    return scores;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        throw new IllegalArgumentException("There is no scoreboard with order " + n);
    }
    
    // unfinished
    private void openScoreboard() {
        Stage scoreboard = new Stage();
        scoreboard.setTitle("Scoreboard");
        
        TabPane root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab tab1 = new Tab("Labyrinth 1");
        Tab tab2 = new Tab("Labyrinth 2");
        Tab tab3 = new Tab("Labyrinth 3");
        root.getTabs().addAll(tab1, tab2, tab3);
        
        Scene scene = new Scene(root, 350, 500);
        scoreboard.setScene(scene);
        
        FlowPane paneTab1 = new FlowPane(Orientation.VERTICAL);
        //String[] nickNames = { "Joe", "Bob", "Jane" };
        //long[] scores = { 300, 200, 100 };
        Score[] scores = null;
        
        try {
            scores = getScoreBoard(1);
        } catch (IllegalArgumentException ex) {
            scoreboard.close();
            
            return;
        }
        
        for (int i = 0; i < scores.length; i++) {
            Label score = new Label(String.format(" %d. ", i + 1) + String.format("%-20s", scores[i].nick) + String.format("%d", scores[i].score));
            score.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
            paneTab1.getChildren().add(score);
        }
        tab1.setContent(paneTab1);
                    
        // initialization of modality
        scoreboard.initOwner(stage);
        scoreboard.initModality(Modality.APPLICATION_MODAL);
        scoreboard.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
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
                } else if (target.getText() == "Pause (P)") {
                    game.pause();
                } else if (target.getText() == "Scoreboard") {
                    game.pause(false);
                    openScoreboard();
                }
            }
        }
    }
    
    static class Score {
        public String nick;
        public int score;
        
        Score(String nick, int score) {
            this.nick = nick;
            this.score = score;
        }
    }
}
