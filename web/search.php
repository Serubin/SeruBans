<?php
/*
 * SeruBans - search script
 * Author: Serubin323 (Solomon Rubin)
 * Version: 2.4
 * 
 * for logging create "log/banlist.log"
 *
 * Fill the following with mysql info.
 */
	include("config.php");
	
	if(!isset($_GET['admin'])){
		$_GET['admin'] = "";
	}
	
	$query = "SELECT * FROM `users` WHERE `name` LIKE '%".$_GET['search']."%' LIMIT 5";
	$res = mysql_query($query);
	$hide = "";
	if($_GET['admin']){
		$hide = "&admin=1";
	}
	if($res){
		if(mysql_num_rows($res) != 0){
			echo "<ul>";
			while($row=mysql_fetch_array($res)) {
				echo "<li><a href='./?search=".$row['name']."&col=".$_GET['col'].$hide."'>".$row['name']."</a>"."</li>";
			}
			echo "</ul>";
		}
	}
?>