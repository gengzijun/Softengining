## 🚀 Version 2.1

Building on everything in Version 2, this release adds four key enhancements to give you more control over your budget and improve the app’s robustness:

---

### 🆘 Emergency-Expense Reservation
If you know you’ll have additional or emergency spending next month, enter the estimated amount into the new **Any Emergency** field and hit **Confirm**. The AI will automatically adjust your monthly consumption budget to reserve that buffer for you.  

![f6b4e1e0c18c205ef13a6d9262ad8bf](https://github.com/user-attachments/assets/6a11daac-6fab-4eb9-8aaf-7c204f462abe)


---

### 📆 Dynamic Savings-Timeline Adjustment
Now when you pick your **Start** and **End** years in the piggy-bank progress panel and click **Confirm**, the app will recalculate:
- The progress bar percentage  
- **Have saved** amount  
- **Still need** amount  
- Detailed monthly breakdown in the **Details** section

![ff3e04cc8151e0706539c3ea389d168](https://github.com/user-attachments/assets/739764d4-d4fc-45ee-9044-5b715ffae0f4)


---

### ⚠️ Input-Validation & Error-Handling
To prevent timeline mistakes, selecting an **End** year earlier than the **Start** year now triggers a clear popup error:
> **Error: Invalid input**  
> The start year must be earlier than the end year!  
> Please re-enter.  
This guards against misconfigured goals and keeps your projections accurate.

![0d02f6f74e760a150ce520a52a4da1e](https://github.com/user-attachments/assets/3563e56c-ce68-4f96-9937-52d6ccaaed80)


---

### 💾 Persistent Data Storage (CSV Export)
Click the **Export Data** button to download a `Saving_data.csv` file containing all of your key plan parameters:

| Column            | Description                                               |
|-------------------|-----------------------------------------------------------|
| `target`          | Your overall savings goal                                 |
| `haveSaved`       | Amount you have already saved                             |
| `stillNeed`       | Remaining amount to reach your goal                       |
| `startYear`       | Timeline start year                                       |
| `endYear`         | Timeline end year                                         |
| `emergency_amount`| Reserved buffer for emergency or additional expenses      |

![4b073cb1134abd9aa0cc23e1e587c3e](https://github.com/user-attachments/assets/957a68b1-efac-4bbc-a0c2-9590639ea0f6)


