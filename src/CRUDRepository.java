import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JoinRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

public class CRUDRepository {
    private int columnNumber;
    private int primaryKeyPosition;
    private Connection conWithFrstbd;
    private Connection conWithSecbd;
    private CachedRowSet crsFromFirstBD;
    private CachedRowSet crsFromSecondBD;
    private JoinRowSet jrs;
    private String dopstr = "";
    private String firstBDurl;
    private String firstBDLogin;
    private String firstBDPassword;
    private String secondBDurl;
    private String secondBDLogin;
    private String secondBDPassword;
    private String firstBDTableName;
    private String secondBDTableName;
    private String primaryKeyFirstTableName;
    private String primaryKeySecondTableName;


    public void connectForRead() throws IOException {
        BDConnect conn = new BDConnect();
        this.conWithFrstbd = conn.getConnectionWithFirstBD();
        this.conWithSecbd = conn.getConnectionWithSecondBD();
        firstBDurl = conn.getFirstBDurl();
        firstBDLogin = conn.getFirstBDLogin();
        firstBDPassword = conn.getFirstBDPassword();
        secondBDurl = conn.getSecondBDurl();
        secondBDLogin = conn.getSecondBDLogin();
        secondBDPassword = conn.getSecondBDPassword();
        firstBDTableName = conn.getFirstBDTableName();
        secondBDTableName = conn.getSecondBDTableName();

    }

    public void connectForUpdate() throws IOException {
        BDConnect conn = new BDConnect();
        this.conWithSecbd = conn.getConnectionWithSecondBD();
        firstBDurl = conn.getFirstBDurl();
        firstBDLogin = conn.getFirstBDLogin();
        firstBDPassword = conn.getFirstBDPassword();

    }

    public void downloadData() throws SQLException {
        RowSetFactory factory = RowSetProvider.newFactory();
        jrs = factory.createJoinRowSet();

        String sql1 = "SELECT * FROM " + firstBDTableName;
        crsFromFirstBD = factory.createCachedRowSet();
        crsFromFirstBD.setUrl(firstBDurl);
        crsFromFirstBD.setUsername(firstBDLogin);
        crsFromFirstBD.setPassword(firstBDPassword);
        crsFromFirstBD.setCommand(sql1);
        crsFromFirstBD.execute();

        String sql2 = "SELECT * FROM " + secondBDTableName;
        crsFromSecondBD = factory.createCachedRowSet();
        crsFromSecondBD.setUrl(secondBDurl);
        crsFromSecondBD.setUsername(secondBDLogin);
        crsFromSecondBD.setPassword(secondBDPassword);
        crsFromSecondBD.setCommand(sql2);
        crsFromSecondBD.execute();
    }

    public String metadata() throws SQLException {
        DatabaseMetaData metadataFromFirstBD = conWithFrstbd.getMetaData();
        DatabaseMetaData metadataFromSecondBD = conWithSecbd.getMetaData();
        ResultSet rsFirstBD;
        ResultSet rsSecondBD;
        String cols = "";
        columnNumber = 0;

        ResultSet primaryKeysFirst = metadataFromFirstBD.getPrimaryKeys(null, null, firstBDTableName);
        while(primaryKeysFirst.next()){
            primaryKeyFirstTableName = primaryKeysFirst.getString("COLUMN_NAME");
        }
        ResultSet primaryKeysSecond = metadataFromFirstBD.getPrimaryKeys(null, null, secondBDTableName);
        while(primaryKeysSecond.next()){
            primaryKeySecondTableName = primaryKeysSecond.getString("COLUMN_NAME");
        }

        rsFirstBD = metadataFromFirstBD.getColumns(null, null, firstBDTableName, null);
        rsSecondBD = metadataFromSecondBD.getColumns(null, null, secondBDTableName, null);
        ArrayList<String> alFirst = new ArrayList();
        ArrayList<String> alSecond = new ArrayList();
        while (rsSecondBD.next()){
            alSecond.add(rsSecondBD.getString("COLUMN_NAME").toLowerCase());
        }
        while (rsFirstBD.next()) {
            alFirst.add(rsFirstBD.getString("COLUMN_NAME").toLowerCase());
            if (rsFirstBD.getString("COLUMN_NAME").equals(primaryKeyFirstTableName)){
                primaryKeyPosition = columnNumber;
            }
            if (cols.equals("")) {
                cols += rsFirstBD.getString("COLUMN_NAME");
            } else {
                cols += ", " + rsFirstBD.getString("COLUMN_NAME");
            }
            columnNumber++;
        }

        if (alFirst.size() == alSecond.size()) {
            Collections.sort(alFirst);
            Collections.sort(alSecond);
            if (!(alFirst.equals(alSecond))){
                throw new IllegalStateException("Tables are not equal");
            }
        } else {
            throw new IllegalStateException("Tables are not equal");
        }
        return cols;
    }

    public boolean create(Object[] res, String cols) {
        try {
            String sql = "INSERT INTO " + secondBDTableName + " (" + cols + ") Values (" + dopstr + ");";
            PreparedStatement st = conWithSecbd.prepareStatement(sql);
            for (int i = 0; i < columnNumber; i++){
                st.setObject(i+1, res[i]);
            }
            st.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public Object[] read(Object id) throws SQLException {
        Object [] rec = new Object[columnNumber];
        String sql = "SELECT * FROM " + firstBDTableName + " WHERE " + primaryKeyFirstTableName + " = " + id + ";";
        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet rs = factory.createCachedRowSet();
        rs.setUrl(firstBDurl);
        rs.setUsername(firstBDLogin);
        rs.setPassword(firstBDPassword);
        rs.setCommand(sql);
        rs.execute();
        rs.next();
        dopstr = "";
        for (int i = 1; i <= columnNumber; i++){
            rec[i-1] = rs.getObject(i);
            if (i != columnNumber) {
                dopstr += "?,";
            } else {
                dopstr += "?";
            }
        }
        rs.close();
        return rec;
    }

    public void update(String res, Object id) {
        try {
            String sql = "UPDATE " + secondBDTableName + " SET " + res + " WHERE " + primaryKeySecondTableName + " = " + id  + ";";
            Statement st = conWithSecbd.createStatement();
            st.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void delete(Object id) throws SQLException {
        String sql = "DELETE  FROM " + secondBDTableName + " WHERE " + primaryKeySecondTableName + " = " + id + " ;" ;
        Statement st = conWithSecbd.createStatement();
        st.execute(sql);
    }

    public CachedRowSet getCrsFromFirstBD() {
        return crsFromFirstBD;
    }

    public CachedRowSet getCrsFromSecondBD() {
        return crsFromSecondBD;
    }

    public Connection getConWithFrstbd() {
        return conWithFrstbd;
    }

    public Connection getConWithSecbd() {
        return conWithSecbd;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public int getPrimaryKeyPosition() {
        return primaryKeyPosition;
    }

    public JoinRowSet getJrs() {
        return jrs;
    }

    public String getPrimaryKeyFirstTableName() {
        return primaryKeyFirstTableName;
    }

    public String getPrimaryKeySecondTableName() {
        return primaryKeySecondTableName;
    }
}
