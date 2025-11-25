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
import java.sql.DriverManager;
import java.sql.Statement;

public class PR210Honor {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println("PR210 Honor funciona correctament.");
        String basePath = System.getProperty("user.dir") + "/data/";
        String filePath = basePath + "honorDataBase.db";

        try {
            File fDataBase = new File(filePath);
            if (!fDataBase.exists()) {
                initDatabase(filePath);
            }

        } catch (Exception e) {
        }

    }

    static void initDatabase(String filePath) {
        String url = "jdbc:sqlite:" + filePath;
        String sqlFaccio = "CREATE TABLE IF NOT EXISTS Faccio(\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	nom varchar (15),\n"
                + "	resum varchar (500)\n"
                + ");";

        String sqlpersonatge = "CREATE TABLE IF NOT EXITS Personatge(\n"
                + " id integer PRIMARY KEY,\n"
                + "nom varchar(15),\n"
                + " atac Real,\n"
                + " defensa Real,\n"
                + " idFaccio references faccio(id)\n"
                + ");";
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sqlFaccio);
            stmt.execute(sqlpersonatge);
            logger.info("Base de dades creada correctament en {}", filePath);
            UtilsSQLite.queryUpdate(conn, "DROP TABLE IF EXISTS Faccio;");
            UtilsSQLite.queryUpdate(conn, "DROP TABLE IF EXISTS Personatge;");
            UtilsSQLite.queryUpdate(conn, sqlpersonatge);
            UtilsSQLite.queryUpdate(conn, sqlFaccio);
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
