import daos.AdresDAOPsql;
import daos.OVChipkaartDAOPsql;
import daos.ProductDAOPsql;
import daos.ReizigerDAOPsql;

import domain.Adres;
import domain.OVChipkaart;
import domain.Product;
import domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;

public class Main {
    private static Connection connection;
    private static AdresDAOPsql adresDAO;
    private static ReizigerDAOPsql reizigerDAO;
    private static OVChipkaartDAOPsql ovChipkaartDAO;
    private static ProductDAOPsql productDAO;

    public static void main(String[] args) {
        Connection localConn = getConnection();
        adresDAO = new AdresDAOPsql(localConn);
        ovChipkaartDAO = new OVChipkaartDAOPsql(localConn);
        reizigerDAO = new ReizigerDAOPsql(localConn, adresDAO, ovChipkaartDAO);
        productDAO = new ProductDAOPsql(localConn, ovChipkaartDAO);

        ovChipkaartDAO.setReizigerDAO(reizigerDAO);
        ovChipkaartDAO.setProductDAO(productDAO);
        adresDAO.setReizigerDAO(reizigerDAO);

        testReizigerDAO();
        testAdresDAO();
        testOvchipkaartDAO();
        testProductDAO();

        closeConnection();
    }

    private static Connection getConnection() {
        try {
            String server = "localhost:5432";
            String database = "ovchip";
            String user = "postgres";
            String pass = "123";

            String dbUrl = "jdbc:postgresql://" + server + "/" + database;

            if (connection == null) {
                connection = DriverManager.getConnection(dbUrl, user, pass);
            }
        } catch (Exception err) {
            System.err.println("error in main.getConnection() " + err.getMessage() );
        }
        return connection;
    }

    private static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception err) {
            System.err.println("error in main.closeConnection() " + err.getMessage() );
        }
    }

    private static void testReizigerDAO() {
        System.out.println("\n---------- Test ReizigerDAO -------------");
        testReizigerFindAll();
        testReizigerSave();
        testReizigerUpdate();
        testReizigerDelete();
        testReizigerFindById();
        testReizigerFindByGbDatum();
    }

    private static void testReizigerFindAll() {
        ArrayList<Reiziger> reizigers = reizigerDAO.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();
    }

    private static void testReizigerSave() {
        ArrayList<Reiziger> reizigers = reizigerDAO.findAll();
        ArrayList<OVChipkaart> ovkaartArrayList = new ArrayList<>();

        String geboorteDatum = "1981-03-14";
        String geldigTot = "2088-03-14";

        Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf(geboorteDatum));
        Adres sietskeAdres = new Adres(42,"Sietskedreef", "45", "Utrecht", "6060DE", 77 );
        OVChipkaart sietskeOv = new OVChipkaart(6666, Date.valueOf(geldigTot), 1, 19.50,77);

        ovkaartArrayList.add(sietskeOv);
        sietske.setAdres(sietskeAdres, false);
        sietske.setOvChipkaartList( ovkaartArrayList, false );

        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        reizigerDAO.save(sietske);
        reizigers = reizigerDAO.findAll();
        System.out.println(reizigers.size() + " reizigers\n");
    }

    private static void testReizigerUpdate() {
        Reiziger sietske = reizigerDAO.findById(77);

        System.out.println("[Test] before update:" + sietske);
        sietske.setTussenvoegsel("de");
        sietske.setAchternaam("boer");
        sietske.getAdres().setHuisnummer("16");
        sietske.getAdres().setPostcode("6787GE");
        reizigerDAO.update(sietske);

        sietske = reizigerDAO.findById(77);
        System.out.print("[Test] after update:" + sietske);
        System.out.println();
    }

    private static void testReizigerDelete() {
        ArrayList<Reiziger> reizigers = reizigerDAO.findAll();
        Reiziger sietske = reizigerDAO.findById(77);
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        reizigerDAO.delete(sietske);
        reizigers = reizigerDAO.findAll();
        System.out.println(reizigers.size() + " reizigers");
        System.out.println();
    }

    private static void testReizigerFindById() {
        System.out.println("[Test] findByID id 4 wordt gezocht");
        Reiziger reizigerVier = reizigerDAO.findById(4);
        System.out.println(reizigerVier);
        System.out.println();
    }

    private static void testReizigerFindByGbDatum() {
        System.out.println("[Test] ReizigerDAO.findByGbDatum(\" 2002-10-22 \") geeft de volgende reizigers:");
        ArrayList<Reiziger> reizigersGb = reizigerDAO.findByGeboorteDatum("2002-10-22");

        for (Reiziger r : reizigersGb) {
            System.out.println(r);
        }
        System.out.println();
    }

    private static void testAdresDAO() {
        if (reizigerDAO.findById(6) == null) {
            String geboorteDatum2 = "1981-03-14";
            Reiziger test = new Reiziger(6, "t", "est", " voor adres", Date.valueOf(geboorteDatum2));
            reizigerDAO.save(test);
        }

        System.out.println("\n---------- Test AdresDAO -------------");
        testAdresSave();
        testAdresUpdate();
        testAdresDelete();
        testAdresFindAll();
        testAdresFindByReiziger();
    }

    private static void testAdresSave() {
        ArrayList<Adres> adressen = adresDAO.findAll();
        Adres newAdres = new Adres(20, "Toetsenbordlaan", "45", "Utrecht", "5654DE", 6);
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");
        adresDAO.save(newAdres);

        adressen = adresDAO.findAll();
        System.out.println(adressen.size() + " adressen\n");
    }

    private static void testAdresUpdate() {
        Adres newAdres = adresDAO.findById(20);

        System.out.println("[Test] before update:" + newAdres);
        newAdres.setWoonplaats("Utrecht");
        newAdres.setPostcode("Qwerty");
        adresDAO.update(newAdres);

        newAdres = adresDAO.findById(20);
        System.out.print("[Test] after update:" + newAdres);

        System.out.println();
    }

    private static void testAdresDelete() {
        ArrayList<Adres> adressen = adresDAO.findAll();
        Adres newAdres = adresDAO.findById(20);

        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.delete() ");
        adresDAO.delete(newAdres);
        adressen = adresDAO.findAll();
        System.out.println(adressen.size() + " adressen");
        System.out.println();
    }

    private static void testAdresFindAll() {
        ArrayList<Adres> adressen = adresDAO.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();
    }

    private static void testAdresFindByReiziger() {
        System.out.println("[Test] findByReiziger reiziger met id 1 wordt gezocht");

        Adres gevondenAdres = adresDAO.findByReiziger( reizigerDAO.findById(1) );
        System.out.println(gevondenAdres);
        System.out.println();
    }

    private static void testOvchipkaartDAO() {
        System.out.println("\n---------- Test OvchipkaartDAO -------------");
//        testOvchipkaartFindAll();
//        testOvchipkaartSave();
//        testOvchipkaartUpdate();
//        testOvchipkaartDelete();
//        testOvchipkaartFindByReiziger();
    }

    private static void testOvchipkaartFindAll() {
        ArrayList<OVChipkaart> OVChipkaarten = ovChipkaartDAO.findAll();
        System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende Ovchipkaarten:");
        for (OVChipkaart o : OVChipkaarten) {
            System.out.println(o);
        }
        System.out.println();
    }

    private static void testOvchipkaartSave() {
        Product oldProduct = productDAO.findById(1945);
        if (oldProduct != null) {
            productDAO.delete(oldProduct);
        }

        ArrayList<Product> productArrayList = new ArrayList<>();
        Product ovProduct = new Product(1945, "", "", 14.50);
        productArrayList.add(ovProduct);

        OVChipkaart newOv = new OVChipkaart(1945,Date.valueOf("2022-12-01"), 1, 25.50, 1945 );
        Reiziger reiziger = new Reiziger(1945, "","","", Date.valueOf("2022-12-01") );
        Adres adres = new Adres(1945, "","", "","", 1945);

        newOv.setProductList(productArrayList, false);
        newOv.setReiziger(reiziger, false);
        reiziger.setAdres(adres, false);

        ArrayList<OVChipkaart> OVChipkaarten = ovChipkaartDAO.findAll();
        System.out.print("[Test] Eerst " + OVChipkaarten.size() + " ovChipkaarten, na OvchipkaartDAO.save() ");
        ovChipkaartDAO.save(newOv);
        OVChipkaarten = ovChipkaartDAO.findAll();
        System.out.println(OVChipkaarten.size() + " ovChipkaarten\n");
    }

    private static void testOvchipkaartUpdate() {
        OVChipkaart newOv = ovChipkaartDAO.findByKaartNummer(1945);

        System.out.println("[Test] before update:" + newOv);
        newOv.setKlasse(4);
        newOv.setSaldo(500.00);
        ovChipkaartDAO.update(newOv);

        System.out.print("[Test] after update:");
        ArrayList<OVChipkaart> OVChipkaarten = ovChipkaartDAO.findAll();
        System.out.println(" OVChipkaartDAO.findAll() geeft de volgende OVChipkaarten:");
        for (OVChipkaart o : OVChipkaarten) {
            System.out.println(o);
        }
        System.out.println();
    }

    private static void testOvchipkaartDelete() {
        ArrayList<OVChipkaart> OVChipkaarten = ovChipkaartDAO.findAll();
        System.out.print("[Test] Eerst " + OVChipkaarten.size() + " Ovchipkaarten, na OVChipkaartDAO.delete() ");
        OVChipkaart newOv = ovChipkaartDAO.findByKaartNummer(1945);
        ovChipkaartDAO.delete(newOv);
        OVChipkaarten = ovChipkaartDAO.findAll();
        System.out.println(OVChipkaarten.size() + " Ovchipkaarten");
        System.out.println();
    }

    private static void testOvchipkaartFindByReiziger() {
        System.out.println("[Test] findByReiziger reiziger met id 1 wordt gezocht");

        ArrayList<OVChipkaart> OVChipkaarten2 = ovChipkaartDAO.findByReiziger( reizigerDAO.findById(1) );
        for (OVChipkaart o : OVChipkaarten2) {
            System.out.println(o);
        }

        System.out.println("[Test] findByReiziger reiziger met id 2 wordt gezocht");

        OVChipkaarten2 = ovChipkaartDAO.findByReiziger( reizigerDAO.findById(2) );
        for (OVChipkaart o : OVChipkaarten2) {
            System.out.println(o);
        }
    }

    private static void testProductDAO() {
        System.out.println("\n---------- Test ProductDAO -------------");
        testProductFindAll();
        testProductSave();
        testProductUpdate();
        testProductDelete();
    }

    /**
     * find all product
     */
    private static void testProductFindAll() {
        ArrayList<Product> producten = productDAO.findAll();
        System.out.println("[Test] ProductDAO.findAll() geeft de volgende producten:");
        for (Product a : producten) {
            System.out.println(a);
        }
        System.out.println();
    }

    /**
     * save product
     */
    private static void testProductSave() {
        OVChipkaart oldCard = ovChipkaartDAO.findByKaartNummer(1488);
        if (oldCard != null) {
            ovChipkaartDAO.delete(oldCard);
        }

        OVChipkaart newOv = new OVChipkaart(1488,Date.valueOf("2022-12-01"), 1, 25.50, 2 );
        ArrayList<OVChipkaart> ovChipkaartArray = new ArrayList();
        ovChipkaartArray.add(newOv);

        Reiziger reiziger = new Reiziger(1488, "","","", Date.valueOf("2022-12-01") );
        newOv.setReiziger(reiziger, false);

        Adres adres = new Adres(1488, "","", "","", 1488);
        reiziger.setAdres(adres, false);

        ArrayList<Product> producten = productDAO.findAll();
        Product oldPeopleProduct = new Product(7, "Oude van dagen pakket", "gratis reizen voor iedereen die 80+ is", 0.50);

        oldPeopleProduct.setOvChipkaartList(ovChipkaartArray, false);
        System.out.print("[Test] Eerst " + producten.size() + " producten, na ProductDao.save() ");
        productDAO.save(oldPeopleProduct);
        producten = productDAO.findAll();
        System.out.println(producten.size() + " producten\n");
    }

    /**
     * update product
     */
    private static void testProductUpdate() {
        Product oldPeopleProduct2 = productDAO.findById(7);
//        oldPeopleProduct2.setOvChipkaartList(ovChipkaartArray, false);
        System.out.println("[Test] before update:" + oldPeopleProduct2);

        oldPeopleProduct2.setPrijs(0.00);
        oldPeopleProduct2.setBeschrijving("Oude van dagen subsidie");
        productDAO.update(oldPeopleProduct2);
        oldPeopleProduct2 = productDAO.findById(7);
        System.out.println("[Test] after update: " + oldPeopleProduct2);
        System.out.println();
    }

    /**
     * delete product
     */
    private static void testProductDelete() {
        Product oldPeopleProduct3 = productDAO.findById(7);
        System.out.println("[Test] before delete:" + oldPeopleProduct3);
        productDAO.delete(oldPeopleProduct3);
        oldPeopleProduct3 = productDAO.findById(7);
        System.out.println("[Test] after delete: " + oldPeopleProduct3);
        System.out.println();
    }
}
