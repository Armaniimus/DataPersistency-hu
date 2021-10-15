package interfaces;

import domain.OVChipkaart;
import domain.Product;
import domain.Reiziger;

import java.util.ArrayList;
import java.util.List;

public interface ProductDAOInterface {
    boolean save(Product product);
    boolean update(Product product);
    void delete(Product product);

    Product findById(int id);
    List<Product> findAll();
}
