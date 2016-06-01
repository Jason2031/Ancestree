<?php

function generaterandomstring($length = 4)
{
    $output = '';
    mt_srand(mktime());
    while (strlen($output) != $length) {
        $temp = chr(mt_rand(33, 126)); // 33-'!' 126-'~'
        if (! ($temp == '(' || $temp == ')' || $temp == '[' || $temp == ']' || $temp == '{' || $temp == '}')) {
            $output .= $temp;
        }
    }
    return $output;
}

?>