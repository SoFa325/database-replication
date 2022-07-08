package com.company;
import java.sql.Connection;
import java.sql.DriverManager;

public class PostgresConn
{
    public Connection getPostgresConnection() {
        Connection con = null;
        try {
            String url = "jdbc:postgresql://localhost:5432/newbd";
            String login = "postgres";
            String password = "postgres";
            con = DriverManager.getConnection(url, login, password);
        } catch (Exception e) {
            System.out.println("Error3");
            e.printStackTrace();
        }
        return con;
    }
}