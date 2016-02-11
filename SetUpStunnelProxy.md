# OpenESPI-GreenButtonCMDTest Secure Messaging with Stunnel Proxy and OpenSSL
In order to manage secure messaging independent of the test software, the test project has been designed to rely on the Stunnel open source OpenSSL proxy for secure messaging.

This page describes the installation and configuration of secure messaging for the project. Stunnel has to be downloaded and installed. OpenSSL comes with most operating systems and on Windows requires cygwin.

##Help on Stunnel and OpenSSL

Stunnel manpage:
https://www.stunnel.org/static/stunnel.html

OpenSSL s_client manpage:
https://www.openssl.org/docs/apps/s_client.html 

Cygwin installation:
https://cygwin.com/install.html 

## Install 

### Install Ubuntu
1. Download and install software

    sudo apt-get install stunnel4

1. Enable stunnel.conf to be written by anyone

1. Create [certificate file](#Create new key for SOAPUI) for use by stunnel for this server 

### Install MAC
TBD
   

### Install Windows

1. Install software
https://www.stunnel.org/downloads.html (stunnel-5.17-installer.exe)

1. Place the stunnel program directory in the path environmental variable

1. Enable stunnel.conf to be written by anyone

## Configure
The configuration for stunnel is in the stunnel.conf file. There are some basic settings and specific ones for client (the remote target) and server (the SOAPUI mock server) roles.

The server role allows stunnel to accept https connections to the server and route them over http to the test harness (SOAPUI).

The client role allows test harness to originate messages to a remote client -- typically the data custodian under test.

Both configurations need a local cert and key to use in TLS messaging. 

Additionally, there needs to be a client certificate and hash for each remote client supported.

### stunnel.conf file
In the stunnel directory, ensure that the file stunnel.conf exists and has all user permissions to write.

The stunnel.conf file is created automatically by the library script:

	/GBCMD/Library/GeneralScripts/GenerateStunnelConf

Note this script should be run after running 
	/GBCMD/Library/GeneralScripts/LoadConfigCert

Here is a sample file generated in Linux:

    ; **************************************************************************
    ; * Service defaults may also be specified in individual service sections  *
    ; **************************************************************************
    
    ; **************************************************************************
    ; * Logging*
    ; **************************************************************************
    
    debug = 7
    output = /home/bitnami/git/energyos/test/OpenESPI-GreenButtonCMDTest/SOAPUI\stunnel.log
    ; Enable support for the insecure SSLv3 protocol
    
    
    ; **************************************************************************
    ; * Service definitions (at least one service has to be defined)   *
    ; **************************************************************************
    
    
    [tpclient]
    accept=8080
    connect=localhost:8443
    ciphers=AES128-SHA
    CAfile = ca-certs.pem
    client = yes
    cert=stunnel.pem
    verify=0
    ;CAfile=apitst_file.pem
    ;cert=apitst_client.pem
    ;key=apitst_private_key.pem
    
    [tpserver]
    accept=localhost:8444
    connect=localhost:8081
    cert=stunnel.pem
    verify=0
    client=no
    ciphers=AES128-SHA

## Start and Stop Stunnel Linux


	sudo /etc/init.d/stunnel4 stop
	sudo /etc/init.d/stunnel4 start
	sudo /etc/init.d/stunnel4 restart

## Start and Stop Stunnel Windows


	stunnel -exit
	stunnel
	stunnel -reload

## Test Stunnel proxy

Test sending message to https://localhost:8444 and through to SOAPUI mock server on localhost:8081

Run mock server on SOAPUI

At terminal prompt:

    openssl s_client -cert ./tc-server-bio-ssl.pem -key tc-server-bio-ssl_private_key.pem -CAfile tc-server-bio-ssl.pem -state -connect localhost:8444

When connection is established type:

    GET /

Test sending message to http://localhost:8080 and routing to DataCustodian on https://localhost:8443

Run DataCustodian with bio-ssl server on port 8443:

    curl -v --header "Content-Type:application/xml" --header "Authorization: Bearer 688b026c-665f-4994-9139-6b21b13fbeee" -X GET  "http://localhost:8080/DataCustodian/espi/1_1/resource/UsagePoint"
    
# Create/Install Certificate for Test Harness as Client
The OpenESPI-GreenButtonCMDTest project requires a valid certificate to be used in communications with remote targets.

In a development test environment, self-signed certificates can be used. However, in an actual test, the target is required to reject self-signed certificates.

## Obtain Certificate from CA
In order to have messaging accepted by a test target, a certificate must be installed of sufficient quality:

Both the Data Custodian and the Third Party shall maintain unexpired, unrevoked RSA certificates with a public key length of at least 2048 bits. These certificates must be issued by a Certificate Authority (CA) that has been successfully audited according to the criteria of ETSI or WebTrust.

The certificate and private key must be placed in the "stunnelConfigDirectory" identified in the test harness configuration file gbcmdcert.conf. 

It must have the names openespi.pem and openespi_private_key.pem.


##Create new self-signed certificate 
###Create new key 

Use the keytool program to generate the RSA certificate. Accept all defauls except the request for first and last name to which you answer server name or "localhost".

Create the key in the Stunnel configuration directory (note this will set the password for the keystore to 'energyos')

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
    
 
###To extract private key:

    openssl pkcs12 -in openespi.pfx -nocerts -out openespi_private_key.pem -nodes

###Extract certificate

	openssl pkcs12 -in openespi.pfx -nokeys -clcerts -out openespi.pem

###verify that key and certificate match (produce same checksums):

    openssl x509 -noout -modulus -in openespi.pem | openssl md5
    openssl rsa -noout -modulus -in openespi_private_key.pem | openssl md5

###combine keys into stunnel.pem file:

    cat openespi_private_key.pem openespi.pem > stunnel.pem
    
### Enable openssl and Data Custodian to use tester self signed certificate

    cd /etc/ssl/certs
    openssl x509 -hash -noout -in {pathto}openespi.pem
    
Use the value shown from the openssl command as the {hash value} in the next command:

    sudo ln -s {pathto}openespi.pem {hash value}.0


Test that it worked

 	openssl verify -CApath /etc/ssl/certs {pathto}openespi.pem

