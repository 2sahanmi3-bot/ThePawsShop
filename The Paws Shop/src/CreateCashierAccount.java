import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CreateCashierAccount extends JFrame {
    private DefaultTableModel tableModel;
    private JTable cashierTable;
    private static final String USERS_FILE_PATH = "users.txt";
    private static final String DETAILS_FILE_PATH = "cashierDetails.txt";

    public CreateCashierAccount(JFrame parentDashboard) {
        setTitle("Create Cashier Accounts");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(176, 224, 230)); // Light blue
        JLabel headerLabel = new JLabel("Create Cashier Accounts", JLabel.CENTER);
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        headerLabel.setForeground(new Color(70, 130, 180)); // Steel blue
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new Color(245, 245, 245)); // Light gray

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(new Color(240, 255, 240)); // Honeydew

        JLabel idLabel = new JLabel("Cashier ID:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField idField = new JTextField();

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField nameField = new JTextField();

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JPasswordField passwordField = new JPasswordField();

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JPasswordField confirmPasswordField = new JPasswordField();

        inputPanel.add(idLabel);
        inputPanel.add(idField);
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        inputPanel.add(confirmPasswordLabel);
        inputPanel.add(confirmPasswordField);

        contentPanel.add(inputPanel, BorderLayout.WEST);

        // Table Panel
        String[] columnNames = {"Cashier ID", "Full Name", "Username"};
        tableModel = new DefaultTableModel(columnNames, 0);
        cashierTable = new JTable(tableModel);
        cashierTable.setRowHeight(25);
        cashierTable.setFont(new Font("Arial", Font.PLAIN, 14));
        cashierTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

        JScrollPane tableScrollPane = new JScrollPane(cashierTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Cashier Accounts"));
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(255, 250, 240)); // Floral white

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(50, 205, 50)); // Green
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));

        JButton editButton = new JButton("Edit");
        editButton.setBackground(new Color(255, 165, 0)); // Orange
        editButton.setForeground(Color.WHITE);
        editButton.setFont(new Font("Arial", Font.BOLD, 14));

        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(100, 149, 237)); // Blue
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));

        buttonPanel.add(saveButton);
        buttonPanel.add(editButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Load existing accounts from file
        loadCashierDetails();

        // Button Actions
        saveButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

            if (id.isEmpty() || name.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            tableModel.addRow(new Object[]{id, name, username});
            saveCashierAccount(username, password);
            saveCashierDetails(id, name, username);
            JOptionPane.showMessageDialog(this, "Cashier account created successfully!");

            idField.setText("");
            nameField.setText("");
            usernameField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
        });

        editButton.addActionListener(e -> {
            int selectedRow = cashierTable.getSelectedRow();
            if (selectedRow >= 0) {
                idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                usernameField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                tableModel.removeRow(selectedRow);
                saveUpdatedCashierDetails();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            parentDashboard.setVisible(true);
        });

        setVisible(true);
    }

    private void saveCashierAccount(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE_PATH, true))) {
            // Save in the format: username|password|role
            writer.write(username + "|" + password + "|Cashier");
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving account to file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveCashierDetails(String id, String name, String username) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DETAILS_FILE_PATH, true))) {
            writer.write(id + "|" + name + "|" + username);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving cashier details!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveUpdatedCashierDetails() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DETAILS_FILE_PATH))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String id = tableModel.getValueAt(i, 0).toString();
                String name = tableModel.getValueAt(i, 1).toString();
                String username = tableModel.getValueAt(i, 2).toString();
                writer.write(id + "|" + name + "|" + username);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving updated cashier details!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCashierDetails() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DETAILS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length == 3) {
                    tableModel.addRow(new Object[]{data[0], data[1], data[2]});
                }
            }
        } catch (IOException e) {
            System.out.println("No existing cashier details found. Starting fresh.");
        }
    }
}

