<?php

require "conn.php";

//Get user
//By ID
echo "<p>GET John Doe BY userID <br />";
$qString = "SELECT name FROM users WHERE userID = 1";
$resultName = mysqli_query($conn, $qString);
if(mysqli_num_rows($resultName) > 0){
	$row = mysqli_fetch_row($resultName);
	echo "Found: " . $row[0] . "<br />";
}else{
	echo "No user found. <br />";
}
echo "</p>";

//By name
echo "<p>GET John Doe BY name <br />";
$qString = "SELECT name FROM users WHERE name = 'John Doe'";
$resultName = mysqli_query($conn, $qString);
if(mysqli_num_rows($resultName) > 0){
	$row = mysqli_fetch_row($resultName);
	echo "Found: " . $row[0] . "<br />";
}else{
	echo "No user found. <br />";
}
echo "</p>";

//Get items
echo "<p>Get all items<br />";
$qString = "SELECT name, description FROM items";
$resultItems = mysqli_query($conn, $qString);
if(mysqli_num_rows($resultItems) > 0){
	while($row = mysqli_fetch_row($resultItems)){
		echo "Found Item: " . $row[0] . " - " . $row[1] . "<br />";
	}
}else{
	echo "No items found. <br />";
}
echo "</p>";

//Get all items in DB, show creators
echo "<p>Get all items from database from creator John Doe<br />";
$qString = "SELECT i.name, u.name 
FROM items i 
INNER JOIN users u ON i.userID = u.userID 
WHERE i.userID = 1 ";
$resultItems = mysqli_query($conn, $qString);
if(mysqli_num_rows($resultItems) > 0){
	while($row = mysqli_fetch_row($resultItems)){
		echo "Found Item: " . $row[0] . " was created by " . $row[1] . "<br />";
	}
}else{
	echo "No items found. <br />";
}
echo "</p>";

//Build a receipt
echo "<p>Build an example receipt for John Doe.<br />";
$userID = 1;
$receiptID = 1;
$qString = "SELECT u.name, r.receiptID, r.creationDate, r.totalCost, r.tax 
FROM users u 
INNER JOIN receipts r ON r.userID = u.userID 
WHERE u.userID =" . $userID . " AND r.receiptID =" . $receiptID;
$result = mysqli_query($conn, $qString);
if(mysqli_num_rows($result) > 0){
	$row = mysqli_fetch_row($result);
	$totalCost = $row[3];
	$tax = $row[4];
	echo "<table border=1><tr><th colspan=2>Receipt for " . $row[0] . "</th></tr>";
	echo "<tr><td>Receipt: " . $row[1] . "</td><td> Created on: " . $row[2] . "</td></tr>";

	$qString = "SELECT i.name, i.description, i.price
	FROM items i 
	INNER JOIN receiptItems ri ON ri.itemID = i.itemID 
	INNER JOIN receipts r ON r.receiptID = ri.receiptID 
	WHERE r.receiptID =" . $receiptID;
	$result = mysqli_query($conn, $qString);
	if(mysqli_num_rows($result) > 0){
		if ($totalCost > 0)
			$dontSum = true;
		while($row = mysqli_fetch_row($result)){
			echo "<tr><td>" . $row[0] . "<br />" . $row[1] . "</td><td>" . $row[2] . "</td></tr>";
			if(!$dontSum)
				$totalCost += $row[2];
		}
	}else{
		echo "<tr><td colspan=2> No items found. </td></tr>";
	}
	echo "<tr><td>Total</td><td>" . $totalCost . "</td></tr>";
	echo "</table>";
}else{
	echo "No data found.";
}

?>