<?php
require_once 'requiredfiles.php';

$user = getusercookie($_COOKIE['noncetoken']);

$uploadtime = date('Y-m-d h:i:s', time());
$uploadheader = str_replace('-', '', str_split($uploadtime, 10)[0]);
$dategraphcount = DB::excutesql(null, 'select count(*) from FamilyTreeGraphs where FamilyTreeGraphID like \'' . $uploadheader . '%\'');
$row = mysqli_fetch_array($dategraphcount, MYSQLI_ASSOC);
$graphcount = (int) $row['count(*)'];
if ($graphcount == 999) {
    $output = array(
        'retcode' => $retcode['Container Full'],
        'rettitle' => 'Container Full'
    );
    die(json_encode($output));
}
$graphid = $uploadheader . sprintf('%03d', $graphcount);
DB::insert('FamilyTreeGraphs', array(
    'UserID' => $user,
    'FamilyTreeGraphID' => $graphid,
    'Title' => $_POST['title']
));

$nodes = json_decode($_POST['nodes']);
for ($i = 0; $i < length($node); ++ $i) {
    if (DB::insert('PersonNode', $nodes[$i]) <= 0) {
        $output = array(
            'retcode' => $retcode['SQL Error'],
            'rettitle' => 'SQL Error',
        );
        die(json_encode($output));
    }
}

$relationships = json_decode($_POST['relationships']);
for ($i = 0; $i < length($relationships); ++ $i) {
    if (DB::insert('Relationship', array(
        'FamilyTreeGraphID1' => $graphid,
        'PersonNodeID1' => $relationships[$i]['PersonNodeID1'],
        'FamilyTreeGraphID2' => $graphid,
        'PersonNodeID2' => $relationships[$i]['PersonNodeID2']
    )) <= 0) {
        $output = array(
            'retcode' => $retcode['SQL Error'],
            'rettitle' => 'SQL Error',
        );
        die(json_encode($output));
    }
}
$output = array(
    'retcode' => $retcode['OK'],
    'rettitle' => 'OK'
);
die(json_encode($output));
?>