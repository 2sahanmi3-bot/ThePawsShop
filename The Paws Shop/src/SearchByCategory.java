import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SearchByCategory extends JFrame {

    private DefaultTableModel tableModel;

    public SearchByCategory(JFrame parentDashboard) {
        setTitle("Search Pet Supplies by Category");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 228, 225));

        JLabel headerLabel = new JLabel("Search Pet Supplies by Category", JLabel.CENTER);
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 32));
        headerLabel.setForeground(new Color(139, 69, 19));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        controlPanel.setBackground(new Color(245, 245, 220));

        JLabel categoryLabel = new JLabel("Select a Category:");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        controlPanel.add(categoryLabel);

        String[] categories = {"All", "Pet Toys", "Harnesses", "Cages", "Grooming Products", "Collars", "Food"};
        JComboBox<String> categoryDropdown = new JComboBox<>(categories);
        categoryDropdown.setFont(new Font("Arial", Font.PLAIN, 14));
        controlPanel.add(categoryDropdown);

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchButton.setBackground(new Color(176, 224, 230));
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(e -> loadSupplies((String) categoryDropdown.getSelectedItem()));
        controlPanel.add(searchButton);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(new Color(240, 128, 128));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            this.dispose();
            if (parentDashboard != null) {
                parentDashboard.setVisible(true);
            }
        });
        controlPanel.add(backButton);

        add(controlPanel, BorderLayout.SOUTH);

        String[] columnNames = {"Product ID", "Product Name", "Category", "Price Rs.", "Quantity Available"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable productTable = new JTable(tableModel);
        productTable.setRowHeight(30);
        productTable.setFont(new Font("Arial", Font.BOLD, 14));
        productTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        productTable.getTableHeader().setBackground(new Color(176, 224, 230));
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(tableScrollPane, BorderLayout.CENTER);

        loadSupplies("All");

        setVisible(true);
    }

    private void loadSupplies(String category) {
        try (BufferedReader reader = new BufferedReader(new FileReader("supplies.txt"))) {
            String line;
            tableModel.setRowCount(0);

            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length != 5) continue;

                String id = details[0];
                String name = details[1];
                String productCategory = details[2];
                double price;
                int quantity;

                try {
                    price = Double.parseDouble(details[3]);
                    quantity = Integer.parseInt(details[4]);
                } catch (NumberFormatException e) {
                    continue;
                }

                if ("All".equals(category) || productCategory.equalsIgnoreCase(category)) {
                    tableModel.addRow(new Object[]{id, name, productCategory, price, quantity});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading supplies file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

