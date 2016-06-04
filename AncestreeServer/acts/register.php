<?php
require_once 'requiredfiles.php';

$email = $_POST['email'];
$psw = $_POST['password'];

$rs = DB::select('Users', array(
    'count(*)'
), array(
    'Email' => $email
))[0];
if ((int) $rs['count(*)'] >= 1) {
    $output = array(
        'retcode' => $retcode['Duplicate Register'],
        'rettitle' => 'Duplicate Register'
    );
    die(json_encode($output));
}
if (DB::insert('Users', array(
    'Email' => $email,
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