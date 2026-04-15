package com.example.expense.controller;

import com.example.expense.model.Budget;
import com.example.expense.repository.BudgetRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@CrossOrigin(origins = "*")
public class BudgetController {

    private final BudgetRepository budgetRepository;

    public BudgetController(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @GetMapping
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    // Upsert: create or update the budget for a given category
    @PutMapping
    public Budget upsertBudget(@Valid @RequestBody Budget budget) {
        return budgetRepository.findByCategory(budget.getCategory())
                .map(existing -> {
                    existing.setMonthlyLimit(budget.getMonthlyLimit());
                    return budgetRepository.save(existing);
                })
                .orElseGet(() -> budgetRepository.save(budget));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        if (!budgetRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        budgetRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
