package daos;

import interfaces.AdresDAO;

import domain.Adres;
import domain.Reiziger;
import lib.GenerateException;

import java.sql.*;
import java.util.ArrayList;


public class AdresDAOPsql implements AdresDAO {
    private final Connection connection;
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
            this.printErr(err);
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
            this.printErr(err);
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
            this.printErr(err);
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
                adres = __retrieveResultSet(rs, reiziger);
            }
            rs.close();
            pst.close();

            return adres;

        } catch (Exception err) {
            this.printErr(err);
            return null;
        }
    }

    public ArrayList<Adres> findAll() {
        ArrayList<Adres> adresArray = new ArrayList<>();
        try {
            Statement st = this.connection.createStatement();
            ResultSet rs = st.executeQuery("select * from adres");

            while (rs.next()) {
                Adres adres = this.__retrieveResultSet(rs, null);
                adresArray.add(adres);
            }
            rs.close();
        } catch (Exception err) {
            this.printErr(err);
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
                adres = this.__retrieveResultSet(rs, null);
            }

            rs.close();
            pst.close();
            return adres;

        } catch(Exception err){
            this.printErr(err);
            return null;
        }
    }

    private Adres __retrieveResultSet(ResultSet rs, Reiziger reiziger)  {
        Adres adres = null;
        try {
            adres = new Adres(
                rs.getInt("adres_id"),
                rs.getString("straat"),
                rs.getString("huisnummer"),
                rs.getString("woonplaats"),
                rs.getString("postcode"),
                rs.getInt("reiziger_id")
            );

            if (reiziger == null) {
                __addRelations(adres);
            } else {
                adres.setReiziger(reiziger, false);
            }

        } catch (Exception err) {
            this.printErr(err);
        }
        return adres;
    }

    private void __addRelations(Adres adres) {
        Reiziger reiziger = reizigerDAO.findByAdres(adres);
        adres.setReiziger(reiziger, false);
    }

    private void printErr(Exception err) {
        String className = "" + this.getClass();
        GenerateException.printError(className, err);
    }

}
