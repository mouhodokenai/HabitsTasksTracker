package com.example.habitstask;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import static com.example.habitstask.StockExchangeDB.connection;

public class TaskController implements Initializable {
    public SplitPane TaskRoot;
    public Button Back;
    public Label taskLabel;
    public Button Del;
    public Button Add;
    public TextField titleField;
    public javafx.scene.control.Label Label;
    public DatePicker datePicker;
    public Label Label1;
    public static boolean New = true;

    public static int id;
    public static String title;
    public static String dateS;
    public static LocalDate dateF;
    public static Button button;
    public static String warning;

    public void Back() throws IOException {
        SceneLoader.loadNewScene("tasks.fxml", TaskRoot);
        New = true;
    }

    public void Menu() throws IOException {
        SceneLoader.loadNewScene("menu.fxml", TaskRoot);
    }

    public void DeleteTask() throws IOException {
        String query;
        if (!New) {
            // Creating a sql query to delete
            query = "DELETE FROM tasks WHERE id = ?";
            // Create a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(SceneLoader.getBundle().getString("alAttention"));
            alert.setHeaderText(SceneLoader.getBundle().getString("alDelete"));
            alert.setContentText(SceneLoader.getBundle().getString("alDeleteTask"));

            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();
                    SceneLoader.loadNewScene("tasks.fxml", TaskRoot);
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



    public void AddTask(ActionEvent actionEvent) throws IOException {

        String query;
        if (New) {

            String title = titleField.getText();

            if (datePicker!=null){
                query = "INSERT INTO tasks (title, date_now, date_finish, user_login, done) VALUES (?, CURRENT_TIMESTAMP, ?, ?, false)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setString(1, title);
                    preparedStatement.setString(2, String.valueOf(datePicker.getValue()));
                    preparedStatement.setString(3, User.login);
                    preparedStatement.executeUpdate();
                    SceneLoader.loadNewScene("tasks.fxml", TaskRoot);
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
                query = "INSERT INTO tasks (title, date_now, user_login, done) VALUES (?, CURRENT_TIMESTAMP, ?, false)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setString(1, title);
                    preparedStatement.setString(2, String.valueOf(datePicker.getValue()));
                    preparedStatement.setString(3, User.login);
                    preparedStatement.executeUpdate();
                    SceneLoader.loadNewScene("tasks.fxml", TaskRoot);
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
            }
        } else {

            String title = titleField.getText();

            if (datePicker!=null){
                query = "UPDATE tasks set title = ?, date_now = CURRENT_TIMESTAMP, date_finish = ? WHERE user_login = ? AND id = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setString(1, title);
                    preparedStatement.setString(2, String.valueOf(datePicker.getValue()));
                    preparedStatement.setString(3, User.login);
                    preparedStatement.setInt(4, id);
                    preparedStatement.executeUpdate();
                    SceneLoader.loadNewScene("tasks.fxml", TaskRoot);
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

            } else {
                query = "UPDATE tasks set title = ?, date_start = CURRENT_TIMESTAMP WHERE user_login = ? AND id = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setString(1, title);
                    preparedStatement.setInt(4, id);
                    preparedStatement.setString(3, User.login);
                    preparedStatement.executeUpdate();
                    SceneLoader.loadNewScene("tasks.fxml", TaskRoot);
                    New = true;
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
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Del.setVisible(!New);
        if (!New) {
            titleField.setText(title);
            taskLabel.setText(title);
            datePicker.setValue(dateF);
        } else {
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
            preparedStatement.setString(2, User.login); // Предположим, что у вас есть переменная User.login
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        SceneLoader.loadNewScene("task.fxml", TaskRoot);
    }
}
