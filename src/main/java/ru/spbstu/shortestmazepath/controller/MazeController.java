package ru.spbstu.shortestmazepath.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MazeController implements Initializable {

    private final static int CELL_SIZE = 36;
    private final static int INITIAL_MAZE_SIZE = 7;
    @FXML
    public ChoiceBox<Integer> heightChoiceBox;
    @FXML
    public ChoiceBox<Integer> widthChoiceBox;
    @FXML
    public Button startPointButton;
    @FXML
    public Button endPointButton;
    @FXML
    public Button randomButton;
    @FXML
    public Button resetButton;
    @FXML
    public Button solveButton;
    @FXML
    public Label statusLabel;
    @FXML
    public GridPane mazePane;
    private boolean isStartSet = false;
    private boolean isEndSet = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Integer> options = getSizeOptionsList();
        heightChoiceBox.setItems(options);
        widthChoiceBox.setItems(options);

        initializeMaze();

        heightChoiceBox.setValue(mazePane.getRowCount());
        widthChoiceBox.setValue(mazePane.getColumnCount());

        checkStartEndSet();
    }

    /**
     * Generates ObservableList containing all size options available.
     *
     * @return ObservableList&lt;Integer&gt; containing all size options available.
     */
    private ObservableList<Integer> getSizeOptionsList() {
        List<Integer> options = new ArrayList<>();
        for (int i = 3; i <= 10; i++) {
            options.add(i);
        }
        return FXCollections.observableArrayList(options);
    }

    /**
     * Checks both start and end points are set, disabling "Solve" button if false.
     */
    private void checkStartEndSet() {
        solveButton.setDisable(!(isStartSet && isEndSet));
    }

    private void initializeMaze() {
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPrefHeight(CELL_SIZE);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPrefWidth(CELL_SIZE);

        mazePane.getRowConstraints().add(rowConstraints);
        mazePane.getColumnConstraints().add(columnConstraints);

        for (int i = 0; i < INITIAL_MAZE_SIZE; i++) {
            mazePane.addColumn(i);
            mazePane.addRow(i);
        }

        Image pathImage = new Image(getClass().getResourceAsStream("path.png"));
        Image wallImage = new Image(getClass().getResourceAsStream("wall.png"));

        for (int i = 0; i < INITIAL_MAZE_SIZE; i++) {
            for (int j = 0; j < INITIAL_MAZE_SIZE; j++) {
                ImageView view = new ImageView();
                view.setImage(pathImage);
                view.setFitHeight(CELL_SIZE);
                view.setFitWidth(CELL_SIZE);

                view.setOnMouseClicked(mouseEvent -> {
                    if (view.getImage().equals(pathImage))
                        view.setImage(wallImage);
                    else
                        view.setImage(pathImage);
                });
                mazePane.add(view, i, j);
            }
        }
    }

    public void onStartSet() {
        isStartSet = true;
        checkStartEndSet();
    }

    public void onEndSet() {
        isEndSet = true;
        checkStartEndSet();
    }

    public void onRandom() {
    }

    public void onReset() {
        isStartSet = false;
        isEndSet = false;
        checkStartEndSet();
    }

    public void onSolve(ActionEvent actionEvent) {
    }
}
