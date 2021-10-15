package interfaces;

import domain.Adres;
import domain.OVChipkaart;
import domain.Product;
import domain.Reiziger;

import java.util.ArrayList;
import java.util.List;

public interface ReizigerDAOInterface {
    boolean save(Reiziger reiziger);
    boolean update(Reiziger reiziger);
    void delete(Reiziger reiziger);

    Reiziger findById(int id);
    List<Reiziger> findByGeboorteDatum(String datum);
    List<Reiziger> findAll();
}
