# Smart Expense Tracker with Budget Alerts

A medium-level **Spring Boot REST API backend** project for tracking personal income, expenses, monthly budgets, and sending automatic Gmail alerts when a user's monthly expense crosses the budget limit.

This project is built as a backend-first REST API and is tested using **Postman**. It uses **MongoDB** as the local database and **JWT authentication** for securing APIs.

---

## Features

### Authentication
- User registration
- User login
- JWT token generation
- Protected APIs using Bearer Token
- Password encryption using BCrypt

### Expense Management
- Add expense
- View all expenses of logged-in user
- View expense by ID
- Update expense
- Delete expense
- Expense categories using enum

### Income Management
- Add income
- View all income records of logged-in user
- View income by ID
- Update income
- Delete income
- Income categories using enum

### Budget Management
- Set monthly budget
- View all budgets
- Update budget
- Delete budget
- Check monthly budget status
- One budget per user per month/year

### Gmail Budget Alert
- Sends an email alert when monthly expenses cross the budget
- Uses Gmail SMTP
- Sends alert to the logged-in user's registered email
- Prevents repeated email spam using `alertSent`

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot | Backend framework |
| Spring Web | REST API development |
| Spring Security | API security |
| JWT | Token-based authentication |
| Spring Data MongoDB | MongoDB integration |
| MongoDB | Local database |
| Lombok | Reduces boilerplate code |
| Jakarta Validation | Request validation |
| Java Mail Sender | Sending Gmail alerts |
| Postman | API testing |
| Maven | Dependency management |

---

## Project Type

This is a **REST API backend project**.

There is no frontend website in this version. All APIs are tested using Postman.

---

## Project Structure

```text
com.expensetracker
│
├── config
│   └── SecurityConfig.java
│
├── controller
│   ├── AuthController.java
│   ├── ExpenseController.java
│   ├── IncomeController.java
│   └── BudgetController.java
│
├── dto
│   ├── request
│   │   ├── RegisterRequest.java
│   │   ├── LoginRequest.java
│   │   ├── ExpenseRequest.java
│   │   ├── IncomeRequest.java
│   │   └── BudgetRequest.java
│   │
│   └── response
│       ├── ApiResponse.java
│       ├── AuthResponse.java
│       ├── ExpenseResponse.java
│       ├── IncomeResponse.java
│       ├── BudgetResponse.java
│       └── BudgetStatusResponse.java
│
├── model
│   ├── User.java
│   ├── Expense.java
│   ├── Income.java
│   └── Budget.java
│
├── repository
│   ├── UserRepository.java
│   ├── ExpenseRepository.java
│   ├── IncomeRepository.java
│   └── BudgetRepository.java
│
├── service
│   ├── AuthService.java
│   ├── ExpenseService.java
│   ├── IncomeService.java
│   ├── BudgetService.java
│   └── EmailService.java
│
├── service.impl
│   ├── AuthServiceImpl.java
│   ├── ExpenseServiceImpl.java
│   ├── IncomeServiceImpl.java
│   ├── BudgetServiceImpl.java
│   └── EmailServiceImpl.java
│
├── security
│   ├── JwtService.java
│   ├── JwtAuthenticationFilter.java
│   ├── CustomUserDetailsService.java
│   └── UserPrincipal.java
│
├── exception
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
│
└── enums
    ├── Role.java
    ├── ExpenseCategory.java
    └── IncomeCategory.java
```

---

## How the Project Works

### 1. User Registration

A new user registers using name, email, and password.

The password is encrypted using BCrypt before saving it into MongoDB.

```text
POST /api/auth/register
```

---

### 2. User Login

The user logs in using email and password.

If the credentials are correct, the backend generates a JWT token.

```text
POST /api/auth/login
```

The JWT token is used to access protected APIs.

---

### 3. Access Protected APIs

All APIs except `/api/auth/**` require JWT authentication.

In Postman, use:

```text
Authorization → Bearer Token → paste JWT token
```

Protected APIs include:

```text
/api/expenses
/api/income
/api/budget
```

---

### 4. Expense Flow

When a user adds an expense:

1. JWT token identifies the logged-in user.
2. Expense is saved with that user's ID.
3. Spring Boot checks if a budget exists for the expense month and year.
4. If total monthly expenses cross the budget, an email alert is sent.

---

### 5. Budget Alert Flow

```text
User sets monthly budget
        ↓
User adds expense for the same month
        ↓
Backend calculates total monthly expense
        ↓
If total expense > budget amount
        ↓
Gmail alert is sent
        ↓
alertSent becomes true
```

The `alertSent` field prevents repeated emails for the same month/year budget.

---

## API Endpoints

## Auth APIs

### Register User

```http
POST /api/auth/register
```

Request body:

```json
{
  "name": "Ashutosh",
  "email": "user@gmail.com",
  "password": "123456"
}
```

Response:

```json
{
  "success": true,
  "message": "User registered successfully"
}
```

---

### Login User

```http
POST /api/auth/login
```

Request body:

```json
{
  "email": "user@gmail.com",
  "password": "123456"
}
```

Response:

```json
{
  "message": "Login successful",
  "token": "jwt-token-here",
  "userId": "user-id",
  "name": "Ashutosh",
  "email": "user@gmail.com"
}
```

---

## Expense APIs

All expense APIs require Bearer Token.

### Add Expense

```http
POST /api/expenses
```

Request body:

```json
{
  "title": "Lunch",
  "amount": 150,
  "category": "FOOD",
  "note": "College canteen",
  "expenseDate": "2026-07-05"
}
```

---

### Get All Expenses

```http
GET /api/expenses
```

---

### Get Expense By ID

```http
GET /api/expenses/{expenseId}
```

---

### Update Expense

```http
PUT /api/expenses/{expenseId}
```

Request body:

```json
{
  "title": "Dinner",
  "amount": 300,
  "category": "FOOD",
  "note": "Restaurant",
  "expenseDate": "2026-07-05"
}
```

---

### Delete Expense

```http
DELETE /api/expenses/{expenseId}
```

---

## Income APIs

All income APIs require Bearer Token.

### Add Income

```http
POST /api/income
```

Request body:

```json
{
  "title": "Monthly Salary",
  "amount": 25000,
  "category": "SALARY",
  "note": "July salary",
  "incomeDate": "2026-07-05"
}
```

---

### Get All Income

```http
GET /api/income
```

---

### Get Income By ID

```http
GET /api/income/{incomeId}
```

---

### Update Income

```http
PUT /api/income/{incomeId}
```

---

### Delete Income

```http
DELETE /api/income/{incomeId}
```

---

## Budget APIs

All budget APIs require Bearer Token.

### Set Budget

```http
POST /api/budget
```

Request body:

```json
{
  "month": 7,
  "year": 2026,
  "amount": 10000
}
```

Important: If a budget already exists for the same user, month, and year, it updates the old budget instead of creating a duplicate.

---

### Get All Budgets

```http
GET /api/budget
```

---

### Get Budget Status

```http
GET /api/budget/status?month=7&year=2026
```

Example response:

```json
{
  "month": 7,
  "year": 2026,
  "budgetAmount": 10000,
  "totalExpense": 12000,
  "remainingAmount": -2000,
  "budgetExceeded": true,
  "percentageUsed": 120.00,
  "message": "Budget crossed! You have spent more than your monthly budget."
}
```

---

### Update Budget

```http
PUT /api/budget/{budgetId}
```

Request body:

```json
{
  "month": 7,
  "year": 2026,
  "amount": 15000
}
```

---

### Delete Budget

```http
DELETE /api/budget/{budgetId}
```

---

## Enums Used

### Expense Categories

```java
FOOD,
TRAVEL,
SHOPPING,
RENT,
BILLS,
EDUCATION,
HEALTH,
ENTERTAINMENT,
OTHER
```

### Income Categories

```java
SALARY,
FREELANCE,
BUSINESS,
GIFT,
INVESTMENT,
OTHER
```

Category values must match the enum exactly.

Example:

```json
"category": "FOOD"
```

Lowercase values like `"food"` are not accepted.

---

## MongoDB Configuration

The project uses local MongoDB.

Example configuration:

```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=smart_expense_tracker
```

MongoDB creates the database automatically when the first document is saved.

---

## Gmail SMTP Configuration

The project uses Gmail SMTP to send budget alerts.

Example configuration:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_sender_gmail@gmail.com
spring.mail.password=your_gmail_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

Important:

```text
spring.mail.username = sender email
registered user email = receiver email
```

Example:

```text
From: your_sender_gmail@gmail.com
To: registereduser@gmail.com
```

Use a Gmail App Password, not your normal Gmail password.

---

## Security Warning

Do not push real Gmail passwords or app passwords to GitHub.

For local testing, the password may be kept in `application.properties`, but for a safer setup use environment variables:

```properties
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
```

---

## How to Run the Project

### 1. Clone the repository

```bash
git clone <your-repository-url>
```

### 2. Open the project in IntelliJ IDEA

Open the project folder as a Maven project.

### 3. Start MongoDB locally

Make sure MongoDB is running on:

```text
localhost:27017
```

### 4. Configure `application.properties`

Add MongoDB, JWT, and Gmail SMTP settings.

### 5. Run the application

Run:

```text
SmartExpenseTrackerApplication.java
```

Application starts on:

```text
http://localhost:8080
```

---

## Postman Testing Flow

Recommended testing order:

```text
1. Register user
2. Login user
3. Copy JWT token
4. Add income
5. Add budget
6. Add expense
7. Check budget status
8. Add expense crossing budget
9. Check Gmail alert
```

---

## Completed Features

```text
MongoDB connection
User registration
User login
JWT authentication
Protected APIs
Expense CRUD
Income CRUD
Budget CRUD
Budget status check
Gmail SMTP budget alert
```

---

## Future Improvements

Planned improvements:

```text
Dashboard monthly summary API
PDF monthly report generation
Swagger API documentation
Environment variables for secrets
Better exception handling
Pagination and filtering
Category-wise expense summary
Case-insensitive enum handling
Frontend integration
```

Suggested future dashboard endpoint:

```http
GET /api/dashboard/summary?month=7&year=2026
```

Expected response:

```json
{
  "month": 7,
  "year": 2026,
  "totalIncome": 25000,
  "totalExpense": 12000,
  "balance": 13000,
  "budgetAmount": 10000,
  "budgetExceeded": true,
  "expenseCount": 5,
  "incomeCount": 2
}
```

---

## Project Status

Current status:

```text
Backend core features completed and tested using Postman.
Gmail budget alert successfully tested with real email.
```
