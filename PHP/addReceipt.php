<?php

require "conn.php";
require "functions.php";

$json 			= file_get_contents('php://input');
$obj 			= json_decode($json);
$userID			= $obj->{'userID'};
$receiptDate 	= $obj->{'date'};		//This must be a date object
$totalCost 		= $obj->{'totalCost'};
$tax 			= $obj->{'tax'};
$businessName 	= $obj->{'businessName'};
$items 			= $obj->{'items'};
$categoryName	= $obj->{'categoryName'};

// INSERT RECEIPT
$qReceiptString = "INSERT INTO receipts (`userID`, `businessName`, `creationDate`, `totalCost`, `tax`, `categoryName`) 
	VALUES('$userID', '$businessName', '$receiptDate', " . $totalCost . ", " . $tax . ", '$categoryName')";
if(mysqli_query($conn, $qReceiptString) === FALSE){
	echo "An error occured creating the receipt. \n";
}

// GET receiptID
$qReceiptString = "SELECT receiptID FROM receipts WHERE userID = '$userID' ORDER BY receiptID DESC LIMIT 1";
$result = mysqli_query($conn, $qReceiptString);
$row = mysqli_fetch_row($result);
$receiptID = $row[0];
//echo "Receipt ID found: " . $rID;	

foreach($items as $key => $itm){
	$itemName  = $itm->itemName;
	$itemDesc  = $itm->itemDesc;
	$itemPrice = $itm->itemPrice;
	
	add_item($conn, $userID, $receiptID, $itemName, $itemDesc, $itemPrice);
}

?>