import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EditVisit extends JFrame {
    private final JTextField countryField;
    private final JTextField cityField;
    private final JTextField yearField;
    private final JTextField seasonField;
    private final JTextField bestFeatureField;
    private final JTextField commentField;
    private final JTextField ratingField;
    private final JButton updateButton;
    private final Runnable onCloseCallback;

    public EditVisit(Runnable onCloseCallback,SQLConnector conn,int id, String country, String city, String year, String season, String best, String comment, String rating){
        this.onCloseCallback = onCloseCallback;

        // Set up the frame
        setTitle("Edit Visit");
        setSize(400, 400);

        setLayout(new BorderLayout());

        // Create a panel with GridLayout for labels and text fields
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));

        // Initialize components
        JLabel countryLabel = new JLabel("Country:");
        JLabel cityLabel = new JLabel("City:");
        JLabel yearLabel = new JLabel("Year:");
        JLabel seasonLabel = new JLabel("Season:");
        JLabel bestFeatureLabel = new JLabel("Best Feature:");
        JLabel commentLabel = new JLabel("Comment:");
        JLabel ratingLabel = new JLabel("Rating:");

        countryField = new JTextField(country,20);
        cityField = new JTextField(city,20);
        yearField = new JTextField(year,20);
        seasonField = new JTextField(season,20);
        bestFeatureField = new JTextField(best,20);
        commentField = new JTextField(comment,20);
        ratingField = new JTextField(rating,20);

        // Add labels and text fields to the panel
        inputPanel.add(countryLabel);
        inputPanel.add(countryField);
        inputPanel.add(cityLabel);
        inputPanel.add(cityField);
        inputPanel.add(yearLabel);
        inputPanel.add(yearField);
        inputPanel.add(seasonLabel);
        inputPanel.add(seasonField);
        inputPanel.add(bestFeatureLabel);
        inputPanel.add(bestFeatureField);
        inputPanel.add(commentLabel);
        inputPanel.add(commentField);
        inputPanel.add(ratingLabel);
        inputPanel.add(ratingField);

        // Initialize and add the Update button
        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(id);
                conn.updateVisit(countryField.getText(),cityField.getText(),Integer.parseInt(yearField.getText()),seasonField.getText(),bestFeatureField.getText(),commentField.getText(),Integer.parseInt(ratingField.getText()), id);
                dispose();
            }
        });

        // Add the panel and the button to the frame
        add(inputPanel, BorderLayout.CENTER);
        add(updateButton, BorderLayout.SOUTH);
        // Add a window listener to call the callback when the window is closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (onCloseCallback != null) {
                    onCloseCallback.run();
                }
            }
        });
    }

}
