package com.example.habitstask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ResourceBundle;
import static com.example.habitstask.StockExchangeDB.connection;
import static com.example.habitstask.User.login;

public class MenuController implements Initializable {

    public SplitPane MenuRoot;
    public Button Statistics;
    public Button Back;
    public Button tasks;
    public Button habits;
    public Button achievements;
    public Button notes;
    public Label title;


    @FXML
    protected void Back() throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(SceneLoader.getBundle().getString("alAttention"));
        alert.setHeaderText(SceneLoader.getBundle().getString("logoutConfigurationT"));
        alert.setContentText(SceneLoader.getBundle().getString("logoutConfiguration"));

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {

            String updateQuery = "UPDATE user SET autho = false WHERE login = ?";

            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

                updateStatement.setString(1, login);
                System.out.println(updateStatement);

                int rowsAffected = updateStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Autho value updated to 0 successfully for user: " + login);
                } else {
                    System.out.println("User with login " + login + " not found.");
                }

                SceneLoader.loadNewScene("welcome.fxml", MenuRoot);

            } catch (SQLException e) {

                throw new RuntimeException(e);
            }
        }

    }

    @FXML
    protected void Stats() throws IOException {
        SceneLoader.loadNewScene("statistics.fxml", MenuRoot);
    }

        @FXML
    protected void Tasks() throws IOException {
        SceneLoader.loadNewScene("tasks.fxml", MenuRoot);
    }

    @FXML
    protected void Achievements() throws IOException {
        SceneLoader.loadNewScene("achievements.fxml", MenuRoot);
    }

    @FXML
    protected void Habits() throws IOException {
        SceneLoader.loadNewScene("habits.fxml", MenuRoot);
    }

    @FXML
    protected void Notes() throws IOException {
        SceneLoader.loadNewScene("notes.fxml", MenuRoot);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResourceBundle resources = ResourceBundle.getBundle("strings");
        LocalTime currentTime = LocalTime.now();

        // Определение временных интервалов
        LocalTime interval1Start = LocalTime.of(0, 0);    // 00:00
        LocalTime interval1End = LocalTime.of(6, 0);      // 06:00

        LocalTime interval2Start = LocalTime.of(6, 0);    // 06:00
        LocalTime interval2End = LocalTime.of(12, 0);     // 12:00

        LocalTime interval3Start = LocalTime.of(12, 0);   // 12:00
        LocalTime interval3End = LocalTime.of(18, 0);     // 18:00

        LocalTime interval4Start = LocalTime.of(18, 0);   // 18:00
        LocalTime interval4End = LocalTime.of(0, 0);      // 24:00

        if (isInInterval(currentTime, interval1Start, interval1End)) {
            try {
                title.setText(SceneLoader.getBundle().getString("menuTitle1") + User.name + "!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (isInInterval(currentTime, interval2Start, interval2End)) {
            try {
                title.setText(SceneLoader.getBundle().getString("menuTitle2") + User.name + "!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (isInInterval(currentTime, interval3Start, interval3End)) {
            try {
                title.setText(SceneLoader.getBundle().getString("menuTitle3") + User.name + "!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (isInInterval(currentTime, interval4Start, interval4End)) {
            try {
                title.setText(SceneLoader.getBundle().getString("menuTitle4") + User.name + "!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    // Метод для проверки, находится ли переданное время в заданном промежутке
    private static boolean isInInterval(LocalTime currentTime, LocalTime intervalStart, LocalTime intervalEnd) {
        return currentTime.isAfter(intervalStart) && currentTime.isBefore(intervalEnd);
    }

    public void changeLanguage() throws IOException {
        // Создаем диалоговое окно
        Alert languageDialog = new Alert(Alert.AlertType.CONFIRMATION);
        languageDialog.initModality(Modality.APPLICATION_MODAL);
        languageDialog.setTitle("Выберите язык");
        languageDialog.setHeaderText("Выберите предпочитаемый язык:");

        // Создаем кнопки для разных языков
        ButtonType russianButton = new ButtonType("Русский");
        ButtonType englishButton = new ButtonType("English");
        ButtonType italianButton = new ButtonType("Italiano");

        // Добавляем кнопки в диалоговое окно
        languageDialog.getButtonTypes().setAll(russianButton, englishButton, italianButton);

        // Отображаем диалоговое окно и ждем выбора пользователя
        languageDialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == russianButton) {
                // Установка значения для русского языка
                User.language = "ru";
            } else if (buttonType == englishButton) {
                // Установка значения для английского языка
                User.language = "en";
            } else if (buttonType == italianButton) {
                // Установка значения для итальянского языка
                User.language = "it";
            }
        });

        String updateLanguageQuery = "UPDATE user SET language = ? WHERE login = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(updateLanguageQuery);) {
            preparedStatement.setString(1, User.language);
            preparedStatement.setString(2, User.login);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        SceneLoader.loadNewScene("menu.fxml", MenuRoot);
    }
}
