<?php

require "conn.php";

$json 			= file_get_contents('php://input');
$obj 			= json_decode($json);
$userID			= $obj->{'userID'};

$qString = "DELETE FROM users WHERE userID = '$uID'";
mysqli_query($conn, $qString);

//Test deletion
$qString = "SELECT name FROM users WHERE userID = '$uID'";
$result = mysqli_query($conn, $qString);
if(mysqli_num_rows($result) == 0){
	echo "Donatello was deleted! <br />";
}else{ 
	echo "Donatello was not deleted! <br />";
}

?>