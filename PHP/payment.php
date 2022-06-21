<?php
$servername = "localhost";
$username = "root";
$password = "root";
$dbname = "ast20207";


$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$username = $_POST["username"];
$price = $_POST["price"];


$credits = $price / 10;
$s = "DELETE FROM user_order where user_id IN (SELECT user_id from user where username = '$username')";
if($result = $conn->query($s)){
	//echo "delete";
}else{
	  echo "Error deleting record: " . mysqli_error($conn);
}

$s = "SELECT credits FROM user WHERE username = '$username'";
$result = $conn->query($s);


while($row = mysqli_fetch_array($result)){
	$old_credit = $row['credits'];
}

echo $old_credit;
$credits = $credits + $old_credit;


$s = "UPDATE user SET credits = '$credits' WHERE username = '$username'";
if($result = $conn->query($s)){
	echo "payment accect";
}else{
	  echo "Error deleting record: " . mysqli_error($conn);
}
?>