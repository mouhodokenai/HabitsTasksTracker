package com.example.habitstask;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.example.habitstask.StockExchangeDB.connection;

public class NoteController implements Initializable {
    public SplitPane NoteRoot;
    public Button Back;
    public TextField titleField;
    public TextArea textField;
    public Button Add;
    public Button Del;
    public Label noteLabel;

    public static String text;
    public static String title;
    public static int id;
    public static String date;

    public static boolean New = true;


    public void AddNote() throws IOException {
        String query;

        String title = titleField.getText();
        String text = textField.getText();
        if (New){

            query = "INSERT INTO note (title, text, date, user_login) VALUES (?, ?, CURRENT_TIMESTAMP, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, title);
                preparedStatement.setString(2, text);
                preparedStatement.setString(3, User.login);
                preparedStatement.executeUpdate();
                New = true;
                SceneLoader.loadNewScene("notes.fxml", NoteRoot);
            } catch (SQLException | IOException e) {
                if (e.getMessage().contains("Column 'title' cannot be null")) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(SceneLoader.getBundle().getString("alAttention"));
                    alert.setHeaderText(SceneLoader.getBundle().getString("alEmptyField"));
                    alert.showAndWait();
                } else {
                    throw new RuntimeException(e);
                }
            }
        } else {

            query = "UPDATE note SET title = ?, text = ?, date = CURRENT_TIMESTAMP WHERE user_login = ? AND id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, title);
                preparedStatement.setString(2, text);
                preparedStatement.setString(3, User.login);
                preparedStatement.setInt(4, id);
                preparedStatement.executeUpdate();
                New = true;
                SceneLoader.loadNewScene("notes.fxml", NoteRoot);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void DeleteNote() throws IOException {
        String query;
        if (!New) {

            query = "DELETE FROM note WHERE id = ?";

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(SceneLoader.getBundle().getString("alAttention"));
            alert.setHeaderText(SceneLoader.getBundle().getString("alDelete"));
            alert.setContentText(SceneLoader.getBundle().getString("alDeleteNote"));

            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();
                    SceneLoader.loadNewScene("notes.fxml", NoteRoot);
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public void Back() throws IOException {
        New = true;
        SceneLoader.loadNewScene("notes.fxml", NoteRoot);
    }

    public void Menu() throws IOException {
        SceneLoader.loadNewScene("menu.fxml", NoteRoot);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Del.setVisible(!New);
        if (!New) {
            titleField.setText(title);
            textField.setText(text);
            noteLabel.setText(date);
        } else {
            textField.setText(null);
            titleField.setText(null);
        }
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
        SceneLoader.loadNewScene("note.fxml", NoteRoot);
    }
}
