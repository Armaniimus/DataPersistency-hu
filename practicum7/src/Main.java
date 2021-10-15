import daos.AdresDAO;
import daos.OVChipkaartDAO;
import daos.ProductDAO;
import daos.ReizigerDAO;
import domain.Adres;

import domain.OVChipkaart;
import domain.Product;
import domain.Reiziger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import utility.TestDataManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Testklasse - deze klasse test alle andere klassen in deze package.
 *
 * System.out.println() is alleen in deze klasse toegestaan (behalve voor exceptions).
 *
 * @author tijmen.muller@hu.nl
 */
public class Main {
    // Creëer een factory voor Hibernate sessions.
    private static final SessionFactory factory;

    private static AdresDAO adresDAO= new AdresDAO();;
    private static ReizigerDAO reizigerDAO = new ReizigerDAO();
    private static OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAO();
    private static ProductDAO productDAO = new ProductDAO();
    private static TestDataManager testDataManager;

    static {
        try {
            // Create a Hibernate session factory
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Retouneer een Hibernate session.
     *
     * @return Hibernate session
     * @throws HibernateException
     */
    private static Session getSession() throws HibernateException {
        return factory.openSession();
    }

    public static void main(String[] args) throws SQLException {
        Session session = getSession();

        adresDAO.setSession( session );
        reizigerDAO.setSession( session );
        ovChipkaartDAO.setSession( session );
        productDAO.setSession( session );

        testDataManager = new TestDataManager(adresDAO, reizigerDAO, ovChipkaartDAO, productDAO);

        testDAOHibernate();

        session.close();
    }


    public static void testDAOHibernate() {
        testDataManager.deleteOldTestData();

        testAdres();
        testReiziger();
        testOVChipkaart();
        testProduct();
    }

    public static void testAdres() {
        System.out.println("\n---------- Test AdresDAO -------------");

        testAdresFindAll();
        testAdresFindById();

        testAdresSave();
        testAdresUpdate();
        testAdresDelete();
    }

    public static void testReiziger() {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        testReizigerFindAll();
        testReizigerFindById();
        testReizigerFindByGbDatum();

        testReizigerSave();
        testReizigerUpdate();
        testReizigerDelete();
    }

    public static void testOVChipkaart() {
        System.out.println("\n---------- Test OVchipkaartDAO -------------");

        testOvchipkaartFindAll();
        testOvchipkaartFindById();

        testOvchipkaartSave();
        testOvchipkaartUpdate();
        testOvchipkaartDelete();
    }

    public static void testProduct() {
        System.out.println("\n---------- Test ProductDAO -------------");

        testProductFindAll();
        testProductFindById();

        testProductSave();
        testProductUpdate();
        testProductDelete();
    }

    /**
     * Start of adres test functions
     */
    private static void testAdresFindAll() {
        List<Adres> adressen = adresDAO.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();
    }

    private static void testAdresFindById() {
        System.out.println("[Test] findById id 1 wordt gezocht");

        Adres result = adresDAO.findById(1);
        System.out.println(result);
        System.out.println();
    }

    private static void testAdresSave() {
        List<Adres> adressen = adresDAO.findAll();
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");

        Adres newAdres = testDataManager.getAdres();
        adresDAO.save(newAdres);

        adressen = adresDAO.findAll();
        System.out.println(adressen.size() + " adressen\n");
    }

    private static void testAdresUpdate() {
        Adres newAdres = adresDAO.findById(1945);

        System.out.println("[Test] before update:" + newAdres);
        newAdres.setWoonplaats("Utrecht");
        newAdres.setPostcode("Qwerty");
        adresDAO.update(newAdres);

        newAdres = adresDAO.findById(1945);
        System.out.println("[Test] after update:" + newAdres);

        System.out.println();
    }

    private static void testAdresDelete() {
        List<Adres> adressen = adresDAO.findAll();
        Adres newAdres = adresDAO.findById(1945);

        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.delete() ");
        adresDAO.delete(newAdres);
        adressen = adresDAO.findAll();
        System.out.println(adressen.size() + " adressen");
        System.out.println();
    }

    /**
     * start test reiziger functions
     */

    private static void testReizigerFindAll() {
        List<Reiziger> reizigers = reizigerDAO.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();
    }

    private static void testReizigerFindById() {
        System.out.println("[Test] findByID id 1 wordt gezocht");
        Reiziger reiziger = reizigerDAO.findById(1);
        System.out.println(reiziger);
        System.out.println();
    }

    private static void testReizigerFindByGbDatum() {
        System.out.println("[Test] ReizigerDAO.findByGbDatum(\" 2002-10-22 \") geeft de volgende reizigers:");
        List<Reiziger> reizigersGb = reizigerDAO.findByGeboorteDatum("2002-10-22");

        for (Reiziger r : reizigersGb) {
            System.out.println(r);
        }
        System.out.println();
    }

    private static void testReizigerSave() {
        List<Reiziger> reizigers = reizigerDAO.findAll();
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");

        Reiziger testReiziger = testDataManager.getReiziger();
        reizigerDAO.save( testReiziger );

        reizigers = reizigerDAO.findAll();
        System.out.println(reizigers.size() + " reizigers\n");
    }

    private static void testReizigerUpdate() {
        Reiziger testReiziger = reizigerDAO.findById(1945);

        System.out.println("[Test] before update:" + testReiziger);

        testReiziger.setVoorletters("S");
        testReiziger.setTussenvoegsel("de");
        testReiziger.setAchternaam("boer");
        testReiziger.getAdres().setHuisnummer("16");
        testReiziger.getAdres().setPostcode("6787GE");
        reizigerDAO.update(testReiziger);

        testReiziger = reizigerDAO.findById(1945);
        System.out.println("[Test] after update:" + testReiziger);
        System.out.println();
    }

    private static void testReizigerDelete() {
        List<Reiziger> reizigers = reizigerDAO.findAll();
        Reiziger testReiziger = reizigerDAO.findById(1945);
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        reizigerDAO.delete(testReiziger);
        reizigers = reizigerDAO.findAll();
        System.out.println(reizigers.size() + " reizigers");
        System.out.println();
    }

    /**
     * start of ovChipkaart test functions
     */

    private static void testOvchipkaartFindAll() {
        List<OVChipkaart> OVChipkaarten = ovChipkaartDAO.findAll();
        System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende Ovchipkaarten:");
        for (OVChipkaart o : OVChipkaarten) {
            System.out.println(o);
        }
        System.out.println();
    }

    private static void testOvchipkaartFindById() {
        System.out.println("[Test] findById id 18326 wordt gezocht");

        OVChipkaart result = ovChipkaartDAO.findById(18326);
        System.out.println(result);
        System.out.println();
    }

    private static void testOvchipkaartSave() {
        List<OVChipkaart> OVChipkaarten = ovChipkaartDAO.findAll();
        System.out.print("[Test] Eerst " + OVChipkaarten.size() + " ovChipkaarten, na OvchipkaartDAO.save() ");

        OVChipkaart newOV = testDataManager.getOvChipkaart();
        ovChipkaartDAO.save(newOV);

        OVChipkaarten = ovChipkaartDAO.findAll();
        System.out.println(OVChipkaarten.size() + " ovChipkaarten\n");
    }

    private static void testOvchipkaartUpdate() {
        OVChipkaart newOv = ovChipkaartDAO.findById(1945);

        System.out.println("[Test] before update:" + newOv);
        newOv.setKlasse(4);
        newOv.setSaldo(500.00);
        ovChipkaartDAO.update(newOv);

        newOv = ovChipkaartDAO.findById(1945);
        System.out.println("[Test] after update:" + newOv);

        System.out.println();
    }

    private static void testOvchipkaartDelete() {
        List<OVChipkaart> OVChipkaarten = ovChipkaartDAO.findAll();
        System.out.println("[Test] Eerst " + OVChipkaarten.size() + " Ovchipkaarten, na OVChipkaartDAO.delete() ");

        OVChipkaart newOv = ovChipkaartDAO.findById(1945);
        ovChipkaartDAO.delete(newOv);

        OVChipkaarten = ovChipkaartDAO.findAll();
        System.out.println(OVChipkaarten.size() + " Ovchipkaarten");
        System.out.println();
    }

    /**
     * start of ovChipkaart test functions
     */

    private static void testProductFindAll() {
        List<Product> producten = productDAO.findAll();
        System.out.println("[Test] ProductDAO.findAll() geeft de volgende producten:");
        for (Product a : producten) {
            System.out.println(a);
        }
        System.out.println();
    }

    private static void testProductFindById() {
        System.out.println("[Test] findById id 1 wordt gezocht");

        Product result = productDAO.findById(1);
        System.out.println(result);
        System.out.println();
    }

    private static void testProductSave() {
        List<Product> producten = productDAO.findAll();
        System.out.print("[Test] Eerst " + producten.size() + " product(en) , na ProductDao.save() ");

        Product product = testDataManager.getProduct();
        productDAO.save(product);

        producten = productDAO.findAll();
        System.out.println(producten.size() + " producten\n");
    }

    /**
     * update product
     */
    private static void testProductUpdate() {
        Product product = productDAO.findById(1945);
        System.out.println("[Test] before update:" + product);

        product.setPrijs(0.00);
        product.setBeschrijving("Oude van dagen subsidie");
        productDAO.update(product);

        product = productDAO.findById(1945);
        System.out.println("[Test] after update: " + product);
        System.out.println();
    }

    private static void testProductDelete() {
        Product product = productDAO.findById(1945);
        System.out.println("[Test] before delete:" + product);

        productDAO.delete(product);

        product = productDAO.findById(1945);
        System.out.println("[Test] after delete: " + product);
        System.out.println();
    }
}