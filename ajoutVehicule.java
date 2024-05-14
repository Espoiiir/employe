package employé;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

public class ajoutVehicule extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField typeField;
    private JTextField dateField;
    private JCheckBox etatCheckBox;

    private vehiculeManager vehiculeManager;

    public ajoutVehicule(vehiculeManager vehiculeManager) {
        this.vehiculeManager = vehiculeManager;

        setTitle("Ajout d'un nouveau véhicule");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Ajout de marges

        JLabel typeLabel = new JLabel("Type:");
        typeField = new JTextField();
        panel.add(typeLabel);
        panel.add(typeField);
        typeLabel.setToolTipText("Type du véhicule : Camion, voiture, etc..");

        JLabel dateLabel = new JLabel("Date Contrôle Technique:");
        dateField = new JTextField();
        panel.add(dateLabel);
        panel.add(dateField);
        dateLabel.setToolTipText("Format attendu : Année-Mois-Jour");

        JLabel etatLabel = new JLabel("État:");
        etatCheckBox = new JCheckBox();
        panel.add(etatLabel);
        panel.add(etatCheckBox);
        etatLabel.setToolTipText("Si la voiture est en état, cocher la case, sinon la laisser vide.");

        JButton addButton = new JButton("Ajouter");
        addButton.setBackground(new Color(128, 128, 255));
        addButton.setForeground(Color.BLACK); // Texte en noir
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterVehicule();
            }
        });
        panel.add(addButton);

        getContentPane().add(panel);
    }

    private void ajouterVehicule() {
        String type = typeField.getText();
        Date dateCT = Date.valueOf(dateField.getText());
        boolean etat = etatCheckBox.isSelected();

        vehicule vehicule = new vehicule(type, dateCT, etat);
        vehiculeManager.addVehicule(vehicule);

        JOptionPane.showMessageDialog(this, "Véhicule ajouté avec succès!");
        clearFields();
    }

    private void clearFields() {
        typeField.setText("");
        dateField.setText("");
        etatCheckBox.setSelected(false);
    }

}
