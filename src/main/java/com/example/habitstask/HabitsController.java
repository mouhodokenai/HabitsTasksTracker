package com.example.habitstask;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.habitstask.StockExchangeDB.connection;

public class HabitsController implements Initializable {

    public SplitPane HabitsRoot;
    public Button Back;

    public Tab days;
    public Tab weeks;
    public Tab months;
    public Tab bad;
    public Button Add;

    public ListView<Habit> HabitsDaysListview;
    public ListView<Habit> HabitsWeeksListview;
    public ListView<Habit> HabitsMonthsListview;
    public ListView<Habit> BadHabitsListview;
    public TableColumn<Habit, Button> buttonColumn;
    public TableColumn<Habit, String> titleColumn;
    public TableColumn<Habit, String> doneColumn;
    public TableView<Habit> daysTableView;
    public TableColumn<Habit, String> dateColumn4;
    public TableColumn<Habit, String> titleColumn4;
    public TableView<Habit> badTableView;
    public TableView<Habit> monthsTableView;
    public TableColumn<Habit, Button> buttonColumn3;
    public TableColumn<Habit, String> titleColumn3;
    public TableColumn<Habit, String> doneColumn3;
    public TableColumn<Habit, Button> buttonColumn2;
    public TableColumn<Habit, String> titleColumn2;
    public TableColumn<Habit, String> doneColumn2;
    public TableView<Habit> weeksTableView;

    ObservableList<Habit> allHabits = FXCollections.observableArrayList();
    ObservableList<Habit> habitsDays = FXCollections.observableArrayList();
    ObservableList<Habit> habitsWeeks = FXCollections.observableArrayList();
    ObservableList<Habit> habitsMonths= FXCollections.observableArrayList();
    ObservableList<Habit> badHabits = FXCollections.observableArrayList();

    @FXML
    protected void Add() throws IOException {
        SceneLoader.loadNewScene("habit.fxml", HabitsRoot);
    }

    public void Back() throws IOException {
        SceneLoader.loadNewScene("menu.fxml", HabitsRoot);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Date currentDate = new Date();
        resetDoneValuesForNewWeek();
        resetDoneValuesForNewDay();
        resetDoneValuesForNewMonth();

        // Создание слушателя событий для кнопок
        buttonColumn.setCellFactory(col -> {
            return new TableCell<Habit, Button>() {
                @Override
                protected void updateItem(Button button, boolean empty) {
                    super.updateItem(button, empty);

                    if (empty || button == null) {
                        setGraphic(null);
                    } else {
                        setGraphic(button);

                        // Добавление слушателя событий для каждой кнопки
                        button.setOnAction(event -> {
                            Habit habit = getTableView().getItems().get(getIndex());
                            // Обработка нажатия на кнопку для конкретной привычки (habit)
                            String query = "UPDATE habit SET done = true, date = CURRENT_TIMESTAMP WHERE user_login = ? AND id = ?";

                            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                                preparedStatement.setString(1, User.login);
                                preparedStatement.setInt(2, habit.getId());
                                preparedStatement.executeUpdate();
                                habit.setDone(true);
                                habit.setMakeDone(true);
                                daysTableView.refresh();

                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("Кнопка нажата для привычки: " + habit.getTitle());

                        });
                    }
                }
            };
        });
        daysTableView.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Habit selectedItem = daysTableView.getSelectionModel().getSelectedItem();

                System.out.println("Selected Item: " + selectedItem);
                HabitController.New = false;
                HabitController.title = selectedItem.getTitle();
                HabitController.type = selectedItem.getPeriodicity();
                HabitController.id = selectedItem.getId();
                HabitController.date = selectedItem.getDate();
                try {
                    SceneLoader.loadNewScene("habit.fxml", HabitsRoot);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        buttonColumn2.setCellFactory(col -> {
            return new TableCell<Habit, Button>() {
                @Override
                protected void updateItem(Button button, boolean empty) {
                    super.updateItem(button, empty);

                    if (empty || button == null) {
                        setGraphic(null);
                    } else {
                        setGraphic(button);

                        // Добавление слушателя событий для каждой кнопки
                        button.setOnAction(event -> {
                            Habit habit = getTableView().getItems().get(getIndex());
                            // Обработка нажатия на кнопку для конкретной привычки (habit)
                            String query = "UPDATE habit SET done = true, date = CURRENT_TIMESTAMP WHERE user_login = ? AND id = ?";

                            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                                // Adding parameters to the query
                                preparedStatement.setString(1, User.login);
                                preparedStatement.setInt(2, habit.getId());
                                preparedStatement.executeUpdate();
                                habit.setDone(true);
                                habit.setMakeDone(true);
                                weeksTableView.refresh();
                                //habitsDays.
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("Кнопка нажата для привычки: " + habit.getTitle());

                        });
                    }
                }
            };
        });
        weeksTableView.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Habit selectedItem = weeksTableView.getSelectionModel().getSelectedItem();

                System.out.println("Selected Item: " + selectedItem);
                HabitController.New = false;
                HabitController.title = selectedItem.getTitle();
                HabitController.type = selectedItem.getPeriodicity();
                HabitController.id = selectedItem.getId();
                HabitController.date = selectedItem.getDate();
                try {
                    SceneLoader.loadNewScene("habit.fxml", HabitsRoot);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        buttonColumn3.setCellFactory(col -> {
            return new TableCell<Habit, Button>() {
                @Override
                protected void updateItem(Button button, boolean empty) {
                    super.updateItem(button, empty);

                    if (empty || button == null) {
                        setGraphic(null);
                    } else {
                        setGraphic(button);

                        // Добавление слушателя событий для каждой кнопки
                        button.setOnAction(event -> {
                            Habit habit = getTableView().getItems().get(getIndex());
                            // Обработка нажатия на кнопку для конкретной привычки (habit)
                            String query = "UPDATE habit SET done = true, date = CURRENT_TIMESTAMP WHERE user_login = ? AND id = ?";

                            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                                // Adding parameters to the query
                                preparedStatement.setString(1, User.login);
                                preparedStatement.setInt(2, habit.getId());
                                preparedStatement.executeUpdate();
                                habit.setDone(true);
                                habit.setMakeDone(true);
                                monthsTableView.refresh();

                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("Кнопка нажата для привычки: " + habit.getTitle());

                        });
                    }
                }
            };
        });
        monthsTableView.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Habit selectedItem = monthsTableView.getSelectionModel().getSelectedItem();

                System.out.println("Selected Item: " + selectedItem);
                HabitController.New = false;
                HabitController.title = selectedItem.getTitle();
                HabitController.type = selectedItem.getPeriodicity();
                HabitController.id = selectedItem.getId();
                HabitController.date = selectedItem.getDate();
                try {
                    SceneLoader.loadNewScene("habit.fxml", HabitsRoot);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        badTableView.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Habit selectedItem = badTableView.getSelectionModel().getSelectedItem();

                if (selectedItem != null) {
                    // Отобразить окно подтверждения перед удалением
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Удаление");
                    alert.setHeaderText("Удаление или обнуление привычки");
                    alert.setContentText("Выберите действие:");

                    // Создать кнопки "Удалить" и "Обнулить"
                    ButtonType deleteButton = new ButtonType("Удалить");
                    ButtonType resetButton = new ButtonType("Обнулить");
                    ButtonType cancelButton = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

                    alert.getButtonTypes().setAll(deleteButton, resetButton, cancelButton);

                    // Получить результат нажатия кнопки
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent()) {
                        String query;
                        if (result.get() == deleteButton) {
                            // Удаление
                            query = "DELETE FROM habit WHERE id = ?";
                            badHabits.remove(selectedItem);
                        } else if (result.get() == resetButton) {
                            // Обнуление (изменение даты на текущую)
                            query = "UPDATE habit SET date = CURRENT_DATE WHERE id = ?";
                            badHabits.remove(selectedItem);
                            selectedItem.setHowDays(0);
                            badHabits.add(selectedItem);

                        } else {
                            // Отмена
                            return;
                        }

                        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                            // Добавление параметров к запросу
                            preparedStatement.setInt(1, selectedItem.getId());
                            preparedStatement.executeUpdate();
                            badTableView.refresh();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        titleColumn.setCellValueFactory(new PropertyValueFactory<Habit, String>("title"));
        buttonColumn.setCellValueFactory(new PropertyValueFactory<Habit, Button>("button"));
        doneColumn.setCellValueFactory(new PropertyValueFactory<Habit, String>("makeDone"));

        titleColumn2.setCellValueFactory(new PropertyValueFactory<Habit, String>("title"));
        buttonColumn2.setCellValueFactory(new PropertyValueFactory<Habit, Button>("button"));
        doneColumn2.setCellValueFactory(new PropertyValueFactory<Habit, String>("makeDone"));

        titleColumn3.setCellValueFactory(new PropertyValueFactory<Habit, String>("title"));
        buttonColumn3.setCellValueFactory(new PropertyValueFactory<Habit, Button>("button"));
        doneColumn3.setCellValueFactory(new PropertyValueFactory<Habit, String>("makeDone"));

        titleColumn4.setCellValueFactory(new PropertyValueFactory<Habit, String>("title"));
        dateColumn4.setCellValueFactory(new PropertyValueFactory<Habit, String>("howDays"));

        daysTableView.setItems(habitsDays);
        weeksTableView.setItems(habitsWeeks);
        monthsTableView.setItems(habitsMonths);
        badTableView.setItems(badHabits);

        String selectQuery1 = "SELECT * FROM habit WHERE user_login = ? AND periodicity = 1";
        String selectQuery2 = "SELECT * FROM habit WHERE user_login = ? AND periodicity = 2";
        String selectQuery3 = "SELECT * FROM habit WHERE user_login = ? AND periodicity = 3";
        String selectQuery4 = "SELECT * FROM habit WHERE user_login = ? AND periodicity = 4";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery1)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    int periodicity = resultSet.getInt("periodicity");
                    LocalDate date = resultSet.getDate("date").toLocalDate();
                    boolean done = resultSet.getBoolean("done");
                    Habit habit = new Habit(id, title, periodicity, date, done);
                    habit.setMakeDone(done);
                    Button button = new Button("✔");
                    button.getStyleClass().add("round-button");
                    habit.setButton(button);
                    habit.getDaysBetweenDates(date);
                    habitsDays.add(habit);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery2)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    int periodicity = resultSet.getInt("periodicity");
                    LocalDate date = resultSet.getDate("date").toLocalDate();
                    boolean done = resultSet.getBoolean("done");
                    Habit habit = new Habit(id, title, periodicity, date, done);
                    habit.setMakeDone(done);
                    Button button1 = new Button("✔");
                    button1.getStyleClass().add("round-button");
                    habit.setButton(button1);
                    habit.getDaysBetweenDates(date);
                    habitsWeeks.add(habit);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery3)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    int periodicity = resultSet.getInt("periodicity");
                    LocalDate date = LocalDate.parse(resultSet.getString("date"));
                    boolean done = resultSet.getBoolean("done");
                    Habit habit = new Habit(id, title, periodicity, date, done);
                    habit.setMakeDone(done);
                    Button button1 = new Button("✔");
                    button1.getStyleClass().add("round-button");
                    habit.setButton(button1);
                    habit.getDaysBetweenDates(date);
                    habitsMonths.add(habit);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery4)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    int periodicity = resultSet.getInt("periodicity");
                    LocalDate date = LocalDate.parse(resultSet.getString("date"));
                    boolean done = resultSet.getBoolean("done");
                    Habit habit = new Habit(id, title, periodicity, date, done);
                    Button button1 = new Button("✔");
                    button1.getStyleClass().add("round-button");
                    habit.setButton(button1);
                    habit.getDaysBetweenDates(date);
                    badHabits.add(habit);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Сброс значения done на false для всех записей при наступлении новой недели/дня/месяца
    public void resetDoneValuesForNewWeek() {
        String updateQuery = "UPDATE habit SET done = false WHERE user_login = ? AND WEEK(date) != WEEK(NOW())";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, User.login);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void resetDoneValuesForNewDay() {
        String updateQuery = "UPDATE habit SET done = false WHERE user_login = ? AND DATE(date) != CURDATE()";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, User.login);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void resetDoneValuesForNewMonth() {
        String updateQuery = "UPDATE habit SET done = false WHERE user_login = ? AND MONTH(date) != MONTH(NOW())";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, User.login);
            preparedStatement.executeUpdate();
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
        SceneLoader.loadNewScene("habits.fxml", HabitsRoot);
    }
}
