import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Manager extends User {
    public Manager(String username, String password) {
        super(username, password, "Manager");
    }

    @Override
    public void dashboard() {
        new ManagerDashboard(this);
    }

    // Method to search supplies by category
    public void searchByCategory(JFrame parentDashboard) {
        new SearchByCategory(parentDashboard).setVisible(true);
    }

    // Method to add supplies
    public void addSupplies(String id, String name, String category, double price, int quantity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("supplies.txt", true))) {
            writer.write(id + "," + name + "," + category + "," + price + "," + quantity);
            writer.newLine();
            JOptionPane.showMessageDialog(null, "Supply added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while adding supplies.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to create a cashier account
    public void createCashier(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write(username + "," + password + ",Cashier");
            writer.newLine();
            JOptionPane.showMessageDialog(null, "Cashier account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while creating cashier account.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to remove a cashier account
    public void removeCashier(String username) {
        JOptionPane.showMessageDialog(null, "Feature not implemented yet!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
