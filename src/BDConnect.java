import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class BDConnect {
    private String firstBDurl;
    private String firstBDLogin;
    private String firstBDPassword;
    private String secondBDurl;
    private String secondBDLogin;
    private String secondBDPassword;
    private String firstBDTableName;
    private String secondBDTableName;

    public BDConnect() throws IOException {
        File file = new File("C:\\Users\\sofya\\IdeaProjects\\database_replication\\src\\config.properties");
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

    public String getFirstBDLogin() {
        return firstBDLogin;
    }

    public String getFirstBDPassword() {
        return firstBDPassword;
    }

    public String getFirstBDTableName() {
        return firstBDTableName;
    }

    public String getFirstBDurl() {
        return firstBDurl;
    }

    public String getSecondBDLogin() {
        return secondBDLogin;
    }

    public String getSecondBDPassword() {
        return secondBDPassword;
    }

    public String getSecondBDurl() {
        return secondBDurl;
    }

    public String getSecondBDTableName() {
        return secondBDTableName;
    }
}