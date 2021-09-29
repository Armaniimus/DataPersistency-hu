package interfaces;

import domain.Reiziger;
import domain.Adres;
import domain.OVChipkaart;

import java.util.ArrayList;


public interface ReizigerDAO {
    boolean save(Reiziger reiziger);
    boolean update(Reiziger reiziger);
    boolean delete(Reiziger reiziger);
    Reiziger findById(int id);
    Reiziger findByAdres(Adres adres);
    Reiziger findByOVChipkaart(OVChipkaart ovChipkaart);
    ArrayList<Reiziger> findByGeboorteDatum(String datum);
    ArrayList<Reiziger> findAll();
}
