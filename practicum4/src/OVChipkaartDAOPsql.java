import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection connection;

    public OVChipkaartDAOPsql(Connection localConn) {
        this.connection = localConn;
    }

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

        } catch(Exception err) {
            System.err.println("OVCHiplaartDAOPsql geeft een error in findAll(): " + err.getMessage() + " " + err.getStackTrace() );
        }

        return OVChipkaartArray;
    }
}
