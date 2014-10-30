OpenESPI-GreenButtonCMDTest
===========================
This project will hold test scripts and tools for Green Button Connect My Data

## Requirements

- [Install SOAPUI](http://sourceforge.net/projects/soapui/files/soapui/5.0.0/SoapUI-x64-5.0.0.sh/downloadl)
- [Install Selenium Library] (http://selenium-release.storage.googleapis.com/2.42/selenium-server-standalone-2.42.2.jar)
- [Install Groovy HTTP Library] (http://repository.codehaus.org/org/codehaus/groovy/modules/http-builder/http-builder/0.7/http-builder-0.7-all.zip)

## Setup
How to integrate selenium with SOAPUI:
see http://siking.wordpress.com/2011/08/22/groovy-selenium-webdriver-and-soapui-part-3/

1.	Install SoapUI.
2.	Download Selenium (you need the selenium-server-standalone-2.*.jar) and drop it into your SoapUI installation (into %SOAPUI_HOME%\bin\ext).	
3.	Add the Groovy libaries to (That zip file has a main jar file and folder dependencies. Copy all these jar files to the <SOAPUI installfolder>/bin/ext)


##	Documentation on the WebDriver API 
This link provides documentation of the WebDriver API for running selenium steps from within a programming language.
http://docs.seleniumhq.org/docs/03_webdriver.jsp#selenium-webdriver-api-commands-and-operations 


===========================================================
Test Data Files
===========================================================

##Files in repository

test_usage_data.xml: 

	UUID of Usage Point:
	
	48C2A019-5598-4E16-B0F9-49E4FF27F5FB
	
gas.xml

	UUID of Usage Point: 
	
	0642EABA-8E42-4D1A-A375-AF54993C007B
	
water.xml

	UUID of Usage Point: 
	
	A8914B45-719D-4131-BB8C-80FC48CD7C9E

ApplicationInformationSample.xml

	UUID of ApplicationInformation: 
	
	979313AE-885D-4860-B6C2-60B42B36B323

AuthorizationInformationSample.xml

	UUID of Authorization: 
	
	A84C74DE-B08D-4F86-8E58-F6FC6EF41CFF


##Larger Files remote on Amazonws

Test GreenButton_10.xml: 

https://s3.amazonaws.com/openespi/TestGreenButtonFiles/Test_GreenButton_10.xml

	UUID of Usage Point: 
	
	9c226002-f1e7-4f56-98b0-cbf227660b48
	

Bulk_10Customer_1UsagePoints_15MinuteInterval_1Year.xml:

https://s3.amazonaws.com/openespi/TestGreenButtonFiles/Bulk_10Customer_1UsagePoints_15MinuteInterval_1Year.xml
	
	UUIDs of Usage Points:
	
		2FBEE9DC-79FE-4D90-A456-44586CA57BAA
		D80EEB46-A73E-44E1-82B6-13B82B7B88F3
		220453B2-BFC0-4B4F-A84A-C527EB7B2DC5
		93284CA6-6B23-4F5B-8BFA-45F148C05ABB
		55D7C87A-B8E6-4B8C-997A-FD963448ADBA
		B4B0B599-4C0C-4457-A857-788AC25234D3
		23F18006-BB5E-4073-B9AC-D20610B0A97C
		F59E7D3A-71D1-4D76-8E26-C63EF9E996B0
		FBC8961A-D84D-4934-866E-2DA782F3E0D2
		D057FB82-6E98-4FBB-9B37-2CEC29DBCF5B

Bulk_400Customer_1UsagePoints_15MinuteInterval_1Year.xml	

https://s3.amazonaws.com/openespi/TestGreenButtonFiles/Bulk_400Customer_1UsagePoints_15MinuteInterval_1Year.xml	

##	Configuring SOAPUI Project


### the gbcmd.conf file
Depending on the location and nature of your test target, you will need a site-specific configuration for the tests. In the Library folder of the SOAPUI project there is a script that will load parameters from a file ./etc/gbcmd.conf. This file is not in source control and may be constructed from the following code snippit:

    FileName="gbcmd.conf"
    
    BaseURL="http://localhost:8080"
    ServiceEndpoint="http://localhost:8080/DataCustodian"
    DataCustodianContext="DataCustodian"
    ThirdPartyContext="ThirdParty"
    linkPrefixForReplace="http://localhost:8080/DataCustodian/"
    mysqlCmd="/usr/local/mysql/bin/mysql"
    TestManager="grace"
    TestManagerPW="koala"
    TestRetailCustomer="alan"
    TestRetailCustomerPW="koala"
    retailCustomerId="1"
    usagePointId="1"
    resourceId="01"
    meterReadingId="1"
    readingTypeId="1"
    intervalBlockId="1"
    electricPowerQualitySummaryId="1"
    TestFile="test_usage_data.xml"
    usagePointUUID1="48C2A019-5598-4E16-B0F9-49E4FF27F5FB"
    usagePointDescription1="Front Electric Meter"
    TestFile2="Gas.xml"
    usagePointUUID2="642EABA-8E42-4D1A-A375-AF54993C007B"
    usagePointDescription2="Gas"
    DBprepopulateScriptName="prepopulatesql_localhost8080.sql"
    electricPowerUsageSummaryId="1"
    subscriptionId="1"
    applicationInformationId="1"
    authorizationId="1"
    bulkId="1"
    localTimeParametersId="1"
    
    
    securityTimeout="2"
    HomePageURLEndpoint="https://services.greenbuttondata.org"
    DataCustodianHostName="127.0.0.1"
    DataCustodianSSLPort="8081"
    ThirdPartyHostName="127.0.0.1"
    ThirdPartySSLPort="8081"
    openSSLCommand="/usr/local/ssl/bin/openssl"
    cmdTimeout="timeout.sh -t"
    cygwinHome=""

### How to change and load the settings
The most common fields to change are:

- BaseURL -the URL of the target system you are running tests against
- mysqlCmd -the shell command to access mysql on your platform (required by some of the scripts)
- TestFile -a path to the test file used in the regression tests

To load these settings into the SOAUI project use the Library script "LoadConfig".
