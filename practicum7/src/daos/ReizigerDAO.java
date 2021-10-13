package daos;

import domain.Adres;
import domain.OVChipkaart;
import domain.Reiziger;
import interfaces.ReizigerDAOInterface;

import java.util.ArrayList;

public class ReizigerDAO implements ReizigerDAOInterface {
    @Override
    public boolean save(Reiziger reiziger) {
        return false;
    }

    @Override
    public boolean update(Reiziger reiziger) {
        return false;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        return false;
    }

    @Override
    public Reiziger findByAdres(Adres adres) {
        return null;
    }

    @Override
    public Reiziger findByOVChipkaart(OVChipkaart ovChipkaart) {
        return null;
    }

    @Override
    public Reiziger findById(int id) {
        return null;
    }

    @Override
    public ArrayList<Reiziger> findAll() {
        return null;
    }

    @Override
    public ArrayList<Reiziger> findByGeboorteDatum(String datum) {
        return null;
    }
}
