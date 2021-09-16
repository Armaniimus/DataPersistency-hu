import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection connection;
    private AdresDAOPsql adresDAO;
    private OVChipkaartDAO ovChipkaartDAO;

    public ReizigerDAOPsql(Connection connection, AdresDAOPsql localAdao, OVChipkaartDAO localOdao) {
        this.connection = connection;
        this.adresDAO = localAdao;
        this.ovChipkaartDAO = localOdao;
    }

    public boolean save(Reiziger reiziger) {
        try {
            String q = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboorteDatum) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, reiziger.getId() );
            pst.setString(2, reiziger.getVoorletters() );
            pst.setString(3, reiziger.getTussenvoegsel() );
            pst.setString(4, reiziger.getAchternaam() );
            pst.setDate(5,  new Date(reiziger.getGeboorteDatum().getTime() ) );

            pst.execute();
            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in save(): " + err.getMessage() );
            return false;
        }
    };

    public boolean update(Reiziger reiziger) {
        try {
            String q = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboorteDatum = ? WHERE reiziger_id=?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setString(1, reiziger.getVoorletters() );
            pst.setString(2, reiziger.getTussenvoegsel() );
            pst.setString(3, reiziger.getAchternaam() );
            pst.setDate(4,  new Date(reiziger.getGeboorteDatum().getTime() ) );
            pst.setInt(5, reiziger.getId() );

            pst.execute();
            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in update(): " + err.getMessage() );
            return false;
        }
    };

    public boolean delete(Reiziger reiziger) {
        try {
            //make sure all relations are set
            reiziger = this.__addRelations( reiziger);

            //load relations in variables
            List<OVChipkaart> ovList = reiziger.getOvChipkaartList();
            Adres adres = reiziger.getAdres();

            //delete relations
            if (ovList != null) {
                for (int i=0; i<ovList.size(); i++) {
                    this.ovChipkaartDAO.delete( ovList.get(i) );
                }
            }
            if (adres != null) {
                this.adresDAO.delete( adres );
            }

            //delete reiziger
            String q = "DELETE FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, reiziger.getId() );
            pst.execute();
            pst.close();

            return true;
        } catch(Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in update(): " + err.getMessage() );
            return false;
        }
    };

    public Reiziger findById(int id) {
        return this.__findById(id, null);
    }

    public Reiziger findByAdres(Adres adres){
        int id = adres.getReizigerId();
        Reiziger reiziger = this.__findById(id, adres);

        return reiziger;
    }

    public Reiziger findByOVChipkaart(OVChipkaart ovChipkaart){
        int id = ovChipkaart.getReizigerId();
        Reiziger reiziger = this.__findById(id, null);

        return reiziger;
    }

    public List<Reiziger> findByGBdatum(String datum) {
        List<Reiziger> reizigersArray = new ArrayList<>();

        try {
            String q = "SELECT * FROM reiziger WHERE geboorteDatum = ?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setDate(1, Date.valueOf(datum));
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Reiziger reiziger =  this.__retrieveResultset(rs, null);
                reizigersArray.add(reiziger);
            }

            pst.close();
            rs.close();

            return reizigersArray;
        } catch(Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in findByGbDatum(): " + err.getMessage() + " " + err.getStackTrace() );
            return reizigersArray;
        }
    };

    public List<Reiziger> findAll() {
        List<Reiziger> reizigersArray = new ArrayList<>();

        try {
            Statement st = this.connection.createStatement();
            ResultSet rs = st.executeQuery("select * from reiziger");

            while (rs.next()) {
                Reiziger reiziger = this.__retrieveResultset(rs, null);
                reizigersArray.add(reiziger);
            }
            rs.close();
        } catch (Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in findAll(): " + err.getMessage() + " " + err.getStackTrace() );
        }

        return reizigersArray;
    };

    private Reiziger __addRelations(Reiziger reiziger) {
        reiziger = this.__addAdresRelation(reiziger);
        reiziger = this.__addOvchipkaartRelation(reiziger);

        return reiziger;
    }

    private Reiziger __addAdresRelation(Reiziger reiziger) {
        Adres adres = adresDAO.findByReiziger( reiziger );
        if (adres != null) {
            reiziger.setAdres(adres);
        }

        return reiziger;
    }

    private Reiziger __addOvchipkaartRelation(Reiziger reiziger) {
        List<OVChipkaart> ovChipkaarten = ovChipkaartDAO.findByReiziger( reiziger );
        if (!ovChipkaarten.isEmpty()) {
            reiziger.setOvChipkaartList(ovChipkaarten);
        }

        return reiziger;
    }

    private Reiziger __findById(int id, Adres adres) {
        try {
            String q = "SELECT * FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, id );
            ResultSet rs = pst.executeQuery();

            Reiziger reiziger = null;
            if ( rs.next() ) {
                reiziger =  this.__retrieveResultset(rs, adres);
            }

            pst.close();
            rs.close();
            return reiziger;


        } catch(Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in __findbyid(): " + err.getMessage() + " " + err.getStackTrace() );
            return null;
        }
    }

    private Reiziger __retrieveResultset(ResultSet rs, Adres adres)  {
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
            System.err.println("ReizigersDAOsql geeft een error in __retrieveResultSet(): " + err.getMessage() + " " +  err.getStackTrace());
        }
        return reiziger;
    }
}
