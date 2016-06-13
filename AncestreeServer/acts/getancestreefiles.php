<?php
require_once 'requiredfiles.php';

$user = getusercookie($_COOKIE['noncetoken']);

$rs = DB::select('FamilyTreeGraphs', array(
    'FamilyTreeGraphID',
    'Title'
), array(
    'UserID' => $user
));

$retdata = array();
while ($row = mysqli_fetch_array($rs, MYSQLI_ASSOC)) {
    $retdata[] = $row;
}
$output = array(
    'retcode' => $retcode['OK'],
    'rettitle' => 'OK',
    'retdata' => $retdata
);
echo json_encode($output);
?>