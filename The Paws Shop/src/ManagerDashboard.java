import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ManagerDashboard extends JFrame {
    public ManagerDashboard(Manager manager) {
        setTitle("Manager Dashboard - The Paws Shop");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Label
        JLabel headerLabel = new JLabel("Welcome, Manager", JLabel.CENTER);
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(255, 223, 186)); // Light pastel color
        headerLabel.setForeground(new Color(255, 133, 151)); // Dark olive green
        add(headerLabel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel, BorderLayout.CENTER);

        // Add Buttons
        addButton(mainPanel, "View Pet Supplies", e -> new ViewPetSupplies().setVisible(true));
        addButton(mainPanel, "Search by Category", e -> manager.searchByCategory(this));
        addButton(mainPanel, "Add Pet Supplies", e -> new AddPetSupplies(this, manager).setVisible(true));
        addButton(mainPanel, "Create Cashier Account", e -> new CreateCashierAccount(this).setVisible(true)); // Fixed here
        addButton(mainPanel, "Make a Transaction", e -> new MakeTransaction(this, "Manager", manager.getUsername()).setVisible(true));
        addButton(mainPanel, "View Transaction History", e -> new ViewTransactionHistory(this).setVisible(true));
        addButton(mainPanel, "Remove Cashier Account", e -> new RemoveCashierAccount(this).setVisible(true)); // Proper integration
        addButton(mainPanel, "Logout", e -> {
            new LoginUI();
            dispose();
        });

        setVisible(true);
    }

    private void addButton(JPanel panel, String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(176, 224, 230)); // Light blue
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.addActionListener(action);
        panel.add(Box.createVerticalStrut(20)); // Spacing
        panel.add(button);
    }
}
