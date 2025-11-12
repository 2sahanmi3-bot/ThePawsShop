import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame {

    public LoginUI() {
        // Frame settings
        setTitle("The Paws Shop - Login");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Background Image
        ImageIcon backgroundIcon = new ImageIcon("C:/Users/Sahanmi Maddumage/Desktop/The Paws Shop/resources/background.png");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, 500, 500);
        add(backgroundLabel);

        // Transparent Panel for Components
        JPanel contentPanel = new JPanel();
        contentPanel.setBounds(50, 50, 400, 400);
        contentPanel.setLayout(null);
        contentPanel.setOpaque(false); // Make the panel transparent
        backgroundLabel.add(contentPanel);

        // Title
        JLabel titleLabel = new JLabel("The Paws Shop");
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(50, 20, 300, 30);
        contentPanel.add(titleLabel);

        // Role Label
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        roleLabel.setBounds(50, 80, 100, 25);
        contentPanel.add(roleLabel);

        // Role Dropdown
        String[] roles = {"Manager", "Cashier"};
        JComboBox<String> roleDropdown = new JComboBox<>(roles);
        roleDropdown.setBounds(150, 80, 200, 30);
        contentPanel.add(roleDropdown);

        // Username Label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameLabel.setBounds(50, 130, 100, 25);
        contentPanel.add(usernameLabel);

        // Username TextField
        JTextField usernameField = new JTextField();
        usernameField.setBounds(150, 130, 200, 30);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        contentPanel.add(usernameField);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setBounds(50, 180, 100, 25);
        contentPanel.add(passwordLabel);

        // Password Field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 180, 200, 30);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        contentPanel.add(passwordField);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(255, 223, 186)); // Light Yellow
        loginButton.setBounds(150, 230, 100, 40);
        contentPanel.add(loginButton);

        // Login Action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String role = (String) roleDropdown.getSelectedItem();

                System.out.println("Username: " + username + ", Password: " + password + ", Role: " + role);

                try {
                    User user = UserService.authenticate(username, password);
                    if (user != null && user.getRole().equalsIgnoreCase(role)) {
                        JOptionPane.showMessageDialog(null, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        if (role.equalsIgnoreCase("Manager")) {
                            new ManagerDashboard((Manager) user);
                        } else if (role.equalsIgnoreCase("Cashier")) {
                            new CashierDashboard((Cashier) user);
                        }
                        dispose(); // Close the login window
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid credentials or role mismatch!", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An error occurred during login. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Set frame visibility
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginUI();
    }
}
