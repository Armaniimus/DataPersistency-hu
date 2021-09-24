import java.sql.*;
import java.util.ArrayList;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private final Connection connection;
    private ReizigerDAO reizigerDAO;
    private ProductDAO productDAO;

    public OVChipkaartDAOPsql(Connection localConn) { this.connection = localConn; }
    public void setReizigerDAO(ReizigerDAO reizigerDAO) { this.reizigerDAO = reizigerDAO; }
    public void setProductDAO(ProductDAO productDAO) { this.productDAO = productDAO; }

    public boolean save(OVChipkaart ovChipkaart) {
        try {
            String q = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, ovChipkaart.getKaartNummer() );
            pst.setDate(2,  new Date(ovChipkaart.getGeldigTot().getTime() ) );
            pst.setInt(3,ovChipkaart.getKlasse() );
            pst.setDouble(4, ovChipkaart.getSaldo() );
            pst.setInt(5, ovChipkaart.getReizigerId() );

            pst.execute();
            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("OVCHiplaartDAOPsql geeft een error in save(): " + err.getMessage() + " " + err.getStackTrace() );
            return false;
        }
    }

    public boolean saveList(ArrayList<OVChipkaart> ovChipkaartList) {
        try {
            if ( ovChipkaartList == null || ovChipkaartList.isEmpty() ) {
                throw new Exception("OvChipkaart Arraylist is invalide");
            }

            for (OVChipkaart ovChipkaart : ovChipkaartList) {
                this.save(ovChipkaart);
            }
            return true;
        } catch(Exception err) {
            System.err.println("OVCHiplaartDAOPsql geeft een error in saveList(): " + err.getMessage() + " " + err.getStackTrace() );
            return false;
        }
    }

    public boolean update(OVChipkaart ovChipkaart) {
        try {
            String q = "UPDATE ov_chipkaart SET geldig_tot=?, klasse=?, saldo=? WHERE kaart_nummer=?;";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setDate(1,  new Date(ovChipkaart.getGeldigTot().getTime() ) );
            pst.setInt(2, ovChipkaart.getKlasse() );
            pst.setDouble(3, ovChipkaart.getSaldo() );
            pst.setInt(4, ovChipkaart.getKaartNummer() );

            pst.execute();

            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("OVCHiplaartDAOPsql geeft een error in update(): " + err.getMessage() + " " + err.getStackTrace() );
            return false;
        }
    }

    public boolean updateList(ArrayList<OVChipkaart> ovChipkaartArrayList) {
        try {
            if ( ovChipkaartArrayList == null || ovChipkaartArrayList.isEmpty() ) {
                throw new Exception("OvChipkaart Arraylist is invalide");
            }

            for (OVChipkaart ovChipkaart : ovChipkaartArrayList) {
                this.update(ovChipkaart);
            }
            return true;
        } catch(Exception err) {
            System.err.println("OVCHiplaartDAOPsql geeft een error in updateList(): " + err.getMessage() + " " + err.getStackTrace() );
            return false;
        }
    }

    public boolean delete(OVChipkaart ovChipkaart) {
        try {
            String q = "DELETE FROM ov_chipkaart WHERE kaart_nummer=?;";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, ovChipkaart.getKaartNummer() );
            pst.execute();

            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("OVCHiplaartDAOPsql geeft een error in delete(): " + err.getMessage() + " " + err.getStackTrace() );
            return false;
        }
    }

    public boolean deleteList(ArrayList<OVChipkaart> ovChipkaartArrayList) {
        try {
            if ( ovChipkaartArrayList == null || ovChipkaartArrayList.isEmpty() ) {
                throw new Exception("OvChipkaart Arraylist is invalide");
            }

            for (OVChipkaart ovChipkaart : ovChipkaartArrayList) {
                this.delete(ovChipkaart);
            }
            return true;
        } catch(Exception err) {
            System.err.println("OVCHiplaartDAOPsql geeft een error in deleteList(): " + err.getMessage() + " " + err.getStackTrace() );
            return false;
        }
    }

    public ArrayList<OVChipkaart> findByReiziger(Reiziger reiziger) {
        ArrayList<OVChipkaart> OVChipkaartArray = new ArrayList<>();
        try {
            String q = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, reiziger.getId() );
            ResultSet rs = pst.executeQuery();

            while (rs.next() ) {
                OVChipkaart ovChipkaart = this.__retrieveResultset(rs,  reiziger);
                OVChipkaartArray.add(ovChipkaart);
            }
            rs.close();
            pst.close();
        } catch(Exception err) {
            System.err.println("OVCHipkaartDAOPsql geeft een error in findByReiziger(): " + err.getMessage() + " " + err.getStackTrace() );
        }

        return OVChipkaartArray;
    }

    public ArrayList<OVChipkaart> findByProduct(Product product) {
        ArrayList<OVChipkaart> OVChipkaartArray = new ArrayList<>();

        try {
            String q = "SELECT ov_chipkaart.kaart_nummer, geldig_tot, klasse, saldo, reiziger_id " +
            "FROM ov_chipkaart_product " +
            "INNER JOIN ov_chipkaart " +
            "ON ov_chipkaart.kaart_nummer = ov_chipkaart_product.kaart_nummer " +
            "WHERE ov_chipkaart_product.product_nummer = ?;";

            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, product.getProduct_nummer() );
            ResultSet rs = pst.executeQuery();

            while (rs.next() ) {
                OVChipkaart ovChipkaart = this.__retrieveResultset(rs,  null);
                OVChipkaartArray.add(ovChipkaart);
            }
            rs.close();
            pst.close();
        } catch(Exception err) {
            System.err.println("OVCHipkaartDAOPsql geeft een error in findByProduct(): " + err.getMessage() + " " + err.getStackTrace() );
        }

        return OVChipkaartArray;
    }

    public ArrayList<OVChipkaart> findAll() {
        ArrayList<OVChipkaart> OVChipkaartArray = new ArrayList<>();
        try {
            Statement st = this.connection.createStatement();
            ResultSet rs = st.executeQuery("select * from ov_chipkaart");

            while (rs.next()) {
                OVChipkaart ovChipkaart = this.__retrieveResultset(rs, null);
                OVChipkaartArray.add(ovChipkaart);
            }
            rs.close();

        } catch(Exception err) {
            System.err.println("OVCHiplaartDAOPsql geeft een error in findAll(): " + err.getMessage() + " " + err.getStackTrace() );
        }

        return OVChipkaartArray;
    }

    public OVChipkaart findByKaartNummer(int id) {
        try {
            String q = "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, id );
            ResultSet rs = pst.executeQuery();

            OVChipkaart ovChipkaart = null;
            if (rs.next() ) {
                ovChipkaart = this.__retrieveResultset(rs, null);
            }
            rs.close();
            pst.close();

            return ovChipkaart;

        } catch(Exception err) {
            System.err.println("OVCHipkaartDAOPsql geeft een eror in findByKaartNummer(): " + err.getMessage() + " " + err.getStackTrace() );
            return null;
        }
    }

    private OVChipkaart __addrelations(OVChipkaart ovChipkaart) {
        Reiziger reiziger = reizigerDAO.findByOVChipkaart(ovChipkaart);
        ovChipkaart.setReiziger(reiziger);

        return ovChipkaart;
    }

    private OVChipkaart __retrieveResultset(ResultSet rs, Reiziger reiziger)  {
        OVChipkaart ovChipkaart = null;
        try {
            ovChipkaart = new OVChipkaart(
                rs.getInt("kaart_nummer"),
                rs.getDate("geldig_tot"),
                rs.getInt("klasse"),
                rs.getDouble("saldo"),
                rs.getInt("reiziger_id"),
                reiziger
            );

            if (reiziger == null) {
                __addrelations(ovChipkaart);
            }
        } catch (Exception err) {
            System.err.println("OvchipkaartDAOsql geeft een error in __retrieveResultSet(): " + err.getMessage() + " " +  err.getStackTrace());
        }
        return ovChipkaart;
    }
}
