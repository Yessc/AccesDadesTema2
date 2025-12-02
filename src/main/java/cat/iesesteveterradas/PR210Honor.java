package cat.iesesteveterradas;

import cat.iesesteveterradas.utils.UtilsSQLite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.sql.DriverManager;
import java.sql.Statement;

public class PR210Honor {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private int option;
    private static final java.util.Scanner scnr = new java.util.Scanner(System.in);
    private static final UtilsSQLite utils = new UtilsSQLite();
    public static void main(String[] args) {
        
        String basePath = System.getProperty("user.dir") + "/data/";
        String filePath = basePath + "honorDataBase.db";

        
            
        try {
            File fDataBase = new File(filePath);
            if (!fDataBase.exists()) {
                System.out.println("no existe creamos una");
                initDatabase(filePath);
            }
            
            Connection conn = UtilsSQLite.connect(filePath);
            while (true) {
                System.out.println("\n1. Mostrar taula Personatge");
                System.out.println("2. Mostrar personatges per facció");
                System.out.println("3. Mostrar millor atacant");
                System.out.println("4. Mostrar millor defensor");
                System.out.println("5. Sortir");
                System.out.println("option:\n");
                String url = "jdbc:sqlite:" + filePath;


                Scanner scanner = new Scanner(System.in);
                int option = scanner.nextInt();

                switch (option) {
                    case 1:
                        utils.showTableData(conn);
                        break;
                    case 2:
                        utils.showPersonatgeByFaccio(conn);
                        break;
                    case 3:
                        utils.showBestAtacant(conn);
                        break;
                    case 4:
                        utils.showBestDefense(conn);
                        break;
                    case 5:
                        UtilsSQLite.disconnect(conn);
                        System.out.println("Sortint de l'aplicació.");
                        return;
                    default:
                        throw new AssertionError();
                }
                
            }
            
    

        } catch (Exception e) {
            logger.error("Error en l'aplicació: {}", e.getMessage());
        }

    }

    public static void initDatabase(String filePath) {
        String url = "jdbc:sqlite:" + filePath;
        String sqlFaccio = "CREATE TABLE IF NOT EXISTS Faccio(\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	nom varchar (15),\n"
                + "	resum varchar (500)\n"
                + ");";

        String sqlpersonatge = "CREATE TABLE IF NOT EXISTS Personatge(\n"
                + " id integer PRIMARY KEY,\n"
                + "nom varchar(15),\n"
                + " atac REAL,\n"
                + " defensa REAL,\n"
                + " idFaccio references faccio(id)\n"
                + ");";
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
                    //UtilsSQLite.queryUpdate(conn, "DROP TABLE IF EXISTS Faccio;");
            //UtilsSQLite.queryUpdate(conn, "DROP TABLE IF EXISTS Personatge;");
            
            logger.info("Base de dades creada correctament en {}", filePath);
           
            //UtilsSQLite.queryUpdate(conn, sqlpersonatge);
            //UtilsSQLite.queryUpdate(conn, sqlFaccio);


            stmt.execute(sqlFaccio);
            stmt.execute(sqlpersonatge);
            // Insertar Facciones
            UtilsSQLite.queryUpdate(conn,"INSERT INTO Faccio (nom, resum) VALUES (\"Cavallers\", \"Though seen as a single group, the Knights are hardly unified. There are many Legions in Ashfeld, the most prominent being The Iron Legion.\");");
            UtilsSQLite.queryUpdate(conn, "INSERT INTO Faccio (nom, resum) VALUES (\"Vikings\",   \"The Vikings are a loose coalition of hundreds of clans and tribes, the most powerful being The Warborn.\");");
            UtilsSQLite.queryUpdate(conn,"INSERT INTO Faccio (nom, resum) VALUES (\"Samurais\",  \"The Samurai are the most unified of the three factions, though this does not say much as the Daimyos were often battling each other for dominance.\");");
            // Insertar Personajes 
            UtilsSQLite.queryUpdate(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Warden\",      1, 3, 1);");
            UtilsSQLite.queryUpdate(conn,"INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Conqueror\",   2, 2, 1);");
            UtilsSQLite.queryUpdate(conn,  "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Peacekeep\",   2, 3, 1);");

            UtilsSQLite.queryUpdate(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Raider\",    3, 3, 2);");
            UtilsSQLite.queryUpdate(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Warlord\",   2, 2, 2);");
            UtilsSQLite.queryUpdate(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Berserker\", 1, 1, 2);");

            UtilsSQLite.queryUpdate(conn,"INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Kensei\",  3, 2, 3);");
            UtilsSQLite.queryUpdate(conn,"INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Shugoki\", 2, 1, 3);");
            UtilsSQLite.queryUpdate(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Orochi\",  3, 2, 3);");
            

            logger.info("Dades inicials inserides correctament.");
        } catch (SQLException e) {
            logger.error("Error en inicialitzar la base de dades: {}", e.getMessage());
        }
    }

}
