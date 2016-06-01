<?php
require_once 'requiredfiles.php';

$user = $_POST['username'];
$psw = $_POST['password'];

$rs = DB::select('Users', array(
    'count(*)'
), array(
    'ID' => $user
))[0];
if ((int) $rs['count(*)'] >= 1) {
    $output = array(
        'retcode' => $retcode['Duplicate Register'],
        'rettitle' => 'Duplicate Register'
    );
    die(json_encode($output));
}
if (DB::insert('Users', array(
    'ID' => $user,
    'Password' => $psw,
    'NonceToken' => md5($psw . 'original token')
)) < 1) {
    $output = array(
        'retcode' => $retcode['SQL Error'],
        'rettitle' => 'SQL Error'
    );
    die(json_encode($output));
}
$output = array(
    'retcode' => $retcode['OK'],
    'rettitle' => 'OK'
);
echo json_encode($output);
?>