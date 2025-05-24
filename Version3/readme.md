# 📘 Version 3

## ✨ Overview
In version 3.1, we mainly optimized the functions related to saving goals.

<img width="1080" alt="1748104757726" src="https://github.com/user-attachments/assets/15b130ec-aacc-40c5-a752-d3ca563523cb" />

---

## 🖼️ Personalized savings tips
The app will, based on the user's consumption habits, savings goals and income situation, provide the user with personalized saving tips.

![15aaf7260ec2691c756103de814c7ec](https://github.com/user-attachments/assets/123bdf53-f86d-48ab-b7cc-9da802b0caa0)

Users can click on the cover of each tip. After clicking, the corresponding tip page will pop up.
![1729dbee2744ae7e1bf5b61c4db8870](https://github.com/user-attachments/assets/6f01bd90-9769-4dac-80aa-2e0c8b825c28)


---


## 🗂️ Real-time update
The information in the "saving goal" page, including the details and progress bar sections, has been changed to be able to update in real time based on the user's input.

![9a16ec962b4b0cfd06476e2d102ffe7](https://github.com/user-attachments/assets/49a0d09a-d3f6-459e-9114-ec123d2fb06a)

![c11b9914184eada184e55b77d1fe5b7](https://github.com/user-attachments/assets/c2224c0d-0ff8-4681-9f3a-1142a8dc392c)

---

## 🎏 Interface simplification
The original two "confirm" buttons have been changed to a unified "confirm" button. After clicking it, the progress bar can be updated, as well as the target and hasSaved information. Additionally, the emergency value at the end of the data will be passed to the AI for analysis.

![5a0673ad662703bdd9ccbf04eaeab21](https://github.com/user-attachments/assets/1068596e-a3a4-4c57-aedb-d47b11591f53)

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

