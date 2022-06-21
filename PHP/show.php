<?php
$servername = "localhost";
$username = "root";
$password = "root";
$dbname = "ast20207";


$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$s = "SELECT * FROM movie";
$result = $conn->query($s);

$movie['data'] = array();
if ($result->num_rows > 0) {
	while($row = mysqli_fetch_array($result)){
		$temp = array();
		$temp['name'] = $row["name"];
		$temp['prodution'] = $row["Production"];
		$temp['time'] = $row["time"];
		$temp['price'] = $row["price"];
		$temp['image'] = $row["image"];
		array_push($movie['data'],$temp);
	}
	echo json_encode($movie['data']);
}else{
	echo "0 result";
}
?>
