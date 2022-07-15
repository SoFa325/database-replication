package com.company;

public class Main
{
    public static void main(String[] args){
        try {
            BDChange bd = new BDChange("jdbc:postgresql://localhost:5432/newbd", "jdbc:mysql://localhost:3306/newdb", "postgres", "root", "postgres", "mysql");
            bd.getFirstConnection();
            bd.crud1.con1.close();
            bd.crud1.con2.close();
            bd.getSecondConnection();
            bd.add();
            bd.delete();
            //bd.update(mysqlCon);
            bd.crud2.con.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}