public class PetSupply {
    private String name;
    private String category;
    private double price;

    public PetSupply(String name, String category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "PetSupply [name=" + name + ", category=" + category + ", price=" + price + "]";
    }
}
