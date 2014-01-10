<?php
/*
 * SeruBans - logout page
 * Author: Serubin323 (Solomon Rubin)
 * Version: 2.4
 * 
 * for logging create "log/banlist.log"
 *
 * Fill the following with mysql info.
 */
session_start(); //to ensure you are using same session
session_destroy(); //destroy the session
header("location:./"); //to redirect back to "index.php" after logging out
exit();
?>