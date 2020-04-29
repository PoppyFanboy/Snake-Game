package poppyfanboy.snakegame.gui;

import java.io.InputStream;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import poppyfanboy.snakegame.data.options.*;

public class OptionsWindowController {
    // Game mode (easy/medium/hard/zen)
    private ArrayList<RadioButton> modeButtons = new ArrayList<>();
    // Labyrinth
    private ArrayList<RadioButton> labButtons = new ArrayList<>();

    private int zenSpeed = 1;

    private GameWindowController primaryController = null;
    private int labyrinthType;
    private GameMode gameMode;

    @FXML private Scene scene;

    @FXML private RadioButton modeBttn1;
    @FXML private RadioButton modeBttn2;
    @FXML private RadioButton modeBttn3;
    @FXML private RadioButton modeBttn4;

    @FXML private RadioButton labBttn1;
    @FXML private RadioButton labBttn2;
    @FXML private RadioButton labBttn3;
    @FXML private RadioButton labBttn4;
    @FXML private RadioButton labBttn5;

    @FXML private Button upBttn;
    @FXML private Button downBttn;
    @FXML private TextField zenSpeedField;

    @FXML private Label multiplierLabel;

    @FXML
    private void initialize() {
        scene.getStylesheets().add(getClass()
            .getResource("/gui/OptionsWindowStylesheet.css").toExternalForm());

        zenSpeedField.setDisable(true);

        ToggleGroup group1 = new ToggleGroup();
        modeBttn1.setToggleGroup(group1);
        modeBttn1.setSelected(true);
        modeBttn2.setToggleGroup(group1);
        modeBttn3.setToggleGroup(group1);
        modeBttn4.setToggleGroup(group1);

        modeButtons.add(modeBttn1);
        modeButtons.add(modeBttn2);
        modeButtons.add(modeBttn3);
        modeButtons.add(modeBttn4);

        ToggleGroup group2 = new ToggleGroup();
        labBttn1.setToggleGroup(group2);
        labBttn1.setSelected(true);
        labBttn2.setToggleGroup(group2);
        labBttn3.setToggleGroup(group2);
        labBttn4.setToggleGroup(group2);
        labBttn5.setToggleGroup(group2);

        labButtons.add(labBttn1);
        labButtons.add(labBttn2);
        labButtons.add(labBttn3);
        labButtons.add(labBttn4);
        labButtons.add(labBttn5);

        modeBttn4.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                upBttn.setDisable(false);
                downBttn.setDisable(false);
            } else {
                upBttn.setDisable(true);
                downBttn.setDisable(true);
            }
        });

        update();
    }

    @FXML
    private void incZenSpeed() {
        if (zenSpeed < 10) {
            zenSpeed++;
        }
        zenSpeedField.setText(zenSpeed + "");
    }

    @FXML
    private void decZenSpeed() {
        if (zenSpeed > 1) {
            zenSpeed--;
        }
        zenSpeedField.setText(zenSpeed + "");
    }

    @FXML
    private void update() {
        int mult = 1;

        for (int i = 0; i < modeButtons.size(); i++) {
            if (modeButtons.get(i).isSelected()) {
                mult *= GameMode.getMult(i);
                this.gameMode = GameMode.values()[i];
            }
        }

        for (int i = 0; i < labButtons.size(); i++) {
            if (labButtons.get(i).isSelected()) {
                mult *= Labyrinth.getMult(i);
                this.labyrinthType = i;
            }
        }

        multiplierLabel.setText("Total multiplier: " + mult);
    }

    @FXML
    private void discard() {
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

    @FXML
    private void save() {
        if (primaryController != null) {
            Labyrinth labyrinth;
            labyrinth = new Labyrinth(labyrinthType);

            primaryController.setOptions(new Options(labyrinth, gameMode, Integer.parseInt(zenSpeedField.getText())));
        }

        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

    static void openOptionsWindow(Options options, GameWindowController primaryController, Stage stage) {
        Stage optionsWindow = new Stage();
        optionsWindow.setTitle("Options");

        try (InputStream fxmlStream = NewHighscoreWindowController.class
                .getResourceAsStream("/gui/FXMLOptionsWindow.fxml")) {
            FXMLLoader loader = new FXMLLoader();
            Scene scene = loader.load(fxmlStream);
            optionsWindow.setScene(scene);

            OptionsWindowController controller = loader.getController();
            controller.setDefaultOptions(options);
            controller.primaryController = primaryController;

            optionsWindow.initOwner(stage);
            optionsWindow.initModality(Modality.APPLICATION_MODAL);
            optionsWindow.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
            optionsWindow.close();
        }
    }

    private void setDefaultOptions(Options options) {
        labyrinthType = options.getLabyrinth().getLabyrinthType();
        gameMode = options.getGameMode();

        labButtons.get(labyrinthType).setSelected(true);
        modeButtons.get(gameMode.ordinal()).setSelected(true);
        zenSpeed = options.getZenSpeedLevel();
        zenSpeedField.setText(zenSpeed + "");
    }
}
