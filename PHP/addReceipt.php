<?php

require "conn.php";


$json 			= file_get_contents('php://input');
$obj 			= json_decode($json);
$userName 		= $obj->{'userName'};
$receiptDate 	= $obj->{'date'};		//This must be a date object
$totalCost 		= $obj->{'totalCost'};
$tax 			= $obj->{'tax'};
$businessName 	= $obj->{'businessName'};
$items 			= $obj->{'items'};

//
$uID = 0;
$rID = 0;
$iID = array();

//For testing purposes
/*$userName = "johnDoe";
$receiptDate = date("Y-m-d");
$totalCost = 100;
$tax = 13;
$items = array(
	array("name"=>"Sample Item", "description"=>"Sample description", "price"=>"100"),
	array("name"=>"Sample Item", "description"=>"Sample description", "price"=>"100")
);
$itemCount = count($items);
foreach($items as $key=>$itm){
	echo $itm['name'];
	if($key < $itemCount - 1){
		echo ", ";
	}
}
*/

//	GET USER ID
$qUsersString = "SELECT userID FROM users WHERE userName = '$userName'";
$result = mysqli_query($conn, $qUsersString);
$row = mysqli_fetch_row($result);
$uID = $row[0];
//echo "User ID found: " . $uID;	

// INSERT RECEIPT
$qReceiptString = "INSERT INTO receipts (`userID`, `creationDate`, `totalCost`, `tax`) 
	VALUES(" . $uID . ", '$receiptDate', " . $totalCost . ", " . $tax . ")";
if(mysqli_query($conn, $qReceiptString) === FALSE){
	echo "An error occured creating the receipt.";
}

// GET receiptID
$qReceiptString = "SELECT receiptID FROM receipts WHERE userID = " . $uID . " ORDER BY receiptID DESC LIMIT 1";
$result = mysqli_query($conn, $qReceiptString);
$row = mysqli_fetch_row($result);
$rID = $row[0];
//echo "Receipt ID found: " . $rID;	

//Add items by first building the query string.
$qItemsString = "INSERT INTO items(`userID`, `name`, `description`, `price`) VALUES";
$itemCount = count($items); //Loop through $items and append data from it into the string
foreach($items as $key => $itm){
	$itemName  = $itm['name'];
	$itemDesc  = $itm['description'];
	$itemPrice = $itm['price'];
	$qItemsString .= "(" . $uID .", '$itemName', '$itemDesc', " . $itemPrice . ")";
	if($key < $itemCount - 1){
		$qItemsString = $qItemsString . ", ";
	}
}
if(mysqli_query($conn, $qItemsString) === FALSE){
	echo "An error has occured. Unable to insert items into database.";
}

//Get the itemIDs of the new items
$qString = "SELECT itemID FROM items WHERE userID = " . $uID ." ORDER BY itemID DESC LIMIT " . $itemCount;
$result = mysqli_query($conn, $qString);
while($row = mysqli_fetch_row($result)){
	array_push($iID, $row[0]);
	//echo "Item ID found: " . $iID[0];
}

//Link tables
$qLinkString = "INSERT INTO receiptItems(`receiptID`, `itemID`) VALUES";
for($i = 0; $i < $itemCount; $i++){
	$qLinkString .= "(" . $rID . "," . $iID[$i] . ")";
	if($i < $itemCount - 1){
		$qLinkString .= ", ";
	}
}
if(mysqli_query($conn, $qLinkString) === FALSE){
	echo "An error has occured. Unable to link items to receipt.";
}

?>