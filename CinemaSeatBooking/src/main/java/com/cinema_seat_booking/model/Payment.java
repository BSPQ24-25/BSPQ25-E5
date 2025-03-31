
package com.cinema_seat_booking.model;
import jakarta.persistence.*;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
    
    // No-argument constructor (needed for JPA)
    public Payment() {
        // This constructor is required by JPA
    }

    public Payment(String paymentMethod, double amount, String paymentDate, PaymentStatus status) {
        super();
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

	public Payment(String paymentMethod, double amount, String paymentDate) {
		this.paymentMethod = paymentMethod;
		this.amount = amount;
		this.paymentDate = paymentDate;
	}
}

