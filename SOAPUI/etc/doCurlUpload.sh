#!/bin/bash
curl --header "Authorization: Bearer 688b026c-665f-4994-9139-6b21b13fbeee" --header "Content-Type:application/xml" -X POST -d @$1 $2



