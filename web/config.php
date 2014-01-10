<?php
/*
 * SeruBans - Config page
 * Author: Serubin323 (Solomon Rubin)
 * Version: 2.4
 * 
 * for logging create "log/banlist.log"
 *
 * Fill the following with mysql info.
 */

$mysqlHost = "localhost";
$mysqlUser = "root";
$mysqlPassword = "password";
$mysqlDatabase = "serubans";

/*
 * $password allows a user to hide ban items when logged in
 * $adminPassword allows a user to delete or hide ban items when logged in
 *
 */
$password = "pass";
$adminPassword = "passop";



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

@mysql_connect($mysqlHost,$mysqlUser,$mysqlPassword);
@mysql_select_db($mysqlDatabase);
?>