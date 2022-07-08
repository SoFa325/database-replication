package com.company;
import java.sql.*;

public class CRUDRepository {

    public boolean create(Record rec, Connection conn) {
        try {
        String sql = "INSERT data(FirstName, LastName, Phone, Email) Values (?, ?, ?, ?);";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, rec.FirstName);
        preparedStatement.setString(2, rec.LastName);
        preparedStatement.setString(3, rec.Phone);
        preparedStatement.setString(4, rec.Email);
        System.out.println(sql);
        preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Error2");
            return false;
        }
        return true;
    }
    public Record read(Connection con, int id) {
        Statement stmt = null;
        Record rec = null;
        try {
            stmt = con.createStatement();
            String sql = "SELECT * FROM data WHERE id = " + id + ";";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            rec = new Record(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
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
