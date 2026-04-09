// Name : Ryan Lowry
// Date : 02/04/2026
// File : UserProductFrame.java
package gui;

import database.InvoiceDAO;
import database.InvoiceItemDAO;
import database.ProductDAO;
import model.Product;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProductFrame extends JFrame {

    // ProductDAO used to get products from database
    private final ProductDAO dao = new ProductDAO();

    // panel that displays all product cards
    private JPanel gridPanel;

    // stores cart items
    private Map<Product, Integer> cart = new HashMap<>();

    // stores currently logged in user
    private final User currentUser;

    public UserProductFrame(User user) {

        // assign logged in user
        this.currentUser = user;

        setTitle("Food Menu - " + currentUser.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // main layout container
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        setContentPane(root);

        // top section with title and cart button
        JPanel headerWrapper = new JPanel(new BorderLayout());
        headerWrapper.setBackground(Color.WHITE);
        headerWrapper.setBorder(new EmptyBorder(20, 20, 10, 20));

        JPanel header = new JPanel();
        header.setBackground(Color.WHITE);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        // main title
        JLabel lblTitle = new JLabel("Restaurant Food Menu");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Top Picks from Our Customers");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSubtitle.setForeground(Color.DARK_GRAY);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(lblTitle);
        header.add(Box.createVerticalStrut(5));
        header.add(lblSubtitle);

        // cart button with image
        JButton cartBtn = new JButton();
        cartBtn.setBorderPainted(false);
        cartBtn.setContentAreaFilled(false);
        cartBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // load and scale cart image
        ImageIcon cartIcon = new ImageIcon(getClass().getResource("/images/cart.jpg"));
        Image scaledCart = cartIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        cartBtn.setIcon(new ImageIcon(scaledCart));

        // open cart when clicked
        cartBtn.addActionListener(e -> showCart());

        headerWrapper.add(header, BorderLayout.CENTER);
        headerWrapper.add(cartBtn, BorderLayout.EAST);

        root.add(headerWrapper, BorderLayout.NORTH);

        // grid layout to display products in rows and columns
        gridPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(new EmptyBorder(15, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        root.add(scroll, BorderLayout.CENTER);

        loadProducts();
        setVisible(true);
    }

    // loads products from database and adds them to grid
    private void loadProducts() {
        try {
            gridPanel.removeAll();
            List<Product> products = dao.getAllProducts();

            // create a card for each product
            for (Product p : products) {
                gridPanel.add(createProductCard(p));
            }

            gridPanel.revalidate();
            gridPanel.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading products");
        }
    }

    // creates a single product card
    private JPanel createProductCard(Product product) {

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(230, 260));

        // card styling
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 2, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // product image
        JLabel imageLabel = new JLabel(loadProductImage(product, 160, 110));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(imageLabel);

        card.add(Box.createVerticalStrut(10));

        // product name
        JLabel nameLabel = new JLabel(product.getProductName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(nameLabel);

        card.add(Box.createVerticalStrut(6));

        // product price
        JLabel priceLabel = new JLabel("€" + String.format("%.2f", product.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setForeground(new Color(220, 70, 20));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(priceLabel);

        card.add(Box.createVerticalStrut(10));

        // button to open product details
        JButton btnOrder = new JButton("View Details");
        btnOrder.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnOrder.setBackground(new Color(255, 153, 0));
        btnOrder.setForeground(Color.WHITE);

        btnOrder.addActionListener(e -> showProductDetails(product));
        card.add(btnOrder);

        return card;
    }

    // shows product details and allows user to add to cart
    private void showProductDetails(Product product) {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // product image size
        JLabel bigImage = new JLabel(loadProductImage(product, 260, 180));
        bigImage.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(bigImage, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(product.getProductName());
        name.setFont(new Font("Arial", Font.BOLD, 18));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel price = new JLabel("€" + product.getPrice());
        price.setAlignmentX(Component.CENTER_ALIGNMENT);

        // quantity selector plus and minus buttons
        JPanel qtyPanel = new JPanel();
        JButton minus = new JButton("-");
        JButton plus = new JButton("+");
        JLabel qtyLabel = new JLabel("1");

        final int[] qty = {1};

        // decrease quantity
        minus.addActionListener(e -> {
            if (qty[0] > 1) {
                qty[0]--;
                qtyLabel.setText(String.valueOf(qty[0]));
            }
        });

        // increase quantity
        plus.addActionListener(e -> {
            qty[0]++;
            qtyLabel.setText(String.valueOf(qty[0]));
        });

        qtyPanel.add(minus);
        qtyPanel.add(qtyLabel);
        qtyPanel.add(plus);

        // product description
        JTextArea desc = new JTextArea(product.getDescription());
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);

        center.add(name);
        center.add(Box.createVerticalStrut(10));
        center.add(price);
        center.add(Box.createVerticalStrut(10));
        center.add(qtyPanel);
        center.add(Box.createVerticalStrut(10));
        center.add(desc);

        panel.add(center, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(350, 450));

        int choice = JOptionPane.showOptionDialog(
                this,
                scrollPane,
                "Product Details",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{"Add to Cart", "Close"},
                "Add to Cart"
        );

        // adds selected quantity to cart
        if (choice == 0) {
            cart.put(product, cart.getOrDefault(product, 0) + qty[0]);
            JOptionPane.showMessageDialog(this, qty[0] + " item(s) added!");
        }
    }

    private void showCart() {

        // creates popup for cart
        JDialog dialog = new JDialog(this, currentUser.getUsername() + "'s Cart", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        // main panel to hold cart items
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane scroll = new JScrollPane(panel);
        dialog.add(scroll);

        // refresh method updates cart whenever changes happen
        final Runnable[] refresh = new Runnable[1];

        refresh[0] = () -> {

            panel.removeAll();

            double total = 0;

            // loop through all items in cart
            for (Product p : cart.keySet()) {
                int qty = cart.get(p);

                JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER));

                JLabel name = new JLabel(p.getProductName() + " (€" + p.getPrice() + ")");
                JLabel quantity = new JLabel("Qty: " + qty);

                JButton plus = new JButton("+");
                JButton minus = new JButton("-");
                JButton remove = new JButton("Remove");

                // increase quantity
                plus.addActionListener(e -> {
                    cart.put(p, cart.get(p) + 1);
                    refresh[0].run();
                });

                // decrease quantity
                minus.addActionListener(e -> {
                    if (cart.get(p) > 1) {
                        cart.put(p, cart.get(p) - 1);
                    }
                    refresh[0].run();
                });

                // remove item from cart
                remove.addActionListener(e -> {
                    cart.remove(p);
                    refresh[0].run();
                });

                row.add(name);
                row.add(minus);
                row.add(quantity);
                row.add(plus);
                row.add(remove);

                panel.add(row);

                // calculate total price
                total += p.getPrice() * qty;
            }

            // display total
            JLabel totalLabel = new JLabel("Total: €" + String.format("%.2f", total));
            totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            totalLabel.setFont(new Font("Arial", Font.BOLD, 16));

            // checkout button
            JButton checkout = new JButton("Checkout");
            checkout.setAlignmentX(Component.CENTER_ALIGNMENT);
            checkout.setBackground(new Color(255, 153, 0));
            checkout.setForeground(Color.WHITE);

            double finalTotal = total;

            // checkout process payment and database update
            checkout.addActionListener(e -> {

                // payment input
                JTextField cardNameField = new JTextField();
                JTextField cardNumberField = new JTextField("1234-5678-9012-3456");
                JTextField cvvField = new JTextField();

                // placeholder styling for card number
                cardNumberField.setForeground(Color.GRAY);

                cardNumberField.addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusGained(java.awt.event.FocusEvent e) {
                        if (cardNumberField.getText().equals("1234-5678-9012-3456")) {
                            cardNumberField.setText("");
                            cardNumberField.setForeground(Color.BLACK);
                        }
                    }

                    public void focusLost(java.awt.event.FocusEvent e) {
                        if (cardNumberField.getText().isEmpty()) {
                            cardNumberField.setText("1234-5678-9012-3456");
                            cardNumberField.setForeground(Color.GRAY);
                        }
                    }
                });

                // formats card number
                cardNumberField.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyReleased(java.awt.event.KeyEvent e) {

                        String text = cardNumberField.getText().replaceAll("-", "").replaceAll("\\D", "");

                        if (text.length() > 16) {
                            text = text.substring(0, 16);
                        }

                        StringBuilder formatted = new StringBuilder();
                        for (int i = 0; i < text.length(); i++) {
                            if (i > 0 && i % 4 == 0) formatted.append("-");
                            formatted.append(text.charAt(i));
                        }

                        cardNumberField.setText(formatted.toString());
                    }
                });

                // CVV is only 3 digits
                cvvField.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyReleased(java.awt.event.KeyEvent e) {
                        String text = cvvField.getText().replaceAll("\\D", "");
                        if (text.length() > 3) text = text.substring(0, 3);
                        cvvField.setText(text);
                    }
                });

                // payment form panel with name number and cvv
                JPanel paymentPanel = new JPanel(new GridLayout(6, 1, 5, 5));

                paymentPanel.add(new JLabel("Card Name:"));
                paymentPanel.add(cardNameField);

                paymentPanel.add(new JLabel("Card Number:"));
                paymentPanel.add(cardNumberField);

                paymentPanel.add(new JLabel("CVV:"));
                paymentPanel.add(cvvField);

                int result = JOptionPane.showConfirmDialog(
                        this,
                        paymentPanel,
                        "Payment Details",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                // if user cancels payment
                if (result != JOptionPane.OK_OPTION) {
                    JOptionPane.showMessageDialog(this, "Payment cancelled");
                    return;
                }

                String cardName = cardNameField.getText().trim();
                String cardNumber = cardNumberField.getText().replace("-", "");
                String cvv = cvvField.getText().trim();

                // validation
                if (cardName.isEmpty() || cardNumber.length() != 16 || cvv.length() != 3) {
                    JOptionPane.showMessageDialog(this, "Invalid payment details");
                    return;
                }

                try {

                    // DAO objects for checkout process
                    InvoiceDAO invoiceDAO = new InvoiceDAO();
                    InvoiceItemDAO itemDAO = new InvoiceItemDAO();
                    ProductDAO productDAO = new ProductDAO();

                    int userId = currentUser.getUserId();

                    // create invoice
                    Integer invoiceId = invoiceDAO.createInvoice(userId);

                    // add each item to invoice and reduces stock
                    for (Product p : cart.keySet()) {
                        int qty = cart.get(p);

                        itemDAO.addItem(invoiceId, p.getProductId(), qty);
                        productDAO.reduceStock(p.getProductId(), qty);
                    }

                    // update total price
                    invoiceDAO.updateTotal(invoiceId, finalTotal);

                    // receipt
                    StringBuilder receipt = new StringBuilder();
                    receipt.append("---RECEIPT---\n");
                    receipt.append("Customer: ").append(currentUser.getUsername()).append("\n");
                    receipt.append("User ID: ").append(currentUser.getUserId()).append("\n\n");

                    for (Product p : cart.keySet()) {
                        int qty = cart.get(p);
                        receipt.append(p.getProductName())
                                .append(" x").append(qty)
                                .append(" €").append(p.getPrice() * qty)
                                .append("\n");
                    }

                    receipt.append("\nTOTAL: €").append(String.format("%.2f", finalTotal));

                    JOptionPane.showMessageDialog(this, receipt.toString());

                    // clear cart and refresh
                    cart.clear();
                    dialog.dispose();
                    loadProducts();

                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Checkout error: " + ex.getMessage());
                }
            });

            panel.add(Box.createVerticalStrut(10));
            panel.add(totalLabel);
            panel.add(checkout);

            panel.revalidate();
            panel.repaint();
        };

        // run refresh to load cart items
        refresh[0].run();
        dialog.setVisible(true);
    }

    // loads and scales product image
    private Icon loadProductImage(Product p, int w, int h) {
        try {
            ImageIcon icon = new ImageIcon(p.getImagePath());
            Image scaled = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            return UIManager.getIcon("OptionPane.informationIcon");
        }
    }
}