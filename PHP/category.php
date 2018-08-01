<?php

require "conn.php";

//Get from JSON object and the option
$json 	= file_get_contents('php://input');
$obj 	= json_decode($json);
$option = $obj->{'option'};

/*JSON shape for each option:
getAllCategory:	userID

getOneCategory:	categoryID

addCategory: 	userID
				categoryName

deleteCategory:	categoryID

editCategory:	categoryID
				categoryName
			  
Data types:
categoryID: 	int
userID: 		string
categoryName: 	string
*/

//GET ALL CATEGORIES
if($option == "getAllCategory"){
	$userID			= $obj->{'userID'};
	$categoryList 	= array();
	
	if($userID != ""){
		$qGetAllQuery = "SELECT categoryID, name FROM categories WHERE userID = '$userID'";
		$result = mysqli_query($conn, $qGetAllQuery);
		while($categoryDB = mysqli_fetch_row($result)){
			$tempCategory = [
				'categoryID' 	=> $categoryDB[0],
				'categoryName'	=> $categoryDB[1],
			];
			array_push($categoryList, $tempCategory);
		}
		echo json_encode($categoryList);
	}else{
		errMsg("No userID.");
	}
//GET CATEGORY BY ID
}else if($option == "getOneCategory"){
	$categoryID		= $obj->{'categoryID'};
	//$categoryList 	= array();
	
	if($categoryID != ""){
		$qGetOneQuery = "SELECT name FROM categories WHERE categoryID = " . $categoryID;
		$result = mysqli_query($conn, $qGetOneQuery);
		$categoryDB = mysqli_fetch_row($result);
		$categoryList = [
			'categoryName'	=> $categoryDB[0]
		];
		echo json_encode($categoryList);
	}else{
		errMsg("Incorrect categoryID.");
	}
//ADD CATEGORY
}else if($option == "addCategory"){
	$userID 	= $obj->{'userID'};
	$catName 	= $obj->{'categoryName'};
	
	if($userID == ""){
		errMsg("No userID.");
	}else if($catName == ""){
		errMsg("No category name.");
	}else if($userID != "" && $catName != ""){
		//First check if category name doesn't already exist.
		//If it doesn't, then insert
		$qCheckQuery = "SELECT name FROM categories WHERE userID = '$userID' AND name = '$catName'";
		$result = mysqli_query($conn, $qCheckQuery);
		$row = mysqli_fetch_row($result);
		
		if (count($row) > 0){
			errMsg("Category name (" . $catName . ") already exists. ");
		}else{
			$qAddQuery = "INSERT INTO categories (`userID`, `name`) VALUES ('$userID', '$catName')";
			if(mysqli_query($conn, $qAddQuery) === false){
				errMsg("Query failed. \n Query string: " . $qAddQuery);
			}
		}
	}else{
		errMsg("Unexpected error adding category.");
	}
//DELETE CATEGORY
}else if($option == "deleteCategory"){
	$categoryID = $obj->{'categoryID'};
	
	if($categoryID != ""){
		$qDeleteQuery = "DELETE FROM categories WHERE categoryID = " . $categoryID;
		mysqli_query($conn, $qDeleteQuery);
	}else{
		errMsg("Incorrect categoryID.");
	}
//EDIT CATEGORY
}else if($option == "editCategory"){
	$categoryID		= $obj->{'categoryID'};
	$catName 	= $obj->{'categoryName'};
	
	if($categoryID != "" && $categoryName != ""){
		
		//Test to see data before change
		//$q = "SELECT * FROM categories WHERE categoryID = " . $categoryID;
		//$result = mysqli_query($conn, $q);
		//$row = mysqli_fetch_row($result);
		//echo $row[0] . ", " . $row[1] . ", " . $row[2] . "\n";
		
		$qEditQuery = "UPDATE categories SET name = '$catName' WHERE categoryID = " . $categoryID;
		mysqli_query($conn, $qEditQuery);
		
		//Test to see data after change
		//$q = "SELECT * FROM categories WHERE categoryID = " . $categoryID;
		//$result = mysqli_query($conn, $q);
		//$row = mysqli_fetch_row($result);
		//echo $row[0] . ", " . $row[1] . ", " . $row[2];
	}else if($categoryID == "" && $categoryName == ""){
		errMsg("No categoryID or name.");
	}else if($categoryID == ""){
		errMsg("No categoryID.");
	}else if($categoryName == ""){
		errMsg("No category name.");
	}else{
		errMsg("Unrecognized error editing category.");
	}
}else{
	echo "Error: Unrecognized option.";
}

function errMsg(string $msg){
	echo "Error: " . $msg;
}
//$userID = $obj->{'userID'};
?>