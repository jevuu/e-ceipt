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
echo "Receipt before: " . $row[0] . ", " . $row[1] . ", " . $row[2] . ", " . $row[3] . ", " . $row[4] . ", " . $row[5] . ", " . $row[6] . ", " . $row[7] . "\n";

$qReceiptString = "UPDATE receipts SET creationDate = '$receiptDate', businessName = '$businessName', totalCost = " . $totalCost . ", tax = " . $tax . ", categoryName = '$categoryName'
WHERE receiptID = " . $receiptID;
mysqli_query($conn, $qReceiptString);

//Test to see data after change
$q = "SELECT * FROM receipts WHERE receiptID = " . $receiptID;
$result = mysqli_query($conn, $q);
$row = mysqli_fetch_row($result);
echo "Receipt after: " . $row[0] . ", " . $row[1] . ", " . $row[2] . ", " . $row[3] . ", " . $row[4] . ", " . $row[5] . ", " . $row[6] . ", " . $row[7] . "\n\n === \n\n";

//Update Items
foreach($items as $key => $itm){
	$itemID	   = $itm->itemID;
	$itemName  = $itm->itemName;
	$itemDesc  = $itm->itemDesc;
	$itemPrice = $itm->itemPrice;
	
	if($itemID == -1){
		add_item($conn, $userID, $receiptID, $itemName, $itemDesc, $itemPrice);
	}else{
		edit_item($conn, $userID, $receiptID, $itemID, $itemName, $itemDesc, $itemPrice);
	}
}

?>