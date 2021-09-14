import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    private static Connection connection;
    private static AdresDAOPsql adao;
    private static ReizigerDAOPsql rdao;
    private static OVChipkaartDAOPsql odao;

    public static void main(String[] args) {
        Connection localConn = getConnection();
        adao = new AdresDAOPsql(localConn);
        odao = new OVChipkaartDAOPsql(localConn);

        rdao = new ReizigerDAOPsql(localConn, adao);

        odao.setRdao(rdao);
        adao.setRdao(rdao);

//        testReizigerDAO();
//        testAdresDAO();
        testOvchipkaartDAO();

        closeConnection();
    }

    private static Connection getConnection() {
        try {
            String dbUrl = "jdbc:postgresql://localhost:5432/ovchip";
            String user = "postgres";
            String pass = "K1ll3r0p";

            connection = DriverManager.getConnection(dbUrl, user, pass);
        } catch (Exception err) {
            System.err.println("error in main.getConnection() " + err.getMessage() );
        }
        return connection;
    }

    private static void closeConnection() {
        try {
            connection.close();
        } catch (Exception err) {
            System.err.println("error in main.closeConnection() " + err.getMessage() );
        }
    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException
     */
    private static void testReizigerDAO() {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf(gbdatum), null);
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // update reiziger
        System.out.println("[Test] before update:" + sietske);
        sietske.setTussenvoegsel("de");
        sietske.setAchternaam("boer");
        rdao.update(sietske);

        System.out.print("[Test] after update:");
        reizigers = rdao.findAll();
        System.out.println(" ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Delete reiziger
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers");
        System.out.println();

        // FindById reiziger
        System.out.println("[Test] findByID id 4 wordt gezocht");
        Reiziger reizigerVier = rdao.findById(4);
        System.out.println(reizigerVier);
        System.out.println();

        // FindByGbDatum
        System.out.println("[Test] ReizigerDAO.findByGBdatum(\" 2002-10-22 \") geeft de volgende reizigers:");
        List<Reiziger> reizigersGb = rdao.findByGBdatum("2002-10-22");

        for (Reiziger r : reizigersGb) {
            System.out.println(r);
        }
        System.out.println();

    }
    private static void testAdresDAO() {
        if (rdao.findById(6) == null) {
            String gbdatum2 = "1981-03-14";
            Reiziger test = new Reiziger(6, "t", "est", " voor adres", Date.valueOf(gbdatum2), null);
            rdao.save(test);
        }

        System.out.println("\n---------- Test AdresDAO -------------");

        // Haal alle reizigers op uit de database
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        // Maak een nieuwe Adres aan en persisteer deze in de database
        Adres newAdres = new Adres(20, "5654DE", "45", "toetsenbordlaan", "utrecht", 6, null);
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");
        adao.save(newAdres);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen\n");

        // update adres
        System.out.println("[Test] before update:" + newAdres);
        newAdres.setWoonplaats("Utrecht");
        newAdres.setPostcode("Qwerty");
        adao.update(newAdres);

        System.out.print("[Test] after update:");
        adressen = adao.findAll();
        System.out.println(" AdresDAO.findAll() geeft de volgende Adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        // Delete adres
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.delete() ");
        adao.delete(newAdres);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen");
        System.out.println();

        // FindByreiziger reiziger
        System.out.println("[Test] findByReiziger reiziger met id 1 wordt gezocht");

        Adres gevondenAdres = adao.findByReiziger( rdao.findById(1) );
        System.out.println(gevondenAdres);
        System.out.println();
    }

    private static void testOvchipkaartDAO() {
        System.out.println("\n---------- Test OvchipkaartDAO -------------");

        // Haal alle reizigers op uit de database
        List<OVChipkaart> OVChipkaarten = odao.findAll();
        System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende Ovchipkaarten:");
        for (OVChipkaart o : OVChipkaarten) {
            System.out.println(o);
        }
        System.out.println();

        // Maak een nieuwe Adres aan en persisteer deze in de database
        OVChipkaart newOv = new OVChipkaart(0,Date.valueOf("2022-12-01"), 1, 25.50, 2, null );
        System.out.print("[Test] Eerst " + OVChipkaarten.size() + " ovChipkaarten, na OVchipkaartDAO.save() ");
        odao.save(newOv);
        OVChipkaarten = odao.findAll();
        System.out.println(OVChipkaarten.size() + " ovChipkaarten\n");

        // update adres
        System.out.println("[Test] before update:" + newOv);
//        newOv.setWoonplaats("Utrecht");
//        newOv.setPostcode("Qwerty");
        odao.update(newOv);

        System.out.print("[Test] after update:");
        OVChipkaarten = odao.findAll();
        System.out.println(" OVChipkaartDAO.findAll() geeft de volgende OVChipkaarten:");
        for (OVChipkaart o : OVChipkaarten) {
            System.out.println(o);
        }
        System.out.println();

        // Delete adres
        System.out.print("[Test] Eerst " + OVChipkaarten.size() + " OVchipkaarten, na OVChipkaartDAO.delete() ");
        odao.delete(newOv);
        OVChipkaarten = odao.findAll();
        System.out.println(OVChipkaarten.size() + " Ovchipkaarten");
        System.out.println();

        // FindByreiziger reiziger
        System.out.println("[Test] findByReiziger reiziger met id 1 wordt gezocht");

        List<OVChipkaart> OVChipkaarten2 = odao.findByReiziger( rdao.findById(1) );
        for (OVChipkaart o : OVChipkaarten2) {
            System.out.println(o);
        }
    }
}
