package com.example.habitstask;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.example.habitstask.StockExchangeDB.connection;

public class NotesController implements Initializable {
    public SplitPane NotesRoot;
    public Button Back;
    public ListView<Note> NotesListview;
    //Map<Integer, String> dataMap = new HashMap<>();
    ObservableList<Note> items = FXCollections.observableArrayList();

    @FXML
    protected void Back() throws IOException {
        SceneLoader.loadNewScene("menu.fxml", NotesRoot);
    }

    @FXML
    protected void Add() throws IOException {
        SceneLoader.loadNewScene("note.fxml", NotesRoot);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Clicks on an item
        NotesListview.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Getting the selected item
                Note selectedItem = NotesListview.getSelectionModel().getSelectedItem();

                System.out.println("Selected Item: " + selectedItem);
                NoteController.New = false;
                NoteController.title = selectedItem.getTitle();
                NoteController.text = selectedItem.getText();
                NoteController.id = selectedItem.getId();
                NoteController.date = selectedItem.getDate();
                try {
                    SceneLoader.loadNewScene("note.fxml", NotesRoot);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        // Setting the parameters in the ListView
        NotesListview.setItems(items);
        String selectQuery = "SELECT * FROM note WHERE user_login = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, User.login);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String text = resultSet.getString("text");
                    String date = resultSet.getString("date");

                    Note note = new Note(id, title, text, date);
                    items.add(note);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        SceneLoader.loadNewScene("notes.fxml", NotesRoot);
    }
}