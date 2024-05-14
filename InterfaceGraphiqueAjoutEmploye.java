package employé;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class InterfaceGraphiqueAjoutEmploye extends JFrame {

    private static final long serialVersionUID = 1L;

    public InterfaceGraphiqueAjoutEmploye() {
        // Configuration de la fenêtre
        super("Gestion des Employés");
        setBackground(new Color(0, 64, 128));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 300);
        setLocationRelativeTo(null);

        // Chargement de l'image
        ImageIcon icon = new ImageIcon("src/resources/logo.png");


        setIconImage(icon.getImage());

        // Création des onglets
        JTabbedPane tabbedPane = new JTabbedPane();

        // Onglet Ajout d'employé
        AjoutEmployePanel ajoutEmployePanel = new AjoutEmployePanel();
        ajoutEmployePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
        ajoutEmployePanel.setBackground(new Color(255, 255, 255));
        tabbedPane.addTab("Ajout d'employé", ajoutEmployePanel);

        // Onglet Autre fonctionnalité
        AutreFonctionnalitePanel autreFonctionnalitePanel = new AutreFonctionnalitePanel();
        autreFonctionnalitePanel.setBackground(new Color(255, 255, 255));
        tabbedPane.addTab("Gestion des employés", autreFonctionnalitePanel);
        autreFonctionnalitePanel.setLayout(new BoxLayout(autreFonctionnalitePanel, BoxLayout.X_AXIS));

        // Onglet Gestion des véhicules
        gestionVehiculePanel gestionVehiculePanel = new gestionVehiculePanel();
        gestionVehiculePanel.setForeground(new Color(0, 0, 0));
        tabbedPane.addTab("Gestion des véhicules", gestionVehiculePanel);
        gestionVehiculePanel.setLayout(new BoxLayout(gestionVehiculePanel, BoxLayout.X_AXIS));

        // Ajout des onglets à la fenêtre
        getContentPane().add(tabbedPane);

        // Bouton pour ouvrir la fenêtre d'ajout de véhicule avec une image
        JButton ajouterVehiculeButton = new JButton("Ajouter un véhicule");
        ajouterVehiculeButton.setBackground(new Color(255, 255, 255));
        ajouterVehiculeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAjoutVehiculeFrame();
            }
        });
        getContentPane().add(ajouterVehiculeButton, BorderLayout.SOUTH);
    }

    private void openAjoutVehiculeFrame() {
        Connection connection = connexionDB.connect();
        vehiculeManager vehiculeManager = new vehiculeManager(connection);
        ajoutVehicule ajoutVehiculeFrame = new ajoutVehicule(vehiculeManager);
        ajoutVehiculeFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfaceGraphiqueAjoutEmploye interfaceGraphique = new InterfaceGraphiqueAjoutEmploye();
            interfaceGraphique.setVisible(true);
        });
    }
}
