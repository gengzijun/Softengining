# 🚀 Overview: Features in Version 2
- **AI Type Prediction:** Provides prediction function to predict the type of each consumption
- **AI Financial Advice:** Provides concrete and actionable budget recommendations to help users achieve their financial goals.
- **Enhanced AI Analysis:** Accurately predicts future expenses and income, enabling proactive financial planning.
- **Data import:** You can import data in format of .csv and you can get the prediction type of each comsumption.
- **Data History Saving:** Each data you import will be stored in month for history page.
- **Data Type change by human:** You can change type if you think the AI prediction is wrong!
- **Saving Goal Interface:** We show the UI of Saving goal Interface.(The function will be finished in version2.1)
- **Chart Interface:** Now chart interface can show prediction content from AI!
  
<img width="1256" alt="670ae71b544059e01c4146f0560dcb9" src="https://github.com/user-attachments/assets/8a93b018-3ce0-4200-892d-6a537ff4993b" />

---

## 📊 Personalized AI Analysis (`AiClassificationResult.java`)

The AI classification data model includes:

- **Object:** Transaction description.
- **Amount:** Monetary value of the transaction.
- **Category:** AI-generated transaction category.

Users can select different time intervals (e.g., quarterly or yearly) to analyze changes in spending habits and financial flows:

- **Quarterly Analysis:**

<img width="1237" alt="0ef5af88a5f2bc769277ca134d7e63b" src="https://github.com/user-attachments/assets/7aaebfa2-b9ea-426a-97b4-38e9c7d7ea09" />


- **Yearly Analysis:**

<img width="1265" alt="ed753bde47817dbf5295fc899aee152" src="https://github.com/user-attachments/assets/208fca97-8ecf-4416-aa30-cb063c7ccd6a" />


After analysis, the AI generates corresponding line charts and pie charts:

- **Line Chart:** Displays daily changes in spending amounts over time.

![59ed64c7960c96bc0303337309ac14f](https://github.com/user-attachments/assets/55b50c67-9061-4f0d-ac2a-0ed0752bc510)

- **Pie Chart:** Illustrates the proportion of different expense categories within total spending.

![54a62d8898cc027305584e11ac9de01](https://github.com/user-attachments/assets/cbf720de-be05-43de-8659-b9e6cdca1ed5)

---

## 🔮 Budget Prediction and Advice Logic (`BudgetAdvisor.java`)

This component forecasts future financial conditions and provides personalized advice.

### Workflow:

1. Aggregates historical spending data using `StatsAggregator`.
2. Constructs AI prompts via `BudgetPromptBuilder` based on historical data and target savings.
3. Sends prompts to the OpenAI chat model to obtain predictive data and financial advice.
4. Parses AI responses into structured predictive data (`PredictionResult`) and actionable advice.
5. Saves results into CSV files using `ResultSaver`.

### Budget Prediction:

The AI analyzes current expenses and forecasts future spending according to different categories, providing detailed recommendations.

![11e0c264df03089d25355b7d035d176](https://github.com/user-attachments/assets/7481fea7-459c-4420-be08-50429cdb0df9)


### Personalized AI Suggestions:

Based on user spending habits, the AI offers personalized and actionable recommendations, such as reducing unnecessary spending.

![a64a4fb1baf5a0d9aae822862de3470](https://github.com/user-attachments/assets/6e0718ee-b134-4266-b3c9-1a432dd31927)

---

## 🛠️ Budget Prompt Construction (`BudgetPromptBuilder.java`)

- Dynamically constructs structured AI prompts based on users' historical monthly spending data.
- Requires the AI to respond in a standardized JSON format, immediately followed by clear financial advice.
- Prompts can be tailored according to user-specific saving goals.

![1df5a292711549190ccf9d9e0b8af1a](https://github.com/user-attachments/assets/f2e10f22-5eae-4d1e-9be7-9aa1c62403b7)

---

## 📌 User Story and AI Feature Mapping

- **Story 1 - Data Import**: Supports importing CSV and JSON files from banking apps and parsing financial data automatically.
- **Story 2 - Categorize Correctly**: Automatically categorizes transactions, allows manual adjustments, and exports categorized charts.
- **Story 3 - Budget Recommendations**: AI generates personalized monthly budgets based on income and spending habits; manual adjustments supported.
- **Story 4 - Localized Financial Scenarios**: Adapts to Chinese user spending patterns and holidays for more accurate categorization.
- **Story 5 - Over-Budget Alert**: Alerts users when spending exceeds budget and provides category-specific overage details and suggestions.
- **Story 6 - Viewing Transaction History**: Support filtering transaction records by month, category, and amount.
- **Story 7 - Abnormal Spending Detection**: Establish a baseline based on user spending patterns to detect unusual transactions (e.g., large purchases or uncommon locations) and send alerts.
- **Story 10 - Annual Financial Summary**: Generate an annual report covering income, expenses, savings, and investments. Support visualization with charts.
- **Story 11 - Money-Saving Tips**：Provide personalized, clear, and user-friendly money-saving tips and strategies, with explanations and accessible design to help users optimize savings.



