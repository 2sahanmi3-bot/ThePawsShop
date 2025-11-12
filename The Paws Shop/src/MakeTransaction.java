import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MakeTransaction extends JFrame {
    private DefaultTableModel tableModel;
    private JTable transactionTable;
    private JLabel totalPriceLabel;
    private double totalPrice = 0.0;

    public MakeTransaction(JFrame parentDashboard, String role, String username) {
        setTitle(role + " - Make a Transaction");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Make a Transaction for Little Paws!", JLabel.CENTER);
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 32));
        headerLabel.setBackground(new Color(140, 240, 232)); // Light pastel color
        headerLabel.setForeground(new Color(255, 133, 151, 255)); // Olive green
        headerLabel.setOpaque(true);
        add(headerLabel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255)); // Alice blue
        add(mainPanel, BorderLayout.CENTER);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBackground(new Color(250, 235, 215, 255)); // Honeydew

        JLabel transactionIdLabel = new JLabel("Transaction ID:");
        JTextField transactionIdField = new JTextField("T001");

        JLabel cashierNameLabel = new JLabel("Cashier Name:");
        JTextField cashierNameField = new JTextField(username);

        JLabel customerNameLabel = new JLabel("Customer Name:");
        JTextField customerNameField = new JTextField();

        JLabel productNameLabel = new JLabel("Product Name:");
        JComboBox<String> productComboBox = new JComboBox<>(new String[]{
                "Pet Toy - Rs. 899",
                "Pet Harness - Rs. 1199",
                "Pet Cage - Rs. 6999",
                "Grooming Kit - Rs. 1999",
                "Pet Collar - Rs. 599",
                "Pet Food - Rs. 1599"
        });

        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField("1");

        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        JComboBox<String> paymentMethodComboBox = new JComboBox<>(new String[]{"Cash", "Card", "Mobile Payment"});

        formPanel.add(transactionIdLabel);
        formPanel.add(transactionIdField);
        formPanel.add(cashierNameLabel);
        formPanel.add(cashierNameField);
        formPanel.add(customerNameLabel);
        formPanel.add(customerNameField);
        formPanel.add(productNameLabel);
        formPanel.add(productComboBox);
        formPanel.add(quantityLabel);
        formPanel.add(quantityField);
        formPanel.add(paymentMethodLabel);
        formPanel.add(paymentMethodComboBox);

        mainPanel.add(formPanel, BorderLayout.NORTH);

        // Table Panel
        tableModel = new DefaultTableModel(new String[]{"Product Name", "Quantity", "Price", "Total", "Action"}, 0);
        transactionTable = new JTable(tableModel);

        // Add a custom renderer and editor for the "Action" column
        transactionTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        transactionTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane tableScrollPane = new JScrollPane(transactionTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Transaction Details"));
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(255, 250, 240)); // Floral white

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.setBackground(new Color(140, 240, 232)); // Khaki
        totalPanel.add(new JLabel("Total Price: Rs. "));
        totalPriceLabel = new JLabel("0.00");
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPriceLabel.setForeground(Color.RED);
        totalPanel.add(totalPriceLabel);
        footerPanel.add(totalPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(250, 235, 215)); // Antique white
        JButton addButton = new JButton("Add Product");
        JButton completeButton = new JButton("Complete Transaction");
        JButton backButton = new JButton("Back");

        addButton.setBackground(new Color(173, 216, 230)); // Light blue
        addButton.setForeground(Color.DARK_GRAY);

        completeButton.setBackground(new Color(144, 238, 144)); // Light green
        completeButton.setForeground(Color.DARK_GRAY);

        backButton.setBackground(new Color(255, 182, 193)); // Light pink
        backButton.setForeground(Color.DARK_GRAY);

        buttonPanel.add(addButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(backButton);
        footerPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(footerPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> {
            String product = (String) productComboBox.getSelectedItem();
            String[] productDetails = product.split(" - Rs. ");
            String productName = productDetails[0];
            double productPrice = Double.parseDouble(productDetails[1]);
            int quantity;

            try {
                quantity = Integer.parseInt(quantityField.getText());
                if (quantity <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double productTotal = productPrice * quantity;
            totalPrice += productTotal;
            totalPriceLabel.setText(String.format("%.2f", totalPrice));

            tableModel.addRow(new Object[]{
                    productName,
                    quantity,
                    productPrice,
                    productTotal,
                    "Remove"
            });
        });

        completeButton.addActionListener(e -> {
            if (totalPrice == 0) {
                JOptionPane.showMessageDialog(this, "No products added!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String transactionId = transactionIdField.getText();
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String cashierName = cashierNameField.getText();
            String customerName = customerNameField.getText();
            String paymentMethod = paymentMethodComboBox.getSelectedItem().toString();

            StringBuilder products = new StringBuilder();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String productName = tableModel.getValueAt(i, 0).toString();
                String quantity = tableModel.getValueAt(i, 1).toString();
                String price = tableModel.getValueAt(i, 2).toString();
                products.append(productName).append(" (x").append(quantity).append(" @ Rs. ").append(price).append("), ");
            }

            if (products.length() > 0) products.setLength(products.length() - 2); // Remove trailing comma

            String transactionRecord = String.join("|", transactionId, dateTime, cashierName, customerName, products.toString(), String.valueOf(totalPrice), paymentMethod);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.txt", true))) {
                writer.write(transactionRecord);
                writer.newLine();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving transaction.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            JOptionPane.showMessageDialog(this, "Transaction Completed!\n" +
                    "Transaction ID: " + transactionId + "\n" +
                    "Cashier: " + cashierName + "\n" +
                    "Total Price: Rs. " + totalPriceLabel.getText() + "\n" +
                    "Payment Method: " + paymentMethod);

            dispose();
            parentDashboard.setVisible(true);
        });

        backButton.addActionListener(e -> {
            dispose();
            parentDashboard.setVisible(true);
        });

        setVisible(true);
    }

    // Renderer for "Action" column
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setText("Remove");
            setBackground(new Color(255, 182, 193)); // Light pink
            setForeground(Color.BLACK);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Editor for "Action" column
    class ButtonEditor extends DefaultCellEditor {
        private final JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Remove");
            button.setBackground(new Color(255, 182, 193)); // Light pink
            button.setForeground(Color.BLACK);
            button.addActionListener(e -> {
                int row = transactionTable.getSelectedRow();
                double productTotal = (double) tableModel.getValueAt(row, 3);
                totalPrice -= productTotal;
                totalPriceLabel.setText(String.format("%.2f", totalPrice));
                tableModel.removeRow(row);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }
    }
}
