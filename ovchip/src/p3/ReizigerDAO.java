package p3;

import java.util.List;

public interface ReizigerDAO {
    public boolean save(Reiziger reiziger);
    public boolean update(Reiziger reiziger);
    public boolean delete(Reiziger reiziger);
    public Reiziger findbyid(int id);
    public List<Reiziger> findByGBdatum(String datum);
    public List<Reiziger> findAll();
}
