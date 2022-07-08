package com.company;
import java.sql.*;

public class Main
{
    public static void main(String[] args){
        try {
        PostgresConn postgr = new PostgresConn();
        Connection postgrCon = postgr.getPostgresConnection();
        MysqlConn mysql = new MysqlConn();
        Connection mysqlCon = mysql.getMySQLConnection();
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