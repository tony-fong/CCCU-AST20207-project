<?php

$servername = "localhost";
$username = "root";
$password = "root";
$dbname = "ast20207";

$conn = new mysqli($servername, $username, $password, $dbname);

$username = $_POST['username'];
$moiveID = $_POST["id"];

// Create connection

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
$sql ="INSERT INTO `user_order`(`user_id`, `moive_id`)
 VALUES ((select user_id FROM user where username = '$username'),$moiveID)";
if ($conn->query($sql) === TRUE) {
	echo "add to sohpping car";
}
 else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}
?>

