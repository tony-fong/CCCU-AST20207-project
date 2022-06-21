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



$s = "SELECT * FROM user where username = '$username'";
$result = $conn->query($s);

$user['data'] = array();
if ($result->num_rows > 0) {
	while($row = mysqli_fetch_array($result)){
		$temp = array();
		$temp['id'] = $row["user_id"];
		$temp['username'] = $row["username"];
		$temp['email'] = $row["email"];
		$temp['age'] = $row["age"];
		$temp['credits'] = $row["credits"];
		array_push($user['data'],$temp);
	}
	echo json_encode($user['data']);
}else{
	echo "0 result";
}
?>