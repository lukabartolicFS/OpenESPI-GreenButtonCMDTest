# Adding SSL Server to OpenESPI STS

##Generate new vfabric server template based on bio-ssl; 

run this command from ~/Spring/sts-bundle/vfabric-tc-server-developer-2.9.6.RELEASE directory

accept all defaults except CN=localhost - where localhost is the network name of your vm

    ./tcruntime-instance.sh create openespissl -t bio-ssl --interactive
	cd openespissl/conf

The next commands get run in this directory.

##Export private key and certificate from TC Server keystore:
Note use catalina.properties file bio-ssl.ssl.keystore.password key value when asked
### generate p12 format
    keytool -v -importkeystore -srckeystore tc-server-bio-ssl.keystore -srcalias tc-server-bio-ssl -destkeystore tc-server-bio-ssl.p12 -deststoretype PKCS12

### export the cert and key
    openssl pkcs12 -in tc-server-bio-ssl.p12 -nocerts -out tc-server-bio-ssl_private_key.pem -nodes
    openssl pkcs12 -in tc-server-bio-ssl.p12 -nokeys -clcerts -out tc-server-bio-ssl.pem

###verify that key and certificate match (both md5 sums match):
    openssl x509 -noout -modulus -in tc-server-bio-ssl.pem | openssl md5
    openssl rsa -noout -modulus -in tc-server-bio-ssl_private_key.pem | openssl md5

## Enable openssl and java to use self signed certificate
Create link for client certificate to /etc/ssl/certs

	sudo ln -s {path to}/tc-server-bio-ssl.pem /etc/ssl/certs/tc-server-bio-ssl.pem

Create hash {hash}

    openssl x509 -hash -noout -in tc-server-bio-ssl.pem

Create symbolic link to certificate with name as hash

    sudo ln -s {path to}/tc-server-bio-ssl.pem /etc/ssl/certs/{hash}.0

Test that it worked

    cd /etc/ssl/certs/
	openssl verify -CApath . tc-server-bio-ssl.pem

Add to java keystore

    sudo keytool --importkeystore --srckeystore tc-server-bio-ssl.keystore --destkeystore $JAVA_HOME/jre/lib/security/cacerts --srcstorepass <catalina.properties.bio-ssl.ssl.key.password> --deststorepass changeit -v


## Establish DataCustodian and ThirdParty running on STS secure server

1. in the servers dialog (usually bottom left) right click and select new server
2. set Server name: OpenESPI-SSL at localhost
3. select Next> 
4. choose existing instance and navigate to new server template
5. select Next>
6. Add DataCustodian and ThirdParty to instance
7. Finish
8. Run the server

##Test server access (should make connection and establish cipher)

	openssl s_client -connect 127.0.01:8443 -cert tc-server-bio-ssl.pem -key tc-server-bio-ssl_private_key.pem

Results in:

    ...
	---
    SSL handshake has read 1438 bytes and written 523 bytes
    ---
    New, TLSv1/SSLv3, Cipher is ECDHE-RSA-AES256-SHA
    Server public key is 2048 bit
    Secure Renegotiation IS supported
    Compression: NONE
    Expansion: NONE
    SSL-Session:
    Protocol  : TLSv1.2
    Cipher: ECDHE-RSA-AES256-SHA
    Session-ID: 55182EE363074FCE784667A62ED77A4F33669FE7ACC147044E59F8EBC3863ED3
    Session-ID-ctx: 
    Master-Key: 0CB1EECE1A1DFAE731E1052E34F06234F0C1CDC849E692F32BEF35FBCE6184CD04218BF175CEEA62CF953EE8AB2ECE81
    Key-Arg   : None
    PSK identity: None
    PSK identity hint: None
    SRP username: None
    Start Time: 1427648227
    Timeout   : 300 (sec)
    Verify return code: 18 (self signed certificate)
    ---
    
