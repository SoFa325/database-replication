package com.company;
import javax.sql.rowset.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class Main
{
    public static void main(String[] args){
        try {
            BDConnect conn = new BDConnect();
            Connection postgrCon = conn.getConnection("jdbc:postgresql://localhost:5432/newbd", "postgres", "postgres");
            Connection mysqlCon = conn.getConnection("jdbc:mysql://localhost:3306/newdb", "root", "mysql");

            //metadata
            DatabaseMetaData metadata = postgrCon.getMetaData();
            ResultSet rs;
            String actualTable = "data";
            String cols = "";
            int n = 0;
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

            CRUDRepository crud = new CRUDRepository();


            BDChange bd = new BDChange("jdbc:postgresql://localhost:5432/newbd", "jdbc:mysql://localhost:3306/newdb", "postgres", "root", "postgres", "mysql", cols, n);
            bd.add(mysqlCon);
            bd.delete(mysqlCon);
            RowSetFactory factory = RowSetProvider.newFactory();
            bd.crs2 = factory.createCachedRowSet();
            String sql = "SELECT * FROM data";
            bd.crs2.setUrl("jdbc:mysql://localhost:3306/newdb");
            bd.crs2.setUsername("root");
            bd.crs2.setPassword("mysql");
            bd.crs2.setCommand(sql);
            bd.crs2.execute();
            bd.update(mysqlCon);
            postgrCon.close();
            mysqlCon.close();
        }
         catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}