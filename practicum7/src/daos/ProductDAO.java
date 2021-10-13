package daos;

import domain.OVChipkaart;
import domain.Product;
import interfaces.ProductDAOInterface;

import java.util.ArrayList;

public class ProductDAO implements ProductDAOInterface {

    @Override
    public boolean save( Product product) {
        return false;
    }

    @Override
    public boolean update( Product product) {
        return false;
    }

    @Override
    public boolean delete( Product product ) {
        return false;
    }

    @Override
    public ArrayList<Product> findAll() {
        return null;
    }

    @Override
    public ArrayList<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        return null;
    }
}
