<?php
error_reporting(E_ERROR | E_PARSE);

$servername = "localhost";
$username = "root";
$password = "root";
$dbname = "ast20207";



// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$result = mysqli_query($conn,"select * from user");
$count = mysqli_num_rows($result);
$id = $count;




$username = $_POST["username"];
$password = $_POST["password"];
$age =  $_POST["age"] != NULL ? $_POST[age] : 0 ;
$email = $_POST["email"];


$sql = "INSERT INTO user(user_id,username,password,age,email)
VALUES ('$id','$username','$password','$age','$email')";
$success = "register success"; 
if ($conn->query($sql) == TRUE) {
    echo $success;
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}
?>