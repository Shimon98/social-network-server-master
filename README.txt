# 🚀 Social Network Server

Welcome to **The Empire** 👑  
We own you… just kidding. (or do we?)

---

## 📦 Project Setup

Follow these steps to run the project locally:

1. Create database:
```sql
CREATE DATABASE social_network_db;
```

2. Run:
db/schema.sql

3. Update application.properties (if needed)

4. Run the Spring Boot project

---

## 🛢️ Database Configuration

The database connection is already configured in the project.

💡 Note:  
Yes… we **hardcoded the database username and password for you**.

Not because it's best practice (it’s really not 😅),  
but because we felt you've suffered enough with installations already.

JWT and Email setup?  
Yeah… you still have to handle that yourself 💀

---

## 🔐 Important

❗ The server will NOT start correctly unless JWT_SECRET is defined

---

## 🔑 JWT Secret Setup (Required)

This project uses JWT tokens for authentication.

For security reasons, the secret key used to sign the tokens is NOT stored in the repository.  
Each developer must generate their own secret locally and store it as an environment variable.

The application reads the secret from:

jwt.secret=${JWT_SECRET}

in application.properties.

---

### Step 1 – Generate a random Base64 secret

Open Windows PowerShell and run:

[Convert]::ToBase64String((1..32 | ForEach-Object {Get-Random -Maximum 256}))

Example output:

QuSRmPD2CuH+40rID1JoGs93GT7ph0vM0ri3Z0ymBL8=

Copy the generated string.

---

### Step 2 – Set the JWT_SECRET environment variable

Paste the generated String in PowerShell ->

setx JWT_SECRET QuSRmPD2CuH+40rID1JoGs93GT7ph0vM0ri3Z0ymBL8=


Expected output:

SUCCESS: Specified value was saved.

---

## 📧 Email Configuration (Gmail)

⚠️ Important:  
You cannot use your regular Gmail password.  
You must generate an App Password from Google.

Step 1 – Enable 2-Step Verification  
Go to: https://myaccount.google.com/security

Step 2 – Generate App Password  
Go to: https://myaccount.google.com/apppasswords

- Select Mail  
- Select Other / Windows Computer  
- Click Generate  
- Copy the generated password

Step 3 – Set environment variables
Paste the following commands in PowerShell with your own data(email + generated password) ->


setx MAIL_USERNAME "yourEmail@gmail.com"
setx MAIL_PASSWORD "your_generated_app_password"


---

## 🔄 Step 3 – Restart IntelliJ

After setting the variables:

Restart IntelliJ so the application can read them.

---

## ▶️ Step 4 – Run the project

Run:

SocialNetworkServerApplication

---

## ⚠️ Troubleshooting

If something doesn’t work:

- Make sure MySQL is running  
- Make sure database exists  
- Make sure environment variables are set  
- Restart IntelliJ (yes… again 😅)

---

## 🧠 Final Words

If it works — you're a genius 🎉  
If it doesn’t — welcome to backend development 🫠

Enjoy building **The Empire** 👑