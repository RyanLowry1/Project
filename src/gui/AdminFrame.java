// Name : Ryan Lowry
// Date : 02/04/2026
// File : AdminFrame.java
package gui;

import database.ProductDAO;
import database.InvoiceDAO;
import model.Invoice;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class AdminFrame extends JFrame {

    // table and model to display products
    DefaultTableModel model;
    JTable table;

    // input fields for product details
    JTextField txtName = new JTextField();
    JTextField txtPrice = new JTextField();
    JTextField txtQty = new JTextField();
    JTextArea txtDesc = new JTextArea();

    // buttons
    JButton btnAdd = new JButton("Add");
    JButton btnUpdate = new JButton("Update");
    JButton btnDelete = new JButton("Delete");

    // image selection button
    JButton btnChooseImage = new JButton("Choose Image");
    JLabel lblImagePath = new JLabel("No image selected");

    // view orders button
    JButton btnOrders = new JButton("View Orders");

    // stores selected image path
    private String selectedImagePath = null;

    // DAO for database operations
    ProductDAO dao = new ProductDAO();

    public AdminFrame() {

        setTitle("Admin Panel");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // main container panel
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        setContentPane(root);

        // title at the top of the screen
        JLabel title = new JLabel("Admin - Product Management");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        root.add(title, BorderLayout.NORTH);

        // table setup to show all products
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Price");
        model.addColumn("Qty");
        model.addColumn("Description");
        model.addColumn("Image Path");

        table = new JTable(model);
        table.setRowHeight(25);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        root.add(tableScroll, BorderLayout.CENTER);

        // input form for adding and updating products
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(new JLabel("Product Name:"));
        formPanel.add(txtName);

        formPanel.add(new JLabel("Price:"));
        formPanel.add(txtPrice);

        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(txtQty);

        formPanel.add(new JLabel("Description:"));
        formPanel.add(new JScrollPane(txtDesc));

        formPanel.add(btnChooseImage);
        formPanel.add(lblImagePath);

        // buttons for actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        styleButton(btnAdd);
        styleButton(btnUpdate);
        styleButton(btnDelete);
        styleButton(btnOrders);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnOrders);

        // combines form and buttons at the bottom
        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(Color.WHITE);
        south.add(formPanel, BorderLayout.CENTER);
        south.add(buttonPanel, BorderLayout.SOUTH);

        root.add(south, BorderLayout.SOUTH);

        // choose image button
        btnChooseImage.addActionListener(e -> chooseImage());

        // when a row is selected, fill input fields
        table.getSelectionModel().addListSelectionListener(e -> fillFields());

        btnAdd.addActionListener(e -> addProduct());
        btnUpdate.addActionListener(e -> updateProduct());
        btnDelete.addActionListener(e -> deleteProduct());

        // view orders
        btnOrders.addActionListener(e -> showOrders());

        loadTable();
        setVisible(true);
    }

    // styles buttons
    private void styleButton(JButton btn) {
        btn.setBackground(new Color(255, 153, 0));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // adds a new product to the database
    private void addProduct() {
        try {
            dao.addProduct(
                    txtName.getText(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtQty.getText()),
                    txtDesc.getText(),
                    selectedImagePath
            );

            loadTable();
            clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding product");
        }
    }

    // updates selected product
    private void updateProduct() {
        try {
            int id = Integer.parseInt(model.getValueAt(table.getSelectedRow(), 0).toString());

            dao.updateProductFull(
                    id,
                    txtName.getText(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtQty.getText()),
                    txtDesc.getText(),
                    selectedImagePath
            );

            loadTable();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating");
        }
    }

    // deletes selected product
    private void deleteProduct() {
        try {
            int id = Integer.parseInt(model.getValueAt(table.getSelectedRow(), 0).toString());
            dao.deleteProduct(id);
            loadTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting");
        }
    }

    // loads all products into the table
    private void loadTable() {
        try {
            model.setRowCount(0);
            List<Product> list = dao.getAllProducts();

            for (Product p : list) {
                model.addRow(new Object[]{
                        p.getProductId(),
                        p.getProductName(),
                        p.getPrice(),
                        p.getStock(),
                        p.getDescription(),
                        p.getImagePath()
                });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // fills input fields when a table row is clicked
    private void fillFields() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtName.setText(model.getValueAt(row, 1).toString());
            txtPrice.setText(model.getValueAt(row, 2).toString());
            txtQty.setText(model.getValueAt(row, 3).toString());
            txtDesc.setText(model.getValueAt(row, 4).toString());

            selectedImagePath = model.getValueAt(row, 5).toString();
            lblImagePath.setText(selectedImagePath);
        }
    }

    // clears input fields after adding product
    private void clearFields() {
        txtName.setText("");
        txtPrice.setText("");
        txtQty.setText("");
        txtDesc.setText("");
        selectedImagePath = null;
    }

    // opens file chooser to select product image
    private void chooseImage() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            selectedImagePath = file.getAbsolutePath();
            lblImagePath.setText(selectedImagePath);
        }
    }

    // updates order status
    private void updateOrder(JList<String> list, InvoiceDAO dao, String status) {
        try {
            String selected = list.getSelectedValue();

            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select an order first");
                return;
            }

            int id = Integer.parseInt(selected.split("#")[1].split(" ")[0]);

            dao.updateStatus(id, status);

            JOptionPane.showMessageDialog(this, "Order updated to " + status);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating order");
        }
    }

    // marks order as completed and removes it from list
    private void completeOrder(JList<String> list, DefaultListModel<String> model, InvoiceDAO dao) {
        try {
            int index = list.getSelectedIndex();

            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Select an order first");
                return;
            }

            String selected = model.getElementAt(index);

            int id = Integer.parseInt(selected.split("#")[1].split(" ")[0]);

            dao.updateStatus(id, "COMPLETED");

            model.remove(index);

            JOptionPane.showMessageDialog(this, "Order marked as COMPLETED");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error completing order");
        }
    }

    // shows all orders
    private void showOrders() {

        try {
            InvoiceDAO dao = new InvoiceDAO();
            List<Invoice> orders = dao.listAllInvoicesJoinCustomer();

            JDialog dialog = new JDialog(this, "Orders", true);
            dialog.setSize(600, 400);
            dialog.setLayout(new BorderLayout());

            DefaultListModel<String> model = new DefaultListModel<>();

            for (Invoice inv : orders) {
                model.addElement(
                        "Invoice #" + inv.getInvoiceId() +
                                " | User: " + inv.getUserId() +
                                " | " + inv.getInvoiceDate() +
                                " | €" + inv.getTotalAmount() +
                                " | " + inv.getStatus()
                );
            }

            JList<String> list = new JList<>(model);
            dialog.add(new JScrollPane(list), BorderLayout.CENTER);

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

            JButton confirm = new JButton("Confirm");
            JButton delivery = new JButton("Out for Delivery");
            JButton complete = new JButton("Complete");

            styleButton(confirm);
            styleButton(delivery);
            styleButton(complete);

            panel.add(confirm);
            panel.add(delivery);
            panel.add(complete);

            dialog.add(panel, BorderLayout.SOUTH);
            // button actions for updating order status
            complete.addActionListener(e -> completeOrder(list, model, dao));
            confirm.addActionListener(e -> updateOrder(list, dao, "CONFIRMED"));
            delivery.addActionListener(e -> updateOrder(list, dao, "OUT FOR DELIVERY"));

            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);

        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading orders");
        }
    }
}