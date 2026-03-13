1. Create database: social_network_db
2. Run db/schema.sql
3. Update application.properties
4. Run the Spring Boot project

Important

The server will not start correctly unless JWT_SECRET is defined.

## JWT Secret Setup (Required)

This project uses JWT tokens for authentication.
For security reasons, the secret key used to sign the tokens is **not stored in the repository**.
Each developer must generate their own secret locally and store it as an environment variable.

The application reads the secret from:

jwt.secret=${JWT_SECRET}

in `application.properties`.

### Step 1 – Generate a random Base64 secret

Open **Windows PowerShell** and run:

```powershell
[Convert]::ToBase64String((1..32 | ForEach-Object {Get-Random -Maximum 256}))

Example output:

QuSRmPD2CuH+40rID1JoGs93GT7ph0vM0ri3Z0ymBL8=

Copy the generated string.

### Step 2 – Set the JWT_SECRET environment variable

Run the following command (replace the value with the secret you generated):

setx JWT_SECRET QuSRmPD2CuH+40rID1JoGs93GT7ph0vM0ri3Z0ymBL8=

Expected output:

SUCCESS: Specified value was saved.

### Step 3 – Restart IntelliJ

After setting the variable, restart IntelliJ so the application can read the new environment variable.