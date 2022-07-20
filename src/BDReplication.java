import javax.sql.rowset.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BDReplication {
    private Object [] coladd;
    private Object [] coldel;
    private CRUDRepository crud = new CRUDRepository();
    private CachedRowSet crsFromFirstBD;
    private CachedRowSet crsFromSecondBD;
    private JoinRowSet jrs;
    private String cols;
    private static Logger log = Logger.getLogger(BDReplication.class.getName());

    private void connectToSecDB() throws IOException {
        crud.connectForUpdate();
    }

    private void closeConnect() throws SQLException {
        crud.getConWithSecbd().close();
    }

    public void run() throws SQLException {
        try {
            initialize();
            getColAdd();
            getColDel();
            connectToSecDB();
            add();
            delete();
            update();
        } catch (IOException ex){
            log.log(Level.WARNING, "There is error with your config", ex);
        } catch (SQLException ex){
            log.log(Level.WARNING, "There is error with your sql", ex);
        } finally {
            closeConnect();
        }

    }

    private void getColAdd() throws SQLException {
        Collection colFromFirstBD = crsFromFirstBD.toCollection(crud.getPrimaryKeyFirstTableName());
        Collection colFromSecondBD = crsFromSecondBD.toCollection(crud.getPrimaryKeySecondTableName());
        colFromFirstBD.removeAll(colFromSecondBD);
        this.coladd = colFromFirstBD.toArray();
    }

    private void getColDel() throws SQLException {
        Collection colFromFirstBD = crsFromFirstBD.toCollection(crud.getPrimaryKeyFirstTableName());
        Collection colFromSecondBD = crsFromSecondBD.toCollection(crud.getPrimaryKeySecondTableName());
        colFromSecondBD.removeAll(colFromFirstBD);
        this.coldel = colFromSecondBD.toArray();
    }

    private void add() throws SQLException {
        for (int i = 0; i < coladd.length; i++){
            Object[] res = crud.read(coladd[i]);
            crud.create(res, cols);
        }
    }

    private void delete() throws SQLException {
        for (int i = 0; i < coldel.length; i++){
            crud.delete(coldel[i]);
        }
    }

    private void initialize() throws SQLException, IOException {
        try {
            crud.connectForRead();
            cols = crud.metadata();
            crud.downloadData();
            this.crsFromFirstBD = crud.getCrsFromFirstBD();
            this.crsFromSecondBD = crud.getCrsFromSecondBD();
            this.jrs = crud.getJrs();
        } finally {
            crud.getConWithFrstbd().close();
            crud.getConWithSecbd().close();
        }

    }

    private void update() throws SQLException {
        jrs.addRowSet(crsFromFirstBD, crud.getPrimaryKeyFirstTableName());
        jrs.addRowSet(crsFromSecondBD, crud.getPrimaryKeySecondTableName());
        String[] s = cols.split(", ");
        while(jrs.next())
        {
            Object id = null;
            String firstRSObject = "";
            String secondRSRowObject = "";
            int i = 1;
            do {
                if (i < (crud.getPrimaryKeyPosition()+1)) {
                    firstRSObject = jrs.getString(i);
                    secondRSRowObject = jrs.getString(i + crud.getColumnNumber());
                } else if (i > (crud.getColumnNumber()+1)){
                    firstRSObject = jrs.getString(i);
                    secondRSRowObject = jrs.getString(i + crud.getColumnNumber() - 1);
                } else {
                    id = jrs.getObject(i);
                }
                i++;
            } while(firstRSObject.equals(secondRSRowObject) && i <= crud.getColumnNumber());
            if (i != (crud.getColumnNumber()+1)) {
                String res = "";
                if (crud.getPrimaryKeyPosition() != 0) {
                    res += s[0] + " = " + "'" + jrs.getString(2) + "'";
                    for (int j = 1; j < s.length; j++){
                        if (j != (crud.getPrimaryKeyPosition()+1)) {
                            res += ", " + s[j] + " = " + "'" + jrs.getString(j + 1) + "'";
                        } 
                    }
                } else {
                    res += s[1] + " = " + "'" + jrs.getString(2) + "'";
                    for (int j = 2; j < s.length; j++) {
                        res += ", " + s[j] + " = " + "'" + jrs.getString(j + 1) + "'";
                    }
                }
                crud.update(res, id);
            }
        }
    }
}