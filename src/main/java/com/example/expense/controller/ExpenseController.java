package com.example.expense.controller;

import com.example.expense.model.Expense;
import com.example.expense.repository.ExpenseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;

    public ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense) {
        if (expense.getDate() == null) {
            expense.setDate(LocalDate.now());
        }
        Expense saved = expenseRepository.save(expense);
        return ResponseEntity.ok(saved);
    }

    // READ ALL
    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpense(@PathVariable long id) {
        Optional<Expense> expense = expenseRepository.findById(id);
        return expense.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable long id,
            @RequestBody Expense updated) {
        return expenseRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(updated.getTitle());
                    existing.setCategory(updated.getCategory());
                    existing.setAmount(updated.getAmount());
                    existing.setDate(updated.getDate());
                    existing.setNotes(updated.getNotes());
                    Expense saved = expenseRepository.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable long id) {
        if (!expenseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        expenseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // FILTER BY DATE RANGE
    @GetMapping("/range")
    public List<Expense> getByDateRange(@RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return expenseRepository.findByDateBetween(start, end);
    }

    // MONTHLY TOTAL
    @GetMapping("/total-month")
    public BigDecimal getMonthlyTotal(@RequestParam int year,
            @RequestParam int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return expenseRepository.sumAmountByDateRange(start, end);
    }
}
