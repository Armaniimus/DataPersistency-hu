package p3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    private Connection conn;

    public AdresDAOPsql(Connection localConn) {
        this.conn = localConn;
    }

    public boolean save(Adres adres) {
        try {
//            String postcode, String huisnummer, String straat, String woonplaats, reiziger_id
            String q = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setInt(1,    adres.getId() );
            pst.setString(2, adres.getPostcode() );
            pst.setString(3, adres.getHuisnummer() );
            pst.setString(4, adres.getStraat() );
            pst.setString(5, adres.getWoonplaats() );
            pst.setInt(6,    adres.getReiziger_id() );

            pst.execute();
            return true;

        } catch(Exception err) {
            System.err.println("AdresDAOsql geeft een error in save(): " + err.getMessage() );
            return false;
        }
    }

    public boolean update(Adres adres) {
        try {
            String q = "UPDATE Adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id=?";
            PreparedStatement pst = this.conn.prepareStatement(q);

            pst.setString(1, adres.getPostcode() );
            pst.setString(2, adres.getHuisnummer() );
            pst.setString(3, adres.getStraat() );
            pst.setString(4, adres.getWoonplaats() );
            pst.setInt(5,    adres.getReiziger_id() );
            pst.setInt(6,    adres.getId() );

            pst.execute();
            return true;
        } catch(Exception err) {
            System.err.println("AdresDAOsql geeft een error in save(): " + err.getMessage() );
            return false;
        }
    }

    public boolean delete(Adres adres) {
        try {
            String q = "DELETE FROM adres WHERE adres_id = ?";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setInt(1, adres.getId() );
            pst.execute();

            return true;
        } catch(Exception err) {
            System.err.println("AdresDAOsql geeft een error in save(): " + err.getMessage() );
            return false;
        }
    };

    public Adres findByReiziger(Reiziger reiziger) {
        int reiziger_id = reiziger.getId();
        try {
            String q = "SELECT * FROM Adres WHERE reiziger_id = ?";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setInt(1, reiziger_id );
            ResultSet rs = pst.executeQuery();

            rs.next();
            return new Adres(
                rs.getInt("adres_id"),
                rs.getString("straat"),
                rs.getString("huisnummer"),
                rs.getString("postcode"),
                rs.getString("woonplaats"),
                rs.getInt("reiziger_id")
            );

        } catch(Exception err) {
            System.err.println("AdresDAOsql geeft een error in save(): " + err.getMessage() );
            return new Adres(0, "", "", "", "", 0);
        }
    };

    public List<Adres> findAll() {
        List<Adres> adresArray = new ArrayList<>();
        try {
            Statement st = this.conn.createStatement();
            ResultSet rs = st.executeQuery("select * from adres");

            while (rs.next()) {
                Adres adres = new Adres(
                    rs.getInt("adres_id"),
                    rs.getString("postcode"),
                    rs.getString("huisnummer"),
                    rs.getString("straat"),
                    rs.getString("woonplaats"),
                    rs.getInt("reiziger_id")
                );
                adresArray.add(adres);
            }
            rs.close();
        } catch(Exception err) {
            System.err.println("AdresDAOsql geeft een error in findAll(): " + err.getMessage() );
        }
        return adresArray;
    }

}
