package com.example.habitstask;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.sql.ResultSet;


import static com.example.habitstask.StockExchangeDB.connection;

public class StatsController implements Initializable {
    public SplitPane StatsRoot;
    public Button Back;
    public Label title;
    public Label tasks;
    public Label habits;
    public Label achievements;
    public Label notes;

    public int taskAll;
    public int taskInTime;
    public int taskDone;

    public int notesAll;

    public int habitsAll;
    public int habitsDay;
    public int habitsWeek;
    public int habitsMonth;
    public int habitsBad;

    public int achAll;
    public int achNowYear;
    public int achNextYear;
    public int achDone;

    public Label taskAllLabel;
    public Label taskInTimeLabel;
    public Label taskDoneLabel;

    public Label notesAllLabel;

    public Label habitsAllLabel;

    public Label achAllLabel;
    public Label achNowYearLabel;
    public Label achNextYearLabel;
    public Label achDoneLabel;

    public Label habitsDayLabel;
    public Label habitsWeekLabel;
    public Label habitsMonthLabel;
    public Label habitsBadLabel;


    public void changeLanguage(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String sql1 = "SELECT COUNT(*) FROM tasks WHERE user_login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql1)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    taskAll = resultSet.getInt(1);
                    System.out.println("Количество записей: " + taskAll);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql2 = "SELECT COUNT(*) FROM note WHERE user_login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql2)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    notesAll = resultSet.getInt(1);
                    System.out.println("Количество записей: " + notesAll);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql3 = "SELECT COUNT(*) FROM habit WHERE user_login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql3)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    habitsAll = resultSet.getInt(1);
                    System.out.println("Количество записей: " + notesAll);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql4 = "SELECT COUNT(*) FROM achievement WHERE user_login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql4)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    achAll = resultSet.getInt(1);
                    System.out.println("Количество записей: " + achAll);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql5 = "SELECT COUNT(*) FROM tasks WHERE date_now < date_finish AND user_login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql5)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    taskInTime = resultSet.getInt(1);
                    System.out.println("Количество записей: " + taskInTime);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql6 = "SELECT COUNT(*) FROM tasks WHERE done = 1 AND user_login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql6)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    taskDone = resultSet.getInt(1);
                    System.out.println("Количество записей: " + taskDone);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql7 = "SELECT COUNT(*) FROM achievement WHERE date < '2024-01-01' AND user_login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql7)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    achNowYear = resultSet.getInt(1);
                    System.out.println("Количество записей: " + notesAll);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql8 = "SELECT COUNT(*) FROM achievement WHERE date > '2023-12-31' AND user_login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql8)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    achNextYear = resultSet.getInt(1);
                    System.out.println("Количество записей: " + achNextYear);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql9 = "SELECT COUNT(*) FROM achievement WHERE done = 1 AND user_login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql9)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    achDone = resultSet.getInt(1);
                    System.out.println("Количество записей: " + achDone);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql10 = "SELECT COUNT(*) FROM habit WHERE periodicity = 1 AND done = 1 AND user_login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql10)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    habitsDay = resultSet.getInt(1);
                    System.out.println("Количество записей: " + notesAll);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql11 = "SELECT COUNT(*) FROM habit WHERE periodicity = 2 AND done = 1 AND user_login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql11)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    habitsWeek = resultSet.getInt(1);
                    System.out.println("Количество записей: " + notesAll);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql12 = "SELECT COUNT(*) FROM habit WHERE periodicity = 3 AND done = 1 AND user_login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql12)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    habitsMonth = resultSet.getInt(1);
                    System.out.println("Количество записей: " + notesAll);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql13 = "SELECT COUNT(*) FROM habit WHERE periodicity = 4 AND done = 1 AND user_login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql13)) {
            preparedStatement.setString(1, User.login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    habitsBad = resultSet.getInt(1);
                    System.out.println("Количество записей: " + notesAll);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            taskAllLabel.setText(SceneLoader.getBundle().getString("taskAllLabel") + taskAll);
            taskInTimeLabel.setText(SceneLoader.getBundle().getString("taskInTimeLabel") + taskInTime);
            taskDoneLabel.setText(SceneLoader.getBundle().getString("taskDoneLabel") + taskDone);
            notesAllLabel.setText(SceneLoader.getBundle().getString("notesAllLabel") + notesAll);
            habitsAllLabel.setText(SceneLoader.getBundle().getString("habitsAllLabel") + habitsAll);
            achAllLabel.setText(SceneLoader.getBundle().getString("achAllLabel") + achAll);
            achNowYearLabel.setText(SceneLoader.getBundle().getString("achNowYearLabel") + achNowYear);
            achNextYearLabel.setText(SceneLoader.getBundle().getString("achNextYearLabel") + achNextYear);
            achDoneLabel.setText(SceneLoader.getBundle().getString("achDoneLabel") + achDone);
            habitsDayLabel.setText(SceneLoader.getBundle().getString("habitsDayLabel") + habitsDay);
            habitsWeekLabel.setText(SceneLoader.getBundle().getString("habitsWeekLabel") + habitsWeek);
            habitsMonthLabel.setText(SceneLoader.getBundle().getString("habitsMonthLabel") + habitsMonth);
            habitsBadLabel.setText(SceneLoader.getBundle().getString("habitsBadLabel") + habitsBad);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Back(ActionEvent actionEvent) throws IOException {
        SceneLoader.loadNewScene("menu.fxml", StatsRoot);
    }
}
