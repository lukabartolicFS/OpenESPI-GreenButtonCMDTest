# SOAPUI Secure Messaging with Stunnel Proxy

##Help

Stunnel manpage:
https://www.stunnel.org/static/stunnel.html


OpenSSL s_client manpage:
https://www.openssl.org/docs/apps/s_client.html 

## Install 

### Install Ubuntu

    sudo apt-get install stunnel4

### Install MAC
TBD
   

### Install Windows
TBD

## Configure


###/etc/stunnel/tpclient.conf
This causes Stunnel to listen on http:/localhost:8080 and route to htts://localhost:8443

    cert=/etc/stunnel/openespi.pem
    key=/etc/stunnel/openespi_private_key.pem
    ;cert=/etc/stunnel/tc-server-bio-ssl_naked.pem
    client=yes
    pid = /tpclient.pid
    debug=7
    output=/etc/stunnel/stunnel.log
    
    [tpclient]
    accept=8080
    connect=localhost:8443
    ciphers=AES128-SHA

###/etc/stunnel/tpserver.conf
This causes Stunnel to listen on https://localhost:8444 and route to http:/localhost:8081

    cert=/etc/stunnel/tc-server-bio-ssl.pem
    key=/etc/stunnel/tc-server-bio-ssl_private_key.pem
    client=no
    pid = /tpserver.pid
    debug=7
    output=/etc/stunnel/stunnel.log
    
    [tpserver]
    accept=8444
    connect=localhost:8081
    ciphers=AES128-SHA

###Configure /etc/default/stunnel4

    ENABLED=1
    FILES="/etc/stunnel/*.conf"
    OPTIONS=""
    


## Start and Stop Stunnel
sudo /etc/init.d/stunnel4 stop
sudo /etc/init.d/stunnel4 start
sudo /etc/init.d/stunnel4 restart

## Test Stunnel proxy

Test sending message to https:localhost:8444 and through to SOAPUI mock server on localhost:8081

Run mock server on SOAPUI
At terminal prompt:

    openssl s_client -cert ./tc-server-bio-ssl.pem -key tc-server-bio-ssl_private_key.pem -CAfile tc-server-bio-ssl.pem -state -connect localhost:8444

When connection is established type:

    GET /


Test sending message to htt://localhost:8080 and routing to DataCustodian on https://localhost:8443

Run DataCustodian with bio-ssl server on port 8443:

    curl -v --header "Content-Type:application/xml" --header "Authorization: Bearer 688b026c-665f-4994-9139-6b21b13fbeee" -X GET  "http://localhost:8080/DataCustodian/espi/1_1/resource/UsagePoint"
    


##Create new self-signed certificate in SOAPUI project 
###Create new key for SOAPUI

    keytool -genkey -alias openespi -keystore openespi.pfx -storepass energyos -validity 365 -keyalg RSA -keysize 2048 -storetype pkcs12

###Dialog output

    keytool -genkey -alias openespi -keystore openespi.pfx -storepass energyos -validity 365 -keyalg RSA -keysize 2048 -storetype pkcs12
    What is your first and last name?
      [Unknown]:  localhost
    What is the name of your organizational unit?
      [Unknown]:  
    What is the name of your organization?
      [Unknown]:  
    What is the name of your City or Locality?
      [Unknown]:  
    What is the name of your State or Province?
      [Unknown]:  
    What is the two-letter country code for this unit?
      [Unknown]:  
    Is CN=localhost, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown correct?
      [no]:  yes
    
  
To extract private key:

    openssl pkcs12 -in openespi.pfx -nocerts -out openespi_private_key.pem -nodes

Extract certificate

	openssl pkcs12 -in openespi.pfx -nokeys -clcerts -out openespi.pem

Test server access for DataCustodian

	openssl s_client -connect 127.0.01:8443 -cert openespi.pem -key openespi_private_key.pem

Generate jks keystore

    keytool -importkeystore -srckeystore openespi.pfx -srcstoretype pkcs12 -destkeystore openespi.jks -deststoretype JKS
    
Add key to java keystore

    sudo keytool -import -file ~/git/energyos/test/OpenESPI-GreenButtonCMDTest/SOAPUI/etc/openespi.pem -alias openespi -storepass energyos

###verify that key and certificate match:

    openssl x509 -noout -modulus -in openespi.pem | openssl md5
    openssl rsa -noout -modulus -in openespi_private_key.pem | openssl md5
    
## Enable openssl to use self signed certificate
 /etc/ssl/certs
Create hash (this produced de1c6683)

    openssl x509 -hash -noout -in openespi.pem

Create symbolic link
	sudo ln -s ~/git/energyos/openespi/test/OpenESPI-GreenButtonCMDTest/SOAPUI/etc/openespi.pem /etc/ssl/certs/openespi.pem

    sudo ln -s ~/git/energyos/openespi/test/OpenESPI-GreenButtonCMDTest/SOAPUI/etc/openespi.pem /etc/ssl/certs/de1c6683.0

Test that it worked

    openssl verify -CApath /etc/ssl/certs/java/cacerts  ~/git/energyos/test/OpenESPI-GreenButtonCMDTest/SOAPUI/etc/openespi.pem