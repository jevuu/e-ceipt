<?php 

//Index

require "conn.php";

$json = file_get_contents('php://input');
$obj = json_decode($json);
$userName = $obj->{'userName'};

//$userName = $_POST["userName"];
//$password = $_POST["password"];

$qString = "SELECT * FROM test WHERE userName='$userName'";

$result = mysqli_query($conn, $qString);

if(mysqli_num_rows($result) > 0){
	while ($row = mysqli_fetch_row($result)){
		echo "Success! ";
		echo $row[0] . " " . $row[2] . " " . $row[3] . " " . $row[4];
	}
}else{

	//var_dump($_POST);
	echo "Failed. Maybe something went wrong? Here's the query: " . $qString;
	//echo "var dump: " . $varDump;
}

?>