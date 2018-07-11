<?php

require "conn.php";

$json 			= file_get_contents('php://input');
$obj 			= json_decode($json);
$rID			= $obj->{'receiptID'};

//Delete receipt
$qString = "DELETE FROM receipts WHERE receiptID = " . $rID;
if(mysqli_query($conn, $qString) === FALSE){
	echo "Failed to delete receipt.";
}

//Test deletion
/*$qString = "SELECT FROM receipts WHERE receiptID = " . $rID;
$result = mysqli_query($conn, $qString);
if(mysqli_num_rows($result) == 0){
	echo "Receipt was deleted! <br />";
}else{ 
	echo "Receipt was not deleted! <br />";
}*/

?>