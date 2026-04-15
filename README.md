# Smart Expense Tracker – Backend (Spring Boot)

The REST API backend for ExpenseIQ, a full-stack personal finance tracker. Built with Spring Boot and Java 21, it handles expense management, budget tracking, and spending analytics.

## Features

- Full **CRUD** for expenses with Bean Validation (`@NotBlank`, `@Positive`)
- **Budget management** — set and track monthly spending limits per category
- **Monthly stats** endpoint — returns last N months of spending, zero-filled for chart continuity
- **Category summary** — spending breakdown by category for any given month
- **Date range filtering** and monthly total aggregation
- **Global exception handler** — clean JSON error responses for validation failures
- **Persistent H2 database** — data survives server restarts
- CORS enabled for local React frontend

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Validation (Bean Validation)
- H2 Database (file-based)
- Maven

## API Endpoints

Base URL: `http://localhost:9090/api`

### Expenses

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/expenses` | Get all expenses |
| `POST` | `/expenses` | Create a new expense |
| `PUT` | `/expenses/{id}` | Update an expense |
| `DELETE` | `/expenses/{id}` | Delete an expense |
| `GET` | `/expenses/total-month?year=&month=` | Monthly spending total |
| `GET` | `/expenses/category-summary?year=&month=` | Spending breakdown by category |
| `GET` | `/expenses/monthly-stats?months=6` | Last N months of totals (zero-filled) |
| `GET` | `/expenses/range?start=&end=` | Filter by date range |

### Budgets

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/budgets` | Get all budgets |
| `PUT` | `/budgets` | Create or update a budget (upsert by category) |
| `DELETE` | `/budgets/{id}` | Remove a budget |

## Running Locally

```bash
mvn spring-boot:run
```

Server starts on `http://localhost:9090`. H2 console available at `http://localhost:9090/h2-console`.

## Project Structure

```
src/main/java/com/example/expense/
 ├─ controller/
 │   ├─ ExpenseController.java
 │   ├─ BudgetController.java
 │   └─ GlobalExceptionHandler.java
 ├─ model/
 │   ├─ Expense.java
 │   ├─ Budget.java
 │   └─ CategorySummary.java
 └─ repository/
     ├─ ExpenseRepository.java
     └─ BudgetRepository.java
```
