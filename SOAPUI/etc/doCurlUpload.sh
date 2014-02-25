#!/bin/bash
ECHO run: curl --header "Content-Type:application/xml" -X POST -d @$1 $2
curl --header "Content-Type:application/xml" -X POST -d @$1 $2
