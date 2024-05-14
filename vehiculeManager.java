package employé;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class vehiculeManager {
    private Connection db;

    public vehiculeManager(Connection db) {
        this.setDB(db);
    }

    public void addVehicule(vehicule vehicule) {
        try {
            // Utiliser la connexion à la base de données (db) pour exécuter la requête SQL
            String query = "INSERT INTO vehicules(Type, DateCT, Etat) VALUES (?, ?, ?)";
            try (PreparedStatement statement = db.prepareStatement(query)) {
                statement.setString(1, vehicule.getType());
                statement.setDate(2, (Date) vehicule.getDate());
                statement.setBoolean(3, vehicule.getEtat());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }	
    }
    public void showVehicule() {
        try {
            // Utiliser la connexion à la base de données (db) pour exécuter la requête SQL
            String query = "SELECT * FROM vehicules";
            try (PreparedStatement statement = db.prepareStatement(query)) {
                // Exécuter la requête et récupérer le résultat dans un ResultSet
                ResultSet resultSet = statement.executeQuery();

                // Parcourir le ResultSet et afficher les informations des employés
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String type = resultSet.getString("Type");
                    Date dateCT = resultSet.getDate("dateCT");
                    boolean Etat = resultSet.getBoolean("Etat");
                    String EtatText = Etat ? "En état" : "Pas en état";
                    

                    System.out.println("ID: " + id + ", Type: " + type + ", Date Contrôle Technique: " + dateCT + ", Etat: " + EtatText);
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
