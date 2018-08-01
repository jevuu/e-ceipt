<?php

require "conn.php";
require "functions.php";

//Get userName from JSON
$json = file_get_contents('php://input');
$obj = json_decode($json);
$userID = $obj->{'userID'};

$receipts = get_receipt($conn, $userID, "user");

//Display the data in json format
echo json_encode($receipts);
?>