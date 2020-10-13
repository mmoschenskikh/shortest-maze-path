package ru.spbstu.shortestmazepath.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import java.util.Random;
import java.util.ResourceBundle;

public class MazeController implements Initializable {

    private final static int INITIAL_MAZE_SIZE = 7;
    private final static int MIN_MAZE_SIZE = 3;
    private final static int MAX_MAZE_SIZE = 12;
    private final static int GRID_PANE_ACTUAL_SIZE = 360;
    private final static int CELL_SIZE = GRID_PANE_ACTUAL_SIZE / MAX_MAZE_SIZE;

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
    private final Image pathImage = new Image(getClass().getResourceAsStream("path.png"));
    private final Image wallImage = new Image(getClass().getResourceAsStream("wall.png"));
    private final Image startImage = new Image(getClass().getResourceAsStream("start.png"));
    private final Image endImage = new Image(getClass().getResourceAsStream("end.png"));
    private boolean settingStart = false;
    private boolean settingEnd = false;
    private ImageView startPoint;
    private ImageView endPoint;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Integer> options = getSizeOptionsList();
        heightChoiceBox.setItems(options);
        widthChoiceBox.setItems(options);

        initializeMaze();

        heightChoiceBox.setValue(mazePane.getRowCount());
        widthChoiceBox.setValue(mazePane.getColumnCount());

        heightChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            setMazeHeight(heightChoiceBox.getValue());
            statusLabel.setText("Constructing the maze...");
        });
        widthChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            setMazeWidth(widthChoiceBox.getValue());
            statusLabel.setText("Constructing the maze...");
        });

        checkStartEndSet();
    }

    /**
     * Generates ObservableList containing all size options available.
     *
     * @return ObservableList&lt;Integer&gt; containing all size options available.
     */
    private ObservableList<Integer> getSizeOptionsList() {
        List<Integer> options = new ArrayList<>();
        for (int i = MIN_MAZE_SIZE; i <= MAX_MAZE_SIZE; i++) {
            options.add(i);
        }
        return FXCollections.observableArrayList(options);
    }

    /**
     * Checks both start and end points are set, disabling "Solve" button if false.
     */
    private void checkStartEndSet() {
        solveButton.setDisable(
                startPoint == null || endPoint == null || startPoint == endPoint
                        || checkNotOnField(startPoint) || checkNotOnField(endPoint)
                        || !startPoint.getImage().equals(startImage)
                        || !endPoint.getImage().equals(endImage)
        );
    }

    private boolean checkNotOnField(Node node) {
        return GridPane.getRowIndex(node) >= mazePane.getRowCount() || GridPane.getColumnIndex(node) >= mazePane.getColumnCount();
    }

    private void setMazeHeight(int height) {
        int currentHeight = mazePane.getRowCount();
        while (height != currentHeight) {
            if (height > currentHeight) {
                addMazeRow();
            } else {
                final int removeIndex = currentHeight - 1;
                mazePane.getChildren().removeIf(
                        node -> GridPane.getRowIndex(node) == null || GridPane.getRowIndex(node) == removeIndex
                );
            }
            currentHeight = mazePane.getRowCount();
        }

        if (startPoint != null && checkNotOnField(startPoint))
            startPoint = null;
        if (endPoint != null && checkNotOnField(endPoint))
            endPoint = null;

        checkStartEndSet();
    }

    private void setMazeWidth(int width) {
        int currentWidth = mazePane.getColumnCount();
        while (width != currentWidth) {
            if (width > currentWidth) {
                addMazeColumn();
            } else {
                final int removeIndex = currentWidth - 1;
                mazePane.getChildren().removeIf(
                        node -> GridPane.getColumnIndex(node) == null || GridPane.getColumnIndex(node) == removeIndex
                );
            }
            currentWidth = mazePane.getColumnCount();
        }

        if (startPoint != null && checkNotOnField(startPoint))
            startPoint = null;
        if (endPoint != null && checkNotOnField(endPoint))
            endPoint = null;

        checkStartEndSet();
    }

    private void addMazeRow() {
        int index = mazePane.getRowCount();
        mazePane.addRow(index);

        for (int i = 0; i < mazePane.getColumnCount(); i++) {
            ImageView cellView = prepareMazeCell();
            mazePane.add(cellView, i, index);
        }
    }

    private void addMazeColumn() {
        int index = mazePane.getColumnCount();
        mazePane.addColumn(index);

        for (int i = 0; i < mazePane.getRowCount(); i++) {
            ImageView cellView = prepareMazeCell();
            mazePane.add(cellView, index, i);
        }
    }

    private ImageView prepareMazeCell() {
        ImageView view = new ImageView();
        view.setImage(pathImage);
        view.setFitHeight(CELL_SIZE);
        view.setFitWidth(CELL_SIZE);

        view.setOnMouseClicked(mouseEvent -> {
            if (settingStart || settingEnd) {
                if (settingStart && view != endPoint) {
                    if (startPoint != null)
                        startPoint.setImage(pathImage);
                    view.setImage(startImage);
                    startPoint = view;
                    settingStart = false;
                    statusLabel.setText("The start point is set");
                } else if (settingEnd && view != startPoint) {
                    if (endPoint != null)
                        endPoint.setImage(pathImage);
                    view.setImage(endImage);
                    endPoint = view;
                    settingEnd = false;
                    statusLabel.setText("The end point is set");
                }
            } else {
                statusLabel.setText("Constructing the maze...");
                if (view.getImage().equals(pathImage)) {
                    view.setImage(wallImage);
                } else {
                    view.setImage(pathImage);
                }
            }
            checkStartEndSet();
        });
        return view;
    }

    private void setGridPaneConstraints(int cellSize) {
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPrefHeight(cellSize);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPrefWidth(cellSize);

        mazePane.getRowConstraints().add(rowConstraints);
        mazePane.getColumnConstraints().add(columnConstraints);
    }

    private void initializeMaze() {
        setGridPaneConstraints(CELL_SIZE);

        ImageView cellView = prepareMazeCell();

        mazePane.addColumn(0);
        mazePane.addRow(0);
        mazePane.add(cellView, 0, 0);

        int i = 0;
        while (++i < INITIAL_MAZE_SIZE) {
            addMazeRow();
            addMazeColumn();
        }
    }

    public void onStartSet() {
        statusLabel.setText("Choose a cell for the start point...");
        settingStart = true;
    }

    public void onEndSet() {
        statusLabel.setText("Choose a cell for the end point...");
        settingEnd = true;
    }

    public void onRandom() {
        onReset();
        Random random = new Random();

        int height = Math.max(random.nextInt(MAX_MAZE_SIZE), MIN_MAZE_SIZE);
        int width = Math.max(random.nextInt(MAX_MAZE_SIZE), MIN_MAZE_SIZE);

        heightChoiceBox.setValue(height);
        widthChoiceBox.setValue(width);

        int startRow = random.nextInt(height - 1);
        int startColumn = random.nextInt(width - 1);

        int endRow = random.nextInt(height - 1);
        int endColumn = random.nextInt(width - 1);

        while (startRow == endRow && startColumn == endColumn) {
            endRow = random.nextInt(height - 1);
            endColumn = random.nextInt(width - 1);
        }

        mazePane.getChildren().forEach(node -> {
            if (node instanceof ImageView) {
                ImageView view = (ImageView) node;
                if (random.nextBoolean())
                    view.setImage(wallImage);
            }
        });

        startPoint = (ImageView) mazePane.getChildren().filtered(node ->
                GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null
                        && GridPane.getRowIndex(node) == startRow && GridPane.getColumnIndex(node) == startColumn
        ).get(0);
        startPoint.setImage(startImage);

        int finalEndRow = endRow;
        int finalEndColumn = endColumn;
        endPoint = (ImageView) mazePane.getChildren().filtered(node ->
                GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null
                        && GridPane.getRowIndex(node) == finalEndRow && GridPane.getColumnIndex(node) == finalEndColumn
        ).get(0);
        endPoint.setImage(endImage);

        statusLabel.setText("Random Maze generated");
        checkStartEndSet();
    }

    public void onReset() {
        startPoint = null;
        endPoint = null;
        checkStartEndSet();

        setMazeHeight(INITIAL_MAZE_SIZE);
        setMazeWidth(INITIAL_MAZE_SIZE);

        mazePane.getChildren().forEach(node -> {
            if (node instanceof ImageView)
                ((ImageView) node).setImage(pathImage);
        });

        heightChoiceBox.setValue(INITIAL_MAZE_SIZE);
        widthChoiceBox.setValue(INITIAL_MAZE_SIZE);

        statusLabel.setText("Successfully reset");
    }

    public void onSolve(ActionEvent actionEvent) {
        statusLabel.setText("Cannot solve it now :((");
    }
}
