import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class ViewTransactionHistory extends JFrame {
    private DefaultTableModel tableModel;
    private JTable transactionTable;
    private static final String TRANSACTION_FILE = "transactions.txt";

    public ViewTransactionHistory(JFrame parentDashboard) {
        setTitle("View Transaction History");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Transaction History", JLabel.CENTER);
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 34));
        headerLabel.setBackground(new Color(250, 240, 230)); // Cream
        headerLabel.setForeground(new Color(70, 130, 180)); // Steel Blue
        headerLabel.setOpaque(true);
        headerLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, new Color(255, 182, 193))); // Pink border
        add(headerLabel, BorderLayout.NORTH);

        // Table Panel
        String[] columns = {"Transaction ID", "Date & Time", "Cashier Name/ID", "Customer Name", "Products", "Total Price", "Payment Method"};
        tableModel = new DefaultTableModel(columns, 0);
        transactionTable = new JTable(tableModel);
        transactionTable.setRowHeight(30);
        transactionTable.setFont(new Font("Arial", Font.PLAIN, 14));
        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        transactionTable.getTableHeader().setBackground(new Color(173, 216, 230)); // Light Blue
        transactionTable.getTableHeader().setForeground(new Color(70, 130, 180)); // Steel Blue

        JScrollPane tableScrollPane = new JScrollPane(transactionTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 182, 193), 3)); // Pink border
        add(tableScrollPane, BorderLayout.CENTER);

        // Load existing transactions
        loadTransactionHistory();

        // Footer Panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        footerPanel.setBackground(new Color(255, 250, 240)); // Cream

        JButton backButton = new JButton("Back");
        JButton exportButton = new JButton("Export");

        styleButton(backButton, new Color(176, 224, 230), Color.DARK_GRAY); // Light Blue
        styleButton(exportButton, new Color(144, 238, 144), Color.DARK_GRAY); // Light Green

        footerPanel.add(exportButton);
        footerPanel.add(backButton);
        add(footerPanel, BorderLayout.SOUTH);

        // Button Actions
        backButton.addActionListener(e -> {
            dispose();
            parentDashboard.setVisible(true);
        });

        exportButton.addActionListener(e -> {
            exportTransactionHistory();
            JOptionPane.showMessageDialog(this, "Transaction history exported successfully!", "Export", JOptionPane.INFORMATION_MESSAGE);
        });

        setVisible(true);
    }

    private void loadTransactionHistory() {
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                tableModel.addRow(data);
            }
        } catch (IOException e) {
            System.out.println("No existing transaction history found.");
        }
    }

    private void exportTransactionHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("exported_transactions.txt"))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    writer.write(tableModel.getValueAt(i, j).toString());
                    if (j < tableModel.getColumnCount() - 1) writer.write("|");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error exporting transactions.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 182, 193), 2), // Pink border
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }
}
