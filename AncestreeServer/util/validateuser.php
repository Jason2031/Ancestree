<?php
require_once 'database.php';
require_once 'returncode.php';

function getusercookie($cookiestr)
{
    $rs = DB::select('Users', array(
        'ID'
    ), array(
        'NonceToken' => $cookiestr
    ));
    
    if (count($rs) == 1) {
        return $rs[0]['ID'];
    } else {
        $output = array(
            'retcode' => $retcode['Bad Cookie'],
            'rettitle' => 'Bad Cookie'
        );
        echo json_encode($output);
        die();
    }
}
?>