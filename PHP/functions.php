<?php
//General functions go in this file

//Build receipt
function build_receipt($conn, $row){
		
	/*Get business name from Business table
	$qString = "SELECT u.name
	FROM users u
	INNER JOIN businesses b ON b.userID = u.userID
	WHERE b.businessID = $receipt[5] limit 1";
	$qBusiness = mysqli_query($conn, $qString);
	$businessName = mysqli_fetch_object($qBusiness);
	*/

	//Build an array of items from the receipt
	//Then push into the "items" array
	$items = array();
	$qString = "SELECT i.name, i.description, i.price, i.itemID
	FROM items i 
	INNER JOIN receiptItems ri ON ri.itemID = i.itemID 
	INNER JOIN receipts r ON r.receiptID = ri.receiptID 
	WHERE r.receiptID =" . $row[1];
	$qReceiptItems= mysqli_query($conn, $qString);
	while($receiptItem= mysqli_fetch_row($qReceiptItems)){
		$tempItem = [
			'itemName'	=> $receiptItem[0],
			'itemDesc'	=> $receiptItem[1],
			'itemPrice'	=> $receiptItem[2],
			'itemID'	=> $receiptItem[3]
		];
		array_push($items, $tempItem);
	}

	/*
	// GET categoryName
	$qCategoryString = "SELECT name FROM categories WHERE categoryID = " . $receipt[6];
	$result = mysqli_query($conn, $qCategoryString);
	$row = mysqli_fetch_row($result);
	$categoryName = $row[0];
	*/

	//Build the receipt
	$receipt = [
		'name' 			=> $row[0],
		'receiptID' 	=> $row[1],
		'date' 			=> $row[2],
		'totalCost' 	=> $row[3],
		'tax' 			=> $row[4],
		'items'			=> $items,
		//'businessName'	=> $businessName->name	//Not used as business is stored in receipt now
		'businessName'	=> $row[5],
		'categoryName'	=> $row[6]
	];

	return $receipt;
}

//Get Receipt (Supports getting all user receipts and by ID)
//option must be either "user" for user receipts or "one" for one receipt
//if option is "user", ID must be userID (string)
//if option is "one", ID must be receiptID (int)
function get_receipt($conn, $ID, $option){
	$q = "SELECT userID, receiptID, creationDate, totalCost, tax, businessName, categoryName FROM receipts";
	if($option == "user"){
		$receipts = array();
		
		$q = $q . " WHERE userID = '$ID'" ;
		$qReceipt= mysqli_query($conn, $q);
		while($row= mysqli_fetch_row($qReceipt)){
			$receipt = build_receipt($conn, $row);
			array_push($receipts, $receipt);
		}
		
		return $receipts;
	}else if ($option == "one"){
		$q = $q . " WHERE receiptID = " . $ID;
		$qReceipt = mysqli_query($conn, $q);
		$row = mysqli_fetch_row($qReceipt);
		
		//$receipt = build_receipt($row);
		return build_receipt($conn, $row);
	}else{
		echo "Error: No option provided. \n\n";
	}
}

//Add Item
function add_item($conn, $userID, $receiptID, $itemName, $itemDesc, $itemPrice){
	//echo "add_item called! '$userID', " . $receiptID . ", '$itemName', '$itemDesc', " . $itemPrice . "\n";
	
	$q = "INSERT INTO items (`userID`, `name`, `description`, `price`) VALUES ('$userID', '$itemName', '$itemDesc', " . $itemPrice . ")";
	mysqli_query($conn, $q);
	
	//Get the itemIDs of the new items
	$q = "SELECT itemID FROM items WHERE userID = '$userID' ORDER BY itemID DESC LIMIT 1";
	$result = mysqli_query($conn, $q);
	$row = mysqli_fetch_row($result);
	$itemID = $row[0];
	//echo "Item ID: " . $row[0] . "\n";
	
	//Link tables
	$q = "INSERT INTO receiptItems (`receiptID`, `itemID`) VALUES (" . $receiptID . "," . $itemID . ")";
	if(mysqli_query($conn, $q) === FALSE){
		echo "An error has occured. Unable to link items to receipt. /n/n";
	}else{
		$q = "SELECT * FROM items WHERE itemID = " . $itemID;
		$result = mysqli_query($conn, $q);
		$row = mysqli_fetch_row($result);
		//echo "Item new   : " . $row[0] . ", " . $row[1] . ", " . $row[2] . ", " . $row[3] . ", " . $row[4] . "\n\n";
	}
	
	return $itemID;
}

//Edit Item
function edit_item($conn, $userID, $receiptID, $itemID, $itemName, $itemDesc, $itemPrice){
	//Test item before update
	$q = "SELECT * FROM items WHERE itemID = " . $itemID;
	$result = mysqli_query($conn, $q);
	$row = mysqli_fetch_row($result);
	//echo "Item before: " . $row[0] . ", " . $row[1] . ", " . $row[2] . ", " . $row[3] . ", " . $row[4] . "\n";

	$qItemsString = "UPDATE items SET name = '$itemName', description = '$itemDesc', price = '$itemPrice' WHERE itemID = " . $itemID;
	mysqli_query($conn, $qItemsString);
	
	//Test item after update
	$q = "SELECT * FROM items WHERE itemID = " . $itemID;
	$result = mysqli_query($conn, $q);
	$row = mysqli_fetch_row($result);
	//echo "Item after : " . $row[0] . ", " . $row[1] . ", " . $row[2] . ", " . $row[3] . ", " . $row[4] . "\n\n";
}
?>