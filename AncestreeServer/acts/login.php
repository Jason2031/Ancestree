<?php
require_once 'requiredfiles.php';

$email = $_POST['email'];
$psw = $_POST['password'];

$rs = DB::select('Users', array(
    'Password'
), array(
    'Email' => $email
))[0];
if (count($rs) == 0) {
    $output = array(
        'retcode' => $retcode['No Such User'],
        'rettitle' => 'No Such User'
    );
    die(json_encode($output));
}
$passwordhash = $rs['Password'];
if ($psw == $passwordhash) {
    $noncetoken = md5($passwordhash . time());
    DB::update('Users', array(
        'NonceToken' => $noncetoken
    ), array(
        'Email' => $email
    ));
    $output = array(
        'retcode' => $retcode['OK'],
        'rettitle' => 'Welcome'
    );
    setcookie('noncetoken', $noncetoken, time() + 60 * 60 * 24);
    die(json_encode($output));
} else {
    $output = array(
        'retcode' => $retcode['Wrong Password'],
        'rettitle' => 'Wrong password'
    );
    die(json_encode($output));
}
echo json_encode($output);

?>