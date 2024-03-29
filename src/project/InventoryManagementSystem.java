package project;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InventoryManagementSystem {

    public static void main(String[] args) {
        InventoryManagementSystem ims = new InventoryManagementSystem();
        ims.run();
    }

    public void run() {
        Connection connection = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // Establishing connection
            String url = "jdbc:mysql://localhost:3306/inventory";
            String username = "root";
            String password = "rahama786";
            connection = DriverManager.getConnection(url, username, password);

            // Creating products table if not exists
            createProductsTable(connection);

            while (true) {
                // Displaying main menu
                displayMainMenu();

                // Handling user input
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                switch (choice) {
                    case 1:
                        displayAllProducts(connection);
                        break;
                    case 2:
                        addProduct(scanner, connection);
                        break;
                    case 3:
                        updateProduct(scanner, connection);
                        break;
                    case 4:
                        deleteProduct(scanner, connection);
                        break;
                    case 5:
                        System.out.println("Exiting program...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please select again.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                scanner.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void createProductsTable(Connection connection) throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS products (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(100)," +
                "quantity INT," +
                "price DECIMAL(10, 2)" +
                ")";
        Statement createTableStatement = connection.createStatement();
        createTableStatement.executeUpdate(createTableQuery);
    }

    private void displayMainMenu() {
        System.out.println("\n=== Inventory Management System ===");
        System.out.println("1. Display All Products");
        System.out.println("2. Add Product");
        System.out.println("3. Update Product");
        System.out.println("4. Delete Product");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private void displayAllProducts(Connection connection) throws SQLException {
        List<Product> products = getAllProducts(connection);
        System.out.println("\nProducts List:");
        for (Product product : products) {
            System.out.println(product.getId() + " - " + product.getName() + " - " + product.getQuantity() + " - " + product.getPrice());
        }
    }

    private void addProduct(Scanner scanner, Connection connection) throws SQLException {
        System.out.println("\nAdding a New Product:");
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline character

        String addProductQuery = "INSERT INTO products (name, quantity, price) VALUES (?, ?, ?)";
        PreparedStatement addProductStatement = connection.prepareStatement(addProductQuery);
        addProductStatement.setString(1, name);
        addProductStatement.setInt(2, quantity);
        addProductStatement.setDouble(3, price);
        addProductStatement.executeUpdate();
        System.out.println("Product added successfully.");
    }

    private void updateProduct(Scanner scanner, Connection connection) throws SQLException {
        System.out.println("\nUpdating a Product:");
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        // Check if the product exists
        if (!productExists(productId, connection)) {
            System.out.println("Product with ID " + productId + " does not exist.");
            return;
        }

        System.out.print("Enter new product name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new product quantity: ");
        int newQuantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        System.out.print("Enter new product price: ");
        double newPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume newline character

        String updateProductQuery = "UPDATE products SET name = ?, quantity = ?, price = ? WHERE id = ?";
        PreparedStatement updateProductStatement = connection.prepareStatement(updateProductQuery);
        updateProductStatement.setString(1, newName);
        updateProductStatement.setInt(2, newQuantity);
        updateProductStatement.setDouble(3, newPrice);
        updateProductStatement.setInt(4, productId);
        updateProductStatement.executeUpdate();
        System.out.println("Product updated successfully.");
    }

    private void deleteProduct(Scanner scanner, Connection connection) throws SQLException {
        System.out.println("\nDeleting a Product:");
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        // Check if the product exists
        if (!productExists(productId, connection)) {
            System.out.println("Product with ID " + productId + " does not exist.");
            return;
        }

        String deleteProductQuery = "DELETE FROM products WHERE id = ?";
        PreparedStatement deleteProductStatement = connection.prepareStatement(deleteProductQuery);
        deleteProductStatement.setInt(1, productId);
        deleteProductStatement.executeUpdate();
        System.out.println("Product deleted successfully.");
    }

    private boolean productExists(int productId, Connection connection) throws SQLException {
        String getProductQuery = "SELECT id FROM products WHERE id = ?";
        PreparedStatement getProductStatement = connection.prepareStatement(getProductQuery);
        getProductStatement.setInt(1, productId);
        ResultSet resultSet = getProductStatement.executeQuery();
        return resultSet.next();
    }

    private List<Product> getAllProducts(Connection connection) throws SQLException {
        List<Product> products = new ArrayList<>();
        String getAllProductsQuery = "SELECT * FROM products";
        Statement getAllProductsStatement = connection.createStatement();
        ResultSet resultSet = getAllProductsStatement.executeQuery(getAllProductsQuery);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int quantity = resultSet.getInt("quantity");
            double price = resultSet.getDouble("price");
            products.add(new Product(id, name, quantity, price));
        }
        return products;
    }
}

class Product {
    private int id;
    private String name;
    private int quantity;
    private double price;

    public Product(int id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

