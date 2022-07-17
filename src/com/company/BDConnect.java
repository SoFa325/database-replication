package com.company;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.Properties;


public class BDConnect {
    String firstBDurl;
    String firstBDLogin;
    String firstBDPassword;
    String secondBDurl;
    String secondBDLogin;
    String secondBDPassword;
    String firstBDTableName;
    String secondBDTableName;

    public BDConnect() throws Exception {
        File file = new File("C:\\Users\\sofya\\IdeaProjects\\database_replication\\src\\com\\company\\config.properties");
        Properties properties = new Properties();
        properties.load(new FileReader(file));
        this.firstBDurl = "jdbc:" + properties.getProperty("firstBDType") + ":" + properties.getProperty("firstBDAddress") + "/" + properties.getProperty("firstBDName");
        this.firstBDLogin = properties.getProperty("firstBDLogin");
        this.firstBDPassword = properties.getProperty("firstBDPassword");
        this.secondBDurl = "jdbc:" + properties.getProperty("secondBDType") + ":" + properties.getProperty("secondBDAddress") + "/" + properties.getProperty("secondBDName");
        this.secondBDLogin = properties.getProperty("secondBDLogin");
        this.secondBDPassword = properties.getProperty("secondBDPassword");
        this.firstBDTableName = properties.getProperty("firstBDTableName");
        this.secondBDTableName = properties.getProperty("secondBDTableName");
    }

    public Connection getConnectionWithFirstBD() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(firstBDurl, firstBDLogin, firstBDPassword);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return con;
    }

    public Connection getConnectionWithSecondBD() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(secondBDurl, secondBDLogin, secondBDPassword);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return con;
    }

}