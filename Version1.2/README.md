# ✨ Overview

Compared to version 1.1, version 1.2 adds historical data retention and display. The history page is visually represented by a clock on the main page.

# 🖼️ Screens and Navigation

## 🏠 Main Page (`MainPage.java`)
<img width="899" alt="dbcfc72383dbe24becbf4918c572a82" src="https://github.com/user-attachments/assets/4d46762b-ef41-4208-b3f3-21c69990181b" />

### 📋 1.1 Data Type

Uses the `Expense` class as the underlying data model, including:
- `date` (transaction date)
- `detail` (transaction details)
- `amount` (amount)
- `type` (transaction type)

Users can rearrange data by selecting different data criteria such as date：
<img width="719" alt="40c989fdea3c29051b9141913540f59" src="https://github.com/user-attachments/assets/6b608fe6-2c50-4e14-b6c4-bfd4e94fa1dd" />

detail：

<img width="717" alt="382811636254fba73d54018397f3dec" src="https://github.com/user-attachments/assets/d07ba7ad-b38d-4a55-b247-7a20bb0c654d" />

or type：

<img width="718" alt="43d4a3880c90e5ef68a57241451eb77" src="https://github.com/user-attachments/assets/3985eced-ece3-4d5d-93b0-d55be330351b" />


Implements JavaFX's `StringProperty` for real-time binding between data and UI components.

### 🧩 1.2 Interface Structure

- **Left Navigation Bar**
  - Provides quick switching to Chart and Details pages through icon-text buttons.

- **Top Section**
  - Contains a search bar and dropdown for combined keyword and type searches.
  - Includes buttons for importing and exporting CSV data, facilitating data backup and bulk import.
  - Provides quick switching to the history page.

- **Center Area**
  - Displays recent financial records using a `TableView`.
  - Supports direct editing (e.g., changing transaction type), with automatic saving upon modification.

<img width="1001" alt="ca37700a0f3b77e11e55ce25c626443" src="https://github.com/user-attachments/assets/7ea2cdfe-dbb2-4c75-93b2-34f7242a6064" />

### 👥 1.3 User Story and MainPage Feature Mapping

| Story ID | Story Name                 | User Story Requirement                                                                                          | MainPage Implementation                                         |
|----------|----------------------------|------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------|
| 1        | Data Import                | As a salaried worker, I want automatic data import from files to save manual entry time and effort.       | Provides bulk CSV data import, asynchronous processing, and AI-based automatic transaction categorization. |
| 2        | Categorize Correctly       | As a frugal person, I want accurate categorization of expenses to clearly track my spending.              | AI-based data import categorizes expenses accurately, results displayed directly in `TableView`.           |
| 3        | Budget Recommendations     | As a wage earner, I want AI to recommend monthly budgets to improve my financial planning.                | Currently offers monthly budget alerts, with room for future expansion into budget recommendations.         |
| 4        | Localized Financial Scenarios | As a Chinese user, I want the software to recognize and adapt to localized financial scenarios.           | Uses RMB symbol (￥) consistently; date and amount formatting comply with local user habits.               |
| 5        | Over-budget Alert          | As a budget-conscious individual, I want alerts when my spending exceeds my budget to adjust spending timely. | Sets monthly budget and provides visual alerts (red warning icon) when spending exceeds budget.            |

## 🕰️ History Page (`HistoryPage.java`)

The history page is structurally similar to the main page.
<img width="899" alt="dbcfc72383dbe24becbf4918c572a82" src="https://github.com/user-attachments/assets/7dbeb429-c735-45b9-a786-ec8f7349b971" />

### 📋 2.1 Data Type

Uses the `DataRecord` class as the base data model, including:
- `task` (task identifier)
- `date` (transaction date)
- `detail` (transaction details)
- `amount` (amount)
- `type` (transaction type)

Each field uses JavaFX's `SimpleStringProperty` for real-time UI data binding.

### 🧩 2.2 Interface Structure

- **Left Navigation Bar**
  - Provides a month list for loading data by month upon user click.

<img width="897" alt="c076ea895f111c83004e8d1202ed234" src="https://github.com/user-attachments/assets/d7dd520e-ec2c-4f56-9c49-f7f67788e61f" />

  - Shows a popup prompt if data for the selected month is not available, instructing users to upload the file.

<img width="899" alt="d69e43ac4153121241366979d18253b" src="https://github.com/user-attachments/assets/19afe0ea-8dd5-4c67-ab96-b947547dce34" />


- **Top Main View**
  - Includes a search and filter section at the top, supporting quick data search by fields (task, date, amount, etc.).

 <img width="901" alt="d42b3c164c7e42628d3e15d013ba5a4" src="https://github.com/user-attachments/assets/3a1539aa-2a0d-4b70-9994-796c05e401fa" />

### 👥 2.3 User Story and History Page Feature Mapping

- **Story 1 - Data Import**: Supports importing CSV and JSON files from banking apps and parsing financial data automatically.
- **Story 2 - Categorize Correctly**: Automatically categorizes transactions, allows manual adjustments, and exports categorized charts.
- **Story 3 - Budget Recommendations**: AI generates personalized monthly budgets based on income and spending habits; manual adjustments supported.
- **Story 4 - Localized Financial Scenarios**: Adapts to Chinese user spending patterns and holidays for more accurate categorization.
- **Story 5 - Over-Budget Alert**: Alerts users when spending exceeds budget and provides category-specific overage details and suggestions.
- **Story 6 - Viewing Transaction History**: Support filtering transaction records by month, category, and amount.
- **Story 7 - Abnormal Spending Detection**: Establish a baseline based on user spending patterns to detect unusual transactions (e.g., large purchases or uncommon locations) and send alerts.
- **Story 11 - Money-Saving Tips**：Provide personalized, clear, and user-friendly money-saving tips and strategies, with explanations and accessible design to help users optimize savings.



