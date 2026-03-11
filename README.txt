1. Create database: social_network_db
2. Run db/schema.sql
3. Update application.properties
4. Run the Spring Boot project

Steps to generate a random base 64 string and then set it to become the JWT_SECRET in application.properties

PS C:\Users\diana> [Convert]::ToBase64String((1..32 | ForEach-Object {Get-Random -Maximum 256}))
QuSRmPD2CuH+40rID1JoGs93GT7ph0vM0ri3Z0ymBL8=
PS C:\Users\diana> setx JWT_SECRET
ERROR: Invalid syntax.
Type "SETX /?" for usage.
PS C:\Users\diana> setx JWT_SECRET QuSRmPD2CuH+40rID1JoGs93GT7ph0vM0ri3Z0ymBL8=

SUCCESS: Specified value was saved.