package daos;

import interfaces.ReizigerDAO;

import domain.Reiziger;
import domain.OVChipkaart;
import domain.Adres;
import lib.GenerateException;

import java.sql.*;
import java.util.ArrayList;

public class ReizigerDAOPsql implements ReizigerDAO {
    private final Connection connection;
    private final AdresDAOPsql adresDAO;
    private final OVChipkaartDAOPsql ovChipkaartDAO;

    public ReizigerDAOPsql(Connection connection, AdresDAOPsql localAdao, OVChipkaartDAOPsql localOdao) {
        this.connection = connection;
        this.adresDAO = localAdao;
        this.ovChipkaartDAO = localOdao;
    }

    public boolean save(Reiziger reiziger) {
        try {
            if (reiziger.getOvChipkaartList() == null || reiziger.getOvChipkaartList().isEmpty() ) {
                throw new Exception("update heeft geen valide OvChipkaart list object");

            } else if (reiziger.getAdres() == null) {
                throw new Exception("update heeft geen valide domain.Adres object");

            } else {
                String q = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboorteDatum) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pst = this.connection.prepareStatement(q);
                pst.setInt(1, reiziger.getId() );
                pst.setString(2, reiziger.getVoorletters() );
                pst.setString(3, reiziger.getTussenvoegsel() );
                pst.setString(4, reiziger.getAchternaam() );
                pst.setDate(5,  new Date(reiziger.getGeboorteDatum().getTime() ) );

                pst.execute();

                this.adresDAO.save( reiziger.getAdres() );
                this.ovChipkaartDAO.saveList( reiziger.getOvChipkaartList() );
                return true;
            }

        } catch(Exception err) {
            this.printErr(err);
            return false;
        }
    }

    public boolean update(Reiziger reiziger) {
        try {
            if (reiziger.getOvChipkaartList() == null || reiziger.getOvChipkaartList().isEmpty() ) {
                throw new Exception("update heeft geen OvChipkaart list object");

            } else if (reiziger.getAdres() == null) {
                throw new Exception("update heeft geen domain.Adres object");

            } else {
                String q = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboorteDatum = ? WHERE reiziger_id=?";
                PreparedStatement pst = this.connection.prepareStatement(q);
                pst.setString(1, reiziger.getVoorletters() );
                pst.setString(2, reiziger.getTussenvoegsel() );
                pst.setString(3, reiziger.getAchternaam() );
                pst.setDate(4,  new Date(reiziger.getGeboorteDatum().getTime() ) );
                pst.setInt(5, reiziger.getId() );

                pst.execute();

                this.adresDAO.update( reiziger.getAdres() );
                this.ovChipkaartDAO.updateList( reiziger.getOvChipkaartList() );
                return true;
            }

        } catch(Exception err) {
            this.printErr(err);
            return false;
        }
    }

    public boolean delete(Reiziger reiziger) {
        try {
            if (reiziger.getOvChipkaartList() == null || reiziger.getOvChipkaartList().isEmpty() ) {
                throw new Exception("Delete heeft geen valide ovArrayList object");

            } else if ( reiziger.getAdres() == null ) {
                throw new Exception("Delete heeft geen valide adres object");

            } else {
                this.adresDAO.delete(reiziger.getAdres() );
                this.ovChipkaartDAO.deleteList( reiziger.getOvChipkaartList() );

                String q = "DELETE FROM reiziger WHERE reiziger_id = ?";
                PreparedStatement pst = this.connection.prepareStatement(q);
                pst.setInt(1, reiziger.getId() );
                pst.execute();

                return true;
            }

        } catch(Exception err) {
            this.printErr(err);
            return false;
        }
    }

    public Reiziger findById(int id) {
        return this.__findById(id, null);
    }

    public Reiziger findByAdres(Adres adres){
        int id = adres.getReizigerId();

        return this.__findById(id, adres);
    }

    public Reiziger findByOVChipkaart(OVChipkaart ovChipkaart){
        int id = ovChipkaart.getReizigerId();

        return this.__findById(id, null);
    }

    public ArrayList<Reiziger> findByGeboorteDatum(String datum) {
        ArrayList<Reiziger> reizigersArray = new ArrayList<>();

        try {
            String q = "SELECT * FROM reiziger WHERE geboorteDatum = ?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setDate(1, Date.valueOf(datum));
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Reiziger reiziger =  this.__retrieveResultSet(rs, null);
                reizigersArray.add(reiziger);
            }

            pst.close();
            rs.close();

            return reizigersArray;
        } catch(Exception err) {
            this.printErr(err);
            return reizigersArray;
        }
    }

    public ArrayList<Reiziger> findAll() {
        ArrayList<Reiziger> reizigersArray = new ArrayList<>();

        try {
            Statement st = this.connection.createStatement();
            ResultSet rs = st.executeQuery("select * from reiziger");

            while (rs.next()) {
                Reiziger reiziger = this.__retrieveResultSet(rs, null);
                reizigersArray.add(reiziger);
            }
            rs.close();
        } catch (Exception err) {
            this.printErr(err);
        }

        return reizigersArray;
    }

    private Reiziger __addRelations(Reiziger reiziger) {
        this.__addAdresRelation(reiziger);
        this.__addOvchipkaartRelation(reiziger);

        return reiziger;
    }

    private void __addAdresRelation(Reiziger reiziger) {
        Adres adres = adresDAO.findByReiziger( reiziger );
        if (adres != null) {
            reiziger.setAdres(adres);
        }
    }

    private void __addOvchipkaartRelation(Reiziger reiziger) {
        ArrayList<OVChipkaart> ovChipkaarten = ovChipkaartDAO.findByReiziger( reiziger );
        if (!ovChipkaarten.isEmpty()) {
            reiziger.setOvChipkaartList(ovChipkaarten);
        }
    }

    private Reiziger __findById(int id, Adres adres) {
        try {
            String q = "SELECT * FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, id );
            ResultSet rs = pst.executeQuery();

            Reiziger reiziger = null;
            if ( rs.next() ) {
                reiziger =  this.__retrieveResultSet(rs, adres);
            }

            pst.close();
            rs.close();
            return reiziger;


        } catch(Exception err) {
            this.printErr(err);
            return null;
        }
    }

    private Reiziger __retrieveResultSet(ResultSet rs, Adres adres)  {
        Reiziger reiziger = null;
        try {
            reiziger = new Reiziger(
                rs.getInt("reiziger_id"),
                rs.getString("voorletters"),
                rs.getString("tussenvoegsel"),
                rs.getString("achternaam"),
                rs.getDate("geboorteDatum"),
                null
            );

            if (adres == null) {
                this.__addAdresRelation(reiziger);
            }
            this.__addOvchipkaartRelation(reiziger);

        } catch (Exception err) {
            this.printErr(err);
        }
        return reiziger;
    }

    private void printErr(Exception err) {
        String className = "" + this.getClass();
        GenerateException.printError(className, err);
    }
}
