package com.example.habitstask;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SceneLoader {

    public static void loadNewScene(String fxml, Pane root) throws IOException {
        ResourceBundle bundle;
        if(User.language=="en"){
            bundle = ResourceBundle.getBundle("strings", new Locale("en", "UK"));
        } else if (User.language==("it")) {
            bundle = ResourceBundle.getBundle("strings", new Locale("it", "IT"));
        } else {
            bundle = ResourceBundle.getBundle("strings", new Locale("ru", "RU"));
        }
        FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource(fxml), bundle);
        Parent newRoot = loader.load();
        Scene newScene = new Scene(newRoot);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(newScene);
        stage.show();
    }

    public static void loadNewScene(String fxml, SplitPane root) throws IOException {
        ResourceBundle bundle;
        if(User.language=="en"){
            bundle = ResourceBundle.getBundle("strings", new Locale("en", "UK"));
        } else if (User.language=="it") {
            bundle = ResourceBundle.getBundle("strings", new Locale("it", "IT"));
        } else {
            bundle = ResourceBundle.getBundle("strings", new Locale("ru", "RU"));
        }
        FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource(fxml), bundle);
        Parent newRoot = loader.load();
        Scene newScene = new Scene(newRoot);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(newScene);
        stage.show();
    }

    public static ResourceBundle getBundle() throws IOException {
        ResourceBundle bundle;
        if(User.language=="en"){
            bundle = ResourceBundle.getBundle("strings", new Locale("en", "UK"));
        } else if (User.language=="it") {
            bundle = ResourceBundle.getBundle("strings", new Locale("it", "IT"));
        } else {
            bundle = ResourceBundle.getBundle("strings", new Locale("ru", "RU"));
        }
        return bundle;
    }


}
