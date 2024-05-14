package employé;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connexionDB {

	public static Connection connect() {
        // Définissez vos paramètres de connexion
        String url = "jdbc:mysql://localhost:3306/employes";
        String utilisateur = "root";
        String motDePasse = "";

        try {
            // Chargez le pilote JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Établissez la connexion à la base de données
            return DriverManager.getConnection(url, utilisateur, motDePasse);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void close(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
