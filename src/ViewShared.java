import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Vector;

public class ViewShared extends JFrame {
    private final JTable visitTable;
    private final DefaultTableModel model;

    public ViewShared(SQLConnector db, int userID) {
        setTitle("ShareFrame");
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Corrected label initialization
        JLabel usernameLabel = new JLabel("Locations shared with YOU!");
        add(usernameLabel, BorderLayout.NORTH);

        // Initialize the table and model
        model = new DefaultTableModel();
        visitTable = new JTable(model);

        // Set column names
        String[] columnNames = {"Shared From", "LocationID", "Country", "City", "Year", "Season", "Best Feature", "Comment", "Rating"};
        for (String col : columnNames) {
            model.addColumn(col);
        }


        // Initialize and add the share button


        // Fetch users and populate the table
        ResultSet rs = db.loadSharedVisits(userID);

        if (rs == null) {
            JOptionPane.showMessageDialog(this, "Error loading users.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        model.setRowCount(0); // Clear existing data
        try {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("sharingPersonUsername"),
                        rs.getInt("locationID"),
                        rs.getString("country"),
                        rs.getString("city"),
                        rs.getInt("year"),
                        rs.getString("season"),
                        rs.getString("best"),
                        rs.getString("comment"),
                        rs.getInt("rating")
                });
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        visitTable.setModel(model);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(visitTable);
        add(scrollPane, BorderLayout.CENTER);



        // Ensure the application exits when the frame is closed
        setVisible(true);
    }

}