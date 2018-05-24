<?php

require "conn.php";

//Get from JSON object the username, nam, and email
$json = file_get_contents('php://input');
$obj = json_decode($json);
$userName = $obj->{'userName'};
$name = $obj->{'name'}

//If they don't exist, just enter dummy data. THIS IS ONLY FOR TESTING PURPOSES. REMOVE IN FINAL
if($userName == ""){
	$userName = "regTest";
}
if($name == ""){
	$name = "registration test";
}
if($email == ""){
	$email = "regTest@example.com";
}

//Create user, return info
$qString = "INSERT INTO users (`userName`,`name`,`creationDate`,`email`) VALUES ('" . $userName . "','" . $name . "', CURRENT_DATE,'" . $email . "')";
mysqli_query($conn, $qString);

$qString = "SELECT userID, userName, name, creationDate FROM users WHERE name='" . $userName. "'";
$result = mysqli_query($conn, $qString);
if(mysqli_num_rows($result) == 1){
	$row = mysqli_fetch_row($result);
	$uID = $row[0];
	$uName = $row[1]
	$gname = $row[2]
	$date = $row[3]
	echo "User created! <br />User ID: " . $uID . " <br />Username: " . $uName . "<br />Real name: " . $gname . "<br />Created on: " . $date . "<br /><br />";
}else{
	echo "Failed to create user or multiple users.<br /><br />";
}

?>