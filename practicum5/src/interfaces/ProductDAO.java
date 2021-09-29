package interfaces;

import domain.Product;
import domain.OVChipkaart;

import java.util.ArrayList;

public interface ProductDAO {
    boolean save(Product product);
    boolean saveList(ArrayList<Product> productArrayList);
    boolean update(Product product);
    boolean updateList(ArrayList<Product> productArrayList);
    boolean delete(Product product);
    boolean deleteList(ArrayList<Product> productArrayList);
    ArrayList<Product>findByOVChipkaart(OVChipkaart ovChipkaart);
    ArrayList<Product> findAll();
}
