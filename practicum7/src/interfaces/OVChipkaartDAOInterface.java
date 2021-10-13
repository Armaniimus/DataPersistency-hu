package interfaces;

import domain.OVChipkaart;
import domain.Product;
import domain.Reiziger;

import java.util.ArrayList;

public interface OVChipkaartDAOInterface {
    boolean save(OVChipkaart ovChipkaart);
//    boolean saveList(ArrayList<OVChipkaart> ovChipkaartArrayList);
    boolean update(OVChipkaart ovChipkaart);
//    boolean updateList(ArrayList<OVChipkaart> ovChipkaartArrayList);
    boolean delete(OVChipkaart ovChipkaart);
//    boolean deleteList(ArrayList<OVChipkaart> ovChipkaartArrayList);
    ArrayList<OVChipkaart>findByReiziger(Reiziger reiziger);
    ArrayList<OVChipkaart>findByProduct(Product product);
    ArrayList<OVChipkaart> findAll();
}
