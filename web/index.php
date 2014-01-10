<?php
session_start();

/*
 * SeruBans - Banlist
 * Author: Serubin323 (Solomon Rubin)
 * Version: 2.4
 * 
 * for logging create "log/banlist.log"
 *
  *
 * Fill the following the config location. (must end with 'config.php')
 */
 $config = "config.php";
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
 
 if(!isset($_GET['admin'])){
	$_GET['admin'] = "";
 }
 if(!isset($_GET['col'])){
	$_GET['col'] = "";
 }
 if(!isset($_GET['search'])){
	$_GET['search'] = "";
 }
 if(!isset($_GET['page'])){
	$_GET['page'] = "";
 }
 if(!isset($_POST['hide'])){
	$_POST['hide'] = "";
 }
 if(!isset($_POST['delete'])){
	$_POST['delete'] = "";
 }
 
$authfail = false;
//CHECK MOD
$isMod=isset($_SESSION['auth']);
$isOp=isset($_SESSION['authOp']);
if(isset($_POST['password'])&&!$isMod){
file_put_contents("/var/log/banlist.log",date('l jS F Y h:i:s A',time()) . " :: " . $_SERVER['REMOTE_ADDR'] . "\n",FILE_APPEND);
  if($_POST['password']==$password){
    $_SESSION['auth']=true;
    $isMod=true;
	header("location: ./?admin");
  }
  if($_POST['password']==$adminPassword){
    $_SESSION['auth']=true;
    $_SESSION['authOp']=true;
    $isOp=true;
	$isMod=true;
	header("location: ./?admin");
  }
  else header("location: ./");
}

$colCheck=array("player", "id", "mod");
$limit1 = 0;
$limit2 = 50;

$_GET['admin'] = floor(abs($_GET['admin']));
if(in_array($_GET['col'], $colCheck))$_GET['col']= $_GET['col'];
$_GET['search'] = substr(al($_GET['search'],"0123456789_"),0,16);
$_GET['page'] = floor(abs($_GET['page']));

if(!$_GET['col']){
	$_GET['col'] = "";
}

//HIDE ROW
if($_GET['admin']==1&&$isMod){
$hidecheck=1;
$hideupdate=0;
$hidetext='Show';
$hideMethod="./?admin=1";
}else{
$hidecheck=0;
$hideupdate=1;
$hidetext='Hide';
$hideMethod="./?admin";
}
//Hides item
$sqlhide="UPDATE bans SET display=" . $hideupdate . " WHERE id=".$_POST['hide'];
@$reshide = mysql_query($sqlhide);
//Deletes item
$sqlhide="DELETE FROM bans WHERE id=".$_POST['delete'];
@$reshide = mysql_query($sqlhide);

$table = 'util/cuboidcreative.htm';
$nav = 'util/nav.php';
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="util/style.css" />
<title>Escapecraft :: Banlist</title>
<script type="text/javascript">
function editPopup(id){
    mywindow = window.open("popup/?id="+id, "mywindow", "status=1,width=500,height=375");
}
function searchHint(searchterm,col,admin){
	document.getElementById("sugg-text").innerHTML="";
		if(document.getElementById('searchbox').value.length > 2 && col != "id"){
			var xmlhttp;
			if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp=new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange=function(){
				if (xmlhttp.readyState==4 && xmlhttp.status==200){
					document.getElementById("sugg-text").innerHTML=xmlhttp.responseText;
				}
			}
			var hide = "";
			if(admin){
				hide = "&admin=1";
			}
			xmlhttp.open("GET","search.php?search="+searchterm+"&col="+col+hide,true);
			xmlhttp.send();
		}
}
function hideSearchHint(close){
	if(close){
		document.getElementById("sugg-text").innerHTML="";
	}
}
</script>
</head>
<body onclick='javascript:hideSearchHint(true);'>
<div id="header">
<!--Logo-->
<div id="title">
<a href="http://dev.bukkit.org/server-mods/serubans"><img src="util/img/blanklogo.gif" width="416" height="112" alt="Er... Oops?" /></a>
</div>
<div id="header_right">
<?php
//Shows login form
if(!$isMod){
echo '<form action="./" method="post" id="loginform">';
echo 'Password: <input type="password" title="Password" name="password" value="********" onfocus="if (this.value == \'********\') {this.value = \'\';}" onblur="if (this.value == \'\') {this.value = \'********\';}" />';
echo '<input type="submit" value="Login" id="login"/>';
echo '</form>';
}
else{
//logout button
echo '<div id="logoutform"><input type="button" value="Log Out" id="login" onclick="javascript: window.location = \'logout.php\';"/></div>';
if($_GET['admin']==1)echo '<div id="logoutform"><input type="button" value="Shown Items" id="hide" onclick="javascript: window.location = \'./?admin\';"/></div>';
else echo '<div id="logoutform"><input type="button" value="Hidden Items" id="hide" onclick="javascript: window.location = \'./?admin=1\';"/></div>';
}
?>
</div>
</div>
<div id="content">
<?php
$search = "";
$searchValue = "";
//filter
if($_GET['search']){
	$searchValue = $_GET['search'];
}
if($isMod) $clearAction = "./?admin";
if($_GET['admin']==1&&$isMod) $clearAction = "./?admin=1";
else $clearAction = "./";
echo '<form action="./" method="get" class="search"><ul id="suggestions"><li><span style="font-weight:bold;">Search: </span><select name="col" value="id" id="col">';
if($_GET['col']=="id"){
	echo '<option value="id">Id</option>
	<option value="player">Player</option>
	<option value="mod">Mod</option>';
}
else if($_GET['col']=="mod"){
	echo '<option value="mod">Mod</option>
	<option value="player">Player</option>
	<option value="id">Id</option>';
}
else{
	echo '<option value="player">Player</option>
	<option value="id">Id</option>
	<option value="mod">Mod</option>';
}
echo '</select>
<input type="text" name="search" id="searchbox" autocomplete="off" onkeyup="javascript:searchHint(this.value,document.getElementById(\'col\').value,'.$_GET['admin'].');" onfocus="javascript:searchHint(this.value,document.getElementById(\'col\').value,'.$_GET['admin'].')" value="'.$searchValue.'"/><input type="submit" value="Submit"><input type="button" value="Clear" onclick="javascript: window.location = \''.$clearAction.'\';"/></li>
<li id="sugg-text" onclick=\'javascript:hideSearchHint(false);\'></li>
</ul>';
if($_GET['admin']==1&&$isMod)echo '<input type="text" value="1" name="admin" style="display:none;"/>';
echo '</form>';

if($_GET['search']){
	if($_GET['col']=="id")$col = "bans.id";
	else if($_GET['col']=="player")$col = "p.name";
	else if($_GET['col']=="mod")$col = "m.name";
	else $col = "p.name";
	$search=" AND ".$col." LIKE '%".$_GET['search']."%'";
	if($_GET['col'] == "id"){
		$search=" AND ".$col." = '".$_GET['search']."'";
	}
	if($_GET['admin']==1&&$isMod){
		$hidecheck = 1;
	}
}
//LIMIT

if($_GET['page']==""){
	$pageNumber = 1;
	$limit1=0;
	$limit2=50;
} else {
	$pageNumber = $_GET['page'];
	$limit2 = $_GET['page'] * 50;
	$limit1 = $limit2 - 50;
}
$limit = "LIMIT ".$limit1.", ".$limit2;
//QUERY
$sql = "SELECT bans.id, p.name as playername, `date`, `display`, `type`, `length`, m.name as modname, `reason` FROM bans, users p,users m WHERE bans.player_id = p.id AND bans.mod = m.id AND display =".$hidecheck.$search." ORDER BY date DESC ".$limit;

@$res = mysql_query($sql);

//DISPLAY

echo "<table id='banstable'><tr id='banshead'>";
echo '<th>#</th><th>Player</th><th style="width: 100px;">Date</th><th>Type</th><th style="width: 100px;">Length</th><th>Mod</th><th style="width: 300px;">Reason</th>';
if($isMod)echo'<th>Display</th><th>Edit</th>';

echo "</tr>";

if($res!=false){
$counter = 0;
$class = "";
  while($row=mysql_fetch_array($res)) {
  //English o matic for type
  $type = $row['type'];
  if($type == 1) $type = "Ban";
  if($type == 2) $type = "TempBan";
  if($type == 3) $type = "Kick";
  if($type == 4) $type = "Warning";
  if($type == 11) $type = "Unbanned";
  if($type == 12) $type = "Untempbanned";
  
  //English o matic for time
  $length = $row['length'];
  if($length == 0) $length = "N/A";
  else $length = date("F jS, Y. g:i A",$length);
  
  $date = date("F jS, Y. g:i A",strtotime($row['date']));
  
  //Adds off color to rows
  $classNone = "";
  $classOdd = "class='odd'";
  $counter++;
  $class = ($counter % 2) ? $classOdd : $classNone;
  
    echo "<tr " .$class . ">";
    echo "<td class='id'>" . $row['id'] . "</td>";
    echo "<td class='player'>" . $row['playername'] . "</td>";
    echo "<td class='date'>" . $date . "</td>";
    echo "<td class='type'>" . $type . "</td>";
    echo "<td class='length'>" . $length . "</td>";
    echo "<td class='mod'>" . $row['modname'] . "</td>";
    echo "<td class='reason'>" . $row['reason'] . "</td>";
	//hold searchs
  $updateAction = "./?admin";
  if($_GET['admin']==1&&$isMod){
	$updateAction = "./?admin=1";
  }
  if($_GET['search']){
	$updateAction = $updateAction."&search=".$_GET['search'];
  }
  if($_GET['col']){
	$updateAction = $updateAction."&col=".$_GET['col'];
  }
  if($_GET['page']){
	$updateAction = $updateAction."&page=".$_GET['page'];
  }
	if($isMod){
		echo "<td><form action='".$updateAction."' method='POST'><input 
		type=\"hidden\" name=\"hide\" value=\"" . $row['id'] . "\"><input 
		type=\"submit\" value=\"".$hidetext."\"></form></td>";

		echo "<td><input 
		type=\"submit\" value=\"Edit\" onclick=\"javascript:editPopup(".$row['id'].");\"></td>";
	}
	echo "</tr>";

	}
  }
  echo "</table>";
  echo "<div id='bottom'>";
   if(mysql_num_rows($res) == 0 ){
	echo "<h2>No Results</h2>";
	}
  //NEXT and PREV link maker
  $pageNext = $pageNumber + 1;
  if($pageNumber == 1)$pagePrev = 1;
  else $pagePrev = $pageNumber - 1;
  if($_GET['search']){
	$pagePrev = $pagePrev."&search=".$_GET['search'];
	$pageNext = $pageNext."&search=".$_GET['search'];
  }
  if($_GET['col']){
	$pagePrev = $pagePrev."&col=".$_GET['col'];
	$pageNext = $pageNext."&col=".$_GET['col'];
  }
  if($_GET['admin']==1&&$isMod){
	$pagePrev = $pagePrev."&admin=1";
	$pageNext = $pageNext."&admin=1";
  }
  
  //TODO work on jump to page
  echo "<span><a href='./?page=".$pagePrev."'>Previous Page</a></span> <span><a href='./?page=".$pageNext."'>Next Page</a></span><form action='./' method='get'><span>Jump to Page: <input type='text' name='page' value='".$pageNumber."'size='2' onfocus='if (this.value == \"".$pageNumber."\") {this.value = \"\";}' onblur='if (this.value == \"\") {this.value = \"".$pageNumber."\";}'/><input type='submit' value='Go' /></span>";
  if($_GET['admin']==1&&$isMod)echo '<input type="text" value="1" name="admin" style="display:none;"/>';
  if($_GET['col'])echo '<input type="text" value="'.$_GET['col'].'" name="col" style="display:none;"/>';
  if($_GET['search'])echo '<input type="text" value="'.$_GET['search'].'" name="search" style="display:none;"/>';
  echo "</form></div>";
?>
</div>
<div id="footer">
	<div id="footer-inside" class="toolbar">
		<p>
			Copyright &copy; 2012 <a href="http://dev.bukkit.org/server-mods/serubans/">SeruBans</a>
			| Theme based from <a href="http://www.christianbullock.com/">Absolution</a> | Coded by <a href="http://www.serubin.net/">Solomon Rubin</a>
			| <span id="back-to-top" >&uarr; <a href="#" rel="nofollow" title="Back to top">Top</a></span>
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