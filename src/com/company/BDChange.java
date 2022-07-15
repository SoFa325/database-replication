package com.company;
import javax.sql.rowset.*;
import java.sql.Connection;
import java.sql.SQLException;

public class BDChange {
    String url1;
    String username1;
    String password1;
    String url2;
    String username2;
    String password2;

    CRUDRepository crud1;
    CRUDRepository crud2;


    public BDChange(String url1, String url2, String username1, String username2, String password1, String password2) throws SQLException{
        this.url1 = url1;
        this.username1 = username1;
        this.password1 = password1;
        this.url2 = url2;
        this.username2 = username2;
        this.password2 = password2;

        //crud.cols = cols;
        //crud.n = n;

    }

    public void getFirstConnection() throws SQLException {
        crud1 = new CRUDRepository(url1, url2, username1, username2, password1, password2);

    }
    public void getSecondConnection() {
        crud2 = new CRUDRepository(url2, username2, password2);
    }

    public void add(){
        for (int i = 0; i < crud1.coladd.length; i++){
            String res = crud2.read(url1, username1, password1, crud1.coladd[i]);
            crud2.create(res);
        }
    }

    public void delete() throws Exception {
        for (int i = 0; i < crud1.coldel.length; i++){
            crud2.delete(crud1.coldel[i]);
        }
    }

    /*public void update(Connection con) throws Exception {
        String[] s = crud2.cols.split(", ");
        while(crs1.next() && crs2.next())
        {
            String firstRSRow = "";
            String secondRSRow = "";
            int i = 1;
            do {
                firstRSRow = crs1.getString(s[i]);
                secondRSRow = crs2.getString(s[i]);
                i++;
            } while(firstRSRow.equals(secondRSRow) && i < crud.n);
            if (i != crud.n) {
                String res = "";
                res += s[1] + " = " + "'" + crs1.getString(2) + "'";
                for (int j = 2; j < s.length; j++){
                    res += ", "+ s[j] + " = " + "'" + crs1.getString(j+1) + "'";
                }
                crud.update(con, res);
            }
        }
    }*/
}