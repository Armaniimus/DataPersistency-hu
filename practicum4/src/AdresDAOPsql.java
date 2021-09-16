import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
            pst.setInt(1,    adres.getId() );
            pst.setString(2, adres.getPostcode() );
            pst.setString(3, adres.getHuisnummer() );
            pst.setString(4, adres.getStraat() );
            pst.setString(5, adres.getWoonplaats() );
            pst.setInt(6,    adres.getReizigerId() );

            pst.execute();
            pst.close();
            return true;

        } catch(Exception err) {
            System.err.println("AdresDAOsql geeft een error in save(): " + err.getMessage() );
            return false;
        }
    }

    public boolean update(Adres adres) {
        try {
            String q = "UPDATE Adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id=?";
            PreparedStatement pst = this.connection.prepareStatement(q);

            pst.setString(1, adres.getPostcode() );
            pst.setString(2, adres.getHuisnummer() );
            pst.setString(3, adres.getStraat() );
            pst.setString(4, adres.getWoonplaats() );
            pst.setInt(5,    adres.getReizigerId() );
            pst.setInt(6,    adres.getId() );

            pst.execute();
            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("AdresDAOsql geeft een error in update(): " + err.getMessage() );
            return false;
        }
    }

    public boolean delete(Adres adres) {
        try {
            String q = "DELETE FROM adres WHERE adres_id = ?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, adres.getId() );
            pst.execute();
            pst.close();

            return true;
        } catch(Exception err) {
            System.err.println("AdresDAOsql geeft een error in delete(): " + err.getMessage() );
            return false;
        }
    };

    public Adres findByReiziger(Reiziger reiziger) {
        try {
            Adres adres = null;

            String q = "SELECT * FROM Adres WHERE reiziger_id = ?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, reiziger.getId() );
            ResultSet rs = pst.executeQuery();

            if (rs.next() ) {
                adres = new Adres(
                    rs.getInt("adres_id"),
                    rs.getString("straat"),
                    rs.getString("huisnummer"),
                    rs.getString("postcode"),
                    rs.getString("woonplaats"),
                    rs.getInt("reiziger_id"),
                    reiziger
                );
            }

            rs.close();
            pst.close();
            return adres;

        } catch(Exception err) {
            System.err.println("AdresDAOsql geeft een error in findByReiziger(): " + err.getMessage() );
            return null;
        }
    };

    public List<Adres> findAll() {
        List<Adres> adresArray = new ArrayList<>();
        try {
            Statement st = this.connection.createStatement();
            ResultSet rs = st.executeQuery("select * from adres");

            while ( rs.next() ) {
                Adres adres = new Adres(
                    rs.getInt("adres_id"),
                    rs.getString("postcode"),
                    rs.getString("huisnummer"),
                    rs.getString("straat"),
                    rs.getString("woonplaats"),
                    rs.getInt("reiziger_id"),
                    null
                );

                Reiziger reiziger = reizigerDAO.findByAdres( adres );
                adres.setReiziger(reiziger);

                adresArray.add(adres);
            }
            rs.close();
        } catch(Exception err) {
            System.err.println("AdresDAOsql geeft een error in findAll(): " + err.getMessage() );
        }
        return adresArray;
    }
}
