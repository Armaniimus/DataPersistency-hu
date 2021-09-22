import java.util.ArrayList;

public interface OVChipkaartDAO {
    public boolean save(OVChipkaart ovChipkaart);
    public boolean saveList(ArrayList<OVChipkaart> ovChipkaartArrayList);
    public boolean update(OVChipkaart ovChipkaart);
    public boolean updateList(ArrayList<OVChipkaart> ovChipkaartArrayList);
    public boolean delete(OVChipkaart ovChipkaart);
    public boolean deleteList(ArrayList<OVChipkaart> ovChipkaartArrayList);
    public ArrayList<OVChipkaart>findByReiziger(Reiziger reiziger);
    public ArrayList<OVChipkaart>findByProduct(Product product);
    public ArrayList<OVChipkaart> findAll();
}


