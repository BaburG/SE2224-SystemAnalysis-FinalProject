import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Vector;

public class ShareFrame extends JFrame {
    private final JTable userTable;
    private final DefaultTableModel model;
    private final JButton shareButton;

    public ShareFrame(SQLConnector db, int userID, int locationID) {
        setTitle("ShareFrame");
        setSize(400, 300);
        setLayout(new BorderLayout());

        // Corrected label initialization
        JLabel usernameLabel = new JLabel("Who do you want to share with?");
        add(usernameLabel, BorderLayout.NORTH);

        // Initialize the table and model
        model = new DefaultTableModel();
        userTable = new JTable(model);

        // Set column names
        String[] columnNames = {"UserID", "Username"};
        for (String col : columnNames) {
            model.addColumn(col);
        }


        // Initialize and add the share button
        shareButton = new JButton("Share!");
        add(shareButton, BorderLayout.SOUTH);

        shareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<Vector> data = model.getDataVector();
                if (userTable.getSelectedRow() != -1){
                    int sharingTO = (Integer) data.get(userTable.getSelectedRow()).get(0);
                    db.addSharedVisit(userID, locationID, sharingTO);
                    dispose();
                }
            }
        });

        // Fetch users and populate the table
        ResultSet rs = db.fetchUsers(userID);

        if (rs == null) {
            JOptionPane.showMessageDialog(this, "Error loading users.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        model.setRowCount(0); // Clear existing data
        try {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("userid"),
                        rs.getString("username")
                });
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);



        // Ensure the application exits when the frame is closed
        setVisible(true);
    }

}