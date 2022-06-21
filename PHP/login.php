<?php
$con = mysqli_connect("localhost","root","root");
if (!$con)
  {
  die('Could not connect: ' . mysqli_error());
  }

mysqli_select_db($con,"ast20207");
$login = $_POST["login"];
$password = $_POST["password"];
//$login = "admin";
//$password = "admin";
$result = mysqli_query($con,"SELECT * FROM user Where username = '$login' and password = '$password'");
$row = mysqli_fetch_row($result);

//$data = ['login' => $row[0] ,'password'=>$row[1] ];
// Mysql_num_row is counting table row
$count=mysqli_num_rows($result);

// If result matched $myusername and $mypassword, table row must be 1 row
echo $count;



?>