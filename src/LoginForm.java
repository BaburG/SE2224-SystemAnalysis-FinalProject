import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class LoginForm extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private SQLConnector conn;

    public LoginForm(SQLConnector conn) {
        super("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();



        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel()); // Empty label for alignment

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (!username.isEmpty() && !password.isEmpty()){
                    try {
                        int userid = conn.auth(username,password);
                        if (userid > 0){
                            System.out.println(STR."Success! authenticated user with userid \{userid}");
                            new MainFrame(userid, username, conn);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(LoginForm.this, "Invalid input!\nUsername or Password is Wrong!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(LoginForm.this,
                                "SQL EXCEPTION \nSQLException: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this, "Invalid input! One or more fields Empty", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(loginButton);


    }
}