<?php
require_once 'requiredfiles.php';

$email = $_POST['email'];
$rs = DB::select('Users', array(
    'ID'
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
$token = md5($email . time());
if (DB::insert('PasswordRecovery', array(
    'UserID' => $rs['ID'],
    'Token' => $token,
    'CreateTime' => date('Y-m-d h:i:s', time()),
    'ExpiredTime' => date('Y-m-d h:i:s', time() + 60 * 10)
)) == 1) {
    $subject = '=?UTF-8?B?' . base64_encode('no-reply') . '?=';
    $message = '请点击下面的链接以重置密码，10分钟内有效：<br/>';
    $message .= 'http://' . $_SERVER['SERVER_NAME'] . ':' . $_SERVER['SERVER_PORT'] . '/Ancestree/AncestreeServer/?act=resetpswpage&token=' . $token . '<br/>';
    $message .= '如果不能直接访问，请将此url复制至地址栏再访问。';
    $headers = "From: Ancestree \n";
    $headers = "MIME-Version: 1.0\r\n";
    $headers .= "Content-type: text/html; charset=utf-8\r\n";
    $headers .= "Content-Transfer-Encoding: 8bit\r\n";
    mail($email, $subject, $message, $headers);
    $output = array(
        'retcode' => $retcode['OK']
    );
    die(json_encode($output));
} else {
    $output = array(
        'retcode' => $retcode['SQL Error'],
        'rettitle' => 'SQL Error'
    );
    die(json_encode($output));
}
?>