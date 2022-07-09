package com.company;
import java.sql.*;

public class Main
{
    public static void main(String[] args){
        try {
            BDConnect conn = new BDConnect();
            Connection postgrCon = conn.getConnection("jdbc:postgresql://localhost:5432/newbd", "postgres", "postgres");
            Connection mysqlCon = conn.getConnection("jdbc:mysql://localhost:3306/newdb", "root", "mysql");
            CRUDRepository crud = new CRUDRepository();
            for (int i = 1; i <= 6; i++) {
                Record rec = crud.read(postgrCon, i);
                crud.create(rec, mysqlCon);
            }
            postgrCon.close();
            mysqlCon.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}