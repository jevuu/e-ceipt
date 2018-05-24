# E-ceipt

This is our project for PRJ666 Project Implementation (prj666_182a06).

E-ceipt is a mobile app designed to read receipts and create and store digital copies. The app will be able to analyse the user's purchasing
habits.

## Developers (this is directed at the group)

### Installation

The following are instructions to set up a personal/local server and development environment. If you just want set up for Android Studio development follow steps 5 and 8 only.

1) Install Ubuntu 16.04 or similar Linux OS on your server
2) Follow this guide to install Apache, MySQL, and PHP: https://www.digitalocean.com/community/tutorials/how-to-install-linux-apache-mysql-php-lamp-stack-on-ubuntu-16-04
3) Install PHPMyAdmin: https://www.digitalocean.com/community/tutorials/how-to-install-and-secure-phpmyadmin-on-ubuntu-12-04
4) Download and install Android Studio
5) Fork/clone the repo
6) Using PHPMyAdmin, import the createTables.sql file to create the database and tables. Optionally, you can also import populateTables.sql to populate the table with dummy data
7) Copy the PHP files into your server's html folder. If you followed the guide above, the address will be /var/www/html
8) Create a "conn.php" file to set up a connection class.
9) Get "googleserverices.json" from our Discord server (or contact Alistair) and save it into the repo's app folder

Now that everything is installed, you can run Android Studio and build the app.

### Parsing Data

The app will parse the server for data using HTTP POST, sending a JSON object. The server will read the contents of the JSON object and query the database for the data requested. All data returned from the server is wrapped as a JSON object. For testing and development purposes, the server is designed to return dummy data for the user johnDoe by default.

### Folder Structure

- root/ 	-> Contains all the files for PHP, SQL, and Android Studio.
	- PHP/ 	-> Contains the PHP files for server-side functions.
		- getTestReceipt.php	-> Development/Testing only. Gets a test receipt.
		- getUserReceipts.php	-> Get all user receipts. Used for the app's the home screen.
		- index.php				-> Development/Testing only. Gets data from the test table.
		- login.php				-> Find user in the database for authorization. Used for the app's login screen.
		- makeGetDelete.php		-> Development/Testing only. Create a user, items, receipt for that user, get receipt, and delete.
		- register.php			-> Adds user data to users table in database. Used for the app's registration screen.
		- testForm.php			-> Development/Testing only. HTML form to test manual receipt entry.
		- testResult.php		-> Development/Testing only. Result page displaying manual receipt created.
	- SQL/ --> SQL scripts to set up the database
		- createTables.sql		-> Script used to create and configure the database and tables.
		- populateTables.sql	-> Script used to populate tables with dummy data for testing purposes.
	- app/ --> Java code for the mobile app

