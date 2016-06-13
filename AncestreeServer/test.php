<?php
/*$subject = '=?UTF-8?B?' . base64_encode('no-reply') . '?=';
$message = '请点击下面的链接以重置密码，10分钟内有效：<br/>';
$message .= 'http://' . $_SERVER['SERVER_NAME'] . ':' . $_SERVER['SERVER_PORT'] . '/Ancestree/AncestreeServer/?act=resetpswpage&token=' . 123123 . '<br/>';
$message .= '如果不能直接访问，请将此url复制至地址栏再访问。';
$headers = "From: Ancestree \n";
$headers = "MIME-Version: 1.0\r\n";
$headers .= "Content-type: text/html; charset=utf-8\r\n";
$headers .= "Content-Transfer-Encoding: 8bit\r\n";
mail('1509223061@qq.com', $subject, $message, $headers);
echo 'Done!';*/

$arr=array('retdata'=>array(array('title'=>'234','id'=>'123'),array('title'=>'897','id'=>'567')));
echo json_encode($arr);
?>