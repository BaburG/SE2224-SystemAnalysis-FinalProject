import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ImageViewer extends JFrame {
    private final JComboBox<Integer> comboBox;
    private final JButton viewButton;
    private final JLabel imageLabel;

    public ImageViewer(int size) {
        // Set up the frame
        setTitle("Image Viewer");
        setSize(400, 400);
        setLayout(new BorderLayout());

        // Initialize components
        comboBox = new JComboBox<>();
        for (int i = 1; i <= size; i++) {
            comboBox.addItem(i);
        }
        viewButton = new JButton("View Image");
        imageLabel = new JLabel();

        // Center the image
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        // Add components to the frame
        JPanel topPanel = new JPanel();
        topPanel.add(comboBox);
        topPanel.add(viewButton);

        add(topPanel, BorderLayout.NORTH);
        add(imageLabel, BorderLayout.CENTER);

        // Add action listener to the button
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected number
                int selectedNumber = (int) comboBox.getSelectedItem();
                selectedNumber = selectedNumber>5? selectedNumber%5 : selectedNumber ;
                // Form the image file name
                String imageName = "C:\\Users\\Babur\\Desktop\\2024 Winter - Yasar\\test\\SE2224Project\\SE2224Project\\Location" + selectedNumber + ".jpg";
                // Load and display the image
                ImageIcon imageIcon = new ImageIcon(imageName);
                imageLabel.setIcon(imageIcon);
            }
        });
    }


}
