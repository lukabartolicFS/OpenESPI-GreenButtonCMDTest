#!/bin/bash
ECHO run: curl --header "Content-Type:text/xml" -X POST -d $1 $2
curl --header "Content-Type:text/xml" -X POST -d @$1 $2
