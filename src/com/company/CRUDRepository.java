package com.company;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JoinRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

public class CRUDRepository {
    int columNumber;
    int primaryKeyPosition;
    Connection conWithFrstbd;
    Connection conWithSecbd;
    CachedRowSet crsFromFirstBD;
    CachedRowSet crsFromSecondBD;
    JoinRowSet jrs;
    String firstBDurl;
    String firstBDLogin;
    String firstBDPassword;
    String secondBDurl;
    String secondBDLogin;
    String secondBDPassword;
    String firstBDTableName;
    String secondBDTableName;
    String primaryKeyFirstTableName;
    String primaryKeySecondTableName;


    public void connectForRead() throws Exception {
        BDConnect conn = new BDConnect();
        this.conWithFrstbd = conn.getConnectionWithFirstBD();
        this.conWithSecbd = conn.getConnectionWithSecondBD();
        firstBDurl = conn.firstBDurl;
        firstBDLogin = conn.firstBDLogin;
        firstBDPassword = conn.firstBDPassword;
        secondBDurl = conn.secondBDurl;
        secondBDLogin = conn.secondBDLogin;
        secondBDPassword = conn.secondBDPassword;
        firstBDTableName = conn.firstBDTableName;
        secondBDTableName = conn.secondBDTableName;

    }

    public void connectForUpdate() throws Exception {
        BDConnect conn = new BDConnect();
        this.conWithSecbd = conn.getConnectionWithSecondBD();
        firstBDurl = conn.firstBDurl;
        firstBDLogin = conn.firstBDLogin;
        firstBDPassword = conn.firstBDPassword;

    }

    public void downloadData() throws Exception{
        RowSetFactory factory = RowSetProvider.newFactory();
        jrs = factory.createJoinRowSet();

        String sql1 = "SELECT * FROM " + firstBDTableName;
        crsFromFirstBD = factory.createCachedRowSet();
        crsFromFirstBD.setUrl(firstBDurl);
        crsFromFirstBD.setUsername(firstBDLogin);
        crsFromFirstBD.setPassword(firstBDPassword);
        crsFromFirstBD.setCommand(sql1);
        crsFromFirstBD.execute();

        String sql2 = "SELECT * FROM " + secondBDTableName;
        crsFromSecondBD = factory.createCachedRowSet();
        crsFromSecondBD.setUrl(secondBDurl);
        crsFromSecondBD.setUsername(secondBDLogin);
        crsFromSecondBD.setPassword(secondBDPassword);
        crsFromSecondBD.setCommand(sql2);
        crsFromSecondBD.execute();
    }

    public String metadata() throws Exception{
        DatabaseMetaData metadataFromFirstBD = conWithFrstbd.getMetaData();
        DatabaseMetaData metadataFromSecondBD = conWithSecbd.getMetaData();
        ResultSet rsFirstBD;
        ResultSet rsSecondBD;
        String cols = "";
        columNumber = 0;

        ResultSet primaryKeysFirst = metadataFromFirstBD.getPrimaryKeys(null, null, firstBDTableName);
        while(primaryKeysFirst.next()){
            primaryKeyFirstTableName = primaryKeysFirst.getString("COLUMN_NAME");
        }
        ResultSet primaryKeysSecond = metadataFromFirstBD.getPrimaryKeys(null, null, secondBDTableName);
        while(primaryKeysSecond.next()){
            primaryKeySecondTableName = primaryKeysSecond.getString("COLUMN_NAME");
        }

        rsFirstBD = metadataFromFirstBD.getColumns(null, null, firstBDTableName, null);
        rsSecondBD = metadataFromSecondBD.getColumns(null, null, secondBDTableName, null);
        ArrayList<String> alFirst = new ArrayList();
        ArrayList<String> alSecond = new ArrayList();
        while (rsSecondBD.next()){
            alSecond.add(rsSecondBD.getString("COLUMN_NAME").toLowerCase());
        }
        while (rsFirstBD.next()) {
            alFirst.add(rsFirstBD.getString("COLUMN_NAME").toLowerCase());
            if (rsFirstBD.getString("COLUMN_NAME").equals(primaryKeyFirstTableName)){
                primaryKeyPosition = columNumber;
            }
            if (cols.equals("")) {
                cols += rsFirstBD.getString("COLUMN_NAME");
            } else {
                cols += ", " + rsFirstBD.getString("COLUMN_NAME");
            }
            columNumber++;
        }

        if (alFirst.size() == alSecond.size()) {
            Collections.sort(alFirst);
            Collections.sort(alSecond);
            if (!(alFirst.equals(alSecond))){
                System.out.println("Tables are not equal");
                return "";
            }
        } else {
            System.out.println("Tables are not equal");
            return "";
        }
        return cols;
    }

    public boolean create(String res, String cols) {
        try {
            String sql = "INSERT " + secondBDTableName + "(" + cols + ") Values (" + res + ");";
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
            String sql = "SELECT * FROM " + firstBDTableName + " WHERE " + primaryKeyFirstTableName + " = " + id + ";";
            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet rs = factory.createCachedRowSet();
            rs.setUrl(firstBDurl);
            rs.setUsername(firstBDLogin);
            rs.setPassword(firstBDPassword);
            rs.setCommand(sql);
            rs.execute();
            rs.next();
            rec += rs.getObject(1);
            for (int i = 2; i <= columNumber; i++){
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
            String sql = "UPDATE " + secondBDTableName + " SET " + res + " WHERE " + primaryKeySecondTableName + " = " + id  + ";";
            Statement st = conWithSecbd.createStatement();
            System.out.println(sql);
            st.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void delete(Object id) throws Exception {
        String sql = "DELETE  FROM " + secondBDTableName + " WHERE " + primaryKeySecondTableName + " = " + id + " ;" ;
        System.out.println(sql);
        Statement st = conWithSecbd.createStatement();
        st.execute(sql);
    }

}
