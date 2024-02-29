package employé;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class  InterfaceGraphiqueAjoutEmploye extends JFrame {

    private static final long serialVersionUID = 1L;

    public InterfaceGraphiqueAjoutEmploye() {
        // Configuration de la fenêtre
        super("Gestion des Employés");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,350 );
        setLocationRelativeTo(null);

        // Création des onglets
        JTabbedPane tabbedPane = new JTabbedPane();

        // Onglet Ajout d'employé
        tabbedPane.addTab("Ajout d'employé", new AjoutEmployePanel());

        // Onglet Autre fonctionnalité
        AutreFonctionnalitePanel autreFonctionnalitePanel = new AutreFonctionnalitePanel();
        tabbedPane.addTab("Autre fonctionnalité", autreFonctionnalitePanel);


        // Ajout des onglets à la fenêtre
        add(tabbedPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfaceGraphiqueAjoutEmploye interfaceGraphique = new InterfaceGraphiqueAjoutEmploye();
            interfaceGraphique.setVisible(true);
        });
    }
}
