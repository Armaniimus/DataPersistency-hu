package p3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAO adao;

    public ReizigerDAOPsql(Connection connection) {
        this.conn = connection;
         adao = new AdresDAOPsql(connection);
    }

    public boolean save(Reiziger reiziger) {
        try {
            String q = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboorteDatum) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setInt(1, reiziger.getId() );
            pst.setString(2, reiziger.getVoorletters() );
            pst.setString(3, reiziger.getTussenvoegsel() );
            pst.setString(4, reiziger.getAchternaam() );
            pst.setDate(5,  new Date(reiziger.getGeboorteDatum().getTime() ) );

            pst.execute();
            return true;
        } catch(Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in save(): " + err.getMessage() );
            return false;
        }
    };

    public boolean update(Reiziger reiziger) {
        try {
            String q = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboorteDatum = ? WHERE reiziger_id=?";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setString(1, reiziger.getVoorletters() );
            pst.setString(2, reiziger.getTussenvoegsel() );
            pst.setString(3, reiziger.getAchternaam() );
            pst.setDate(4,  new Date(reiziger.getGeboorteDatum().getTime() ) );
            pst.setInt(5, reiziger.getId() );

            pst.execute();
            return true;
        } catch(Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in update(): " + err.getMessage() );
            return false;
        }
    };

    public boolean delete(Reiziger reiziger) {
        try {
            String q = "DELETE FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setInt(1, reiziger.getId() );
            pst.execute();

            return true;
        } catch(Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in update(): " + err.getMessage() );
            return false;
        }
    };

    public Reiziger findbyid(int id) {
        try {
            String q = "SELECT * FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setInt(1, id );
            ResultSet rs = pst.executeQuery();

            rs.next();
            return new Reiziger(
                rs.getInt("reiziger_id"),
                rs.getString("voorletters"),
                rs.getString("tussenvoegsel"),
                rs.getString("achternaam"),
                rs.getDate("geboorteDatum")
            );

        } catch(Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in findbyid(): " + err.getMessage() );
            return new Reiziger(0, "", "", "", Date.valueOf("00-00-0000") );
        }
    };

    public List<Reiziger> findByGBdatum(String datum) {
        List<Reiziger> reizigersArray = new ArrayList<>();

        try {
            String q = "SELECT * FROM reiziger WHERE geboorteDatum = ?";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setDate(1, Date.valueOf(datum));
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Reiziger reiziger = new Reiziger(
                    rs.getInt("reiziger_id"),
                    rs.getString("voorletters"),
                    rs.getString("tussenvoegsel"),
                    rs.getString("achternaam"),
                    rs.getDate("geboorteDatum")
                );
                reizigersArray.add(reiziger);
            }

            return reizigersArray;
        } catch(Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in findByGbDatum(): " + err.getMessage() );
            return reizigersArray;
        }
    };

    public List<Reiziger> findAll() {
        List<Reiziger> reizigersArray = new ArrayList<>();

        try {
            Statement st = this.conn.createStatement();
            ResultSet rs = st.executeQuery("select * from reiziger");

            while (rs.next()) {
                Reiziger reiziger = new Reiziger(
                    rs.getInt("reiziger_id"),
                    rs.getString("voorletters"),
                    rs.getString("tussenvoegsel"),
                    rs.getString("achternaam"),
                    rs.getDate("geboorteDatum")
                );
                reizigersArray.add(reiziger);
            }
            rs.close();
        } catch (Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in findAll(): " + err.getMessage() );
        }

        return reizigersArray;
    };
}
