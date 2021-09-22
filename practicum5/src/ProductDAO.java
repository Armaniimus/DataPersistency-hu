import java.util.ArrayList;

public interface ProductDAO {
    public boolean save(OVChipkaart ovChipkaart);
    public boolean saveList(ArrayList<Product> productArrayList);
    public boolean update(OVChipkaart ovChipkaart);
    public boolean updateList(ArrayList<Product> productArrayList);
    public boolean delete(OVChipkaart ovChipkaart);
    public boolean deleteList(ArrayList<Product> productArrayList);
    public ArrayList<Product>findByOVChipkaart(OVChipkaart ovChipkaart);
    public ArrayList<Product> findAll();
}
