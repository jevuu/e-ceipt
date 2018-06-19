<?php

require "conn.php";

//Get userName from JSON
$json = file_get_contents('php://input');
$obj = json_decode($json);
$userID = $obj->{'userID'};

//If userName doesn't exist, use grab from John Doe. ONLY FOR TESTING PURPOSES. REMOVE IN FINAL
if($userID == ""){
	$userID = "1";
}

//Array of receipts to pass back
$receipts = array();

//Get all receipts from userName
$qString = "SELECT u.name, r.receiptID, r.creationDate, r.totalCost, r.tax, r.businessID 
FROM users u 
INNER JOIN receipts r ON r.userID = u.userID 
WHERE u.userID= '$userID'" ;
$qReceipt= mysqli_query($conn, $qString);
while($receipt= mysqli_fetch_row($qReceipt)){
	
	$qString = "SELECT u.name
	FROM users u
	INNER JOIN businesses b ON b.userID = u.userID
	WHERE b.businessID = $receipt[5] limit 1";
	$qBusiness = mysqli_query($conn, $qString);
	$businessName = mysqli_fetch_object($qBusiness);
	
	//Build an array of items from the receipt
	//Then push into the "items" array
	$items = array();
	$qString = "SELECT i.name, i.description, i.price
	FROM items i 
	INNER JOIN receiptItems ri ON ri.itemID = i.itemID 
	INNER JOIN receipts r ON r.receiptID = ri.receiptID 
	WHERE r.receiptID =" . $receipt[1];
	$qReceiptItems= mysqli_query($conn, $qString);
	while($receiptItem= mysqli_fetch_row($qReceiptItems)){
		$tempItem = [
			'itemName'	=> $receiptItem[0],
			'itemDesc'	=> $receiptItem[1],
			'itemPrice'	=> $receiptItem[2]
		];
		array_push($items, $tempItem);
	}
	
	//Build the receipt array and push into "receipts" array
	$tempReceipt = [
		'name' 			=> $receipt[0],
		'receiptID' 	=> $receipt[1],
		'date' 			=> $receipt[2],
		'totalCost' 	=> $receipt[3],
		'tax' 			=> $receipt[4],
		'items'			=> $items,
		'businessName'	=> $businessName->name
	];
	array_push($receipts, $tempReceipt);
}

//Display the data in json format
echo json_encode($receipts);
?>