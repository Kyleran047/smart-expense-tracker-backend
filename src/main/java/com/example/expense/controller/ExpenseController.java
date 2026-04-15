package com.example.expense.controller;

import com.example.expense.model.CategorySummary;
import com.example.expense.model.Expense;
import com.example.expense.repository.ExpenseRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody Expense expense) {
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
        return expenseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable long id,
            @Valid @RequestBody Expense updated) {
        return expenseRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(updated.getTitle());
                    existing.setCategory(updated.getCategory());
                    existing.setAmount(updated.getAmount());
                    existing.setDate(updated.getDate());
                    existing.setNotes(updated.getNotes());
                    return ResponseEntity.ok(expenseRepository.save(existing));
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

    // CATEGORY SUMMARY FOR A MONTH
    @GetMapping("/category-summary")
    public List<CategorySummary> getCategorySummary(@RequestParam int year,
            @RequestParam int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return expenseRepository.categoryTotals(start, end)
                .stream()
                .map(row -> new CategorySummary((String) row[0], (BigDecimal) row[1]))
                .collect(Collectors.toList());
    }

    // MONTHLY STATS — last N months, zero-filled
    @GetMapping("/monthly-stats")
    public List<Map<String, Object>> getMonthlyStats(
            @RequestParam(defaultValue = "6") int months) {

        LocalDate from = LocalDate.now().minusMonths(months - 1).withDayOfMonth(1);
        LocalDate to = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        // Fetch all expenses in range and group in Java (portable, no DB-specific functions)
        List<Expense> expenses = expenseRepository.findByDateBetween(from, to);

        Map<String, BigDecimal> totals = new LinkedHashMap<>();
        // Pre-fill every month with zero so there are no gaps
        for (int i = months - 1; i >= 0; i--) {
            LocalDate d = LocalDate.now().minusMonths(i);
            totals.put(d.getYear() + "-" + d.getMonthValue(), BigDecimal.ZERO);
        }

        for (Expense e : expenses) {
            if (e.getDate() != null) {
                String key = e.getDate().getYear() + "-" + e.getDate().getMonthValue();
                if (totals.containsKey(key)) {
                    totals.merge(key, e.getAmount(), BigDecimal::add);
                }
            }
        }

        return totals.entrySet().stream().map(entry -> {
            String[] parts = entry.getKey().split("-");
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("year", Integer.parseInt(parts[0]));
            m.put("month", Integer.parseInt(parts[1]));
            m.put("total", entry.getValue());
            return m;
        }).collect(Collectors.toList());
    }
}
