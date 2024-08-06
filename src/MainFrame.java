import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

public class MainFrame extends JFrame {
    private final JTextField searchField;
    private final JButton deleteVisit;
    private final JButton editVisit;
    private final JButton sortByFoodAndRating;
    private final JButton lastYearVisits;
    private final JButton viewImage;
    private final JButton shareVisit;
    private final JButton viewSharedWithMe;
    private final JButton addVisit;
    private final JTable visitTable;
    private final int userID;
    private final String username;
    private final SQLConnector db;
    private final DefaultTableModel model;
    private Vector<Vector> data;
    private final JLabel Stats;

    public MainFrame(int id,String username, SQLConnector con) {
        // Set up the frame
        this.username = username;
        userID = id;
        db = con;
        setTitle("MainFrame");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Username label
        JLabel usernameLabel = new JLabel(STR."Logged in as: \{username}");
        add(usernameLabel, BorderLayout.NORTH);


        model = new DefaultTableModel();
        visitTable = new JTable(model);

        String[] columnNames = {"LocationID", "Country", "City", "Year", "Season", "Best Feature", "Comment", "Rating"};
        for(String col : columnNames){
            model.addColumn(col);
        }

        loadVisits(db.loadVisits(userID));
        data = model.getDataVector();



        JScrollPane scrollPane = new JScrollPane(visitTable);
        add(scrollPane, BorderLayout.CENTER);

        // Search panel at the bottom
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));



        // Panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4));

        // Search by Diagnosis button
        deleteVisit = new JButton("Delete Visit");
        buttonPanel.add(deleteVisit);

        deleteVisit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                data = model.getDataVector();
                if (visitTable.getSelectedRow() != -1){
                    db.removeVisit((Integer) data.get(visitTable.getSelectedRow()).get(0));
                    JOptionPane.showMessageDialog(MainFrame.this, "Location deleted Successfully", "Success!", JOptionPane.INFORMATION_MESSAGE);
                    updateDisplay();
                }
            }
        });

        // Search by Age button
        editVisit = new JButton("Edit Visit");
        buttonPanel.add(editVisit);

        editVisit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = visitTable.getSelectedRow();
                if (idx < 0) return;
                Vector vis = data.get(idx);
                int ID = Integer.parseInt(vis.get(0).toString());
                String country = (String)vis.get(1);
                String city = (String)vis.get(2);
                String year = vis.get(3).toString();
                String season = (String)vis.get(4);
                String best = (String)vis.get(5);
                String comment = (String)vis.get(6);
                String rating = vis.get(7).toString();
                new EditVisit(() -> updateDisplay(),db,ID, country,city,year,season,best,comment,rating).setVisible(true);

            }
        });



        // Add Patient button
        lastYearVisits = new JButton("Visits from given year");
        buttonPanel.add(lastYearVisits);

        searchField = new JTextField();
        buttonPanel.add(searchField);

        lastYearVisits.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (searchField.getText().isEmpty()){
                    loadVisits(db.loadVisits(userID));
                } else {
                    int year = Integer.parseInt(searchField.getText());
                    loadVisits(db.loadVisits(userID, year));
                }
            }
        });

        viewImage = new JButton("View Visit Image");
        buttonPanel.add(viewImage);
        viewImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ImageViewer(model.getRowCount()).setVisible(true);
            }
        });

        shareVisit = new JButton("Share Visit");
        buttonPanel.add(shareVisit);
        shareVisit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                data = model.getDataVector();
                if (visitTable.getSelectedRow() != -1){

                    new ShareFrame(db, userID, (Integer) data.get(visitTable.getSelectedRow()).get(0));
                }
            }
        });

        viewSharedWithMe = new JButton("View Visits Shared with me");
        buttonPanel.add(viewSharedWithMe);
        viewSharedWithMe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewShared(db, userID);
            }
        });

        // Search by Name button
        sortByFoodAndRating = new JButton("Visits with best food");
        buttonPanel.add(sortByFoodAndRating);
        sortByFoodAndRating.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadVisits(db.loadVisitsWithBestFood(userID));
            }
        });

        Stats = new JLabel("Most Visited: " + mostVisited(id) + "  ---  Visited in Spring: " + visitedSpring(id));

        bottomPanel.add(Stats);
        bottomPanel.add(buttonPanel);
        addVisit = new JButton("Add Visit");
        bottomPanel.add(addVisit);
        addVisit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddVisit(() -> updateDisplay(),db,userID).setVisible(true);
            }
        });
        add(bottomPanel, BorderLayout.SOUTH);



        // Make the frame visible
        setVisible(true);
    }

    public void loadVisits(ResultSet input) {
        ResultSet rs = input;

        if (rs == null){
            JOptionPane.showMessageDialog(this, "Error loading patients.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        model.setRowCount(0); // Clear existing data
        try {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("locationID"),
                        rs.getString("country"),
                        rs.getString("city"),
                        rs.getInt("year"),
                        rs.getString("season"),
                        rs.getString("best"),
                        rs.getString("user_comment"),
                        rs.getInt("rating")
                });
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        visitTable.setModel(model);
    }

    public String mostVisited(int id){
        ArrayList<String> ans = new ArrayList<>();
        ResultSet rs = db.mostVisit(id);

        try{
            while(rs.next()){
                ans.add(rs.getString("country"));
            }
        } catch (Exception e){
            System.out.println(e.getStackTrace());
        }

        return ans.toString();
    }

    public String visitedSpring(int id){
        ArrayList<String> ans = new ArrayList<>();
        ResultSet rs = db.springVisit(id);

        try{
            while(rs.next()){
                ans.add(rs.getString("country"));
            }
        } catch (Exception e){
            System.out.println(e.getStackTrace());
        }

        return ans.toString();
    }

    public void loadStats(int id){
        Stats.setText("Most Visited: " + mostVisited(id) + "  ---  Visited in Spring: " + visitedSpring(id));
        this.update(this.getGraphics());
    }

    public void updateDisplay(){
        loadVisits(db.loadVisits(userID));
        loadStats(userID);
    }


}
