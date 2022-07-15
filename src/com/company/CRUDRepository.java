package com.company;
import java.sql.*;
import java.util.Collection;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;


public class CRUDRepository {
    static String cols;
    static int n;
    Connection con;
    Connection con1;
    Connection con2;
    CachedRowSet crs1;
    CachedRowSet crs2;
    Object[] coladd;
    Object[] coldel;

    public CRUDRepository(String url, String login, String password){
        BDConnect conn = new BDConnect();
        this.con = conn.getConnection(url, login, password);

    }
    public CRUDRepository(String url1, String url2, String login1, String login2, String password1, String password2) throws SQLException {
        BDConnect conn = new BDConnect();
        this.con1 = conn.getConnection(url1, login1, password1);
        this.con2 = conn.getConnection(url2, login2, password2);
        //metadata
        DatabaseMetaData metadata = con1.getMetaData();
        ResultSet rs;
        String actualTable = "data";
        cols = "";
        rs = metadata.getColumns(null, null, actualTable, null);
        while (rs.next()) {
            if (cols.equals("")) {
                cols += rs.getString("COLUMN_NAME");
            } else {
                cols += ", " + rs.getString("COLUMN_NAME");
            }
            n++;
        }
        //end metadata
        String sql = "SELECT * FROM data";
        RowSetFactory factory = RowSetProvider.newFactory();
        crs1 = factory.createCachedRowSet();
        crs2 = factory.createCachedRowSet();

        crs1.setUrl(url1);
        crs1.setUsername(login1);
        crs1.setPassword(password1);
        crs1.setCommand(sql);
        crs1.execute();
        Collection col1 = crs1.toCollection("Id");
        Collection col3 = crs1.toCollection("Id");
        crs2.setUrl(url2);
        crs2.setUsername(login2);
        crs2.setPassword(password2);
        crs2.setCommand(sql);
        crs2.execute();
        Collection col2 = crs2.toCollection("Id");
        Collection col4 = crs2.toCollection("Id");

        //add
        col1.removeAll(col2);
        Object[] al = col1.toArray();
        this.coladd = al;
        System.out.println(col1);

        //delete
        col4.removeAll(col3);
        Object[] al1 = col4.toArray();
        this.coldel = al1;
    }

    public boolean create(String res) {
        try {
            String sql = "INSERT data(" + cols + ") Values (" + res + ");";
            Statement St = con.prepareStatement(sql);
            System.out.println(sql);
            St.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    public String read(String url, String username, String password, Object id) {
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
    public void update(String res) {
        try {
            String sql = "UPDATE data SET" + res + ");";
            Statement St = con.prepareStatement(sql);
            System.out.println(sql);
            St.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void delete(Object id) throws Exception {
        String sql = "DELETE  FROM data WHERE Id = " + id + " ;" ;
        System.out.println(sql);
        Statement st = con.createStatement();
        st.execute(sql);
    }
}
