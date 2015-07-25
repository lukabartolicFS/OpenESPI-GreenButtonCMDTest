# run script that takes host ($1) and port ($2) as arguments to retrieve certificate 
cd /etc/stunnel
sudo echo Q | openssl s_client -showcerts -connect $1:$2 -CApath /etc/ssl/certs -cert openespi.pem -key openespi_private_key.pem  | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > ~/Desktop/$1.pem


sudo cp ~/Desktop/$1.pem /etc/ssl/certs/$1.pem
cd /etc/ssl/certs
sudo ln -s $1.pem `openssl x509 -hash -noout -in $1.pem`.0


###!/bin/bash
##bash -i -c "$1 $2 $3 $4 s_client -connect $5 2>&1 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > $6"
