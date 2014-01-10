<?php
session_start();

/*
 * SeruBans - BanItem page
 * Author: Serubin323 (Solomon Rubin)
 * Version: 2.4
 * 
 * for logging create "log/banlist.log"
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
	header("location: item.php");
  }
  if($_POST['password']==$adminPassword){
    $_SESSION['auth']=true;
    $_SESSION['authOp']=true;
    $isOp=true;
	$isMod=true;
	header("location: item.php");
  } else { 
		echo "<h1>You are not logged in!</h1>";
		$resb = false;
  }
}

$GET[]="";
if($_GET['edit']==1)$GET['edit']=1;
else $GET['edit']="";
if( is_numeric($_GET['id']))$id = $_GET['id'];

?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="../util/style.css" />
	<link rel="stylesheet" type="text/css" href="../util/item.css" />
	<title>Escapecraft :: Edit Ban</title>
</head>
<body>
<div id="content">
<?php
$close =  '<input class="button_bar" type="button" value="Close" id="hide" onclick="javascript: window.close();"/>';
//Shows login form
	$logb=true;
if(!$isMod){
	echo "<h1>You are not logged in!</h1>";
	echo $close;
	$logb = false;
}

@$res = mysql_query("SELECT bans.id, p.name as playername, `date`, `display`, `type`, `length`, m.name as modname, `reason` FROM bans, users p,users m WHERE bans.player_id = p.id AND bans.mod = m.id AND bans.id=".$id);

if(mysql_num_rows($res)==1){
	$row = mysql_fetch_array($res);
	$id = $row['id'];
	$player = $row['playername'];
	$mod = $row['modname'];
	$reason = $row['reason'];
	$date = $row['date'];
	$type = $row['type'];
	$length = $row['length'];
	$display = $row['display'];
	$resb = true;
} else if(!$logb){
	echo "<h1>No ban connected to this id!</h1>";
	$resb = false;
}

//English o matic for type
if($type == 1) $type = "Ban";
if($type == 2) $type = "TempBan";
if($type == 3) $type = "Kick";
if($type == 4) $type = "Warning";
if($type == 11) $type = "Unbanned";
if($type == 12) $type = "Untempbanned";
 //English o matic for time
if($length == 0) $length = "N/A";
else $length = date("F jS, Y. g:i A",$length);
$date = date("F jS, Y. g:i A",strtotime($row['date']));

	if($GET['edit']==1)$edit =  '<input class="button_bar" type="button" value="Done" id="hide" onclick="javascript: window.location = \./?id=' . $id . '\';"/>';
	else $edit =  '<input class="button_bar" type="button" value="Edit" id="hide" onclick="javascript: window.location = \'./?id=' . $id . '&edit=1\';"/>';
	$delete="";
	if($isOp)$delete =  '<input class="button_bar" type="button" value="Delete" id="hide" onclick="javascript: window.location = \'delete.php?id=' . $id . '\';"/>';	
	
 if($GET['edit']==1&&$resb&&$logb){
 	echo "<h1> Ban Id: " . $id . $close . $delete . $edit . "</h1>
	<form action='./?id=".$id."' method='post'>
	<p>Player: <span>" . $player . "</span></p>
	<p>Mod: <span>" . $mod . "</span></p>
	<p>Type: <span>" . $type . "</span></p>
	<p>Date: <span>" . $date . "</span></p>
	<p>Reason: <input type='text' size='". strlen($reason) ."'name='reason' value='" . $reason . "'/></p>
	<input type='submit' value='Save' id='hide'/></form>";
 } else if ($resb&&$logb) {
	echo "<h1> Ban Id: " . $id . $close . $delete . $edit . "</h1>
	<p>Player: <span>" . $player . "</span></p>
	<p>Mod: <span>" . $mod . "</span></p>
	<p>Type: <span>" . $type . "</span></p>
	<p>Date: <span>" . $date . "</span></p>
	<p>Reason: <span>" . $reason . "</span></p>";
}

$POST['reason'] = al($_POST['reason'],"1234567890_,.!/'() ");
if($isMod&&$POST['reason']){
	$log = "INSERT INTO `log`(`action`, `banid`, `ip`, `data`) VALUES ('update','" . $id . "','" . $_SERVER['REMOTE_ADDR'] . "','UPDATE:Id=" . $id . "Reason=" . $POST['reason'] . "')";
	$update = "UPDATE `bans` SET `reason`='" . $POST['reason'] . "' WHERE id=".$id;
	mysql_query($log) or die(mysql_error());
	mysql_query($update) or die(mysql_error());
	header("location:./?id=".$id);
}
?>
</div>
<div id="footer">
	<div id="footer-inside" class="toolbar">
		<p>
			Copyright &copy; 2012 <a href="http://dev.bukkit.org/server-mods/serubans/">SeruBans</a>
			| Coded by <a href="http://www.serubin.net/">Solomon Rubin</a>
		</p>
	</div>
</div>
</body>
</html>
<?php
function al($a="") {

  /*****[ © Rich Innovations ]*******************************************************************************

	This strips all non-alphabetic characters from $a. If you want to keep extra characters, then include
	a second parameter with each character you want kept.

	Required Functions: NONE


	- $a		= String to be stripped.

  **********************************************************************************************************/

  $extra = "";
  if (func_num_args() >= 2) $extra = func_get_arg(1);
  $temp = "";
  $aln = "abcdefghijklmnopqrstuvwxyz".$extra;
  for ($b=0;$b<strlen($a);$b++) {
    $temp2 = substr($a,$b,1);
    if (stristr($aln,$temp2)) {
      $temp .= $temp2;
    }
  }
  return $temp;
}
?>