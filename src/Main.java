import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        System.out.println("Hello world!");
        LoginForm loginform = new LoginForm(new SQLConnector());
        loginform.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginform.setVisible(true);
    }
}