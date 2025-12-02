package cat.iesesteveterradas.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UtilsSQLite {
    
    private static final Logger logger = LoggerFactory.getLogger(UtilsSQLite.class);

    public static Connection connect(String filePath) throws SQLException {
        String url = "jdbc:sqlite:" + filePath;
        Connection conn = DriverManager.getConnection(url);

        logger.info("BBDD SQLite connectada a {}", filePath);
        DatabaseMetaData meta = conn.getMetaData();
        logger.info("BBDD driver: {}", meta.getDriverName());

        return conn;
    }
    
    public static void disconnect(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                logger.info("DDBB SQLite desconnectada");
            } catch (SQLException e) {
                // S'ha substituït System.err.println per logger.error
                logger.error("Error en tancar la connexió: {}", e.getMessage());
            }
        }
    }
    
    public static int queryUpdatePS(Connection conn, String sql, Object... params) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            int affectedRows = pstmt.executeUpdate();
            logger.debug("Executada queryUpdatePS. Files afectades: {}. SQL: {}", affectedRows, sql);
            return affectedRows;
        }
    }

    public static int queryUpdate(Connection conn, String sql) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            int affectedRows = stmt.executeUpdate(sql);
            logger.debug("Executada queryUpdate. Files afectades: {}. SQL: {}", affectedRows, sql);
            return affectedRows;
        }
    }
    //metodo donde muestro tabla con datos faccio o personaje 
    public static void showTableData(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Selecciona la taula a mostrar:");
        System.out.println("1. Faccio");
        System.out.println("2. Personatge");
        System.out.print("Opció: ");
        int opcio = scanner.nextInt();
        String query = (opcio == 1) ? "SELECT * FROM Faccio;" : "SELECT * FROM Personatge;";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                
                if (opcio == 1) {
                       
                    System.out.printf("%-7s %-20s %-30s%n", "Id:" + rs.getInt("id"), "Faccio:" + rs.getString("nom"),
                            "Resum:" + rs.getString("resum"));
                } else {
                    
                    System.out.printf("%-10s %-25s %-15s %-15s%n","Id: "+rs.getString("id") , "Personatge: "+ rs.getString("nom") , "Atac: "+rs.getString("atac"), "Defense: "+rs.getString("defensa"));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al mostrar los datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void showPersonatgeByFaccio(Connection conn) throws SQLException {
        String query = "Select f.nom as faccio , p.nom as personatge,p.atac  as atac, p.defensa as defense from Personatge p join Faccio f on f.id = p.idFaccio order by faccio;";

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
                    System.out.printf("%-15s %-10s %-10s %-15s%n", "PERSONATGE", "ATAC", "DEFENSA", "FACCIO");

            while (rs.next()) {
                
                     
                System.out.printf("%-15s %-10d %-10d %-15s%n", rs.getString("personatge"),rs.getInt("atac"),rs.getInt("defense"), rs.getString("faccio"));
            }
        } catch (SQLException e) {
            logger.error("Error al mostrar los datos: " + e.getMessage());
            e.printStackTrace();
        }

        
    }

    public static void showBestAtacant(Connection conn){
        //String query= "Select p.nom as personatge, p.atac as atac ,f.nom as nomfaccio from Personatge p join Faccio f on  f.id = p.idFaccio  where  p.atac= (select max(atac) from Personatge);";
        String query = "SELECT t.personatge,t.atac,t.nomfaccio FROM (SELECT p.nom AS personatge,p.atac AS atac, f.nom AS nomfaccio,  ROW_NUMBER() OVER (PARTITION BY p.idFaccio ORDER BY p.atac DESC) AS rn FROM Personatge p JOIN Faccio f ON f.id = p.idFaccio) AS t WHERE t.rn = 1;";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
                System.out.println(String.format("%-15s %-10s %-10s", "PERSONATGE", "ATAC", "FACCIO"));
            while (rs.next()) {
                System.out.println(String.format("%-15s %-10d %-10s",rs.getString("personatge"),rs.getInt("atac"),rs.getString("nomfaccio")));
            }
        } catch (SQLException e) {
            logger.error("Error al mostrar los datos: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void showBestDefense(Connection conn){
        String query ="SELECT t.personatge,t.defensa,t.nomfaccio FROM (SELECT p.nom AS personatge,p.defensa AS defense, f.nom AS nomfaccio,  ROW_NUMBER() OVER (PARTITION BY p.idFaccio ORDER BY p.defensa DESC) AS rn FROM Personatge p JOIN Faccio f ON f.id = p.idFaccio) AS t WHERE t.rn = 1;";
        System.out.println(String.format("%-15s %-10s %-10s", "PERSONATGE", "DEFENSA ", "FACCIO"));

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {        
           while (rs.next()) {
                System.out.println(String.format("%-15s %-10d %-10s", rs.getString("personatge"), rs.getInt("defense"),rs.getString("nomfaccio")));
                
            }
        } catch (SQLException e) {
            logger.error("Error al mostrar los datos: " + e.getMessage());
            e.printStackTrace();
        }

    }
        
    
    
}
