package com.company;
import java.sql.*;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;


public class CRUDRepository {
    int n;
    Connection conWithFrstbd;
    Connection conWithSecbd;
    CachedRowSet crs1;
    CachedRowSet crs2;
    String url1;
    String login1;
    String password1;
    String url2;
    String login2;
    String password2;
    String firstTableName;
    String secondTableName;
    String primaryKeyFirstTableName;
    String primaryKeySecondTableName;

    public void ConnectForRead() {
        BDConnect conn = new BDConnect();
        this.conWithFrstbd = conn.getConnectionWithFirstBD();
        this.conWithSecbd = conn.getConnectionWithSecondBD();
        url1 = conn.url1;
        login1 = conn.login1;
        password1 = conn.password1;
        url2 = conn.url2;
        login2 = conn.login2;
        password2 = conn.password2;
        firstTableName = conn.firstTableName;
        secondTableName = conn.secondTableName;
        primaryKeyFirstTableName = conn.primaryKeyFirstTableName;
        primaryKeySecondTableName = conn.primaryKeySecondTableName;

    }
    public void ConnectForUpdate(){
        BDConnect conn = new BDConnect();
        this.conWithSecbd = conn.getConnectionWithSecondBD();
        url1 = conn.url1;
        login1 = conn.login1;
        password1 = conn.password1;

    }

    public void downloadData() throws Exception{
        RowSetFactory factory = RowSetProvider.newFactory();

        String sql1 = "SELECT * FROM " + firstTableName;
        crs1 = factory.createCachedRowSet();
        crs1.setUrl(url1);
        crs1.setUsername(login1);
        crs1.setPassword(password1);
        crs1.setCommand(sql1);
        crs1.execute();

        String sql2 = "SELECT * FROM " + secondTableName;
        crs2 = factory.createCachedRowSet();
        crs2.setUrl(url2);
        crs2.setUsername(login2);
        crs2.setPassword(password2);
        crs2.setCommand(sql2);
        crs2.execute();
    }

    public String metadata() throws Exception{
        DatabaseMetaData metadata = conWithFrstbd.getMetaData();
        ResultSet rs;
        String cols = "";
        n = 0;
        rs = metadata.getColumns(null, null, firstTableName, null);
        while (rs.next()) {
            if (cols.equals("")) {
                cols += rs.getString("COLUMN_NAME");
            } else {
                cols += ", " + rs.getString("COLUMN_NAME");
            }
            n++;
        }
        return cols;
    }

    public boolean create(String res, String cols) {
        try {
            String sql = "INSERT " + secondTableName + "(" + cols + ") Values (" + res + ");";
            Statement St = conWithSecbd.prepareStatement(sql);
            System.out.println(sql);
            St.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    public String read(Object id) {
        String rec = "";
        try {
            String sql = "SELECT * FROM " + firstTableName + " WHERE " + primaryKeyFirstTableName + " = " + id + ";";
            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet rs = factory.createCachedRowSet();
            rs.setUrl(url1);
            rs.setUsername(login1);
            rs.setPassword(password1);
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
            String sql = "UPDATE " + secondTableName + " SET" + res + ");";
            Statement St = conWithSecbd.prepareStatement(sql);
            System.out.println(sql);
            St.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void delete(Object id) throws Exception {
        String sql = "DELETE  FROM " + secondTableName + " WHERE " + primaryKeySecondTableName + " = " + id + " ;" ;
        System.out.println(sql);
        Statement st = conWithSecbd.createStatement();
        st.execute(sql);
    }
}
