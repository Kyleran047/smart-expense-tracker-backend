package com.example.expense.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // e.g. "Groceries"
    private String category; // e.g. "Food", "Rent", "Transport"

    @Column(precision = 12, scale = 2)
    private BigDecimal amount;

    private LocalDate date; // when the money was spent

    private String notes;

    public Expense() {
    }

    public Expense(String title, String category, BigDecimal amount, LocalDate date, String notes) {
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.notes = notes;
    }

    // --- Getters & setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
