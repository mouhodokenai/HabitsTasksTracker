package com.example.habitstask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static com.example.habitstask.StockExchangeDB.connection;

public class AuthorizationController {

    public AnchorPane AuthorizationRoot;
    public TextField loginField;
    public PasswordField passwordField;

    static String query;

    @FXML
    protected void Login() throws IOException {
        if (!Objects.equals(loginField.getText(), "") && !Objects.equals(passwordField.getText(), "")){
            String login = loginField.getText();
            String password = passwordField.getText();

            query = "SELECT * FROM user WHERE login = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, login);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String hashedPasswordFromDB = resultSet.getString("password");
                    User.name = resultSet.getString("name");
                    User.login = login;
                    if (BCrypt.checkpw(password, hashedPasswordFromDB)){

                        String updateQuery = "UPDATE user SET autho = true WHERE login = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setString(1, login);
                            updateStatement.executeUpdate();
                            System.out.println(updateStatement);
                        }

                        SceneLoader.loadNewScene("menu.fxml", AuthorizationRoot);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle(SceneLoader.getBundle().getString("alAttention"));
                        alert.setHeaderText(SceneLoader.getBundle().getString("alAuthoErr"));
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(SceneLoader.getBundle().getString("alAttention"));
                    alert.setHeaderText(SceneLoader.getBundle().getString("alAuthoErr"));
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
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
        SceneLoader.loadNewScene("welcome.fxml", AuthorizationRoot);
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
        SceneLoader.loadNewScene("authorization.fxml", AuthorizationRoot);
    }
}
