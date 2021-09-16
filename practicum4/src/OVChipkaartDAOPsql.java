import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection conn;
    private ReizigerDAO rdao;

    public OVChipkaartDAOPsql(Connection localConn) { this.conn = localConn; }
    public void setRdao(ReizigerDAO rdao) { this.rdao = rdao; }

    public boolean save(OVChipkaart ovChipkaart) {
        try {
            String q = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setInt(1, ovChipkaart.getKaartNummer() );
            pst.setDate(2,  new Date(ovChipkaart.getGeldigTot().getTime() ) );
            pst.setInt(3,ovChipkaart.getKlasse() );
            pst.setDouble(4, ovChipkaart.getSaldo() );
            pst.setInt(5, ovChipkaart.getReizigerId() );

            pst.execute();
            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("OVCHiplaartDAOPsql geeft een error in save(): " + err.getMessage() );
            return false;
        }
    }

    public boolean update(OVChipkaart ovChipkaart) {
        try {
            String q = "UPDATE ov_chipkaart SET geldig_tot=?, klasse=?, saldo=?, reiziger_id=? WHERE kaart_nummer=?;";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setDate(1,  new Date(ovChipkaart.getGeldigTot().getTime() ) );
            pst.setInt(2, ovChipkaart.getKlasse() );
            pst.setDouble(3, ovChipkaart.getSaldo() );
            pst.setInt(4, ovChipkaart.getReizigerId() );
            pst.setInt(5, ovChipkaart.getKaartNummer() );

            pst.execute();

            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("OVCHiplaartDAOPsql geeft een error in update(): " + err.getMessage() );
            return false;
        }
    }

    public boolean delete(OVChipkaart ovChipkaart) {
        try {
            String q = "DELETE FROM ov_chipkaart WHERE kaart_nummer=?;";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setInt(1, ovChipkaart.getKaartNummer() );
            pst.execute();

            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("OVCHiplaartDAOPsql geeft een error in delete(): " + err.getMessage() );
            return false;
        }
    }

    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        List<OVChipkaart> OVChipkaartArray = new ArrayList<>();
        try {
            String q = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setInt(1, reiziger.getId() );
            ResultSet rs = pst.executeQuery();

            while (rs.next() ) {
                OVChipkaart ovChipkaart = new OVChipkaart(
                    rs.getInt("kaart_nummer"),
                    rs.getDate("geldig_tot"),
                    rs.getInt("klasse"),
                    rs.getDouble("saldo"),
                    rs.getInt("reiziger_id"),
                    reiziger
                );
                OVChipkaartArray.add(ovChipkaart);
            }
            rs.close();
            pst.close();

        } catch(Exception err) {
            System.err.println("OVCHipkaartDAOPsql geeft een error in findByReiziger(): " + err.getMessage() );
        }

        return OVChipkaartArray;
    }

    public List<OVChipkaart> findAll() {
        List<OVChipkaart> OVChipkaartArray = new ArrayList<>();
        try {
            Statement st = this.conn.createStatement();
            ResultSet rs = st.executeQuery("select * from ov_chipkaart");

            while (rs.next()) {
                OVChipkaart ovChipkaart = new OVChipkaart(
                    rs.getInt("kaart_nummer"),
                    rs.getDate("geldig_tot"),
                    rs.getInt("klasse"),
                    rs.getDouble("saldo"),
                    rs.getInt("reiziger_id"),
                    null
                );
                Reiziger reiziger = this.rdao.findByOVChipkaart( ovChipkaart );
                ovChipkaart.setReizigerObj( reiziger );

                OVChipkaartArray.add(ovChipkaart);
            }
            rs.close();

        } catch(Exception err) {
            System.err.println("OVCHiplaartDAOPsql geeft een error in findAll(): " + err.getMessage() + " " + err.getStackTrace() );
        }

        return OVChipkaartArray;
    }
}
