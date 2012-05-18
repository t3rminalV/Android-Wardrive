<?php

	$db_host = 'Localhost';
	$db_user = 'root';
	$db_pass = '';
	$db_database = 'adam'; 

	$link = mysql_connect($db_host,$db_user,$db_pass) or die('Unable to establish a DB connection');

	mysql_select_db($db_database,$link);
	
	$query = "SELECT * FROM data";
	
	$result = mysql_query($query);
?>

<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
		<style type="text/css">
			html { height: 100% }
			body { height: 100%; padding: 0; margin: 0; }
			#map_canvas { position:relative;}	
		</style>
		
		<script type="text/javascript"
			src="http://maps.googleapis.com/maps/api/js?key=AIzaSyAK5VGkvh1rz2Wwpj0BtQOBkGZMEcWqvO0&sensor=false">
		</script>
		
		<script type="text/javascript">
			var myLatlng = new google.maps.LatLng(56.392624, -2.882538);
			
			function initialize() {
				var myOptions = {
					center: myLatlng,
					zoom: 10,
					mapTypeId: google.maps.MapTypeId.ROADMAP
				};
				var map = new google.maps.Map(document.getElementById("map_canvas"),myOptions);
				
				<?php
					while ($row = mysql_fetch_assoc($result)) {
						echo "var marker" . $row['id'] . " = new google.maps.Marker({";
						echo "position: new google.maps.LatLng(" . $row['LAT'] . ", " . $row['LON'] . "),";
						echo "map: map,";
						echo "animation: google.maps.Animation.DROP,";
						echo "title:'" . $row['SSID'] . "'";
						echo "});";
					}	
				?>
				
				
					//position: myLatlng,
					//map: map,
					//animation: google.maps.Animation.DROP,
					//title:''
					
			}
		</script>
	</head>	
	<body onload="initialize()">
		<div id="map_canvas" style="width:100%; height:100%"><div>
	<body>

<?php mysql_free_result($result); ?>