import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CashierDashboard extends JFrame {
    public CashierDashboard(Cashier cashier) {
        setTitle("Cashier Dashboard - The Paws Shop");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("Welcome, Cashier", JLabel.CENTER);
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(255, 223, 186));
        headerLabel.setForeground(new Color(255, 133, 151));
        add(headerLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel, BorderLayout.CENTER);

        addButton(mainPanel, "View Pet Supplies", e -> new ViewPetSupplies().setVisible(true));
        addButton(mainPanel, "Search by Category", e -> new SearchByCategory(this).setVisible(true));
        addButton(mainPanel, "Add Pet Supplies", e -> new AddPetSupplies(this, cashier).setVisible(true));
        addButton(mainPanel, "Make a Transaction", e -> new MakeTransaction(this, "Cashier", cashier.getUsername()).setVisible(true));
        addButton(mainPanel, "View Transaction History", e -> {
            new ViewTransactionHistory(this); // Open the transaction history
            setVisible(false); // Hide the dashboard while viewing transaction history
        });
        addButton(mainPanel, "Logout", e -> {
            new LoginUI();
            dispose();
        });

        setVisible(true);
    }

    private void addButton(JPanel panel, String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(176, 224, 230));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.addActionListener(action);
        panel.add(Box.createVerticalStrut(20));
        panel.add(button);
    }
}

