<?php

class DB
{

    public static function getConnection()
    {
        $settings = array(
            'host' => '127.0.0.1:3306',
            'user' => 'root',
            'psw' => 'root',
            'dbname' => 'Ancestree'
        );
        $connection = new mysqli($settings['host'], $settings['user'], $settings['psw'], $settings['dbname']);
        if ($connection->connect_error) {
            die($connection->connect_error);
        }
        if (! $connection) {
            error_log(date("[Y-m-d H:i:s]") . mysqli_error($connection), 3, '/log/db_log.log');
            return null;
        }
        $connection->set_charset('utf8');
        return $connection;
    }

    public static function excutesql($connection, $sqlstr)
    {
        if (! $connection) {
            $connection = self::getConnection();
        }
        $rs = mysqli_query($connection, $sqlstr);
        if (! $rs) {
            error_log(date("[Y-m-d H:i:s]") . mysqli_error($connection), 3, '/log/db_log.log');
        }
        mysqli_close($connection);
        return $rs;
    }

    public static function insert($tablename, $datamap)
    {
        $connection = self::getConnection();
        $sqlstr = 'insert into ' . mysqli_real_escape_string($connection, $tablename);
        $keystr = '(';
        $valuestr = '(';
        foreach ($datamap as $key => $value) {
            $keystr .= mysqli_real_escape_string($connection, $key) . ', ';
            if (is_bool($value)) {
                if ($value == true) {
                    $valuestr .= '\'1\', ';
                } else {
                    $valuestr .= '\'0\', ';
                }
            } else {
                $valuestr .= '\'' . mysqli_real_escape_string($connection, $value) . '\', ';
            }
        }
        $keystr = rtrim($keystr, ', ');
        $keystr .= ')';
        $valuestr = rtrim($valuestr, ', ');
        $valuestr .= ')';
        $sqlstr .= $keystr . ' values ' . $valuestr;
        return self::excutesql($connection, $sqlstr);
    }

    public static function delete($tablename, $wheremap)
    {
        $connection = self::getConnection();
        $sqlstr = 'delete from ' . mysqli_real_escape_string($connection, $tablename) . ' where ';
        foreach ($wheremap as $key => $value) {
            if (is_bool($value)) {
                if ($value == true) {
                    $sqlstr .= mysqli_real_escape_string($connection, $key) . ' = \'1\' and ';
                } else {
                    $sqlstr .= mysqli_real_escape_string($connection, $key) . ' = \'0\' and ';
                }
            } else {
                $sqlstr .= mysqli_real_escape_string($connection, $key) . ' = \'' . mysqli_real_escape_string($connection, $value) . '\' and ';
            }
        }
        $sqlstr = rtrim($sqlstr, ' and ');
        $rs = mysqli_query($connection, $sqlstr);
        return self::excutesql($connection, $sqlstr);
    }

    public static function update($tablename, $datamap, $wheremap)
    {
        $connection = self::getConnection();
        $sqlstr = 'update ' . mysqli_real_escape_string($connection, $tablename) . ' set ';
        foreach ($datamap as $key => $value) {
            $sqlstr .= $key . ' = \'' . $value . '\', ';
        }
        $sqlstr = rtrim($sqlstr, ', ');
        if (count($wheremap) > 0) {
            $sqlstr .= ' where ';
            foreach ($wheremap as $key => $value) {
                if (is_bool($value)) {
                    if ($value == true) {
                        $sqlstr .= mysqli_real_escape_string($connection, $key) . ' = \'1\' and ';
                    } else {
                        $sqlstr .= mysqli_real_escape_string($connection, $key) . ' = \'0\' and ';
                    }
                } else {
                    $sqlstr .= mysqli_real_escape_string($connection, $key) . ' = \'' . mysqli_real_escape_string($connection, $value) . '\' and ';
                }
            }
            $sqlstr = rtrim($sqlstr, ' and ');
        }
        return self::excutesql($connection, $sqlstr);
    }

    public static function select($tablename, $selectlist, $wheremap, $orderby = null)
    {
        $connection = self::getConnection();
        $sqlstr = 'select ';
        for ($i = 0; $i < count($selectlist); ++ $i) {
            $sqlstr .= mysqli_real_escape_string($connection, $selectlist[$i]) . ', ';
        }
        $sqlstr = rtrim($sqlstr, ', ');
        $sqlstr .= ' from ' . mysqli_real_escape_string($connection, $tablename);
        if ($wheremap != null && count($wheremap) > 0) {
            $sqlstr .= ' where ';
            foreach ($wheremap as $key => $value) {
                if (is_bool($value)) {
                    if ($value == true) {
                        $sqlstr .= mysqli_real_escape_string($connection, $key) . ' = \'1\' and ';
                    } else {
                        $sqlstr .= mysqli_real_escape_string($connection, $key) . ' = \'0\' and ';
                    }
                } else {
                    $sqlstr .= mysqli_real_escape_string($connection, $key) . ' = \'' . mysqli_real_escape_string($connection, $value) . '\' and ';
                }
            }
            $sqlstr = rtrim($sqlstr, ' and ');
        }
        if ($orderby != null) {
            $sqlstr .= ' order by ' . $orderby;
        }
        $rs = self::excutesql($connection, $sqlstr);
        $output = array();
        while ($row = mysqli_fetch_array($rs, MYSQLI_ASSOC)) {
            $output[] = $row;
        }
        return $output;
    }
}
?>