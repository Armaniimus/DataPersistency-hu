import java.util.ArrayList;

public interface ReizigerDAO {
    public boolean save(Reiziger reiziger);
    public boolean update(Reiziger reiziger);
    public boolean delete(Reiziger reiziger);
    public Reiziger findById(int id);
    public Reiziger findByAdres(Adres adres);
    public Reiziger findByOVChipkaart(OVChipkaart ovChipkaart);
    public ArrayList<Reiziger> findByGBdatum(String datum);
    public ArrayList<Reiziger> findAll();
}
