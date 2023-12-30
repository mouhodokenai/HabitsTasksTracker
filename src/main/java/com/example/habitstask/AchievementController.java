package com.example.habitstask;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.example.habitstask.StockExchangeDB.connection;

public class AchievementController implements Initializable {
    public SplitPane AchievementRoot;
    public Button Back;
    public Label achievementLabel;
    public Button Del;
    public Button Add;
    public ComboBox<String> titleBox;
    public Label yearLabel;
    public RadioButton radio23;
    public RadioButton radio24;

    public static String text;
    public static String title;
    public static int id;
    public static String date;
    public static String year;

    public void AddAchievement() throws IOException {
        year = null;
        String query;
        // Берем данные из полей
        String title = titleBox.getValue();
        ToggleGroup group = new ToggleGroup();
        radio24.setToggleGroup(group);
        radio23.setToggleGroup(group);
        // Обработка выбора радиокнопки
        RadioButton selection = (RadioButton) group.getSelectedToggle();
        try {
            if (selection.getId().equals("radio23")) {
                year = "2023-01-01";
            } else if (selection.getId().equals("radio24")) {
                year = "2024-01-01";
            }
        } catch (Exception e) {
            // Отобразить предупреждение о том, что поле не может быть пустым
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(SceneLoader.getBundle().getString("alAttention"));
            alert.setHeaderText(SceneLoader.getBundle().getString("alEmptyField"));
            alert.showAndWait();

            throw new RuntimeException(e);
        }

        query = "INSERT INTO achievement (title, date, user_login) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Добавляем параметры в запрос
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, year);
            preparedStatement.setString(3, User.login);
            preparedStatement.executeUpdate();
            SceneLoader.loadNewScene("achievements.fxml", AchievementRoot);
        } catch (SQLException | IOException e) {
            if (e.getMessage().contains("Column 'title' cannot be null")) {
                // Отобразить предупреждение о том, что поле 'title' не может быть пустым
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(SceneLoader.getBundle().getString("alAttention"));
                alert.setHeaderText(SceneLoader.getBundle().getString("alEmptyField"));
                alert.showAndWait();
            } else {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String selectQuery = "SELECT * FROM allAchievements;";
        ObservableList<String> achievementsAll = FXCollections.observableArrayList();
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    achievementsAll.add(title);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        titleBox.setItems(achievementsAll);
    }

    public void Back() throws IOException {
        SceneLoader.loadNewScene("achievements.fxml", AchievementRoot);
    }

    public void Menu() throws IOException {
        SceneLoader.loadNewScene("menu.fxml", AchievementRoot);
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
        SceneLoader.loadNewScene("achievement.fxml", AchievementRoot);
    }
}
