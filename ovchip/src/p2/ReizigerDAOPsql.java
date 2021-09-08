package p2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;

    public ReizigerDAOPsql(Connection connection) {
        this.conn = connection;
    }

    public boolean save(Reiziger reiziger) {
        return false;
    };
    public boolean update(Reiziger reiziger) {
        return false;
    };

    public boolean delete(Reiziger reiziger) {
        return false;
    };

    public Reiziger findbyid(int id) {
        return new Reiziger(0, "", "", "", java.sql.Date.valueOf("00-00-0000") );
    };

    public List<Reiziger> findByGBdatum(String datum) {
        List<Reiziger> reizigers = new ArrayList<>();
        return reizigers;
    };

    public List<Reiziger> findAll() {

        List<Reiziger> reizigers = new ArrayList<>();
        return reizigers;
    };
}
