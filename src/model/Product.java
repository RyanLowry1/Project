// Name : Ryan Lowry
// Date : 02/04/2026
// File : Product.java
package model;

public class Product {

    // stores product details from the database
    private int productId;
    private String productName;
    private double price;
    private int stock;
    private String description;
    private String imagePath;

    // constructor used when loading products from database
    public Product(int productId, String productName, double price, int stock, String description, String imagePath) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.imagePath = imagePath;
    }

    // GETTERS

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }
}