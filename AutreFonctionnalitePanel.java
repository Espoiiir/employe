package employé;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutreFonctionnalitePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel tableModel;

    public AutreFonctionnalitePanel() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.add(new JLabel("Liste des employés"));

        // Ajoutez le JPanel au BorderLayout.NORTH (au-dessus) de votre panneau principal
        add(headerPanel, BorderLayout.NORTH);

        // Création du modèle de tableau avec des colonnes spécifiques
        String[] columnNames = {"ID", "Nom", "Prénom", "Service", "Jours de congés restant"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;

            // Désactiver l'édition des cellules
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Permettre l'édition uniquement pour la colonne des jours de congés
            }
        };

        // Création du tableau avec le modèle
        table = new JTable(tableModel);

        // Ajout d'un écouteur pour détecter les changements dans le modèle de la table
        setupTableModel();

        // Ajout du tableau dans un JScrollPane pour permettre le défilement si nécessaire
        JScrollPane scrollPane = new JScrollPane(table);

        // Ajout du JScrollPane dans le panneau
        add(scrollPane, BorderLayout.CENTER);

        // Création du panneau pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton deleteButton = new JButton("Supprimer");
        JButton refreshButton = new JButton("Rafraîchir");
        // Ajout d'un écouteur d'événements au bouton de rafraîchissement
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        buttonPanel.add(refreshButton);

        // Ajout d'un écouteur d'événements au bouton de suppression
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {
                    // Si une ligne est sélectionnée, appeler la fonction deleteDatabase
                    deleteDatabase(selectedRow);

                    // Ensuite, mettre à jour le modèle du tableau après la suppression
                    tableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(AutreFonctionnalitePanel.this,
                            "Veuillez sélectionner une ligne à supprimer.",
                            "Aucune ligne sélectionnée",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        buttonPanel.add(deleteButton);

        // Ajout du panneau de boutons au panneau principal
        add(buttonPanel, BorderLayout.SOUTH);

        // Chargement des données dans le tableau
        loadData();
    }

    private void setupTableModel() {
        // Ajout d'un écouteur pour détecter les changements dans le modèle de la table
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                if (column == -1) {
                    // Traitement pour un changement global dans le modèle de la table
                } else {
                    // Modification d'une cellule spécifique
                    Object newValue = tableModel.getValueAt(row, column);

                    // Convertissez la valeur en String (le type original de la cellule)
                    String stringValue = String.valueOf(newValue);

                    // Assurez-vous que la valeur est de type Float (pour demie journée) avant la conversion
                    try {
                        float newJoursConges = Float.parseFloat(stringValue);
                        updateDatabase(row, newJoursConges);
                    } catch (NumberFormatException ex) {
                        // Gérez le cas où la valeur n'est pas de type Integer
                    	JOptionPane.showMessageDialog(AutreFonctionnalitePanel.this,
                    	        "La valeur n'est pas de type float.",
                    	        "Erreur de conversion",
                    	        JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
    }

    private void refreshData() {
        // Effacer toutes les lignes du modèle du tableau
        tableModel.getDataVector().removeAllElements();

        // Charger à nouveau les données dans le tableau
        loadData();
    }

    private void loadData() {
        try {
            // Utiliser la connexion à la base de données pour exécuter la requête SQL
            Connection connection = connexionDB.connect();
            String query = "SELECT * FROM employe";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                // Exécuter la requête et récupérer le résultat dans un ResultSet
                ResultSet resultSet = statement.executeQuery();

                // Ajouter les données du ResultSet dans le modèle du tableau
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    String service = resultSet.getString("service");
                    float joursConges = resultSet.getFloat("joursConges");

                    tableModel.addRow(new Object[]{id, nom, prenom, service, joursConges});
                }
            }

            // Fermer la connexion à la base de données
            connexionDB.close(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDatabase(int row, float newJoursConges) {
        try {
            // Utiliser la connexion à la base de données (db) pour exécuter la requête SQL
            Connection connection = connexionDB.connect(); // Renommé la classe connexionDB à connexionDB
            int idEmploye = (int) tableModel.getValueAt(row, 0); // ID de l'employé

            String query = "UPDATE employe SET joursConges = ? WHERE ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setFloat(1, newJoursConges);
                statement.setInt(2, idEmploye);
                statement.executeUpdate();
            }

            // Fermer la connexion à la base de données
            connexionDB.close(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ajout de la méthode manquante pour supprimer un employé de la base de données
    private void deleteDatabase(int row) {
        try {
            // Utiliser la connexion à la base de données (db) pour exécuter la requête SQL
            Connection connection = connexionDB.connect(); // Renommé la classe connexionDB à connexionDB
            int idEmploye = (int) tableModel.getValueAt(row, 0); // ID de l'employé

            String query = "DELETE FROM employe WHERE ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idEmploye);
                statement.executeUpdate();
            }

            // Fermer la connexion à la base de données
            connexionDB.close(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
