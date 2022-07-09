package com.company;
import java.sql.*;

public class CRUDRepository {
    String cols;
    int n;

    public boolean create(String res, Connection conn) {
        try {
            String sql = "INSERT data(" + cols + ") Values (" + res + ");";
            Statement St = conn.prepareStatement(sql);
            System.out.println(sql);
            St.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Error2");
            return false;
        }
        return true;
    }
    public String read(Connection con, int id) {
        Statement stmt;
        String rec = "";
        try {
            stmt = con.createStatement();
            String sql = "SELECT * FROM data WHERE id = " + id + ";";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            rec += rs.getObject(1);
            for (int i = 2; i <= n; i++){
                rec += ", " + "'" + rs.getObject(i) + "'";
            }
            rs.close();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Error1");
        }
        return rec;
    }
    public boolean update() {




        return true;
    }
    public boolean delete() {




        return true;
    }
}
