# 📘 Version 1 Feature Overview

## ✨ Overview
Version 1 focuses on building the foundational structure and user interface flow of the application. It implements core screen transitions and visual layout, setting the stage for future functionality.

## 🖼️ Screens and Navigation

### 1. Login Page (`LoginPage.java`)
- Basic user login interaction
- Navigates to main dashboard upon entering a username

![1745135181999](https://github.com/user-attachments/assets/f9635a79-9cea-496b-b4a5-9f2d7c66b652)




---

### 2. Main Page (`MainPage.java`)
- Central navigation hub of the app
- Buttons lead to History, Charts, and Saving Goal modules

![1745135251408](https://github.com/user-attachments/assets/578b4cce-5982-4366-95b3-3dc8c70f5366)


---

### 3. History Page (`HistoryPage.java`)
- Displays a static or CSV-loaded list of transactions
- Allows switching between months




---

### 4. Chart Page (`ChartPage.java`)
- Displays spending trends in a graphical format (using preset images)

![1745135324110](https://github.com/user-attachments/assets/ff7dfb59-b962-46df-8188-e35fea7caf6c)


---

### 5. Saving Goal Page (`SavingGoalPage.java`)
- Displays savings progress, goals, and financial tips
- Includes modular components like tips, emergency savings, and progress bars



<!--  -->

## 🗂️ Technical Highlights
- UI built with Java Swing components
- Static transaction data from CSV files
- Navigation logic handled via button actions

## 🚧 Limitations
- Static interaction only; no real-time updates
- No transaction classification or smart analytics

## ✅ Version 1 (User Story)

- **Story 1 - Data Import**: Supports importing CSV and JSON files from banking apps and parsing financial data automatically.
- **Story 2 - Categorize Correctly**: Automatically categorizes transactions, allows manual adjustments, and exports categorized charts.
- **Story 3 - Budget Recommendations**: AI generates personalized monthly budgets based on income and spending habits; manual adjustments supported.
- **Story 5 - Over-budget Alert**: Monitor expenditures, alert users when the budget is exceeded, and provide improvement suggestions based on the users' spending habits.
- **Story 7 - Abnormal Spending Detection**: Establish a baseline based on user spending patterns to detect unusual transactions (e.g., large purchases or uncommon locations) and send alerts.
- **Story 11 - Money-Saving Tips**：Provide personalized, clear, and user-friendly money-saving tips and strategies, with explanations and accessible design to help users optimize savings.
