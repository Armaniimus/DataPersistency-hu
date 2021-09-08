package p1;

import java.sql.*;
public class p1Data {
    public static void main(String[] args) {

        try {
            String dbUrl = "jdbc:postgresql://localhost:5432/ovchip";
            String user = "postgres";
            String pass = "K1ll3r0p";

            Connection myConn = DriverManager.getConnection(dbUrl, user, pass);

            Statement myStmt = myConn.createStatement();

            ResultSet myRs = myStmt.executeQuery("select * from reiziger");

            //from result set give metadata
            ResultSetMetaData rsmd = myRs.getMetaData();
            int numOfCols = rsmd.getColumnCount();


            System.out.println("Mijn reizigers uitvoer:");
            while (myRs.next()) {
                String row = "    #";
                for(int i = 1; i <= numOfCols; i++)  {
                    if (myRs.getString(i) != null) {
                        if (i > 1) {
                            row += " ";
                        }
                        row += myRs.getString(i);
                    }
                }
                System.out.println(row);
            }
            myRs.close();


        } catch (Exception exc){

        }


    }

}