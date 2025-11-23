```md
# Smart Expense Tracker – Backend (Spring Boot)

This is the Spring Boot backend for my Smart Expense Tracker project.  
It exposes REST APIs for managing expenses and computing monthly totals.

## Features

- Create, read, update and delete expenses
- Filter expenses by **date range**
- Compute **monthly total** for a given year and month
- CORS enabled so the React frontend can call the API
- Uses an in-memory H2 database (easy to run locally)

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- H2 Database
- Maven

## Main Endpoints

Base URL (local):

```text
http://localhost:9090/api/expenses
