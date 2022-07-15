package com.company;
import java.sql.*;


public class BDConnect {
    public Connection getConnection(String url, String login, String password) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, login, password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return con;
    }

}