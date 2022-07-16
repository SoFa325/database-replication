package com.company;
import javax.sql.rowset.*;
import java.sql.SQLException;
import java.util.Collection;

public class BDReplication {
    Object [] coladd;
    Object [] coldel;
    Object [] colupd;
    CRUDRepository crud = new CRUDRepository();
    CachedRowSet crsFromFirstBD;
    CachedRowSet crsFromSecondBD;
    JoinRowSet jrs;
    String cols;

    public void connectToSecDB(){
        crud.ConnectForUpdate();
    }

    public void closeConnect() throws SQLException {
        crud.conWithSecbd.close();
    }

    public void getColAdd() throws Exception{
        Collection colFromFirstBD = crsFromFirstBD.toCollection(crud.primaryKeyFirstTableName);
        Collection colFromSecondBD = crsFromSecondBD.toCollection(crud.primaryKeySecondTableName);
        colFromFirstBD.removeAll(colFromSecondBD);
        this.coladd = colFromFirstBD.toArray();
    }

    public void getColDel() throws Exception{
        Collection colFromFirstBD = crsFromFirstBD.toCollection(crud.primaryKeyFirstTableName);
        Collection colFromSecondBD = crsFromSecondBD.toCollection(crud.primaryKeySecondTableName);
        colFromSecondBD.removeAll(colFromFirstBD);
        this.coldel = colFromSecondBD.toArray();
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
        this.crsFromFirstBD = crud.crsFromFirstBD;
        this.crsFromSecondBD = crud.crsFromSecondBD;
        this.jrs = crud.jrs;
        crud.conWithSecbd.close();
        crud.conWithFrstbd.close();

    }

    public void update() throws Exception {
        jrs.addRowSet(crsFromFirstBD, "Id");
        jrs.addRowSet(crsFromSecondBD, "Id");
        String[] s = cols.split(", ");
        while(jrs.next())
        {
            Object id = null;
            String firstRSObject = "";
            String secondRSRowObject = "";
            int i = 1;
            do {
                if (i < (crud.k+1)) {
                    firstRSObject = jrs.getString(i);
                    secondRSRowObject = jrs.getString(i + crud.n);
                } else if (i > (crud.k+1)){
                    firstRSObject = jrs.getString(i);
                    secondRSRowObject = jrs.getString(i + crud.n - 1);
                } else {
                    id = jrs.getObject(i);
                }
                i++;
            } while(firstRSObject.equals(secondRSRowObject) && i <= crud.n);
            if (i != (crud.n+1)) {
                String res = "";
                if (crud.k != 0) {
                    res += s[0] + " = " + "'" + jrs.getString(2) + "'";
                    for (int j = 1; j < s.length; j++){
                        if (j != (crud.k+1)) {
                            res += ", " + s[j] + " = " + "'" + jrs.getString(j + 1) + "'";
                        } 
                    }
                } else {
                    res += s[1] + " = " + "'" + jrs.getString(2) + "'";
                    for (int j = 2; j < s.length; j++) {
                        res += ", " + s[j] + " = " + "'" + jrs.getString(j + 1) + "'";
                    }
                }
                System.out.println(res);
                crud.update(res, id);
            }
        }
    }
}