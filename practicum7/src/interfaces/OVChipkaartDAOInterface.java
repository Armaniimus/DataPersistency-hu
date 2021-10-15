package interfaces;

import domain.OVChipkaart;
import domain.Product;
import domain.Reiziger;

import java.util.ArrayList;
import java.util.List;

public interface OVChipkaartDAOInterface {
    boolean save(OVChipkaart ovChipkaart);
    boolean update(OVChipkaart ovChipkaart);
    void delete(OVChipkaart ovChipkaart);

    OVChipkaart findById(int id);
    List<OVChipkaart> findAll();
}
