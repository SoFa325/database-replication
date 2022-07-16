package com.company;
import java.sql.*;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JoinRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

public class CRUDRepository {
    int n;
    int k;
    Connection conWithFrstbd;
    Connection conWithSecbd;
    CachedRowSet crsFromFirstBD;
    CachedRowSet crsFromSecondBD;
    JoinRowSet jrs;
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
        jrs = factory.createJoinRowSet();

        String sql1 = "SELECT * FROM " + firstTableName;
        crsFromFirstBD = factory.createCachedRowSet();
        crsFromFirstBD.setUrl(url1);
        crsFromFirstBD.setUsername(login1);
        crsFromFirstBD.setPassword(password1);
        crsFromFirstBD.setCommand(sql1);
        crsFromFirstBD.execute();

        String sql2 = "SELECT * FROM " + secondTableName;
        crsFromSecondBD = factory.createCachedRowSet();
        crsFromSecondBD.setUrl(url2);
        crsFromSecondBD.setUsername(login2);
        crsFromSecondBD.setPassword(password2);
        crsFromSecondBD.setCommand(sql2);
        crsFromSecondBD.execute();
    }

    public String metadata() throws Exception{
        DatabaseMetaData metadata = conWithFrstbd.getMetaData();
        ResultSet rs;
        String cols = "";
        n = 0;
        ResultSet primaryKeysFirst = metadata.getPrimaryKeys(null, null, firstTableName);
        while(primaryKeysFirst.next()){
            primaryKeyFirstTableName = primaryKeysFirst.getString("COLUMN_NAME");
        }
        ResultSet primaryKeysSecond = metadata.getPrimaryKeys(null, null, secondTableName);
        while(primaryKeysSecond.next()){
            primaryKeySecondTableName = primaryKeysSecond.getString("COLUMN_NAME");
        }

        rs = metadata.getColumns(null, null, firstTableName, null);
        while (rs.next()) {
            if (rs.getString("COLUMN_NAME").equals(primaryKeyFirstTableName)){
                k = n;
            }
            if (cols.equals("")) {
                cols += rs.getString("COLUMN_NAME");
            } else {
                cols += ", " + rs.getString("COLUMN_NAME");
            }
            n++;
        }
        System.out.println(k);
        return cols;
    }

    public boolean create(String res, String cols) {
        try {
            String sql = "INSERT " + secondTableName + "(" + cols + ") Values (" + res + ");";
            Statement St = conWithSecbd.createStatement();
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

    public void update(String res, Object id) {
        try {
            String sql = "UPDATE " + secondTableName + " SET " + res + " WHERE " + primaryKeySecondTableName + " = " + id  + ";";
            Statement st = conWithSecbd.createStatement();
            System.out.println(sql);
            st.execute(sql);
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
