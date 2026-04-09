// Name : Ryan Lowry
// Date : 02/04/2026
// File : InvoiceDAO
package database;

import model.Invoice;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    public Integer createInvoice(int userId) throws Exception {
        // user_id is passed in, date is set automatically, total starts at 0, status is PENDING
        String sql = "INSERT INTO invoice (user_id, invoice_date, total_amount, status) " +
                "VALUES (?, CURRENT_DATE(), 0, 'PENDING')";

        // connection to the database
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); // RETURN_GENERATED_KEYS lets us get the auto-created invoice_id after insert

        // set user id into the query
        ps.setInt(1, userId);

        ps.executeUpdate();


        ResultSet keys = ps.getGeneratedKeys();
        Integer invoiceId = null;

        if (keys.next()) {
            invoiceId = keys.getInt(1);
        }

        keys.close();
        ps.close();
        conn.close();

        return invoiceId;
    }

    // UPDATE TOTAL
    public int updateTotal(int invoiceId, double newTotal) throws Exception {

        // SQL to update total
        String sql = "UPDATE invoice SET total_amount=? WHERE invoice_id=?";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        // set new total and invoice id
        ps.setDouble(1, newTotal);
        ps.setInt(2, invoiceId);

        int rows = ps.executeUpdate();

        ps.close();
        conn.close();

        return rows;
    }

    // UPDATE STATUS (ADMIN)
    public int updateStatus(int invoiceId, String status) throws Exception {

        String sql = "UPDATE invoice SET status=? WHERE invoice_id=?";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        // set new status and invoice id
        ps.setString(1, status);
        ps.setInt(2, invoiceId);

        int rows = ps.executeUpdate();

        ps.close();
        conn.close();

        return rows;
    }

    // READ ALL INVOICES
    public List<Invoice> listAllInvoicesJoinCustomer() throws Exception {

        List<Invoice> invoices = new ArrayList<>();

        String sql =
                "SELECT invoice_id, user_id, invoice_date, total_amount, status " +
                        "FROM invoice " +
                        "ORDER BY invoice_id DESC";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            // loop through results and build an Invoice object for each row
            Invoice inv = new Invoice(
                    rs.getInt("invoice_id"),
                    0,
                    rs.getInt("user_id"),
                    rs.getDate("invoice_date"),
                    rs.getDouble("total_amount")
            );
            inv.setStatus(rs.getString("status"));
            invoices.add(inv);
        }

        rs.close();
        ps.close();
        conn.close();

        return invoices;
    }
}