package com.company;
import javax.sql.rowset.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public class BDChange {
    String url;
    String username;
    String password;
    Object [] adding;
    Object [] deleting;
    CachedRowSet crs1;
    CachedRowSet crs2;
    CRUDRepository crud = new CRUDRepository();


    public BDChange(String url1, String url2, String username1, String username2, String password1, String password2, String cols, int n) throws SQLException {

        this.url = url1;
        this.username = username1;
        this.password = password1;
        String sql = "SELECT * FROM data";
        RowSetFactory factory = RowSetProvider.newFactory();
        crs1 = factory.createCachedRowSet();
        crs2 = factory.createCachedRowSet();

        crs1.setUrl(url1);
        crs1.setUsername(username1);
        crs1.setPassword(password1);
        crs1.setCommand(sql);
        crs1.execute();
        Collection col1 = crs1.toCollection("Id");
        Collection col3 = crs1.toCollection("Id");

        crs2.setUrl(url2);
        crs2.setUsername(username2);
        crs2.setPassword(password2);
        crs2.setCommand(sql);
        crs2.execute();

        Collection col2 = crs2.toCollection("Id");
        Collection col4 = crs2.toCollection("Id");

        //add
        col1.removeAll(col2);
        Object[] al = col1.toArray();
        this.adding = al;
        System.out.println(col1);

        //delete
        col4.removeAll(col3);
        Object[] al1 = col4.toArray();
        this.deleting = al1;
        System.out.println(col4);
        crud.cols = cols;
        crud.n = n;

    }

    public void add(Connection con){
        for (int i = 0; i < adding.length; i++){
            String res = crud.read(url, username, password, adding[i]);
            crud.create(res, con);
        }
    }

    public void delete(Connection con) throws Exception {
        for (int i = 0; i < deleting.length; i++){
            crud.delete(con, deleting[i]);
        }
    }

    public void update(Connection con) throws Exception {
        String[] s = crud.cols.split(", ");
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
    }
}
