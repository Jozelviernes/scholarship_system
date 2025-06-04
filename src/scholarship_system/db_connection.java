/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scholarship_system;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Admin
 */
public class db_connection {
     private static final String URL = "jdbc:mysql://localhost:3306/scholarship_db"; // your DB name
    private static final String USER = "root"; // your DB username
    private static final String PASS = ""; // your DB password

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // for MySQL 8 and up
            return (Connection) DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            return null;
        }
    }
}
