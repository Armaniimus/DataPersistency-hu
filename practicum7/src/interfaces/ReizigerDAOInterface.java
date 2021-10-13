package interfaces;

import domain.Adres;
import domain.OVChipkaart;
import domain.Product;
import domain.Reiziger;

import java.util.ArrayList;

public interface ReizigerDAOInterface {
    boolean save(Reiziger reiziger);
    boolean update(Reiziger reiziger);
    boolean delete(Reiziger reiziger);
    Reiziger findById(int id);
    Reiziger findByAdres(Adres adres);
    Reiziger findByOVChipkaart(OVChipkaart ovChipkaart);
    ArrayList<Reiziger> findByGeboorteDatum(String datum);
    ArrayList<Reiziger> findAll();
}
