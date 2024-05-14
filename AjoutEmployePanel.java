package employé;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class AjoutEmployePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField nomField, prenomField, serviceField;

    public AjoutEmployePanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        nomField = new JTextField(20);
        prenomField = new JTextField(20);
        serviceField = new JTextField(20);
        JButton addButton = new JButton("Ajouter l'employé");
        addButton.setBackground(new Color(255, 255, 255));

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nom : "), gbc);
        gbc.gridx = 1;
        add(nomField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Prénom : "), gbc);
        gbc.gridx = 1;
        add(prenomField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Service : "), gbc);
        gbc.gridx = 1;
        add(serviceField, gbc);
        


        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        add(addButton, gbc);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               ajouterEmploye();
            }
        });
    }
    

    private void ajouterEmploye() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String service = serviceField.getText();

        // Validation des champs
        if (nom.isEmpty() || prenom.isEmpty() || service.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Champs manquants", JOptionPane.WARNING_MESSAGE);
            return;
        }

        employe nouvelEmploye = new employe(nom, prenom, service);

        Connection connection = connexionDB.connect();
        if (connection != null) {
            employeManager employeManager = new employeManager(connection);
            
            try {
                employeManager.addEmploye(nouvelEmploye);
                JOptionPane.showMessageDialog(this, "Employé ajouté avec succès !");
                
                // Effacer les champs après l'ajout
                nomField.setText("");
                prenomField.setText("");
                serviceField.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'employé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } finally {
                connexionDB.close(connection);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Échec de la connexion à la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
