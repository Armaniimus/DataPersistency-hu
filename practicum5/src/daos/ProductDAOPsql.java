package daos;

import interfaces.ProductDAO;

import domain.OVChipkaart;
import domain.Product;
import lib.GenerateException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Date;

public class ProductDAOPsql implements ProductDAO {
    private final Connection connection;
    private final OVChipkaartDAOPsql ovChipkaartDAO;

    public ProductDAOPsql ( Connection localConnection, OVChipkaartDAOPsql ovChipkaartDAO) {
        this.connection = localConnection;
        this.ovChipkaartDAO = ovChipkaartDAO;
    }

    public boolean save(Product product) {
        if ( this.__save(product) ) {
            this.ovChipkaartDAO.saveList(product.getOvChipkaartList());
            this.__saveLink(product);
        }
        return false;
    }

    private boolean __save(Product product) {
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
            this.printErr(err);
            return false;
        }
    }

    public boolean saveList(ArrayList<Product> productArrayList) {
        try {
            if ( productArrayList == null || productArrayList.isEmpty() ) {
                throw new Exception("Product Arraylist is invalid");
            }

            for (Product product : productArrayList) {
                this.__save(product);
                this.__saveLink(product);
            }

            return true;
        } catch(Exception err) {
            this.printErr(err);
            return false;
        }
    }

    private boolean __update(Product product) {
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
            this.printErr(err);
            return false;
        }
    }

    public boolean update(Product product) {
        if ( this.__update(product) ) {
            this.ovChipkaartDAO.updateList( product.getOvChipkaartList() );
            this.__updateLink(product);

            return true;
        }
        return false;
    }

    public boolean updateList(ArrayList<Product> productArrayList) {
        try {
            if ( productArrayList == null || productArrayList.isEmpty() ) {
                throw new Exception("Product Arraylist is invalid");
            }

            for (Product product : productArrayList) {
                this.__update(product);
            }
            return true;
        } catch(Exception err) {
            this.printErr(err);
            return false;
        }
    }

    public boolean delete(Product product) {
        try {
            if (this.__deleteLink(product)) {
                String q = "DELETE FROM product WHERE product_nummer=?";
                PreparedStatement pst = this.connection.prepareStatement(q);
                pst.setInt(1, product.getProduct_nummer() );

                pst.execute();
                pst.close();
                return true;
            }
            throw new Exception("productLinks could not be deleted");
        } catch(Exception err) {
            this.printErr(err);
            return false;
        }
    }

    private void __saveLink(Product product) {
        try {
            Date date = new Date();
            java.sql.Date dateNow = new java.sql.Date(date.getTime());

            ArrayList<OVChipkaart> ovChipkaartArray = product.getOvChipkaartList();
            int product_nummer = product.getProduct_nummer();

            for (int i = 0; i<ovChipkaartArray.size(); i++) {
                String q = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer, status, last_update)"
                + "VALUES (?, ?, null, ?);";

                PreparedStatement pst = this.connection.prepareStatement(q);
                pst.setInt(1, ovChipkaartArray.get(i).getKaartNummer() );
                pst.setInt(2, product_nummer );
                pst.setDate(3, dateNow);

                pst.execute();
                pst.close();
            }
        } catch(Exception err) {
            this.printErr(err);
        }
    }

    private void __updateLink(Product product) {
        try {
            this.__deleteLink(product);
            this.__saveLink(product);

        } catch(Exception err) {
            this.printErr(err);
        }
    }

    public void __updateLink(OVChipkaart ovChipkaart) {
        try {
            Date date = new Date();
            java.sql.Date dateNow = new java.sql.Date(date.getTime());

            ArrayList<Product> productArray = ovChipkaart.getProductList();

            String q = "DELETE FROM ov_chipkaart_product " +
                    "WHERE kaart_nummer = ?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, ovChipkaart.getKaartNummer() );
            ResultSet rs = pst.executeQuery();
            rs.close();
            pst.close();


            // insert()
            for (int i = 0; i<productArray.size(); i++) {
                String q2 = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer, status, last_update)"
                        + "VALUES (?, ?, null, ?);";

                PreparedStatement pst2 = this.connection.prepareStatement(q2);
                pst2.setInt(1, ovChipkaart.getKaartNummer() );
                pst2.setInt(2, productArray.get(i).getProduct_nummer() );
                pst2.setDate(3, dateNow);

                pst2.execute();
                pst2.close();
            }

        } catch(Exception err) {
            this.printErr(err);
        }
    }

    private boolean __deleteLink(Product product) {
        try {
            String q = "DELETE FROM ov_chipkaart_product WHERE product_nummer=?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, product.getProduct_nummer() );

            pst.execute();
            pst.close();
            return true;
        } catch(Exception err) {
            this.printErr(err);
            return false;
        }
    }

    public boolean deleteList(ArrayList<Product> productArrayList) {
        try {
            if ( productArrayList == null || productArrayList.isEmpty() ) {
                throw new Exception("Product Arraylist is invalid");
            }

            for (Product product : productArrayList) {
                this.delete(product);
            }
            return true;
        } catch(Exception err) {
            this.printErr(err);
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
                Product product = this.__retrieveResultSet(rs, ovChipkaart);
                productArrayList.add(product);
            }
            rs.close();
            pst.close();
        } catch(Exception err) {
            this.printErr(err);
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
                product = this.__retrieveResultSet(rs, null);
            }
            rs.close();
            pst.close();

        } catch(Exception err) {
            this.printErr(err);
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
                Product product = this.__retrieveResultSet(rs, null);
                productArrayList.add(product);
            }
            rs.close();
            pst.close();
        } catch(Exception err) {
            this.printErr(err);
        }

        return productArrayList;
    }

    private void __addRelations(Product product ) {
        ArrayList<OVChipkaart> ovChipkaartList = ovChipkaartDAO.findByProduct( product );
        product.setOvChipkaartList(ovChipkaartList, false);
    }

    private Product __retrieveResultSet(ResultSet rs, OVChipkaart ovChipkaart)  {
        Product product = null;
        try {
            product = new Product(
                rs.getInt("product_nummer"),
                rs.getString("naam"),
                rs.getString("beschrijving"),
                rs.getDouble("prijs")
            );

            if (ovChipkaart == null) {
                __addRelations(product);
            } else {
                ArrayList<OVChipkaart> ovChipkaartList = new ArrayList();
                ovChipkaartList.add(ovChipkaart);
                product.setOvChipkaartList(ovChipkaartList, false);
            }

        } catch (Exception err) {
            this.printErr(err);
        }
        return product;
    }

    private void printErr(Exception err) {
        String className = "" + this.getClass();
        GenerateException.printError(className, err);
    }
}
