<?php
require "conn.php";
$username = $_POST['username'];
$pass = $_POST['password'];
$sql = "SELECT * from table_name
		WHERE username = :username AND
		password = :pass";
	
$namedParameters = array();
$namedparameters[':username'] = $username;
$namedparameters[':pass'] = $pass;

$statement = $conn->prepare($sql);
$statement->execute($namedParameters);
$result = $statement->fetch();

if(!empty($result))
{
	echo $result['username'] . " has logged in";
}else
{
	echo "Wrong username or password.";
}

?>