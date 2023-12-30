package com.example.habitstask;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.time.LocalDate;

import static com.example.habitstask.StockExchangeDB.connection;

public class AchievementsController implements Initializable {

    public SplitPane AchievementsRoot;
    public Button Back;
    public Button Add;
    public Tab y2023;
    public TableView<Achievement> TableView23;
    public TableColumn<Achievement, Button> buttonColumn23;
    public TableColumn<Achievement, String> titleColumn23;
    public TableColumn<Achievement, String> doneColumn23;
    public Tab y2024;
    public TableView<Achievement> TableView24;
    public TableColumn<Achievement, Button> buttonColumn24;
    public TableColumn<Achievement, String> titleColumn24;
    public TableColumn<Achievement, String> doneColumn24;

    ObservableList<Achievement> list23 = FXCollections.observableArrayList();
    ObservableList<Achievement> list24 = FXCollections.observableArrayList();

    @FXML
    protected void Add() throws IOException {
        SceneLoader.loadNewScene("achievement.fxml", AchievementsRoot);
    }

    public void Back() throws IOException {
        SceneLoader.loadNewScene("menu.fxml", AchievementsRoot);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Получить текущую дату
        LocalDate currentDate = LocalDate.now();
        // Получить текущий год из текущей даты
        int currentYear = currentDate.getYear();
        // Создание слушателя событий для кнопок
        buttonColumn23.setCellFactory(col -> {
            return new TableCell<Achievement, Button>() {
                @Override
                protected void updateItem(Button button, boolean empty) {
                    super.updateItem(button, empty);

                    if (empty || button == null) {
                        setGraphic(null);
                    } else {
                        setGraphic(button);
                        // Добавление слушателя событий для каждой кнопки
                        button.setOnAction(event -> {
                            Achievement achievement = getTableView().getItems().get(getIndex());
                            // Обработка нажатия на кнопку для конкретной привычки (habit)
                            String query = "UPDATE achievement SET done = true, date = CURRENT_TIMESTAMP WHERE user_login = ? AND id = ?";

                            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                                // Adding parameters to the query
                                preparedStatement.setString(1, User.login);
                                preparedStatement.setInt(2, achievement.getId());
                                preparedStatement.executeUpdate();
                                achievement.setDone(true);
                                achievement.setMakeDone(true);
                                TableView23.refresh();
                                //habitsDays.
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                        });
                    }
                }
            };
        });


        TableView23.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Получаем выбранный элемент
                Achievement selectedItem = TableView23.getSelectionModel().getSelectedItem();

                System.out.println("Selected Item: " + selectedItem);
                AchievementController.title = selectedItem.getTitle();
                AchievementController.id = selectedItem.getId();
                AchievementController.date = selectedItem.getDate();
                String delQuery = "DELETE FROM achievement WHERE id = ?";
                // Create a confirmation dialog
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                try {
                    alert.setTitle(SceneLoader.getBundle().getString("alAttention"));
                    alert.setHeaderText(SceneLoader.getBundle().getString("alDelete"));
                    alert.setContentText(SceneLoader.getBundle().getString("alDeleteAch"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Результат нажатия
                ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

                if (result == ButtonType.OK) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(delQuery)) {

                        preparedStatement.setInt(1, AchievementController.id);
                        preparedStatement.executeUpdate();
                        list23.remove(selectedItem);
                        TableView23.refresh();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        TableView24.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Achievement selectedItem = TableView24.getSelectionModel().getSelectedItem();

                System.out.println("Selected Item: " + selectedItem);
                AchievementController.title = selectedItem.getTitle();
                AchievementController.id = selectedItem.getId();
                AchievementController.date = selectedItem.getDate();
                String delQuery = "DELETE FROM achievement WHERE id = ?";

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Удаление");
                alert.setHeaderText("Удаление достижения");
                alert.setContentText("Вы точно хотите удалить достижение?");


                ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

                if (result == ButtonType.OK) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(delQuery)) {

                        preparedStatement.setInt(1, AchievementController.id);
                        preparedStatement.executeUpdate();
                        list24.remove(selectedItem);
                        TableView24.refresh();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });


        titleColumn23.setCellValueFactory(new PropertyValueFactory<Achievement, String>("title"));
        buttonColumn23.setCellValueFactory(new PropertyValueFactory<Achievement, Button>("button"));
        doneColumn23.setCellValueFactory(new PropertyValueFactory<Achievement, String>("makeDone"));

        titleColumn24.setCellValueFactory(new PropertyValueFactory<Achievement, String>("title"));
        doneColumn24.setCellValueFactory(new PropertyValueFactory<Achievement, String>("makeDone"));
        // Передаем параметры в листВью
        TableView23.setItems(list23);
        TableView24.setItems(list24);

        String selectQuery23 = "SELECT * FROM achievement WHERE user_login = ? AND date < '2023-12-31'";
        String selectQuery24 = "SELECT * FROM achievement WHERE user_login = ? AND date > '2023-12-31'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery23)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String date = resultSet.getString("date");
                    boolean done = resultSet.getBoolean("done");
                    Achievement achievement = new Achievement(id, title, date, done);
                    achievement.setMakeDone(done);
                    Button button = new Button("✔");
                    button.getStyleClass().add("round-button");
                    achievement.setButton(button);
                    list23.add(achievement);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery24)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String date = resultSet.getString("date");
                    boolean done = resultSet.getBoolean("done");
                    Achievement achievement = new Achievement(id, title, date, done);
                    achievement.setMakeDone(done);
                    Button button = new Button("✔");
                    button.getStyleClass().add("round-button");
                    achievement.setButton(button);
                    list24.add(achievement);
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
            preparedStatement.setString(2, User.login); // Предположим, что у вас есть переменная User.login
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        SceneLoader.loadNewScene("achievements.fxml", AchievementsRoot);
    }
}
