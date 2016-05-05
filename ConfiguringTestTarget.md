# Adding Green Button Data Custodian Connect My Data Applications to Green Button Data Custodian Connect My Data Test Harness: #

## Overview ##
The following tasks are required to add a Test Target Data Custodian Application to the Green Button Data Custodian Connect My Data test harness:

1. Create a unique gbcmd_target.conf file for the Data Custodian.
2. The SSL Certificates for the Data Custodian's test and production Resource, Authorization, and SFTP servers must be retrieved.  (SSL certificates for the authorization and SFTP servers are required only if the Data Custodian uses a separate authorization server and/or SFTP server).
3. All retrieved Data Custodian SSL Certificates must be placed in the /etc/ssl/certs directory.
4. Hash links must be generated for all retrieved Data Custodian SSL Certificates and placed in the /etc/ssl/certs directory.
4. A routing configuration file must be created for the stunnel proxy server (this is automatically accomplished by a test harness script executed during the testing process).


# Example gbcmd_target.conf file #

    FileName="gbcmdcert_target.conf"

    // Test Target's GBA Certification ID assigned by GBA when Connect My Data Certification Application
    // is received
    GBACertId="xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
    
    // Test Target's Resource, Authorization, and SFTP Server TLS Library provider's name and NIST FIPS 140-2 
    // Certificate ID Resource Server information is required, Authorization and SFTP Server information is 
    // only required if test target uses a separate Authorization Server and/or SFTP Server.  If test target
    // does not use separate Authorization or SFTP Servers, they values should be "".
    productionAuthorizationServerTLSLibrary="Apple Inc."
    productionAuthorizationServerTLSLibCert="2411"
    productionResourceServerTLSLibrary="Symantec Corporation"
    productionResourceServerTLSLibCert="1937"
    productionSFTPServerTLSLibrary="Symantec Corporation"
    productionSFTPServerTLSLibCert="1937"
    
    // Test target's Test and Production Resource, Authorization, and SFTP Server domains (i.e. host URI and
    // port value). Resource Server information is required, Authorization and SFTP Server information is only
    // required if test target uses a separate Authorization Server and/or SFTP Server.  If test target does
    // not use separate Authorization or SFTP Servers, they values should be "".    
    productionAuthorizationServerDomain="https://localhost:8443"
    productionResourceServerDomain="https://localhost:8443"
    productionSFTPServerDomain="https://localhost:8443"
    testAuthorizationServerDomain="https://localhost:8443"
    testResourceServerDomain="https://localhost:8443"
    testSFTPServerDomain="https://localhost:8443"
        
    // Test target's Resource and OAuth 2.0 Token Endpoints
    dataCustodianResourceEndpoint="https://localhost:8443/DataCustodian/espi/1_1/resource"
    authorizationServerTokenEndpoint="https://localhost:8443/DataCustodian/oauth/token"
    
    // Test target's assigned Connect My Data test harness ApplicationtInformation and Authorization retrieval
    // URI ID values
    // 	  resourceUri: 
    //		{dataCustodianResourceEndpoint}/ApplicationInformation/{applicationInformationId}
    // 	  authorizationUri: 
    //		{dataCustodianResourceEndpoint}/Authorization/{authorizationId}
    applicationInformationId="2"
    authorizationId="4"
    
    // Test target's assigned Connect My Data test harness client_id and secret
    client_id=""
    client_secret=""
    
    // Test targets's assigned Connect My Data test harness registration_access_token or Registration client_id
    // and secret
    registration_access_token=""
    registration_access_token_client_id="REGISTRATION_surface_tp"
    registration_access_token_secret="secret"
    
    
    // Test target's assigned Connect My Data test harness client_access_token or Admin client_id and secret
    client_access_token=""
    client_access_token_client_id="surface_tp_admin"
    client_access_token_secret="secret"
    
    scope="FB=4_5_15;IntervalDuration=3600;BlockDuration=monthly;HistoryLength=13"
    certDataScopeFBs="FB=1_4_5_15"
    
    // if an offline authorization (FB_XX) is used, provide the authorization id and access token
    optionalOfflineAuthorizationID="5"
    optionalOfflineAccess_token="57673811-5a25-4412-89e1-e15043f9703f"
    
# Retrieve Target SSL Certificates
The SSL Certificates for the test target's resource, authorization, and SFTP servers must be retrieved and
installed on the Connect My Data test harness.

## Retrieve and install SSL Certificates 
The following command line entries need to be performed for all test target production and test Resource, 
Authorization, and SFTP servers.  Elements shown in brackets "{}" indicate contents of the 
gbcmdcert_target.conf file.  

	cd /etc/stunnel
	sudo echo Q | openssl s_client -showcerts -connect {testAuthorizationServerDomain} | {testResourceServerDomain} | {testSFTPServerDomain} | {productionAuthorizationServerDomain} | {productionResourceServerDomain} | {productionSFTPServerDomain} -CApath /etc/ssl/certs -cert greenbuttonalliance_org_SSL_Cert.crt -key greenbuttonalliance_private_key.pem  | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > ~/Desktop/{testAuthorizationServerDomain} | {testResourceServerDomain} | {testSFTPServerDomain} | {productionAuthorizationServerDomain} | {productionResourceServerDomain} | {productionSFTPServerDomain}.pem
	
	sudo cp ~/Desktop/{testAuthorizationServerDomain} | {testResourceServerDomain} | {testSFTPServerDomain} | {productionAuthorizationServerDomain} | {productionResourceServerDomain} | {productionSFTPServerDomain}.pem /etc/ssl/certs/{testAuthorizationServerDomain} | {testResourceServerDomain} | {testSFTPServerDomain} | {productionAuthorizationServerDomain} | {productionResourceServerDomain} | {productionSFTPServerDomain}.pem
	cd /etc/ssl/certs
	sudo ln -s {testAuthorizationServerDomain} | {testResourceServerDomain} | {testSFTPServerDomain} | {productionAuthorizationServerDomain} | {productionResourceServerDomain} | {productionSFTPServerDomain}.pem `openssl x509 -hash -noout -in {testAuthorizationServerDomain} | {testResourceServerDomain} | {testSFTPServerDomain} | {productionAuthorizationServerDomain} | {productionResourceServerDomain} | {productionSFTPServerDomain}.pem`.0
	
Note: The above commands add Test Target SSL Certificates to the Green Button Alliance Data Custodian AWS EC2 test harness image. If running your own Green Button Data Custodian Connect My Data configured test harness, replace **"-cert greenbuttonalliance_org_SSL_Cert.crt -key greenbuttonalliance_private_key.pem"** with **"-cert {your SSL Certificate} -key {SSL Certificate's Private Key}"** in the above commands.

## Test the retrieved SSL Certificate can be verified
The following command line entries need to be performed for all retrieved and installed SSL Certificates.  Elements
shown in brackets "{}" indicate contents of the gbcmdcert_target.conf file.  Verify the test ends with a value of
return code:0 (ok):

	cd /etc/stunnel
	echo Q | openssl s_client -verify 10 -showcerts -CApath /etc/ssl/certs -cert greenbuttonalliance_org_SSL_Cert.crt -key greenbuttonalliance_private_key.pem -connect {testAuthorizationServerDomain} | {testResourceServerDomain} | {testSFTPServerDomain} | {productionAuthorizationServerDomain} | {productionResourceServerDomain} | {productionSFTPServerDomain}  

Note: If there are any errors listed in the exchange (even if the verify is ok) you may need to check intermediate
certificates in the chain from the "-showcerts" parameter. If so, you may need to acquire these certificates from
DigiCert, Verisign or appropriate source and add them to the /etc/ssl/certs directory.

Note: The above command tests Test Target SSL Certificates added to the Green Button Alliance Data Custodian Connect My Data AWS EC2 test harness image. If running your own Green Button Data Custodian Connect My Data configured test harness, replace **"-cert greenbuttonalliance_org_SSL_Cert.crt -key greenbuttonalliance_private_key.pem"** with **"-cert {your SSL Certificate} -key {SSL Certificate's Private Key}"** in the above command.
