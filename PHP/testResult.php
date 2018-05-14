<?php

require "conn.php";

echo "<hr /><h2>Create a user based on input, create a receipt , add items to receipt, display receipt, delete user (and all receipts and items)</h2>";

$userName = $_POST["userName"];
$items = $_POST["items"];
$uID = 0;
$rID = 0;
$iID = array();

//BIG TEST BELOW
//Create user
$qString = "INSERT INTO users (`name`,`creationDate`) VALUES ('" . $userName . "', CURRENT_DATE)";
mysqli_query($conn, $qString);

$qString = "SELECT userID FROM users WHERE name='" . $userName. "'";
$result = mysqli_query($conn, $qString);
if(mysqli_num_rows($result) == 1){
	$row = mysqli_fetch_row($result);
	$uID = $row[0];
	echo "User created! User ID: " . $uID . "<br /><br />";
}else{
	echo "Failed to create user or multiple users.<br /><br />";
}

//Create receipt
$qString = "INSERT INTO receipts (`userID`, `creationDate`) VALUES (" . $uID . ", CURRENT_DATE)";
mysqli_query($conn, $qString);

$qString = "SELECT receiptID FROM receipts WHERE userID =" . $uID;	//This only works because only one receipt was ever created for this user. Won't work in real world.
$result = mysqli_query($conn, $qString);
if(mysqli_num_rows($result) == 1){
	$row = mysqli_fetch_row($result);
	$rID = $row[0];
	echo "Receipt created! Receipt ID: " . $rID . "<br /><br />";
}else{
	echo "Failed to create receipt, or multiple receipts.<br /><br />";
}

/*
for($i = 0; $i < 5; $i++){
	if($items[itemName][$i])
		echo $items[itemName][$i] . ": " . $items[itemPrice][$i] . "<br />";
}
*/


//Add items
$qString = "INSERT INTO items (`userID`, `name`, `price`) VALUES ";
for($i = 0; $i < 5; $i++){
	if($items[itemName][$i])
		$qString = $qString . "(" . $uID . ", '" . $items[itemName][$i] . "', " . $items[itemPrice][$i] . ")";
	if($items[itemName][$i+1])
		$qString = $qString . ", ";
}
mysqli_query($conn, $qString);

$qString = "SELECT itemID FROM items WHERE userID = " . $uID;
$result = mysqli_query($conn, $qString);
$numRows = mysqli_num_rows($result);
if($numRows > 0){
	echo "Success! " . $numRows . " items created. <br /> <br />";
	while($row = mysqli_fetch_row($result)){
		array_push($iID, $row[0]);
	}
}else{
	echo "Error, no items or not 2 items created.<br /><br />";
}


//Link tables
$qString = "INSERT INTO receiptItems(`receiptID`, `itemID`) VALUES ";
for($i = 0; $i < $numRows; $i++){
		$qString = $qString . "(" . $rID . "," . $iID[$i]. ")";
	if($i < $numRows-1)
		$qString = $qString . ", ";
}
mysqli_query($conn, $qString);

//Build receipt
echo "<p>Build an example receipt for Donatello.<br />";
$qString = "SELECT u.name, r.creationDate, r.totalCost, r.tax 
FROM users u 
INNER JOIN receipts r ON r.userID = u.userID 
WHERE u.userID =" . $uID . " AND r.receiptID =" . $rID;
$result = mysqli_query($conn, $qString);
if(mysqli_num_rows($result) > 0){
	$row = mysqli_fetch_row($result);
	$totalCost = $row[2];
	$tax = $row[3];
	echo "<table border=1><tr><th colspan=2>Receipt for " . $row[0] . "</th></tr>";
	echo "<tr><td colspan=2> Created on: " . $row[1] . "</td></tr>";

	$qString = "SELECT i.name, i.description, i.price
	FROM items i 
	INNER JOIN receiptItems ri ON ri.itemID = i.itemID 
	INNER JOIN receipts r ON r.receiptID = ri.receiptID 
	WHERE r.receiptID =" . $rID;
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
	echo "</table><br /><br />";
}else{
	echo "No data found. <br /><br />";
}

//Delete user
echo "Delete by userID, which should automatically delete all receipts and items belonging to him. <br /><br />";

$qString = "DELETE FROM users WHERE userID = " . $uID;
mysqli_query($conn, $qString);

$qString = "SELECT name FROM users WHERE userID = " . $uID;
$result = mysqli_query($conn, $qString);
if(mysqli_num_rows($result) == 0){
	echo "Account was deleted! <br />";
}else{ 
	echo "Account was not deleted! <br />";
}

$qString = "SELECT receiptID FROM receipts WHERE userID = " . $uID;
$result = mysqli_query($conn, $qString);
if(mysqli_num_rows($result) == 0){
	echo "Account's receipt was deleted! <br />";
}else{
	echo "Account's receipt was not deleted! <br />";
}

$qString = "SELECT itemID FROM items WHERE userID = " . $uID;
$result = mysqli_query($conn, $qString);
if(mysqli_num_rows($result) == 0){
	echo "Account's items were deleted! <br />";
}else{
	echo "Account's items were not deleted! <br />";
}

$qString = "SELECT itemID FROM receiptItems WHERE receiptID = " . $rID;
$result = mysqli_query($conn, $qString);
if(mysqli_num_rows($result) == 0){
	echo "Account's data in link table for receipt and items was deleted! <br />";
}else{
	echo "Account's data in link table for receipt and items was not deleted! <br />";
}

?>

<a href="testForm.php" target="_self">Return to test form.</a>