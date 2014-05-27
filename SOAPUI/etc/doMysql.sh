#!/bin/bash
echo $1, $2, $3
echo $1 --user=root --password=password "<" $2
cd $3
$1 --user=root --password=password < $2
