package com.company;
import java.sql.*;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;


public class CRUDRepository {
    String cols;
    int n;

    public boolean create(String res, Connection conn) {
        try {
            String sql = "INSERT data(" + cols + ") Values (" + res + ");";
            Statement St = conn.prepareStatement(sql);
            System.out.println(sql);
            St.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    public String read(String url, String username, String password, int id) {
        String rec = "";
        try {
            String sql = "SELECT * FROM data WHERE id = " + id + ";";
            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet rs = factory.createCachedRowSet();

            rs.setUrl(url);
            rs.setUsername(username);
            rs.setPassword(password);
            rs.setCommand(sql);

            rs.execute();
            rs.next();
            rec += rs.getObject(1);
            for (int i = 2; i <= n; i++){
                rec += ", " + "'" + rs.getObject(i) + "'";
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rec;
    }
    public boolean update() {




        return true;
    }
    public void delete(Connection conn, int id) throws Exception {
        String sql = "DELETE  FROM data WHERE Id = " + id + " ;" ;
        Statement st = conn.createStatement();
        st.execute(sql);
    }
}
