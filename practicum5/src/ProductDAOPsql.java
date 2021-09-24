import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ProductDAOPsql implements ProductDAO{
    private Connection connection;
    private OVChipkaartDAO ovChipkaartDAO;

    public ProductDAOPsql ( Connection localConnection, OVChipkaartDAO localOvDao) {
        this.connection = localConnection;
        this.ovChipkaartDAO = localOvDao;
    }

    public boolean save(Product product) {
        try {
            String q = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, product.getProduct_nummer() );
            pst.setString(2, product.getNaam() );
            pst.setString(3, product.getBeschrijving() );
            pst.setDouble(4, product.getPrijs() );

            pst.execute();
            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("ProductDAOPsql geeft een error in save(): " + err.getMessage() + " " + err.getStackTrace() );
            return false;
        }
    }

    public boolean saveList(ArrayList<Product> productArrayList) {
        try {
            if ( productArrayList == null || productArrayList.isEmpty() ) {
                throw new Exception("Product Arraylist is invalide");
            }

            for (int i=0; i<productArrayList.size(); i++) {
                this.save( productArrayList.get(i) );
            }
            return true;
        } catch(Exception err) {
            System.err.println("ProductDAOPsql geeft een error in saveList(): " + err.getMessage() + " " + err.getStackTrace() );
            return false;
        }
    }

    public boolean update(Product product) {
        try {
            String q = "UPDATE product SET naam=?, beschrijving=?, prijs=? WHERE product_nummer=?;";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setString(1, product.getNaam() );
            pst.setString(2, product.getBeschrijving() );
            pst.setDouble(3, product.getPrijs() );
            pst.setInt(4, product.getProduct_nummer() );

            pst.execute();
            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("ProductDAOPsql geeft een error in update(): " + err.getMessage() + " " + err.getCause() );
            return false;
        }
    }

    public boolean updateList(ArrayList<Product> productArrayList) {
        try {
            if ( productArrayList == null || productArrayList.isEmpty() ) {
                throw new Exception("Product Arraylist is invalide");
            }

            for (int i=0; i<productArrayList.size(); i++) {
                this.update( productArrayList.get(i) );
            }
            return true;
        } catch(Exception err) {
            System.err.println("ProductDAOPsql geeft een error in updateList(): " + err.getMessage() + " " + err.getStackTrace() );
            return false;
        }
    }

    public boolean delete(Product product) {
        try {
            String q = "DELETE FROM product WHERE product_nummer=?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, product.getProduct_nummer() );

            pst.execute();
            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("ProductDAOPsql geeft een error in delete(): " + err.getMessage() + " " + err.getCause() + " " + err.getClass() );
            return false;
        }
    }

    public boolean deleteList(ArrayList<Product> productArrayList) {
        try {
            if ( productArrayList == null || productArrayList.isEmpty() ) {
                throw new Exception("Product Arraylist is invalide");
            }

            for (int i=0; i<productArrayList.size(); i++) {
                this.delete( productArrayList.get(i) );
            }
            return true;
        } catch(Exception err) {
            System.err.println("ProductDAOPsql geeft een error in deleteList(): " + err.getMessage() + " " + err.getStackTrace() );
            return false;
        }
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

    public Product findById(int id) {
        Product product = null;
        try {
            String q = "SELECT product_nummer, naam, beschrijving, prijs FROM product WHERE product_nummer = ?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next() ) {
                product = this.__retrieveResultset(rs);
            }
            rs.close();
            pst.close();

        } catch(Exception err) {
            System.err.println("ProductDAOPsql geeft een error in findById(): " + err.getMessage() + " " + err.getStackTrace() );
        }
        return product;
    }

    public ArrayList<Product> findAll() {
        ArrayList<Product> productArrayList = new ArrayList<>();
        try {
            String q = "SELECT product_nummer, naam, beschrijving, prijs FROM product";
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
