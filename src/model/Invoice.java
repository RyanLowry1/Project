// Name : Ryan Lowry
// Date : 02/04/2026
// File : Invoice.java
package model;

import java.sql.Date;

public class Invoice {

    // stores basic invoice info from the database
    private int invoiceId;
    private int customerId;
    private int userId;
    private Date invoiceDate;
    private double totalAmount;

    // used for order status (PENDING, CONFIRMED)
    private String status;

    public Invoice() {}

    // used when reading full invoice from database
    public Invoice(int invoiceId, int customerId, int userId, Date invoiceDate, double totalAmount) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.userId = userId;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
    }

    // used when creating new invoice object
    public Invoice(int customerId, int userId, Date invoiceDate, double totalAmount) {
        this.customerId = customerId;
        this.userId = userId;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
    }

    // GETTERS

    public int getInvoiceId() {
        return invoiceId;
    }

    public int getUserId() {
        return userId;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    // SETTERS

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", userId=" + userId +
                ", invoiceDate=" + invoiceDate +
                ", totalAmount=" + totalAmount +
                '}';
    }
}