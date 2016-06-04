<!DOCTYPE unspecified PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<?php
require_once 'requiredfiles.php';
if (! isset($_POST['token'])) {
    die('<h3>token串丢失</h3>');
}
$token = $_POST['token'];
$psw = $_POST['psw'];
$rs = DB::select('PasswordRecovery', array(
    'UserID',
    'ExpiredTime'
), array(
    'Token' => $token
))[0];
if (count($rs) < 1) {
    die('<h3>token串错误</h3>');
}
$expiredtime = strtotime($rs['ExpiredTime']);
if ($expiredtime < time()) {
    die('<h3>您已重置过密码或重置期限已过，请重新申请密码重置</h3>');
}
$id = $rs['UserID'];
$rs = DB::select('Users', array(
    'Email'
), array(
    'ID' => $id
))[0];
if (DB::update('Users', array(
    'Password' => md5($rs['Email'] . $psw)
), array(
    'ID' => $id
)) == 1) {
    DB::update('PasswordRecovery', array(
        'ExpiredTime' => date('Y-m-d h:i:s', time())
    ), array(
        'UserID' => $id
    ));
    die('<h3>密码已重置，请重新登录</h3>');
} else {
    die('<h3>密码重置时出现错误</h3>');
}
?>
</body>
</html>
