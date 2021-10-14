import daos.AdresDAO;
import daos.OVChipkaartDAO;
import daos.ProductDAO;
import daos.ReizigerDAO;
import domain.Adres;

import domain.OVChipkaart;
import domain.Reiziger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import utility.TestDataManager;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
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
        adresDAO.setSession( getSession() );
        reizigerDAO.setSession( getSession() );
        ovChipkaartDAO.setSession( getSession() );
        productDAO.setSession( getSession() );

        testDataManager = new TestDataManager(adresDAO, reizigerDAO, ovChipkaartDAO, productDAO);

        testDAOHibernate();
    }


    public static void testDAOHibernate() {
        testDataManager.deleteOldTestData();

//        testAdres();
        testReiziger();
        testOVChipkaart();
        testProduct();
    }

    public static void testAdres() {
        System.out.println("\n---------- Test AdresDAO -------------");

//        testAdresFindAll();
//        testAdresFindById();
//
        testAdresSave();
        testAdresUpdate();
        testAdresDelete();
    }

    public static void testReiziger() {
        System.out.println("\n---------- Test ReizigerDAO -------------");

//        testReizigerFindAll();
//        testReizigerFindById();
//        testReizigerFindByGbDatum();

        testReizigerSave();
        testReizigerUpdate();
        testReizigerDelete();
    }

    public static void testOVChipkaart() {
        System.out.println("\n---------- Test OVchipkaartDAO -------------");
    }

    public static void testProduct() {
        System.out.println("\n---------- Test ProductDAO -------------");
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
        System.out.println("[Test] findById reiziger met id 1 wordt gezocht");

        Adres gevondenAdres = adresDAO.findById(1);
        System.out.println(gevondenAdres);
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
        System.out.print("[Test] after update:" + newAdres);

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
        System.out.print("[Test] after update:" + testReiziger);
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
}