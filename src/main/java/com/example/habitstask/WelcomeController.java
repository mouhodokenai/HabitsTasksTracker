package com.example.habitstask;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import static com.example.habitstask.StockExchangeDB.connection;

public class WelcomeController {

    public AnchorPane WelcomeRoot;

    @FXML
    protected void Authorization() throws IOException {
        SceneLoader.loadNewScene("authorization.fxml", WelcomeRoot);
    }
    @FXML
    protected void Registration() throws IOException {
        SceneLoader.loadNewScene("registration.fxml", WelcomeRoot);
    }

    public void changeLanguage() throws IOException {
        // Создаем диалоговое окно
        Alert languageDialog = new Alert(Alert.AlertType.CONFIRMATION);
        languageDialog.initModality(Modality.APPLICATION_MODAL);
        languageDialog.setTitle("Выберите язык");
        languageDialog.setHeaderText(SceneLoader.getBundle().getString("lanTitle"));

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
        SceneLoader.loadNewScene("welcome.fxml", WelcomeRoot);
    }
}