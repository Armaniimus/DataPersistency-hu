import java.util.ArrayList;

public interface ProductDAO {
    public boolean save(Product product);
    public boolean saveList(ArrayList<Product> productArrayList);
    public boolean update(Product product);
    public boolean updateList(ArrayList<Product> productArrayList);
    public boolean delete(Product product);
    public boolean deleteList(ArrayList<Product> productArrayList);
    public ArrayList<Product>findByOVChipkaart(OVChipkaart ovChipkaart);
    public ArrayList<Product> findAll();
}
