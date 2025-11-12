import javax.swing.*;

public class Cashier extends User {
    public Cashier(String username, String password) {
        super(username, password, "Cashier");
    }

    @Override
    public void dashboard() {
        new CashierDashboard(this);
    }

    // Method to search supplies by category
    public void searchByCategory(JFrame parentDashboard) {
        new SearchByCategory(parentDashboard).setVisible(true);
    }

    // Method to add supplies
    public void addSupplies(String id, String name, String category, double price, int quantity) {
        JOptionPane.showMessageDialog(null, "Only managers can add supplies.", "Permission Denied", JOptionPane.ERROR_MESSAGE);
    }
}
