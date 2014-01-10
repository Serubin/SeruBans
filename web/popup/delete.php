<?php
session_start();

/*
 * SeruBans - BanItem Delete page
 * Author: Serubin323 (Solomon Rubin)
 * Version: 2.4
 * 
 * for logging create "log/banlist.log"
 *
  *
 * Fill the following the config location. (must end with 'config.php')
 */
 $config = "../config.php";
/*
 * The following should not be touched unless you know what your doing
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

include($config);
 
$authfail = false;
//CHECK MOD
$isMod=isset($_SESSION['auth']);
$isOp=isset($_SESSION['authOp']);
if(isset($_POST['password'])&&!$isMod){
file_put_contents("/var/log/banlist.log",date('l jS F Y h:i:s A',time()) . " :: " . $_SERVER['REMOTE_ADDR'] . "\n",FILE_APPEND);
  if($_POST['password']==$password){
    $_SESSION['auth']=true;
    $isMod=true;
	header("location: index.php?admin");
  }
  if($_POST['password']==$adminPassword){
    $_SESSION['auth']=true;
    $_SESSION['authOp']=true;
    $isOp=true;
	$isMod=true;
	header("location: index.php?admin");
  }
  else header("location: index.php");
}

$id = nu($_GET['id']);
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="../util/style.css" />
	<link rel="stylesheet" type="text/css" href="../util/item.css" />
	<title>Escapecraft :: Delete Ban</title>
</head>
<body>
<div id="content">
<?php
if($isOp){
	if($_POST['delete']){
		$delete = "DELETE FROM `bans` WHERE `id`=".$id;
		$log = "INSERT INTO `log`(`action`, `banid`, `ip`, `data`) VALUES ('update','" . $id . "','" . $_SERVER['REMOTE_ADDR'] . "','DELETE:BanId=" . $id . "')";
		mysql_query($log) or die(mysql_error());
		mysql_query($delete) or die(mysql_error());
		echo "<h2>Ban Deleted</h2>";
		echo "<p><input type='button' value='Close' id='hide' name='delete' onclick=\"javascript: window.close();\"/>";
	} else {
		echo '<h1>Are you sue you want to delete ban number'.$id.'?</h1>';
		echo '<p>Warning! This will delete the ban item, if the player is currently banned, this will unban them at next restart! Please do not use this as a method of unbanning!</p>';
		echo '<form action="delete.php?id='.$id.'" method="post"><input type="hidden" name="delete" value="'.$id.'/"><input class="button_bar" type="submit" value="Yes" id="hide"/></form>';
		echo '<input class="button_bar" type="button" value="No" id="hide" onclick="javascript: window.close();"/>';		
	}
}
?>
</div>
</body>
</html>
<?php

function nu($a="") {

  $extra = "";
  if (func_num_args() >= 2) $extra = func_get_arg(1);
  $temp = "";
  $aln = "1234567890".$extra;
  for ($b=0;$b<strlen($a);$b++) {
    $temp2 = substr($a,$b,1);
    if (stristr($aln,$temp2)) {
      $temp .= $temp2;
    }
  }
  return $temp;
}
?>