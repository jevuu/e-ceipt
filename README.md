# e-ceipt

Here are the files for the app and site.

Everything here except git specific files, readme, and PHP folder are for the Android app.

PHP folder contains all the PHP files needed for the server.
  1. conn.php contains the connection information. It is currently configured to send data to my webhost, but for security I removed the password. Please see the Discord for the password. However, if you want to run your own WampServer, you're free to any make changes you need.
  2. login.php is the file that handles logging in. Or supposed to. Anyway, this page is set up for accepting POST data and returning data.
  3. index.php simply returns the contents of the table.
  
Currently there are two records in the table. Below are the property names and the records:

id | userName | name | created | comments

1 | JohnDoe | John Doe | ... | Hello World!

2 | JaneDoe | Jane Doe | ... | Goodbye!
