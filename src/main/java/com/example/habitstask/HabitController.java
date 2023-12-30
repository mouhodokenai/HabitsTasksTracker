package com.example.habitstask;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.habitstask.StockExchangeDB.connection;

public class HabitController implements Initializable {
    public SplitPane HabitRoot;
    public Button Back;
    public TextField titleField;

    public Button Add;
    public Button Del;
    public Label habitLabel;

    public static String text;
    public static String title;
    public static int id;
    public static int type;
    public static LocalDate date;

    public static boolean New = true;
    public Label typeLabel;
    public RadioButton daysRadio;
    public RadioButton weeksRadio;
    public RadioButton monthRadio;
    public RadioButton badRadio;
    public DatePicker datePicker;

    public static boolean isBad = false;
    String selectedDate;

    public void AddNote() throws IOException {
        type = 0;
        String query;

        String title = titleField.getText();
        ToggleGroup group = new ToggleGroup();
        daysRadio.setToggleGroup(group);
        weeksRadio.setToggleGroup(group);
        monthRadio.setToggleGroup(group);
        badRadio.setToggleGroup(group);

        RadioButton selection = (RadioButton) group.getSelectedToggle();
        try {
            if (selection.getId().equals("daysRadio")) {
                type = 1;
            } else if (selection.getId().equals("weeksRadio")) {
                type = 2;
            } else if (selection.getId().equals("monthRadio")) {
                type = 3;
            } else if (selection.getId().equals("badRadio")) {
                type = 4;
                isBad = true;
            }
        } catch (Exception e) {
            // Отобразить предупреждение о том, что поле 'title' не может быть пустым
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(SceneLoader.getBundle().getString("alAttention"));
            alert.setHeaderText(SceneLoader.getBundle().getString("alEmptyField"));
            alert.showAndWait();

            throw new RuntimeException(e);
        }


        if (New) {

            if (isBad && datePicker.getValue() != null) {
                query = "INSERT INTO habit (title, date, periodicity, user_login) VALUES (?, ?, ?, ?)";
                selectedDate = datePicker.getValue().toString();
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setString(1, title);
                    preparedStatement.setInt(3, type);
                    preparedStatement.setString(4, User.login);
                    preparedStatement.setString(2, selectedDate);
                    preparedStatement.executeUpdate();
                    New = true;
                    SceneLoader.loadNewScene("habits.fxml", HabitRoot);
                } catch (SQLException | IOException e) {
                    // Отобразить предупреждение о том, что поле не может быть пустым
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(SceneLoader.getBundle().getString("alAttention"));
                    alert.setHeaderText(SceneLoader.getBundle().getString("alEmptyField"));
                    alert.showAndWait();

                    throw new RuntimeException(e);

                }
            } else {
                query = "INSERT INTO habit (title, date, periodicity, user_login) VALUES (?, CURRENT_TIMESTAMP, ?, ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setString(1, title);
                    preparedStatement.setInt(2, type);
                    preparedStatement.setString(3, User.login);
                    preparedStatement.executeUpdate();
                    New = true;
                    SceneLoader.loadNewScene("habits.fxml", HabitRoot);
                } catch (SQLException | IOException e) {
                    // Отобразить предупреждение о том, что поле не может быть пустым
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(SceneLoader.getBundle().getString("alAttention"));
                    alert.setHeaderText(SceneLoader.getBundle().getString("alEmptyField"));
                    alert.showAndWait();

                    throw new RuntimeException(e);
                }
            }

        } else {

            if (isBad && datePicker.getValue() != null) {
                query = "UPDATE habit SET title = ?, periodicity = ?, date = ?, user_login = ? AND id = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setString(1, title);
                    preparedStatement.setInt(2, type);
                    preparedStatement.setString(4, User.login);
                    preparedStatement.setDate(3, Date.valueOf(datePicker.getValue()));
                    preparedStatement.setInt(5, id);
                    preparedStatement.executeUpdate();
                    New = true;
                    SceneLoader.loadNewScene("habits.fxml", HabitRoot);
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                query = "UPDATE habit SET title = ?, periodicity = ?, date = CURRENT_TIMESTAMP WHERE user_login = ? AND id = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setString(1, title);
                    preparedStatement.setInt(2, type);
                    preparedStatement.setString(3, User.login);
                    preparedStatement.setInt(4, id);
                    preparedStatement.executeUpdate();
                    New = true;
                    SceneLoader.loadNewScene("habits.fxml", HabitRoot);
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public void DeleteNote() throws IOException {
        String query;
        if (!New) {

            query = "DELETE FROM habit WHERE id = ?";

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(SceneLoader.getBundle().getString("alAttention"));
            alert.setHeaderText(SceneLoader.getBundle().getString("alDelete"));
            alert.setContentText(SceneLoader.getBundle().getString("alDeleteHabit"));


            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();
                    SceneLoader.loadNewScene("habits.fxml", HabitRoot);
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void Back() throws IOException {
        New = true;
        SceneLoader.loadNewScene("habits.fxml", HabitRoot);
    }

    public void Menu() throws IOException {
        SceneLoader.loadNewScene("menu.fxml", HabitRoot);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isBad = false;
        Del.setVisible(!New);
        if (!New) {
            titleField.setText(title);
            habitLabel.setText(title);
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
            preparedStatement.setString(2, User.login);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        SceneLoader.loadNewScene("habit.fxml", HabitRoot);
    }
}