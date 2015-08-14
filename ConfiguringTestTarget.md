# Configuring test harness for a new target: #
Note: this document is extremely draft

## Overview ##
In order to configure test harness for a particular Data Custodian:
1. gbcmd_target.conf file must be produced
2. the certificates for the resource server and authorization server must be retrieved
3. The certificates must be placed in /etc/ssl/certs with hashes
4. the stunnel proxy must have routing to the target


# gbcmd_target.conf file #


    FileName="gbcmdcert_target.conf"
    
    // Certification Input Configuration from testee
    federalEIN="123456"
        
    dataCustodianResourceEndpoint="https://localhost:8443/DataCustodian/espi/1_1/resource"
	authorizationServerTokenEndpoint="https://localhost:8443/DataCustodian/oauth/token"

    
    // for retrieval of information for this third party, provide the IDs to use
    // 	resourceUri: 
    //		{dataCustodianResourceEndpoint}/ApplicationInformation/{applicationInformationId}
    // 	authorizationUri: 
    //		{dataCustodianResourceEndpoint}/Authorization/{authorizationId}
    applicationInformationId="2"
    authorizationId="4"
    
    // client id
    client_id=""
    client_secret=""
    
    // registration_access_token
    // either provide access token, or, id and secret
    registration_access_token=""
    registration_access_token_client_id="REGISTRATION_surface_tp"
    registration_access_token_secret="secret"
    
    
    // client_access_token
    // either provide access token, or, id and secret
    client_access_token=""
    client_access_token_client_id="surface_tp_admin"
    client_access_token_secret="secret"
    
    scope="FB=4_5_15;IntervalDuration=3600;BlockDuration=monthly;HistoryLength=13"
    certDataScopeFBs="FB=1_4_5_15"
    
    // if an offline authorization (FB_XX) is used, provide the authorization id and access token
    optionalOfflineAuthorizationID="5"
    optionalOfflineAccess_token="57673811-5a25-4412-89e1-e15043f9703f"
    
# Retrieve Target Certificates
The certificates for the resource server and authorization server must be retrieved and installed on the test client.

For this step, run from the stunnelConfigDirectory directory and install certificates in the CApathDirectory. These paths are in the gbcmdcer.conf file. 

## fetch certificates and install 
host ($1) and port ($2) as arguments to retrieve certificate. Perform this once for each remote target server to be used.

	cd {stunnelConfigDirectory}
	sudo echo Q | openssl s_client -showcerts -connect $1:$2 -CApath /etc/ssl/certs -cert openespi.pem -key openespi_private_key.pem  | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > ~/Desktop/$1.pem
	
	sudo cp ~/Desktop/$1.pem {CApathDirectory}/$1.pem
	cd /etc/ssl/certs
	sudo ln -s $1.pem `openssl x509 -hash -noout -in $1.pem`.0
	

## test that the certificates can be verified
From the stunnelConfigDirectory directory test each server - look for Verify return code:0 (ok):

	echo Q | openssl s_client -verify 10 -showcerts -CApath {CApathDirectory} -cert openespi.pem -key openespi_private_key.pem -connect $1:$2  

Note: If there are any errors listed in the exchange (even if the verify is ok) you may need to check intermediate certificates in the chain from the "-showcerts" parameter. If so, you may need to acquire these certificates from DigiCert, Verisign or appropriate source and add them to the certs directory.