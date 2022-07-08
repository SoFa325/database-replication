package com.company;
import java.sql.*;


public class MysqlConn {
    public static Connection getMySQLConnection() {
        Connection con = null;
        try {
            String url = "jdbc:mysql://localhost:3306/newdb";
            String login = "root";
            String password = "mysql";
            con = DriverManager.getConnection(url, login, password);
        } catch (Exception e) {
            System.out.println("Error4");
            e.printStackTrace();
        }
        return con;
    }

}