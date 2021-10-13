package daos;

import domain.Adres;
import domain.Reiziger;
import interfaces.AdresDAOInterface;

import java.util.ArrayList;

public class AdresDAO implements AdresDAOInterface {
    @Override
    public boolean save(Adres adres) {
        return false;
    }

    @Override
    public boolean update(Adres adres) {
        return false;
    }

    @Override
    public boolean delete(Adres adres) {
        return false;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        return null;
    }

    @Override
    public ArrayList<Adres> findAll() {
        return null;
    }
}
