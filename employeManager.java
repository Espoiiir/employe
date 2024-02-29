package employé;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class employeManager {
    private Connection db;

    public employeManager(Connection db) {
        this.setDB(db);
    }

    public void addEmploye(employe employe) {
        try {
            // Utiliser la connexion à la base de données (db) pour exécuter la requête SQL
            String query = "INSERT INTO employe(nom, prenom, service, joursConges) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = db.prepareStatement(query)) {
                statement.setString(1, employe.getNom());
                statement.setString(2, employe.getPrenom());
                statement.setString(3, employe.getService());
                statement.setInt(4, employe.getJoursConges());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }	
    }
    public void showEmploye() {
        try {
            // Utiliser la connexion à la base de données (db) pour exécuter la requête SQL
            String query = "SELECT * FROM employe";
            try (PreparedStatement statement = db.prepareStatement(query)) {
                // Exécuter la requête et récupérer le résultat dans un ResultSet
                ResultSet resultSet = statement.executeQuery();

                // Parcourir le ResultSet et afficher les informations des employés
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    String service = resultSet.getString("service");
                    int joursConges = resultSet.getInt("joursConges");

                    System.out.println("ID: " + id + ", Nom: " + nom + ", Prénom: " + prenom + ", Service: " + service + ", Jours de congés: " + joursConges);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setDB(Connection db) {
        this.db = db;
    }

    public static void main(String[] args) {
        // Exemple d'utilisation
        Connection connection = connexionDB.connect();

        if (connection != null) {
            System.out.println("Connexion réussie à la base de données.");

            // N'oubliez pas de fermer la connexion lorsque vous avez terminé
            connexionDB.close(connection);
        } else {
            System.out.println("Échec de la connexion à la base de données.");
        }
    }

}
