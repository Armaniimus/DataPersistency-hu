import java.sql.*;
import java.util.ArrayList;

public class AdresDAOPsql implements AdresDAO {
    private Connection connection;
    private ReizigerDAOPsql reizigerDAO;

    public AdresDAOPsql(Connection localConn) {
        this.connection = localConn;
    }

    public void setReizigerDAO(ReizigerDAOPsql reizigerDAO) {
        this.reizigerDAO = reizigerDAO;
    }

    public boolean save(Adres adres) {
        try {
            String q = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, adres.getId());
            pst.setString(2, adres.getPostcode());
            pst.setString(3, adres.getHuisnummer());
            pst.setString(4, adres.getStraat());
            pst.setString(5, adres.getWoonplaats());
            pst.setInt(6, adres.getReizigerId());

            pst.execute();
            pst.close();
            return true;

        } catch (Exception err) {
            System.err.println("AdresDAOsql geeft een error in save(): " + err.getMessage() + " " + err.getStackTrace() );
            return false;
        }
    }

    public boolean update(Adres adres) {
        try {
            String q = "UPDATE Adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ? WHERE adres_id=?";
            PreparedStatement pst = this.connection.prepareStatement(q);

            pst.setString(1, adres.getPostcode());
            pst.setString(2, adres.getHuisnummer());
            pst.setString(3, adres.getStraat());
            pst.setString(4, adres.getWoonplaats());
            pst.setInt(5, adres.getId());

            pst.execute();
            pst.close();
            return true;
        } catch (Exception err) {
            System.err.println("AdresDAOsql geeft een error in update(): " + err.getMessage() + " " + err.getCause() + " " + err.getClass() + " " + adres.getPostcode() + adres.getHuisnummer()  );
            return false;
        }
    }

    public boolean delete(Adres adres) {
        try {
            String q = "DELETE FROM adres WHERE adres_id = ?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, adres.getId());
            pst.execute();
            pst.close();

            return true;
        } catch (Exception err) {
            System.err.println("AdresDAOsql geeft een error in delete(): " + err.getMessage());
            return false;
        }
    }

    public Adres findByReiziger(Reiziger reiziger) {
        try {
            String q = "SELECT * FROM Adres WHERE reiziger_id = ?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, reiziger.getId());
            ResultSet rs = pst.executeQuery();

            Adres adres = null;
            if (rs.next()) {
                adres = __retrieveResultset(rs, null);
            }
            rs.close();
            pst.close();

            return adres;

        } catch (Exception err) {
            System.err.println("AdresDAOsql geeft een error in findByReiziger(): " + err.getMessage() + " " + err.getStackTrace() );
            return null;
        }
    }

    public ArrayList<Adres> findAll() {
        ArrayList<Adres> adresArray = new ArrayList<>();
        try {
            Statement st = this.connection.createStatement();
            ResultSet rs = st.executeQuery("select * from adres");

            while (rs.next()) {
                Adres adres = this.__retrieveResultset(rs, null);
                adresArray.add(adres);
            }
            rs.close();
        } catch (Exception err) {
            System.err.println("AdresDAOsql geeft een error in findAll(): " + err.getMessage() + " " + err.getStackTrace() );
        }
        return adresArray;
    }

    public Adres findById(int id) {
        try {
            Adres adres = null;

            String q = "SELECT * FROM Adres WHERE adres_id = ?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                adres = this.__retrieveResultset(rs, null);
            }

            rs.close();
            pst.close();
            return adres;

        } catch(Exception err){
            System.err.println("AdresDAOsql geeft een error in findByReiziger(): " + err.getMessage() + " " + err.getStackTrace() );
            return null;
        }
    }

    private Adres __retrieveResultset(ResultSet rs, Reiziger reiziger)  {
        Adres adres = null;
        try {
            adres = new Adres(
                rs.getInt("adres_id"),
                rs.getString("straat"),
                rs.getString("huisnummer"),
                rs.getString("woonplaats"),
                rs.getString("postcode"),
                rs.getInt("reiziger_id"),
                reiziger
            );

            if (reiziger == null) {
                __addrelations(adres);
            }
        } catch (Exception err) {
            System.err.println("AdresDAOsql geeft een error in __retrieveResultSet(): " + err.getMessage() + " " +  err.getStackTrace());
        }
        return adres;
    }

    private Adres __addrelations(Adres adres) {
        Reiziger reiziger = reizigerDAO.findByAdres(adres);
        adres.setReiziger(reiziger);

        return adres;
    }
}
