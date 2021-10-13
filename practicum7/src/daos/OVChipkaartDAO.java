package daos;

import domain.OVChipkaart;
import domain.Product;
import domain.Reiziger;
import interfaces.OVChipkaartDAOInterface;

import java.util.ArrayList;

public class OVChipkaartDAO implements OVChipkaartDAOInterface {
    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        return false;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        return false;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        return false;
    }

    @Override
    public ArrayList<OVChipkaart> findAll() {
        return null;
    }

    @Override
    public ArrayList<OVChipkaart> findByProduct(Product product) {
        return null;
    }

    @Override
    public ArrayList<OVChipkaart> findByReiziger(Reiziger reiziger) {
        return null;
    }
}
