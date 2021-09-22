import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ProductDAOPsql implements ProductDAO{
    private Connection connection;
    private OVChipkaartDAO ovChipkaartDAO;

    public ProductDAOPsql ( Connection localConnection, OVChipkaartDAO localOvDao){
        this.connection = localConnection;
        this.ovChipkaartDAO = localOvDao;
    }


    public boolean save(OVChipkaart ovChipkaart) {
        return false;
    }

    public boolean saveList(ArrayList<Product> productArrayList) {
        return false;
    }

    public boolean update(OVChipkaart ovChipkaart) {
        return false;
    }

    public boolean updateList(ArrayList<Product> productArrayList) {
        return false;
    }

    public boolean delete(OVChipkaart ovChipkaart) {
        return false;
    }

    public boolean deleteList(ArrayList<Product> productArrayList) {
        return false;
    }

    public ArrayList<Product>findByOVChipkaart(OVChipkaart ovChipkaart) {
        ArrayList<Product> productArrayList = new ArrayList<>();
        try {
            String q = "SELECT product.product_nummer, naam, beschrijving, prijs FROM ov_chipkaart_product " +
                    "INNER JOIN product ON ov_chipkaart_product.product_nummer = product.product_nummer " +
                    "WHERE kaart_nummer = ?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, ovChipkaart.getKaartNummer() );
            ResultSet rs = pst.executeQuery();

            while (rs.next() ) {
                Product product = this.__retrieveResultset(rs);
                productArrayList.add(product);
            }
            rs.close();
            pst.close();
        } catch(Exception err) {
            System.err.println("ProductDAOPsql geeft een error in findByOVChipkaart(): " + err.getMessage() + " " + err.getStackTrace() );
        }

        return productArrayList;
    }

    public ArrayList<Product> findAll() {
        ArrayList<Product> productArrayList = new ArrayList<>();
        try {
            String q = "SELECT product_nummer, naam, beschrijving, prijs FROM ov_chipkaart_product";
            PreparedStatement pst = this.connection.prepareStatement(q);
            ResultSet rs = pst.executeQuery();

            while (rs.next() ) {
                Product product = this.__retrieveResultset(rs);
                productArrayList.add(product);
            }
            rs.close();
            pst.close();
        } catch(Exception err) {
            System.err.println("ProductDAOPsql geeft een error in findAll(): " + err.getMessage() + " " + err.getStackTrace() );
        }

        return productArrayList;
    }

    private Product __addrelations( Product product ) {
        ArrayList<OVChipkaart> ovChipkaartList = ovChipkaartDAO.findByProduct( product );
        product.setOvChipkaartList(ovChipkaartList);

        return product;
    }

    private Product __retrieveResultset(ResultSet rs)  {
        Product product = null;
        try {
            product = new Product(
                rs.getInt("product_nummer"),
                rs.getString("naam"),
                rs.getString("beschrijving"),
                rs.getDouble("prijs")
            );
            __addrelations(product);

        } catch (Exception err) {
            System.err.println("ProductDAOPsql geeft een error in __retrieveResultSet(): " + err.getMessage() + " " +  err.getStackTrace());
        }
        return product;
    }
}
