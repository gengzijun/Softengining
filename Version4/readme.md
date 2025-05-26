# ✨  Overview
AI-Powered Intelligent Accounting Assistant is your all-in-one financial companion. With a single tap, you can instantly log every income and expense—no more tedious manual entry—while our built-in AI engine analyzes your cash flow in real time. Watch as it automatically:

- Categorizes each transaction (e.g., groceries, utilities, entertainment)

- Detects unusual spending patterns and flags potential savings opportunities

- Predicts upcoming bills based on your historical data

- Visualizes your budget with interactive charts and trend lines

- Generates monthly health reports highlighting where you’re thriving and where you can optimize

Whether you’re planning for a big purchase, setting aside an emergency fund, or just curious about your day-to-day spending habits, our assistant gives you instant, actionable insights—so you stay one step ahead of your money, stress less, and build a healthier financial future effortlessly.
# ✅Runtime Environment & Configuration

## 1. Java Environment
- **JDK 17** or higher (to support the Java module system)  
- Make sure `java` and `javac` are on your system `PATH`

---

## 2. JavaFX SDK
1. Download the JavaFX SDK (recommended version 21+) from the [Gluon website](https://gluonhq.com/products/javafx/).  
2. Unzip it anywhere on your machine and note the `lib` directory, for example:  /path/to/javafx-sdk-21/lib

---

## 3. External Library Dependencies
This project uses the Java module system. The following modules/dependencies are declared in module-info.java. You can manage versions via Maven or Gradle:
- javafx.controls
- javafx.fxml
- Jackson Databind (com.fasterxml.jackson.databind)
- ControlsFX (org.controlsfx.controls)
- FormsFX (com.dlsc.formsfx)
- BootstrapFX (org.kordamp.bootstrapfx.core)
- FXGL (game engine, com.almasb.fxgl.all)
- LangChain4j OpenAI (langchain4j.open.ai)
- dotenv-java (io.github.cdimascio.dotenv.java)
- OpenCSV (com.opencsv)

Tip:
- For Maven, add each dependency to your pom.xml.
- For Gradle, declare them under implementation in build.gradle.

---

## 4. Environment Variables
This project uses dotenv-java to load sensitive settings (e.g. API keys) from a .env file.

Create a .env in the project root (or copy .env.example) with entries like:
OPENAI_API_KEY=your_openai_key_here

---

## 5. Build & Run
### Using Maven：
mvn clean package

mvn javafx:run

### Direct java Command
java --module-path $PATH_TO_FX \
       --add-modules javafx.controls,javafx.fxml \
       -jar target/app.jar

---

## 6. IDE Configuration
### IntelliJ IDEA
- Add JDK 17 in Project Structure → SDKs.
- In your Run Configuration’s VM options, include:--module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml
### Eclipse
- In Run Configurations → VM arguments, set the same --module-path and --add-modules values.

---
# 🌈Actual operation demonstration
## 0. Login Page (`LoginPage.java`)
- Basic user login interaction
- Navigates to main dashboard upon entering a username

<img width="593" alt="2eda123bec04cb2303ac414472c7065" src="https://github.com/user-attachments/assets/e9c3e5dd-0323-4713-9196-a3bb549d48ad" />

---

## 🏠 1. Main Page (`MainPage.java`)
We generate the main page by importing data and conducting analysis with AI.

![e29b3c086c9358f7d4adeef7872bc9a](https://github.com/user-attachments/assets/1400e38f-5342-4d00-abf6-b96183d1bd35)

![8f1f5f0efe7c7f1b9f283438a1055f9](https://github.com/user-attachments/assets/09e15b82-41db-4f68-8cff-591e53ae363a)

<img width="899" alt="ab2ab1a04df8be2c478e575528964de" src="https://github.com/user-attachments/assets/4be8b1c7-0a5c-441d-8dfa-f7abacac88eb" />

---

### 📋 1.1 Data Type

Uses the `Expense` class as the underlying data model, including:
- `date` (transaction date)
- `detail` (transaction details)
- `amount` (amount)
- `type` (transaction type)

Users can rearrange data by selecting different data criteria such as date：

<img width="892" alt="7626caf72ae3c5269b7a0fbb7f37bf6" src="https://github.com/user-attachments/assets/a21032d8-03ae-464d-8ba5-416b561d929e" />


detail：

<img width="895" alt="05540b18e2eacb60c8a5106a6e0572f" src="https://github.com/user-attachments/assets/825e7979-5e20-494b-a3e6-9f403fd21fb9" />


or type：

<img width="911" alt="c22dbca662f4f3433326010bd30e632" src="https://github.com/user-attachments/assets/6d881b43-289b-41aa-ba95-b86794f5fa3e" />



Implements JavaFX's `StringProperty` for real-time binding between data and UI components.

---

### 🧩 1.2 Interface Structure

- **Left Navigation Bar**
  - Provides quick switching to History and Details pages through icon-text buttons.

- **Top Section**
  - Contains a search bar and dropdown for combined keyword and type searches.
  - Includes buttons for importing and exporting CSV data, facilitating data backup and bulk import.
  - Provides quick switching to the history page.

<img width="911" alt="5b62110533cbd8d4220a5766cd077d2" src="https://github.com/user-attachments/assets/dc7ca266-0071-4722-95dc-e5a9d3347b1a" />

- **Center Area**
  - Displays recent financial records using a `TableView`.
  - Supports direct editing (e.g., changing transaction type), with automatic saving upon modification.

<img width="900" alt="4901d43cade2cdfcebbe535f38a9edf" src="https://github.com/user-attachments/assets/1585ba07-c9cc-4cb5-8d7f-a75213b3ebbd" />

---

### 🧩 1.3 File export
The generated file can be exported in the form of a CSV file, which is convenient for sharing and storage.

<img width="911" alt="60c20fb82a9d6a3f42789e048946146" src="https://github.com/user-attachments/assets/40c114a2-7c5d-4f7a-968d-3eb41a42b2ee" />

---

## 🕰️ 2.History Page (`HistoryPage.java`)

The history page is structurally similar to the main page.

<img width="899" alt="ab2ab1a04df8be2c478e575528964de" src="https://github.com/user-attachments/assets/8878ef58-8bb0-4867-ad5f-222fca6aee7b" />

---

### 📋 2.1 Data Type

Uses the `DataRecord` class as the base data model, including:
- `task` (task identifier)
- `date` (transaction date)
- `detail` (transaction details)
- `amount` (amount)
- `type` (transaction type)

Each field uses JavaFX's `SimpleStringProperty` for real-time UI data binding.

---

### 🧩 2.2 Interface Structure

- **Left Navigation Bar**
  - Provides a month list for loading data by month upon user click.

<img width="895" alt="8df838e636f91126a69d918090ef2db" src="https://github.com/user-attachments/assets/9aa2b8d9-75ed-468b-a068-bb69f7cfb829" />


  - Shows a popup prompt if data for the selected month is not available, instructing users to upload the file.

<img width="896" alt="b3447ff3cc0a9aaa71a3457aaafc0aa" src="https://github.com/user-attachments/assets/3bc1bca9-1869-475c-b55f-4aec1d10f900" />

- **Top Main View**
  - Includes a search and filter section at the top, supporting quick data search by fields (task, date, amount, etc.).

 <img width="895" alt="8df838e636f91126a69d918090ef2db" src="https://github.com/user-attachments/assets/fbd008b5-484e-4b38-a7d4-8c5741212cec" />

---

## 🕰️ 3. Detail Page 

<img width="1091" alt="6c653840948e7ec4eeae7cea39909d3" src="https://github.com/user-attachments/assets/65b7115c-8784-4ac2-834c-3f94bffc38b4" />


### 🆘 3.1 Emergency-Expense Reservation
If you know you’ll have additional or emergency spending next month, enter the estimated amount into the new **Any Emergency** field and hit **Confirm**. The AI will automatically adjust your monthly consumption budget to reserve that buffer for you.  

![f6b4e1e0c18c205ef13a6d9262ad8bf](https://github.com/user-attachments/assets/c41da1ef-dc31-47ba-937e-1f39718cd0c8)



---

### 📆 3.2 Dynamic Savings-Timeline Adjustment
Now when you pick your **Start** and **End** years in the piggy-bank progress panel and click **Confirm**, the app will recalculate:
- The progress bar percentage  
- **Have saved** amount  
- **Still need** amount  
- Detailed monthly breakdown in the **Details** section

![ff3e04cc8151e0706539c3ea389d168](https://github.com/user-attachments/assets/af6fe36f-b0fc-4c4f-bbfc-6f563d5164d9)



---

### ⚠️ 3.3 Input-Validation & Error-Handling
To prevent timeline mistakes, selecting an **End** year earlier than the **Start** year now triggers a clear popup error:
> **Error: Invalid input**  
> The start year must be earlier than the end year!  
> Please re-enter.  
This guards against misconfigured goals and keeps your projections accurate.

![0d02f6f74e760a150ce520a52a4da1e](https://github.com/user-attachments/assets/655f6393-135e-4275-81cb-5db5bccee015)



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

![4b073cb1134abd9aa0cc23e1e587c3e](https://github.com/user-attachments/assets/0c3f10fc-fafe-4615-9811-cdc1e9a3c6a5)


---

### 🖼️ 3.5 Personalized savings tips
The app will, based on the user's consumption habits, savings goals and income situation, provide the user with personalized saving tips.

![15aaf7260ec2691c756103de814c7ec](https://github.com/user-attachments/assets/5b684def-c460-45b3-879c-5981c275a944)


Users can click on the cover of each tip. After clicking, the corresponding tip page will pop up.

![1729dbee2744ae7e1bf5b61c4db8870](https://github.com/user-attachments/assets/5a43006d-b7a7-4b62-8ade-d5d0c4ed5f3e)


---


### 🗂️ 3.6 Real-time update
The information in the "saving goal" page, including the details and progress bar sections, has been changed to be able to update in real time based on the user's input.

![9a16ec962b4b0cfd06476e2d102ffe7](https://github.com/user-attachments/assets/834667af-b2ce-46ba-a6d0-cb31a4ce6ed0)

![c11b9914184eada184e55b77d1fe5b7](https://github.com/user-attachments/assets/2ec6abe0-0cf0-4bf4-89c0-9751d24f1cea)


---

### 🎏 3.7 Interface simplification
The original two "confirm" buttons have been changed to a unified "confirm" button. After clicking it, the progress bar can be updated, as well as the target and hasSaved information. Additionally, the emergency value at the end of the data will be passed to the AI for analysis.

![5a0673ad662703bdd9ccbf04eaeab21](https://github.com/user-attachments/assets/ea8b81c8-00bb-41a5-bacb-d33e9f390e9c)


---

## 🕰️ 4. Chart Page

### 📊 4.1Personalized AI Analysis (`AiClassificationResult.java`)

The AI classification data model includes:

- **Object:** Transaction description.
- **Amount:** Monetary value of the transaction.
- **Category:** AI-generated transaction category.

Users can select different time intervals (e.g., quarterly or yearly) to analyze changes in spending habits and financial flows:

- **Monthly Analysis:**

<img width="1061" alt="4e90eb09da72623dea051abb209a939" src="https://github.com/user-attachments/assets/e2446a7e-95ea-45c3-9271-b83a01b167d8" />

- **Quarterly Analysis:**

<img width="1061" alt="badc7a6a4a853acbe59c98688771138" src="https://github.com/user-attachments/assets/23476618-0394-42ba-b3f4-766011ab2a60" />

- **Yearly Analysis:**

<img width="1061" alt="967d4f3c7045cef6534025ba63363e7" src="https://github.com/user-attachments/assets/611db244-a380-4712-8578-d1d031d475ff" />

After analysis, the AI generates corresponding line charts and pie charts:

- **Line Chart:** Displays daily changes in spending amounts over time.

<img width="590" alt="c645c349d58a469bdaae83e2e859507" src="https://github.com/user-attachments/assets/09784fff-fb5f-4888-90f2-63d56aa15443" />


- **Pie Chart:** Illustrates the proportion of different expense categories within total spending.

<img width="587" alt="0e40689f4a1d0e887eb4e8b5ae11afb" src="https://github.com/user-attachments/assets/80f843ed-553f-435b-b16c-251a56e15c2e" />


---

## 🔮 4.2 Budget Prediction and Advice Logic (`BudgetAdvisor.java`)

This component forecasts future financial conditions and provides personalized advice.

#### Workflow:

1. Aggregates historical spending data using `StatsAggregator`.
2. Constructs AI prompts via `BudgetPromptBuilder` based on historical data and target savings.
3. Sends prompts to the OpenAI chat model to obtain predictive data and financial advice.
4. Parses AI responses into structured predictive data (`PredictionResult`) and actionable advice.
5. Saves results into CSV files using `ResultSaver`.

#### Budget Prediction:

The AI analyzes current expenses and forecasts future spending according to different categories, providing detailed recommendations.

<img width="491" alt="2762518ed390fa605662d5ecc8e5c54" src="https://github.com/user-attachments/assets/275cb4e6-91a2-4e05-8921-1d78420ff3f3" />



### Personalized AI Suggestions:

Based on user spending habits, the AI offers personalized and actionable recommendations, such as reducing unnecessary spending.

<img width="495" alt="3e89966fa4efbe396b8aaab0e25cfea" src="https://github.com/user-attachments/assets/750065d8-4e91-4d78-8730-db7de48a3d90" />


---

### 🛠️ 4.3 Budget Prompt Construction (`BudgetPromptBuilder.java`)

- Dynamically constructs structured AI prompts based on users' historical monthly spending data.
- Requires the AI to respond in a standardized JSON format, immediately followed by clear financial advice.
- Prompts can be tailored according to user-specific saving goals.

<img width="1047" alt="5b175a413c13158e8f77c32c3ab232f" src="https://github.com/user-attachments/assets/2cdd2fc5-48e7-44f3-9ada-643fce7aedfe" />



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











