<?php

require "conn.php";

//Get from JSON object the username, nam, and email
$json = file_get_contents('php://input');
$obj = json_decode($json);
$userID = $obj->{'userID'};
$name = $obj->{'name'};
$email = $obj->{'email'};

//If they don't exist, just enter dummy data. THIS IS ONLY FOR TESTING PURPOSES. REMOVE IN FINAL
if($userID == ""){
	$userID = "regTest";
}
if($name == ""){
	$name = "registration test";
}
if($email == ""){
	$email = "regTest@example.com";
}

//Create user, return info
$qString = "INSERT INTO users (`userID`,`name`,`creationDate`,`email`) VALUES ('" . $userID . "','" . $name . "', CURRENT_DATE,'" . $email . "')";
mysqli_query($conn, $qString);
/*
$qString = "SELECT userID, name, creationDate, email FROM users WHERE userID='" . $userID. "'";
$result = mysqli_query($conn, $qString);
if(mysqli_num_rows($result) == 1){
	$row = mysqli_fetch_row($result);
	$uID = $row[0];
	$gName = $row[1]
	$date = $row[2]
	$email = $row[3]
	echo "User created! <br />User ID: " . $uID . " <br />Name: " . $gName . "<br />Created on: " . $date . "<br /> Email: " . $email . "<br /><br />";
}else{
	echo "Failed to create user or multiple users.<br /><br />" . $userID . $name . $email;
}
*/
?>