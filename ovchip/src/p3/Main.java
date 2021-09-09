package p3;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    private static Connection connection;

    public static void main(String[] args) {
        Connection localConn = getConnection();
        ReizigerDAO rdao = new ReizigerDAOPsql(localConn);

        try {
            testReizigerDAO(rdao);
        }  catch(Exception err) {
            System.err.println("error in testReizigersDAO " + err.getMessage() );
        }

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
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
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
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.

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
        Reiziger reizigerVier = rdao.findbyid(4);
        System.out.println(reizigerVier);
        System.out.println();

        // FindByGbDatum
//        "2002-10-22";
        System.out.println("[Test] ReizigerDAO.findByGBdatum(\" 2002-10-22 \") geeft de volgende reizigers:");
        List<Reiziger> reizigersGb = rdao.findByGBdatum("2002-10-22");

        for (Reiziger r : reizigersGb) {
            System.out.println(r);
        }
        System.out.println();
    }
}
