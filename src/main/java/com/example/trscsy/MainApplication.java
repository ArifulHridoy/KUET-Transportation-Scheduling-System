package com.example.trscsy;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApplication extends Application {
    @Override public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/trscsy/fxml/loginAdmin.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 700, 550);
        scene.getStylesheets().add(getClass().getResource("/com/example/trscsy/style/style.css").toExternalForm());
        stage.setTitle("KUET Transportation & Schedueling System Login");
        stage.setScene(scene);
        stage.show();
    }
}
