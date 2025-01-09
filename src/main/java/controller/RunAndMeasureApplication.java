package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class   RunAndMeasureApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RunAndMeasureApplication.class.getResource("/view/main_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1327, 877);
        stage.setTitle("Run and measure");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
       launch();
    }
}