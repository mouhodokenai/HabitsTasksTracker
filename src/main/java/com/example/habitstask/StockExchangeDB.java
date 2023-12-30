package com.example.habitstask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class StockExchangeDB {
    // Блок объявления констант
    public static final String NAME_USER = "root";
    public static final String PASSWORD = "MA58sh62.";
    public static final String URL = "jdbc:mysql://localhost:3306/HabitsTasks?serverTimezone=UTC";
    public static final String DB_Driver = "com.mysql.cj.jdbc.Driver";
    public static Connection connection;
    public static Statement statement;

    public static int ID = 1;

    static {
        try {
            Class.forName(DB_Driver);
            connection = DriverManager.getConnection(URL, NAME_USER, PASSWORD);
            statement = connection.createStatement();
            System.out.println("ок");
        } catch (ClassNotFoundException | SQLException throwable) {
            throwable.printStackTrace();
            throw new RuntimeException();
        }
    }


    public static void main(String[] args) throws SQLException {


    }


}
