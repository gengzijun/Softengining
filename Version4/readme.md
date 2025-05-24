# ✨ Overview
AI intelligent accounting assistant, allowing you to record transactions with just one click and conduct real-time analysis, helping you easily manage your financial health.

## 0. Login Page (`LoginPage.java`)
- Basic user login interaction
- Navigates to main dashboard upon entering a username
登录界面图片



## 🏠 1. Main Page (`MainPage.java`)
We generate the main page by importing data and conducting analysis with AI.
主页面照片

### 📋 1.1 Data Type

Uses the `Expense` class as the underlying data model, including:
- `date` (transaction date)
- `detail` (transaction details)
- `amount` (amount)
- `type` (transaction type)

Users can rearrange data by selecting different data criteria such as date：
data图片

detail：

detail图片

or type：

type图片


Implements JavaFX's `StringProperty` for real-time binding between data and UI components.

### 🧩 1.2 Interface Structure

- **Left Navigation Bar**
  - Provides quick switching to History and Details pages through icon-text buttons.

- **Top Section**
  - Contains a search bar and dropdown for combined keyword and type searches.
  - Includes buttons for importing and exporting CSV data, facilitating data backup and bulk import.
  - Provides quick switching to the history page.

- **Center Area**
  - Displays recent financial records using a `TableView`.
  - Supports direct editing (e.g., changing transaction type), with automatic saving upon modification.

更改类型图片

## 🕰️ 2.History Page (`HistoryPage.java`)

The history page is structurally similar to the main page.
主界面照片

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

历史界面照片

  - Shows a popup prompt if data for the selected month is not available, instructing users to upload the file.

切屏错误图片


- **Top Main View**
  - Includes a search and filter section at the top, supporting quick data search by fields (task, date, amount, etc.).

 历史界面图片

## 🕰️ 3. Detail Page 

细节界面照片


### 🆘 3.1 Emergency-Expense Reservation
If you know you’ll have additional or emergency spending next month, enter the estimated amount into the new **Any Emergency** field and hit **Confirm**. The AI will automatically adjust your monthly consumption budget to reserve that buffer for you.  

白图片1


---

### 📆 3.2 Dynamic Savings-Timeline Adjustment
Now when you pick your **Start** and **End** years in the piggy-bank progress panel and click **Confirm**, the app will recalculate:
- The progress bar percentage  
- **Have saved** amount  
- **Still need** amount  
- Detailed monthly breakdown in the **Details** section

白图片2


---

### ⚠️ 3.3 Input-Validation & Error-Handling
To prevent timeline mistakes, selecting an **End** year earlier than the **Start** year now triggers a clear popup error:
> **Error: Invalid input**  
> The start year must be earlier than the end year!  
> Please re-enter.  
This guards against misconfigured goals and keeps your projections accurate.

白图片3


---

### 💾 3.4 Persistent Data Storage (CSV Export)
Click the **Export Data** button to download a `Saving_data.csv` file containing all of your key plan parameters:

| Column            | Description                                               |
|-------------------|-----------------------------------------------------------|
| `target`          | Your overall savings goal                                 |
| `haveSaved`       | Amount you have already saved                             |
| `stillNeed`       | Remaining amount to reach your goal                       |
| `startYear`       | Timeline start year                                       |
| `endYear`         | Timeline end year                                         |
| `emergency_amount`| Reserved buffer for emergency or additional expenses      |

白图片4


## 🖼️ 3.5 Personalized savings tips
The app will, based on the user's consumption habits, savings goals and income situation, provide the user with personalized saving tips.

个人建议图片

Users can click on the cover of each tip. After clicking, the corresponding tip page will pop up.

个人建议详细图片

---


## 🗂️ 3.6 Real-time update
The information in the "saving goal" page, including the details and progress bar sections, has been changed to be able to update in real time based on the user's input.

白图片

白图片

---

## 🎏 3.7 Interface simplification
The original two "confirm" buttons have been changed to a unified "confirm" button. After clicking it, the progress bar can be updated, as well as the target and hasSaved information. Additionally, the emergency value at the end of the data will be passed to the AI for analysis.

白图片

---

## 🕰️ 4. Chart Page

## 📊 4.1Personalized AI Analysis (`AiClassificationResult.java`)

The AI classification data model includes:

- **Object:** Transaction description.
- **Amount:** Monetary value of the transaction.
- **Category:** AI-generated transaction category.

Users can select different time intervals (e.g., quarterly or yearly) to analyze changes in spending habits and financial flows:

- **Monthly Analysis:**

月度照片

- **Quarterly Analysis:**

季度照片

- **Yearly Analysis:**

年度照片


After analysis, the AI generates corresponding line charts and pie charts:

- **Line Chart:** Displays daily changes in spending amounts over time.

线型图

- **Pie Chart:** Illustrates the proportion of different expense categories within total spending.

饼图

---

## 🔮 4.2 Budget Prediction and Advice Logic (`BudgetAdvisor.java`)

This component forecasts future financial conditions and provides personalized advice.

### Workflow:

1. Aggregates historical spending data using `StatsAggregator`.
2. Constructs AI prompts via `BudgetPromptBuilder` based on historical data and target savings.
3. Sends prompts to the OpenAI chat model to obtain predictive data and financial advice.
4. Parses AI responses into structured predictive data (`PredictionResult`) and actionable advice.
5. Saves results into CSV files using `ResultSaver`.

### Budget Prediction:

The AI analyzes current expenses and forecasts future spending according to different categories, providing detailed recommendations.

截图


### Personalized AI Suggestions:

Based on user spending habits, the AI offers personalized and actionable recommendations, such as reducing unnecessary spending.

截图

---

## 🛠️ 4.3 Budget Prompt Construction (`BudgetPromptBuilder.java`)

- Dynamically constructs structured AI prompts based on users' historical monthly spending data.
- Requires the AI to respond in a standardized JSON format, immediately followed by clear financial advice.
- Prompts can be tailored according to user-specific saving goals.

chart上半截图


---

## 📌 User Story and AI Feature Mapping

- **Story 1 - Data Import**: Supports importing CSV and JSON files from banking apps and parsing financial data automatically.
- **Story 2 - Categorize Correctly**: Automatically categorizes transactions, allows manual adjustments, and exports categorized charts.
- **Story 3 - Budget Recommendations**: AI generates personalized monthly budgets based on income and spending habits; manual adjustments supported.
- **Story 4 - Localized Financial Scenarios**: Adapts to Chinese user spending patterns and holidays for more accurate categorization.
- **Story 5 - Over-Budget Alert**: Alerts users when spending exceeds budget and provides category-specific overage details and suggestions.
- **Story 6 - Viewing Transaction History**: Support filtering transaction records by month, category, and amount.
- **Story 7 - Abnormal Spending Detection**: Establish a baseline based on user spending patterns to detect unusual transactions (e.g., large purchases or uncommon locations) and send alerts.
- **Story 8 - Savings Goal Setting and Tracking**: Enables users to set savings goals (amount, term), dynamically update progress, adjust goals, and provide detailed reports including amount, remaining goals, and probability analysis.
- **Story 9 - Reserve Fund**: Offers a "reserve fund" function, allowing users to deposit a portion of funds on a monthly basis. Users can view detailed information such as the total amount and the time and amount of each deposit.
- **Story 10 - Annual Financial Summary**: Generate an annual report covering income, expenses, savings, and investments. Support visualization with charts.
- **Story 11 - Money-Saving Tips**：Provide personalized, clear, and user-friendly money-saving tips and strategies, with explanations and accessible design to help users optimize savings.











