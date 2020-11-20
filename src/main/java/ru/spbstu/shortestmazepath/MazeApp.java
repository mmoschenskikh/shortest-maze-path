package ru.spbstu.shortestmazepath;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.spbstu.shortestmazepath.controller.MazeController;
import ru.spbstu.shortestmazepath.util.StringsSupplier;

import java.io.IOException;
import java.util.ResourceBundle;

public class MazeApp extends Application {


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        ResourceBundle strings = StringsSupplier.getStrings();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("maze.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setScene(scene);
        stage.setTitle(strings.getString("title"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        stage.setResizable(false);

        MazeController mc = loader.getController();
        stage.setOnCloseRequest(windowEvent -> {
            mc.onExit();
            windowEvent.consume();
        });

        stage.show();
    }

}