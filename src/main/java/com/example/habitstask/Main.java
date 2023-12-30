package com.example.habitstask;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.example.habitstask.StockExchangeDB.connection;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader;
        if (checkAuthoLogin() != null){
            User.login = checkAuthoLogin();
            checkAuthoLogin();
            fxmlLoader = new FXMLLoader(WelcomeController.class.getResource("menu.fxml"), SceneLoader.getBundle());
        }
        else {
            fxmlLoader = new FXMLLoader(WelcomeController.class.getResource("welcome.fxml"), SceneLoader.getBundle());
        }

        Scene scene = new Scene(fxmlLoader.load(), 885, 498);
        stage.setTitle("Habits&TaskTrail");
        stage.setScene(scene);
        stage.setFullScreen(false);
        stage.show();
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch();
    }

    static String checkAuthoLogin() {
        String name;
        String authoLogin = null;
        String lan;
        String query = "SELECT * FROM user WHERE autho = 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean found = false;

            while (resultSet.next()) {
                authoLogin = resultSet.getString("login");
                name = resultSet.getString("name");
                lan = resultSet.getString("language");
                User.name = name;
                User.language = lan;
                System.out.println("Login with autho = 1: " + authoLogin);
                found = true;
            }
            if (!found) {
                System.out.println("No login found with autho = 1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authoLogin;
    }

}
