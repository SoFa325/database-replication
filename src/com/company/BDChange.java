package com.company;
import javax.sql.rowset.*;
import java.sql.SQLException;
import java.util.Collection;

public class BDChange {

    Object [] coladd;
    Object [] coldel;
    CRUDRepository crud = new CRUDRepository();
    CachedRowSet crs1;
    CachedRowSet crs2;
    String cols;

    public void write(){
        crud.ConnectForUpdate();
    }
    public void endOfWriting() throws SQLException {
        crud.conWithSecbd.close();
    }
    public void getColAdd() throws Exception{
        Collection col1 = crs1.toCollection(crud.primaryKeyFirstTableName);
        Collection col2 = crs2.toCollection(crud.primaryKeySecondTableName);
        col1.removeAll(col2);
        Object[] al = col1.toArray();
        this.coladd = al;
    }
    public void getColDel() throws Exception{
        Collection col3 = crs1.toCollection(crud.primaryKeyFirstTableName);
        Collection col4 = crs2.toCollection(crud.primaryKeySecondTableName);
        col4.removeAll(col3);
        Object[] al1 = col4.toArray();
        this.coldel = al1;
    }

    public void add(){
        for (int i = 0; i < coladd.length; i++){
            String res = crud.read(coladd[i]);
            crud.create(res, cols);
        }
    }

    public void delete() throws Exception {
        for (int i = 0; i < coldel.length; i++){
            crud.delete(coldel[i]);
        }
    }

    public void initialize() throws Exception{
        crud.ConnectForRead();
        cols = crud.metadata();
        crud.downloadData();
        this.crs1 = crud.crs1;
        this.crs2 = crud.crs2;
        crud.conWithSecbd.close();
        crud.conWithFrstbd.close();
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