// Name : Ryan Lowry
// Date : 02/04/2026
// File : InvoiceItemDAO
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class InvoiceItemDAO {

    public int addItem(int invoiceId, int productId, int quantity) throws Exception {
        // SQL to insert a new item into the invoiceitem table
        String sql = "INSERT INTO invoiceitem (invoice_id, product_id, quantity) VALUES (?, ?, ?)";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        // replace the ? placeholders with actual values
        ps.setInt(1, invoiceId);
        ps.setInt(2, productId);
        ps.setInt(3, quantity);

        int rows = ps.executeUpdate();

        ps.close();
        conn.close();

        return rows;
    }
}