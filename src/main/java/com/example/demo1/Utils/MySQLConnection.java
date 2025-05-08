package com.example.demo1.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MySQLConnection {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/medicare";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection;

    // Buat koneksi baru tiap kali dibutuhkan
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Menutup koneksi
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method untuk mengeksekusi INSERT, UPDATE, DELETE
//    public int executeUpdate(String query) {
//        try (Statement statement = connection.createStatement()) {
//            return statement.executeUpdate(query);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

    // Method untuk mengeksekusi query SELECT dan mengembalikan hasilnya dalam List of Map
    public List<Map<String, String>> executeQuery(String query) {
        List<Map<String, String>> results = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), resultSet.getString(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static void main(String[] args) {
        MySQLConnection db = new MySQLConnection();

        List<Map<String, String>> users = db.executeQuery("SELECT * FROM users");
        for (Map<String, String> user : users) {
            System.out.println(user.get("nama") + " : " + user.get("email"));
        }
        db.closeConnection();
    }
}
