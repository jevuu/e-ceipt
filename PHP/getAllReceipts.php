<?php

require "conn.php";

$json = file_get_contents('php://input');
$obj = json_decode($json);
$userName = $obj->{'userName'};

if($userName == ""){
	$userName = "John Doe";
}

$qString = "SELECT u.name, r.receiptID, r.creationDate, r.totalCost, r.tax 
FROM users u 
INNER JOIN receipts r ON r.userID = u.userID 
WHERE u.name= '$userName'" ;
$qReceipt= mysqli_query($conn, $qString);
if(mysqli_num_rows($qReceipt) > 0){
	while($receipt= mysqli_fetch_row($qReceipt)){
		$totalCost = $receipt[3];
		$tax = $receipt[4];
		echo "<table border=1><tr><th colspan=2>Receipt for " . $receipt[0] . "</th></tr>";
		echo "<tr><td>Receipt: " . $receipt[1] . "</td><td> Created on: " . $receipt[2] . "</td></tr>";

		$qString = "SELECT i.name, i.description, i.price
		FROM items i 
		INNER JOIN receiptItems ri ON ri.itemID = i.itemID 
		INNER JOIN receipts r ON r.receiptID = ri.receiptID 
		WHERE r.receiptID =" . $receipt[1];
		$qReceiptItems= mysqli_query($conn, $qString);
		if(mysqli_num_rows($qReceiptItems) > 0){
			if ($totalCost > 0)
				$dontSum = true;
			while($receiptItem= mysqli_fetch_row($qReceiptItems)){
				echo "<tr><td>" . $receiptItem[0] . "<br />" . $receiptItem[1] . "</td><td>" . $receiptItem[2] . "</td></tr>";
				if(!$dontSum)
					$totalCost += $receiptItem[2];
			}
		}else{
			echo "<tr><td colspan=2> No items found. </td></tr>";
		}
		echo "<tr><td>Total</td><td>" . $totalCost . "</td></tr>";
		echo "</table>";
	}
}else{
	echo "No data found.";
}

?>