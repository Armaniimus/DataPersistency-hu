import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection conn;
    private ReizigerDAO rdao;

    public OVChipkaartDAOPsql(Connection localConn) { this.conn = localConn; }
    public void setRdao(ReizigerDAO rdao) { this.rdao = rdao; }

    public boolean save(OVChipkaart ovChipkaart) {
        try {
            return false;
        } catch(Exception err) {
            System.err.println("OVCHiplaartDAOPsql geeft een error in save(): " + err.getMessage() );
            return false;
        }
    }

    public boolean update(OVChipkaart ovChipkaart) {
        try {
            return false;
        } catch(Exception err) {
            System.err.println("OVCHiplaartDAOPsql geeft een error in update(): " + err.getMessage() );
            return false;
        }
    }

    public boolean delete(OVChipkaart ovChipkaart) {
        try {
            return false;
        } catch(Exception err) {
            System.err.println("OVCHiplaartDAOPsql geeft een error in delete(): " + err.getMessage() );
            return false;
        }
    }

    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        List<OVChipkaart> OVChipkaartArray = new ArrayList<>();
        try {

        } catch(Exception err) {
            System.err.println("OVCHiplaartDAOPsql geeft een error in findByReiziger(): " + err.getMessage() );
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
