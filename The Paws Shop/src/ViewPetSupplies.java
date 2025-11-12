import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ViewPetSupplies extends JFrame {

    public ViewPetSupplies() {
        setTitle("Pet Supplies");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("- Little Paws, Big Love: Brightening Your Life Every Day.", JLabel.CENTER);
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(255, 223, 186));
        headerLabel.setForeground(new Color(85, 107, 47));
        add(headerLabel, BorderLayout.NORTH);

        add(Box.createVerticalStrut(40), BorderLayout.CENTER);

        String[] columns = {"Product ID", "Product Name", "Category", "Price Rs.", "Quantity Available"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable suppliesTable = new JTable(tableModel);
        suppliesTable.setFont(new Font("Arial", Font.PLAIN, 14));
        suppliesTable.setRowHeight(30);

        suppliesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        suppliesTable.getTableHeader().setBackground(new Color(176, 224, 230));
        suppliesTable.getTableHeader().setForeground(Color.BLACK);

        loadSupplies(tableModel);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < suppliesTable.getColumnCount(); i++) {
            suppliesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(suppliesTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(240, 128, 128));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 239, 213));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadSupplies(DefaultTableModel tableModel) {
        try (BufferedReader reader = new BufferedReader(new FileReader("supplies.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                tableModel.addRow(data);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading supplies file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
