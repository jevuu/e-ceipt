<?php 

//Index

require "conn.php";

$qString = "select * from test";

$result = mysqli_query($conn, $qString);

if(mysqli_num_rows($result) > 0){
	while ($row = mysqli_fetch_row($result)){
		echo $row[0] . " " . $row[1] . " " . $row[2] . " " . $row[3] . "<br />";
	}
}else{
	echo "No data.";
}

?>
<!--
<form action="login.php" method="post">
<input type="text" name="userName">
<input type="submit">
</form>
--!>