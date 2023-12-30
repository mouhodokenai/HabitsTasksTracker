package com.example.habitstask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import org.mindrot.jbcrypt.BCrypt;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static com.example.habitstask.StockExchangeDB.connection;
import static com.example.habitstask.StockExchangeDB.statement;

public class RegistrationController {

    public AnchorPane RegistrationRoot;
    public TextField userNameField;
    public TextField loginField;
    public PasswordField passwordField;

    static String query;

    @FXML
    protected void Login() throws IOException {
        if (!Objects.equals(loginField.getText(), "") && !Objects.equals(passwordField.getText(), "") && !Objects.equals(userNameField.getText(), "")){
            String name = userNameField.getText();
            String login = loginField.getText();
            String password = passwordField.getText();
            String hashPassword = hashPassword(password);
            query = "INSERT INTO user (name, login, password, autho, language) VALUES (?, ?, ?, ?, 'ru')";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, login);
                preparedStatement.setString(3, hashPassword);
                preparedStatement.setBoolean(4, true);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {

                    User.name = name;
                    User.login = login;
                    SceneLoader.loadNewScene("menu.fxml", RegistrationRoot);
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate entry")) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(SceneLoader.getBundle().getString("alAttention"));
                    alert.setHeaderText(SceneLoader.getBundle().getString("alRegErr"));
                    alert.showAndWait();
                } else {
                    throw new RuntimeException(e);
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(SceneLoader.getBundle().getString("alAttention"));
            alert.setHeaderText(SceneLoader.getBundle().getString("alEmptyField"));
            alert.showAndWait();
        }

    }


    @FXML
    protected void Back() throws IOException {
        SceneLoader.loadNewScene("welcome.fxml", RegistrationRoot);
    }

    public static String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
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
        SceneLoader.loadNewScene("registration.fxml", RegistrationRoot);
    }
}
