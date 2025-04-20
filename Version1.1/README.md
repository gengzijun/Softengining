# 🤖 Version 1.1 Feature Overview

## ✨ Overview
Version 1.1 builds on the UI foundation by introducing a basic AI-powered classification system. Transactions are now automatically tagged with relevant categories, streamlining the data organization process.

## 🧠 AI Classification Feature

### 1. Module Entry
- Implemented in `AiClassificationResult.java`
- Automatically assigns categories like "Shopping", "Living", etc., based on input

![cfdde72191139c0ae69a90d36dcb11e](https://github.com/user-attachments/assets/f5bce7be-d5e9-45ae-bc91-e22af07a8f60)


---

### 2. Displaying Classification
- Classification tags are shown alongside transaction records
- Helps users better visualize their spending patterns



---

### 3. Current State and Scalability
- AI classification is simulated (not dynamically computed)
- The system is designed for future integration of real models

## 🧩 Structural Enhancements
- New model classes in the `model/` directory:
  - `AiClassificationResult.java`: wraps AI results
  - `TransactionRecord.java`: unified transaction structure
- Improved file organization for scalability

## 🚧 Limitations
- Classification is based on static mappings or mock data
- Accuracy may vary without a dynamic backend model

## ✅ Version 1.1 (Localization & Alerts)

- **Story 1 - Data Import**: Supports importing CSV and JSON files from banking apps and parsing financial data automatically.
- **Story 2 - Categorize Correctly**: Automatically categorizes transactions, allows manual adjustments, and exports categorized charts.
- **Story 3 - Budget Recommendations**: AI generates personalized monthly budgets based on income and spending habits; manual adjustments supported.
- **Story 4 - Localized Financial Scenarios**: Adapts to Chinese user spending patterns and holidays for more accurate categorization.
- **Story 5 - Over-Budget Alert**: Alerts users when spending exceeds budget and provides category-specific overage details and suggestions.
- **Story 7 - Abnormal Spending Detection**: Establish a baseline based on user spending patterns to detect unusual transactions (e.g., large purchases or uncommon locations) and send alerts.
- **Story 11 - Money-Saving Tips**：Provide personalized, clear, and user-friendly money-saving tips and strategies, with explanations and accessible design to help users optimize savings.

