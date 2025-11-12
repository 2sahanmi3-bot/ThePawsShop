import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class RemoveCashierAccount extends JFrame {
    private DefaultTableModel tableModel;
    private JTable cashierTable;
    private static final String CASHIER_DETAILS_FILE = "cashierDetails.txt";
    private static final String USERS_FILE = "users.txt";

    public RemoveCashierAccount(JFrame parentDashboard) {
        setTitle("Remove Cashier Account");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color
        getContentPane().setBackground(new Color(240, 248, 255)); // Alice Blue

        JLabel headerLabel = new JLabel("Remove Cashier Account", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(72, 61, 139)); // Dark Slate Blue
        add(headerLabel, BorderLayout.NORTH);

        String[] columns = {"Cashier ID", "Full Name", "Username", "Role", "Action"};
        tableModel = new DefaultTableModel(columns, 0);
        cashierTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        cashierTable.setRowHeight(40);
        cashierTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        cashierTable.getTableHeader().setBackground(new Color(255, 182, 193)); // Light Pink
        cashierTable.getTableHeader().setForeground(new Color(75, 0, 130)); // Indigo
        cashierTable.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane tableScrollPane = new JScrollPane(cashierTable);
        add(tableScrollPane, BorderLayout.CENTER);

        loadCashierDetails();

        cashierTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        cashierTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBackground(new Color(240, 248, 255)); // Match the main background

        JButton addNewButton = new JButton("Add New Cashier");
        styleButton(addNewButton, new Color(60, 179, 113), Color.WHITE); // Medium Sea Green
        footerPanel.add(addNewButton);
        JButton backButton = new JButton("Back");
        styleButton(backButton, new Color(135, 206, 250), new Color(25, 25, 112)); // Sky Blue and Midnight Blue
        footerPanel.add(backButton);
        add(footerPanel, BorderLayout.SOUTH);

        addNewButton.addActionListener(e -> addNewCashier());
        backButton.addActionListener(e -> {
            dispose();
            parentDashboard.setVisible(true);
        });

        setVisible(true);
    }

    private void loadCashierDetails() {
        tableModel.setRowCount(0);
        try (BufferedReader reader = new BufferedReader(new FileReader(CASHIER_DETAILS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length < 4) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }
                Object[] rowData = new Object[]{
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        "Remove"
                };
                tableModel.addRow(rowData);
            }
        } catch (IOException e) {
            System.out.println("No existing cashier details found.");
        }
    }

    private void removeCashierAccount(String cashierId, String username) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this cashier?",
                "Confirm Removal", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean removedFromCashierDetails = removeFromFile(CASHIER_DETAILS_FILE, cashierId);
            boolean removedFromUsers = removeFromFile(USERS_FILE, username);

            if (removedFromCashierDetails && removedFromUsers) {
                loadCashierDetails();
                JOptionPane.showMessageDialog(this, "Cashier account removed successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Error removing cashier. Please try again.");
            }
        }
    }

    private boolean removeFromFile(String filePath, String identifier) {
        File inputFile = new File(filePath);
        File tempFile = new File("temp_" + filePath);
        boolean success = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.split("\\|")[0].equals(identifier)) {
                    writer.write(line);
                    writer.newLine();
                } else {
                    success = true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating file: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        }

        return success;
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void addNewCashier() {
        String cashierId = JOptionPane.showInputDialog(this, "Enter Cashier ID:");
        if (cashierId == null || cashierId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Cashier ID.");
            return;
        }

        String fullName = JOptionPane.showInputDialog(this, "Enter Full Name:");
        if (fullName == null || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the cashier's full name.");
            return;
        }

        String username = JOptionPane.showInputDialog(this, "Enter Username:");
        if (username == null || username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a username for the cashier.");
            return;
        }

        String role = (String) JOptionPane.showInputDialog(this, "Select Role:", "Cashier Role",
                JOptionPane.QUESTION_MESSAGE, null, new String[]{"Cashier"}, null);
        if (role == null) {
            return;
        }

        Object[] rowData = new Object[]{cashierId, fullName, username, role, "Remove"};
        tableModel.addRow(rowData);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CASHIER_DETAILS_FILE, true))) {
            writer.write(cashierId + "|" + fullName + "|" + username + "|" + role);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to file: " + CASHIER_DETAILS_FILE, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText((value == null) ? "" : value.toString());
            setBackground(new Color(255, 160, 122)); // Light Salmon
            setForeground(new Color(25, 25, 112)); // Midnight Blue
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener((ActionEvent e) -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            button.setBackground(new Color(255, 105, 180)); // Hot Pink
            button.setForeground(Color.WHITE);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = cashierTable.getSelectedRow();
                String cashierId = (String) cashierTable.getValueAt(row, 0);
                String username = (String) cashierTable.getValueAt(row, 2);
                removeCashierAccount(cashierId, username);
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
