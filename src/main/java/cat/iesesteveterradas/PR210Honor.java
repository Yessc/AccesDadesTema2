package cat.iesesteveterradas;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class PR210Honor {

   public static void main(String[] args) {
        System.out.println("PR210 Honor funciona correctament.");
        String basePath = System.getProperty("user.dir") + "/data/";
        String filePath = basePath + "honorDataBase.db";

        try {
            File fDataBase = new File(filePath);
            if(!fDataBase.exists()) {
                initDatabase(filePath);
            }

        } catch (Exception e) {
        }



   }

    static void initDatabase(String filePath) {
        String url = "jdbc:sqlite:" + filePath;
        String sqlFaccio = "CREATE TABLE IF NOT EXISTS faccio(\n"
                + "	id integer PRIMARY KEY,\n"
                + "	nom varchar (15),\n"
                + "	resum varchar (500)\n"
                + ");";

       String sqlpersonatge="CREATE TABLE IF NOT EXITS personatjes(\n"
                + " id integer PRIMARY KEY,\n"
                + "nom varchar(15),\n"
                + " atac Real,\n"
                + " defensa Real,\n"
                + " idFaccio references faccio(id)\n"
                + ");"; 
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()){
            stmt.execute(sqlFaccio);
            stmt.execute(sqlpersonatge);
            System.out.println("Taula creada");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
