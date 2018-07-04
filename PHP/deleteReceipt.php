<?php

require "conn.php";

$json 			= file_get_contents('php://input');
$obj 			= json_decode($json);
$rID			= $obj->{'receiptID'};

$qString = "DELETE FROM receipts WHERE receiptID = " . $rID;
mysqli_query($conn, $qString);

//Test deletion
/*$qString = "SELECT FROM receipts WHERE receiptID = " . $rID;
$result = mysqli_query($conn, $qString);
if(mysqli_num_rows($result) == 0){
	echo "Receipt was deleted! <br />";
}else{ 
	echo "Receipt was not deleted! <br />";
}*/

?>