package employé;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class gestionVehiculePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel tableModel;

    public gestionVehiculePanel() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.add(new JLabel("Liste des véhicules"));

        add(headerPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Type", "Date Contrôle Technique", "Etat"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Seule la colonne du contrôle technique est éditable
            }
        };

        table = new JTable(tableModel);
        table.setForeground(new Color(0, 0, 0));
        table.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
        table.setBackground(new Color(255, 255, 255));

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton deleteButton = new JButton("Supprimer");
        deleteButton.setBackground(new Color(255, 255, 255));
        JButton refreshButton = new JButton("Rafraîchir");
        refreshButton.setBackground(new Color(255, 255, 255));

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        buttonPanel.add(refreshButton);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {
                    deleteDatabase(selectedRow);
                    tableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(gestionVehiculePanel.this,
                            "Veuillez sélectionner une ligne à supprimer.",
                            "Aucune ligne sélectionnée",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        loadData();

        setupTableModel();
    }

    private void refreshData() {
        tableModel.getDataVector().removeAllElements();
        loadData();
    }

    private void loadData() {
        try {
            Connection connection = connexionDB.connect();
            String query = "SELECT * FROM vehicules";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String Type = resultSet.getString("Type");
                    Date dateCT = resultSet.getDate("dateCT");
                    boolean Etat = resultSet.getBoolean("Etat");

                    String etatText = Etat ? "En état" : "Pas en état";

                    tableModel.addRow(new Object[]{id, Type, dateCT, etatText, "Modifier"});
                }
            }

            connexionDB.close(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupTableModel() {
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (col == table.getColumnCount() - 1) {
                    // Si l'utilisateur a cliqué sur le bouton "Modifier"
                    showEtatSelectionDialog(row);
                }
            }
        });
        
        table.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    TableModel model = (TableModel)e.getSource();
                    Object data = model.getValueAt(row, column);
                    if (column == 2) { // Colonne du contrôle technique
                    	try {
                    	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    	    java.util.Date parsedDate = sdf.parse((String)data);
                    	    java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
                    	    updateDateCT(row, sqlDate);
                    	} catch (ParseException ex) {
                    	    ex.printStackTrace();
                    	}
                    }
                }
            }
        });
    }

    private void showEtatSelectionDialog(int row) {
        Object[] options = {"En état", "Pas en état"};
        int choice = JOptionPane.showOptionDialog(gestionVehiculePanel.this,
                "Sélectionnez le nouvel état du véhicule :", "Modifier l'état",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == JOptionPane.YES_OPTION) {
            updateEtat(row, true); // Mettre à jour l'état à "En état"
        } else if (choice == JOptionPane.NO_OPTION) {
            updateEtat(row, false); // Mettre à jour l'état à "Pas en état"
        }
    }

    private void updateEtat(int row, boolean newEtat) {
        try {
            Connection connection = connexionDB.connect();
            int idVehicule = (int) tableModel.getValueAt(row, 0);

            String query = "UPDATE vehicules SET Etat = ? WHERE ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setBoolean(1, newEtat);
                statement.setInt(2, idVehicule);
                statement.executeUpdate();
            }

            connexionDB.close(connection);
            refreshData(); // Rafraîchir le tableau après la mise à jour
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void updateDateCT(int row, Date newDate) {
        try {
            Connection connection = connexionDB.connect();
            int idVehicule = (int) tableModel.getValueAt(row, 0);

            String query = "UPDATE vehicules SET dateCT = ? WHERE ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setDate(1, newDate);
                statement.setInt(2, idVehicule);
                statement.executeUpdate();
            }

            connexionDB.close(connection);
            refreshData(); // Rafraîchir le tableau après la mise à jour
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void deleteDatabase(int row) {
        try {
            Connection connection = connexionDB.connect();
            int idVehicule = (int) tableModel.getValueAt(row, 0);

            String query = "DELETE FROM vehicules WHERE ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idVehicule);
                statement.executeUpdate();
            }

            connexionDB.close(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
