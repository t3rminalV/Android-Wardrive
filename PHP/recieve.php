<?php

	$db_host = 'Localhost';
	$db_user = 'root';
	$db_pass = '';
	$db_database = 'adam'; 

	$link = mysql_connect($db_host,$db_user,$db_pass) or die('Unable to establish a DB connection');

	mysql_select_db($db_database,$link);

	$data = $_GET['data'];
	$make = $_GET["make"];
	$model = $_GET["model"];
	
	$rows = preg_split("/!!!/", $data);
	
	for ($i=0; $i<sizeof($rows)-1; $i++) {
		$cols = preg_split("/,/", $rows[$i]);
		$result = mysql_query("SELECT * FROM data WHERE BSSID='" . $cols[1] . "'");
		if (!$row = mysql_fetch_assoc($result))
		{
			mysql_query("INSERT INTO data (SSID,BSSID,LAT,LON,SECURITY,MAKE,MODEL) VALUES ('".$cols[0]."','".$cols[1]."','".$cols[5]."','".$cols[6]."','".$cols[2]."','".$make."','".$model."')");
		}
	}		
?>