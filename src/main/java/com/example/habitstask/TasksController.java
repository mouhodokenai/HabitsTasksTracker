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
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import static com.example.habitstask.StockExchangeDB.connection;

public class TasksController implements Initializable {
    public SplitPane TasksRoot;
    public Button Back;
    public TableView<Task> tasksTableView;
    public TableColumn<Task, Button> buttonColumn;
    public TableColumn<Task, String> titleColumn;
    public TableColumn<Task, String> dateColumn;
    public TableColumn<Task, String> warningColumn;
    public Tab done;
    public ListView<Task> tasksListview;

    ObservableList<Task> doneTasks = FXCollections.observableArrayList();
    ObservableList<Task> allTasks = FXCollections.observableArrayList();
    ObservableList<Task> nowTasks = FXCollections.observableArrayList();

    @FXML
    protected void Add() throws IOException {
        SceneLoader.loadNewScene("task.fxml", TasksRoot);
    }

    public void Back() throws IOException {
        SceneLoader.loadNewScene("menu.fxml", TasksRoot);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonColumn.setCellFactory(col -> {
            return new TableCell<Task, Button>() {
                @Override
                protected void updateItem(Button button, boolean empty) {
                    super.updateItem(button, empty);

                    if (empty || button == null) {
                        setGraphic(null);
                    } else {
                        setGraphic(button);

                        // Добавление слушателя событий для каждой кнопки
                        button.setOnAction(event -> {
                            Task task = getTableView().getItems().get(getIndex());
                            // Обработка нажатия на кнопку для конкретной привычки (habit)
                            String query = "UPDATE tasks SET done = true, date_now = CURRENT_TIMESTAMP WHERE user_login = ? AND id = ?";

                            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                                preparedStatement.setString(1, User.login);
                                preparedStatement.setInt(2, task.getId());
                                preparedStatement.executeUpdate();
                                task.setDone(true);
                                task.setMakeDone(true);
                                tasksTableView.refresh();

                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                        });
                    }
                }
            };
        });


        tasksTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Task selectedItem = tasksTableView.getSelectionModel().getSelectedItem();

                System.out.println("Selected Item: " + selectedItem);
                TaskController.New = false;
                TaskController.title = selectedItem.getTitle();
                TaskController.id = selectedItem.getId();
                TaskController.dateF = selectedItem.getDateF();
                try {
                    SceneLoader.loadNewScene("task.fxml", TasksRoot);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        titleColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("title"));
        buttonColumn.setCellValueFactory(new PropertyValueFactory<Task, Button>("button"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("dateF"));
        warningColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("warning"));

        tasksTableView.setItems(nowTasks);
        tasksListview.setItems(doneTasks);

        String selectQuery1 = "SELECT * FROM tasks WHERE user_login = ? and done = 0";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery1)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Task task;
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    boolean done = resultSet.getBoolean("done");
                    String dateS = resultSet.getString("date_now");
                    try {
                        LocalDate dateF = resultSet.getDate("date_finish").toLocalDate();
                        task = new Task(id, title, dateS, dateF, done);
                        task.setWarning(resultSet.getDate("date_finish"));
                        System.out.println(task.getWarning());

                    } catch (SQLException e) {
                        task = new Task(id, title, dateS, done);
                    }
                    Button button = new Button("✔");
                    button.getStyleClass().add("round-button");
                    task.setButton(button);
                    nowTasks.add(task);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String selectQuery2 = "SELECT * FROM tasks WHERE user_login = ? and done = 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery2)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Task task;
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    boolean done = resultSet.getBoolean("done");
                    String dateS = resultSet.getString("date_now");
                    try {
                        LocalDate dateF = resultSet.getDate("date_finish").toLocalDate();
                        task = new Task(id, title, dateS, dateF, done);
                        task.setWarning(resultSet.getDate("date_finish"));

                    } catch (SQLException e) {
                        task = new Task(id, title, dateS, done);
                    }
                    doneTasks.add(task);
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
        SceneLoader.loadNewScene("tasks.fxml", TasksRoot);
    }
}
