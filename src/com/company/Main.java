package com.company;
import java.sql.*;
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
            crud.cols = cols;
            crud.n = n;
            for (int i = 1; i <= 6; i++) {
                String res = crud.read(postgrCon, i);
                if (!crud.create(res, mysqlCon)) {
                    System.out.println("Error");
                }
            }
            postgrCon.close();
            mysqlCon.close();
        }
         catch (Exception e) {
            e.printStackTrace();
        }

    }


}