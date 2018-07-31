<?php

require "conn.php";
require "functions.php";

$json 			= file_get_contents('php://input');
$obj 			= json_decode($json);
$userID			= $obj->{'userID'};
$receiptID		= $obj->{'receiptID'};
$receiptDate 	= $obj->{'date'};		//This must be a date object
$totalCost 		= $obj->{'totalCost'};
$tax 			= $obj->{'tax'};
$businessName 	= $obj->{'businessName'};
$items 			= $obj->{'items'};
$categoryName	= $obj->{'categoryName'};

//Update Receipt
//Test to see data before change
$q = "SELECT * FROM receipts WHERE receiptID = " . $receiptID;
$result = mysqli_query($conn, $q);
$row = mysqli_fetch_row($result);
//echo "Receipt before: " . $row[0] . ", " . $row[1] . ", " . $row[2] . ", " . $row[3] . ", " . $row[4] . ", " . $row[5] . ", " . $row[6] . ", " . $row[7] . "\n";

$qReceiptString = "UPDATE receipts SET creationDate = '$receiptDate', businessName = '$businessName', totalCost = " . $totalCost . ", tax = " . $tax . ", categoryName = '$categoryName'
WHERE receiptID = " . $receiptID;
mysqli_query($conn, $qReceiptString);

//Test to see data after change
$q = "SELECT * FROM receipts WHERE receiptID = " . $receiptID;
$result = mysqli_query($conn, $q);
$row = mysqli_fetch_row($result);
//echo "Receipt after: " . $row[0] . ", " . $row[1] . ", " . $row[2] . ", " . $row[3] . ", " . $row[4] . ", " . $row[5] . ", " . $row[6] . ", " . $row[7] . "\n\n === \n\n";

//Update Items
$receivedItemIDs = array();
foreach($items as $key => $itm){
	$itemID	   = $itm->itemID;
	$itemName  = $itm->itemName;
	$itemDesc  = $itm->itemDesc;
	$itemPrice = $itm->itemPrice;
	
	if($itemID == -1){
		$itemID = add_item($conn, $userID, $receiptID, $itemName, $itemDesc, $itemPrice);
	}else{
		edit_item($conn, $userID, $receiptID, $itemID, $itemName, $itemDesc, $itemPrice);
	}
	
	array_push($receivedItemIDs, $itemID);
}

//Delete Items
///Compare number of items in JSON receipt with same receipt in DB, and record IDs that are missing from JSON receipt
///These missing IDs will be marked for removal.
$q = "SELECT itemID FROM receiptItems WHERE receiptID = " . $receiptID;
$result = mysqli_query($conn, $q);
$removeItems = array();
$itemsFlipped = array_flip($receivedItemIDs);
while($row = mysqli_fetch_row($result)){
	if(!isset($itemsFlipped[$row[0]])){
		//echo "item " . $row[0] . " is not in received list. marked for removal. \n";
		array_push($removeItems, $row[0]);
	}
}
///Now delete them from database
foreach($removeItems as $i){
	$q = "DELETE FROM items WHERE itemID = " . $i;
	mysqli_query($conn, $q);
}
?>