package daos;

import interfaces.OVChipkaartDAO;

import domain.OVChipkaart;
import domain.Product;
import domain.Reiziger;
import lib.GenerateException;

import java.sql.*;
import java.util.ArrayList;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private final Connection connection;
    private ReizigerDAOPsql reizigerDAO;
    private ProductDAOPsql productDAO;

    public OVChipkaartDAOPsql(Connection localConn) { this.connection = localConn; }
    public void setReizigerDAO(ReizigerDAOPsql reizigerDAO) { this.reizigerDAO = reizigerDAO; }
    public void setProductDAO(ProductDAOPsql productDAO) { this.productDAO = productDAO; }

    public boolean save(OVChipkaart ovChipkaart) {
        if ( this.__save(ovChipkaart) ) {
            if ( productDAO.saveList(ovChipkaart.getProductList()) ) {
                return reizigerDAO.save(ovChipkaart.getReiziger());
            }
        }
        return false;
    }

    private boolean __save(OVChipkaart ovChipkaart) {
        try {
            String q = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, ovChipkaart.getKaartNummer() );
            pst.setDate(2,  new Date(ovChipkaart.getGeldigTot().getTime() ) );
            pst.setInt(3,ovChipkaart.getKlasse() );
            pst.setDouble(4, ovChipkaart.getSaldo() );
            pst.setInt(5, ovChipkaart.getReizigerId() );

            pst.execute();
            pst.close();
            return true;
        } catch(Exception err) {
            this.printErr(err);
            return false;
        }
    }

    public boolean saveList(ArrayList<OVChipkaart> ovChipkaartList) {
        try {
            if ( ovChipkaartList == null || ovChipkaartList.isEmpty() ) {
                throw new Exception("OvChipkaart Arraylist is invalid");
            }

            for (OVChipkaart ovChipkaart : ovChipkaartList) {
                this.__save(ovChipkaart);
            }
            return true;
        } catch(Exception err) {
            this.printErr(err);
            return false;
        }
    }

    private boolean __update(OVChipkaart ovChipkaart) {
        try {
            String q = "UPDATE ov_chipkaart SET geldig_tot=?, klasse=?, saldo=? WHERE kaart_nummer=?;";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setDate(1,  new Date(ovChipkaart.getGeldigTot().getTime() ) );
            pst.setInt(2, ovChipkaart.getKlasse() );
            pst.setDouble(3, ovChipkaart.getSaldo() );
            pst.setInt(4, ovChipkaart.getKaartNummer() );

            pst.execute();

            pst.close();
            return true;
        } catch(Exception err) {
            this.printErr(err);
            return false;
        }
    }

    public boolean update(OVChipkaart ovChipkaart) {
        if ( this.__update(ovChipkaart) ) {
            if ( productDAO.updateList(ovChipkaart.getProductList()) ) {
                return reizigerDAO.update(ovChipkaart.getReiziger());
            }
        }
        return false;
    }

    public boolean updateList(ArrayList<OVChipkaart> ovChipkaartArrayList) {
        try {
            if ( ovChipkaartArrayList == null || ovChipkaartArrayList.isEmpty() ) {
                throw new Exception("OvChipkaart Arraylist is invalid");
            }

            for (OVChipkaart ovChipkaart : ovChipkaartArrayList) {
                this.__update(ovChipkaart);
            }
            return true;
        } catch(Exception err) {
            this.printErr(err);
            return false;
        }
    }

    public boolean delete(OVChipkaart ovChipkaart) {
        try {
            if ( this.deleteLink(ovChipkaart) ) {
                String q = "DELETE FROM ov_chipkaart WHERE kaart_nummer=?;";
                PreparedStatement pst = this.connection.prepareStatement(q);
                pst.setInt(1, ovChipkaart.getKaartNummer() );
                pst.execute();

                pst.close();
                return true;
            }

            throw new Exception("OvChipkaart links could not be deleted");

        } catch(Exception err) {
            this.printErr(err);
            return false;
        }
    }

    private boolean deleteLink(OVChipkaart ovChipkaart) {
        try {
            String q = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer=?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, ovChipkaart.getKaartNummer() );

            pst.execute();
            pst.close();
            return true;
        } catch(Exception err) {
            this.printErr(err);
            return false;
        }
    }

    public boolean deleteList(ArrayList<OVChipkaart> ovChipkaartArrayList) {
        try {
            if ( ovChipkaartArrayList == null || ovChipkaartArrayList.isEmpty() ) {
                throw new Exception("OvChipkaart Arraylist is invalid");
            }

            for (OVChipkaart ovChipkaart : ovChipkaartArrayList) {
                this.delete(ovChipkaart);
            }
            return true;
        } catch(Exception err) {
            this.printErr(err);
            return false;
        }
    }

    public ArrayList<OVChipkaart> findByReiziger(Reiziger reiziger) {
        ArrayList<OVChipkaart> OVChipkaartArray = new ArrayList<>();
        try {
            String q = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, reiziger.getId() );
            ResultSet rs = pst.executeQuery();

            while (rs.next() ) {
                OVChipkaart ovChipkaart = this.__retrieveResultSet(rs, reiziger);
                OVChipkaartArray.add(ovChipkaart);
            }
            rs.close();
            pst.close();
        } catch(Exception err) {
            this.printErr(err);
        }

        return OVChipkaartArray;
    }

    public ArrayList<OVChipkaart> findByProduct(Product product) {
        ArrayList<OVChipkaart> OVChipkaartArray = new ArrayList<>();

        try {
            String q = "SELECT ov_chipkaart.kaart_nummer, geldig_tot, klasse, saldo, reiziger_id " +
            "FROM ov_chipkaart_product " +
            "INNER JOIN ov_chipkaart " +
            "ON ov_chipkaart.kaart_nummer = ov_chipkaart_product.kaart_nummer " +
            "WHERE ov_chipkaart_product.product_nummer = ?;";

            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, product.getProduct_nummer() );
            ResultSet rs = pst.executeQuery();

            while (rs.next() ) {
                OVChipkaart ovChipkaart = this.__retrieveResultSet(rs, product);
                OVChipkaartArray.add(ovChipkaart);
            }
            rs.close();
            pst.close();
        } catch(Exception err) {
            this.printErr(err);
        }

        return OVChipkaartArray;
    }

    public ArrayList<OVChipkaart> findAll() {
        ArrayList<OVChipkaart> OVChipkaartArray = new ArrayList<>();
        try {
            Statement st = this.connection.createStatement();
            ResultSet rs = st.executeQuery("select * from ov_chipkaart");

            while (rs.next()) {
                OVChipkaart ovChipkaart = this.__retrieveResultSet(rs, true);
                OVChipkaartArray.add(ovChipkaart);
            }
            rs.close();

        } catch(Exception err) {
            this.printErr(err);
        }

        return OVChipkaartArray;
    }

    public OVChipkaart findByKaartNummer(int id) {
        try {
            String q = "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?";
            PreparedStatement pst = this.connection.prepareStatement(q);
            pst.setInt(1, id );
            ResultSet rs = pst.executeQuery();

            OVChipkaart ovChipkaart = null;
            if (rs.next() ) {
                ovChipkaart = this.__retrieveResultSet(rs, true);
            }
            rs.close();
            pst.close();

            return ovChipkaart;

        } catch(Exception err) {
            this.printErr(err);
            return null;
        }
    }

    private void __addRelations(OVChipkaart ovChipkaart) {
        this.__addReizigerRelation(ovChipkaart);
        this.__addProductRelation(ovChipkaart);
    }

    private void __addReizigerRelation(OVChipkaart ovChipkaart) {
        Reiziger reiziger = reizigerDAO.findByOVChipkaart(ovChipkaart);
        ovChipkaart.setReiziger(reiziger, false);
    }

    private void __addProductRelation(OVChipkaart ovChipkaart) {
        ovChipkaart.setProductList( productDAO.findByOVChipkaart(ovChipkaart), false );
    }

    private OVChipkaart __retrieveResultSet(ResultSet rs, Product product)  {
        OVChipkaart ovChipkaart=  this.__retrieveResultSet(rs, false);
        if (product == null) {
            System.err.println("Product is null");
            this.__addReizigerRelation(ovChipkaart);
        }
        ArrayList<Product> productList = new ArrayList();
        productList.add(product);
        ovChipkaart.setProductList(productList, false);

        this.__addReizigerRelation(ovChipkaart);

        return ovChipkaart;
    }

    private OVChipkaart __retrieveResultSet(ResultSet rs, Reiziger reiziger)  {
        OVChipkaart ovChipkaart=  this.__retrieveResultSet(rs, false);
        if (reiziger == null) {
            System.err.println("reiziger is null");
            this.__addReizigerRelation(ovChipkaart);
        }
        this.__addProductRelation(ovChipkaart);

        return ovChipkaart;
    }

    private OVChipkaart __retrieveResultSet(ResultSet rs, boolean withRelations)  {
        OVChipkaart ovChipkaart = null;
        try {
            ovChipkaart = new OVChipkaart(
                    rs.getInt("kaart_nummer"),
                    rs.getDate("geldig_tot"),
                    rs.getInt("klasse"),
                    rs.getDouble("saldo"),
                    rs.getInt("reiziger_id")
            );
            if (withRelations) {
                this.__addRelations(ovChipkaart);
            }

        } catch (Exception err) {
            this.printErr(err);
        }
        return ovChipkaart;
    }

    private void printErr(Exception err) {
        String className = "" + this.getClass();
        GenerateException.printError(className, err);
    }
}