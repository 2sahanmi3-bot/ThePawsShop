import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class AddPetSupplies extends JFrame {
    private DefaultTableModel tableModel;
    private static final String FILE_PATH = "supplies.txt";

    public AddPetSupplies(JFrame parentDashboard) {
        setTitle("Paws and Claws: Add New Supplies with Love");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Label with a Cute Theme
        JLabel headerLabel = new JLabel("Paws and Claws: Add New Supplies with Love", JLabel.CENTER);
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        headerLabel.setForeground(new Color(255, 105, 180)); // Hot Pink
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(255, 239, 213)); // Blanched Almond
        add(headerLabel, BorderLayout.NORTH);

        // Input Panel with Colorful Fields
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(new Color(255, 248, 220)); // Cornsilk

        JTextField idField = new JTextField(10);
        JTextField nameField = new JTextField(10);
        JTextField categoryField = new JTextField(10);
        JTextField priceField = new JTextField(10);
        JTextField quantityField = new JTextField(10);

        JLabel idLabel = new JLabel("ID:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel categoryLabel = new JLabel("Category:");
        JLabel priceLabel = new JLabel("Price:");
        JLabel quantityLabel = new JLabel("Quantity:");

        // Styling Labels
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        idLabel.setFont(labelFont);
        nameLabel.setFont(labelFont);
        categoryLabel.setFont(labelFont);
        priceLabel.setFont(labelFont);
        quantityLabel.setFont(labelFont);

        inputPanel.add(idLabel);
        inputPanel.add(idField);
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryField);
        inputPanel.add(priceLabel);
        inputPanel.add(priceField);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityField);

        add(inputPanel, BorderLayout.NORTH);

        // Table for Supplies with a Colorful Theme
        String[] columnNames = {"ID", "Name", "Category", "Price", "Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable supplyTable = new JTable(tableModel);
        supplyTable.setFont(new Font("Arial", Font.PLAIN, 14));
        supplyTable.setRowHeight(30);
        supplyTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        supplyTable.getTableHeader().setBackground(new Color(176, 224, 230)); // Powder Blue
        supplyTable.getTableHeader().setForeground(Color.BLACK);

        JScrollPane tableScrollPane = new JScrollPane(supplyTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(tableScrollPane, BorderLayout.CENTER);

        loadSupplies();

        // Button Panel with Vibrant Colors
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(255, 228, 225)); // Misty Rose

        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Remove");
        JButton backButton = new JButton("Back");

        // Button Styling
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        addButton.setFont(buttonFont);
        addButton.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        addButton.setForeground(Color.WHITE);

        removeButton.setFont(buttonFont);
        removeButton.setBackground(new Color(237, 82, 82)); // Tomato
        removeButton.setForeground(Color.WHITE);

        backButton.setFont(buttonFont);
        backButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        backButton.setForeground(Color.WHITE);

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String category = categoryField.getText();
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                if (name.isEmpty() || category.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                tableModel.addRow(new Object[]{id, name, category, price, quantity});
                saveSupplies();
                JOptionPane.showMessageDialog(this, "Product added successfully!");

                idField.setText("");
                nameField.setText("");
                categoryField.setText("");
                priceField.setText("");
                quantityField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeButton.addActionListener(e -> {
            int selectedRow = supplyTable.getSelectedRow();
            if (selectedRow >= 0) {
                tableModel.removeRow(selectedRow);
                saveSupplies();
                JOptionPane.showMessageDialog(this, "Product removed successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a product to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            parentDashboard.setVisible(true);
        });

        setVisible(true);
    }

    public AddPetSupplies(JFrame parentDashboard, Object user) {
        this(parentDashboard); // Call the existing JFrame constructor logic
    }

    private void loadSupplies() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                tableModel.addRow(data);
            }
        } catch (IOException e) {
            System.out.println("No existing supplies file found. Starting fresh.");
        }
    }

    private void saveSupplies() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    writer.write(tableModel.getValueAt(i, j).toString());
                    if (j < tableModel.getColumnCount() - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving supplies file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
