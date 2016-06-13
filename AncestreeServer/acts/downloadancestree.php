<?php
require_once 'requiredfiles.php';

$user = getusercookie($_COOKIE['noncetoken']);
$graphid = $_POST['graphid'];

$rs = DB::select('PersonNode', array(
    'PersonNodeID',
    'Firstname',
    'Lastname',
    'IsMale',
    'Birthday',
    'Deathday'
), array(
    'FamilyTreeGraphID' => $graphid
));
$nodes = array();
while ($row = mysqli_fetch_array($rs, MYSQLI_ASSOC)) {
    $nodes[] = $row;
}

$rs = DB::excutesql(null, 'select r.RelationshipType, p1.Firstname as p1f, p1.Lastname as p1l, p2.Firstname as p2f, p2.Lastname as p2l from Relationship r, PersonNode p1, PersonNode p2 where r.FamilyTreeGraphID1=' . $graphid . ' and p1.FamilyTreeGraphID=' . $graphid . ' and p1.PersonNodeID=r.PersonNodeID1 and p1.FamilyTreeGraphID=' . $graphid . ' and p2.PersonNodeID=r.PersonNodeID2');
$relationships = array();
while ($row = mysqli_fetch_array($rs, MYSQLI_ASSOC)) {
    $relationships[] = $row;
}

$output = array(
    'retcode' => $retcode['OK'],
    'rettitle' => 'OK',
    'retdata' => array(
        'nodes' => $nodes,
        'relationships' => $relationships
    )
);
echo json_encode($output);
?>