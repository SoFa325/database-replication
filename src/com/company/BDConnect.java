package com.company;
import java.sql.*;


public class BDConnect {
    String url1 = "jdbc:postgresql://localhost:5432/newbd";
    String login1 = "postgres";
    String password1 = "postgres";
    String url2 = "jdbc:mysql://localhost:3306/newdb";
    String login2 = "root";
    String password2 = "mysql";
    String firstTableName = "data";
    String secondTableName = "data";
    String primaryKeyFirstTableName = "Id";
    String primaryKeySecondTableName = "Id";
    public Connection getConnectionWithFirstBD() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url1, login1, password1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return con;
    }
    public Connection getConnectionWithSecondBD() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url2, login2, password2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return con;
    }

}