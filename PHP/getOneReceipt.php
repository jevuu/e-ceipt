<?php

require "conn.php";
require "functions.php";

//Get userName from JSON
$json = file_get_contents('php://input');
$obj = json_decode($json);
$receiptID = $obj->{'receiptID'};

$receipt = get_receipt($conn, $receiptID, "one");

//Display the data in json format
echo json_encode($receipt);
?>