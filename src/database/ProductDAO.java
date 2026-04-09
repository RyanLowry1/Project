// Name : Ryan Lowry
// Date : 02/04/2026
// File : ProductDAO
package database;

import model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public int addProduct(String name, double price, int stock, String description, String imagePath) throws Exception {
        // adds a new product into the database
        String sql = "INSERT INTO product (product_name, price, stock, description, image_path) VALUES (?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, name);
        ps.setDouble(2, price);
        ps.setInt(3, stock);
        ps.setString(4, description);
        ps.setString(5, imagePath);

        int rows = ps.executeUpdate();

        ps.close();
        conn.close();

        return rows;
    }
    // gets all products from the database and stores them in a list
    public List<Product> getAllProducts() throws Exception {
        List<Product> list = new ArrayList<>();
        // SQL to select all products
        String sql = "SELECT product_id, product_name, price, stock, description, image_path FROM product";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            // create a Product object using products from the current row
            Product p = new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getString("description"),
                    rs.getString("image_path")
            );
            list.add(p);
        }

        rs.close();
        ps.close();
        conn.close();

        return list;
    }
    // updates all product details at once
    public int updateProductFull(int productId, String newName, double newPrice, int newStock, String newDescription, String newImagePath) throws Exception {
        String sql = "UPDATE product SET product_name=?, price=?, stock=?, description=?, image_path=? WHERE product_id=?";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, newName);
        ps.setDouble(2, newPrice);
        ps.setInt(3, newStock);
        ps.setString(4, newDescription);
        ps.setString(5, newImagePath);
        ps.setInt(6, productId);

        int rows = ps.executeUpdate();

        ps.close();
        conn.close();

        return rows;
    }
    // removes a product from the database using its id
    public int deleteProduct(int productId) throws Exception {
        String sql = "DELETE FROM product WHERE product_id=?";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, productId);

        int rows = ps.executeUpdate();

        ps.close();
        conn.close();

        return rows;
    }

    // subtracts the quantity from current stock
    public int reduceStock(int productId, int quantity) throws Exception {

        // SQL decreases stock value in the database
        String sql = "UPDATE product SET stock = stock - ? WHERE product_id=?";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, quantity);
        ps.setInt(2, productId);

        int rows = ps.executeUpdate();

        ps.close();
        conn.close();

        return rows;
    }
}

