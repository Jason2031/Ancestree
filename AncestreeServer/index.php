<?php
require_once 'util/returncode.php';
define('WWW_ROOT', 'root');
$acts_folder = 'acts/';
$page = isset($_GET['act']) ? $_GET['act'] : 'index';
$page = $acts_folder . $page . '.php';
if (file_exists($page)) {
    include_once $page;
} else {
    $output = array(
        'retcode' => $retcode['Greeting'],
        'rettitle' => 'Greeting',
        'detail' => 'This is the index of the server.'
    );
    die(json_encode($output));
}
?>