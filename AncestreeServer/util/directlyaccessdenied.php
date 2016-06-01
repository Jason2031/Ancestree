<?php
require_once 'returncode.php';

if (! defined('WWW_ROOT')) {
    $output = array(
        'retcode' => $retcode['Bad Request'],
        'rettitle' => 'Bad Request',
        'detail' => 'Directly access denied.'
    );
    die(json_encode($output));
}
?>