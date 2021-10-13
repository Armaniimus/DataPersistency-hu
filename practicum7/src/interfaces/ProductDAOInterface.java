package interfaces;

import domain.OVChipkaart;
import domain.Product;

import java.util.ArrayList;

public interface ProductDAOInterface {
    boolean save(Product product);
    boolean update(Product product);
    boolean delete(Product product);

//    boolean saveList(ArrayList<Product> productArrayList);
//    boolean updateList(ArrayList<Product> productArrayList);
//    boolean deleteList(ArrayList<Product> productArrayList);

    ArrayList<Product>findByOVChipkaart(OVChipkaart ovChipkaart);
    ArrayList<Product> findAll();
}
