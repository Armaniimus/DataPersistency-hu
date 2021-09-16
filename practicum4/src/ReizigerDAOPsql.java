import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAOPsql adao;
    private OVChipkaartDAO odao;

    public ReizigerDAOPsql(Connection connection, AdresDAOPsql localAdao, OVChipkaartDAO localOdao) {
        this.conn = connection;
        this.adao = localAdao;
        this.odao = localOdao;
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
            PreparedStatement pst = this.conn.prepareStatement(q);
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
            String q = "DELETE FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement pst = this.conn.prepareStatement(q);
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
        return this.__findByIdWithRelations(id);
    }

    public Reiziger findByAdres(Adres adres){
        int id = adres.getReizigerId();
        Reiziger reiziger = this.__findById(id);
        reiziger.setAdres(adres);
        reiziger = this.__addOvchipkaartRelation(reiziger);

        return reiziger;
    }

    public Reiziger findByOVChipkaart(OVChipkaart ovChipkaart){
        int id = ovChipkaart.getReizigerId();
        Reiziger reiziger = this.__findByIdWithRelations(id);

        return reiziger;
    }

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
                    rs.getDate("geboorteDatum"),
                    null
                );
                reiziger = this.__addRelations(reiziger);

                reizigersArray.add(reiziger);
            }

            pst.close();
            rs.close();

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
                        rs.getDate("geboorteDatum"),
                        null
                );
                reiziger = this.__addRelations(reiziger);

                reizigersArray.add(reiziger);
            }
            rs.close();
        } catch (Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in findAll(): " + err.getMessage() );
        }

        return reizigersArray;
    };

    private Reiziger __findByIdWithRelations(int id) {
        Reiziger reiziger = this.__findById(id);
        return this.__addRelations(reiziger);
    }

    private Reiziger __addRelations(Reiziger reiziger) {
        reiziger = this.__addAdresRelation(reiziger);
        reiziger = this.__addOvchipkaartRelation(reiziger);

        return reiziger;
    }

    private Reiziger __addAdresRelation(Reiziger reiziger) {
        Adres adres = adao.findByReiziger( reiziger );
        if (adres != null) {
            reiziger.setAdres(adres);
        }

        return reiziger;
    }

    private Reiziger __addOvchipkaartRelation(Reiziger reiziger) {
        List<OVChipkaart> ovChipkaarten = odao.findByReiziger( reiziger );
        if (!ovChipkaarten.isEmpty()) {
            reiziger.setOvChipkaartList(ovChipkaarten);
        }

        return reiziger;
    }

    private Reiziger __findById(int id) {
        try {
            Reiziger reiziger = null;
            String q = "SELECT * FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setInt(1, id );
            ResultSet rs = pst.executeQuery();

            if ( rs.next() ) {
                reiziger = new Reiziger(
                    rs.getInt("reiziger_id"),
                    rs.getString("voorletters"),
                    rs.getString("tussenvoegsel"),
                    rs.getString("achternaam"),
                    rs.getDate("geboorteDatum"),
                    null
                );
            }

            pst.close();
            rs.close();
            return reiziger;


        } catch(Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in __findbyid(): " + err.getMessage() );
            return null;
        }
    }
}
