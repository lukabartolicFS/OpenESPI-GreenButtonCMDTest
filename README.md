<p id="top"></p>
OpenESPI-GreenButtonCMDTest
===========================
This project will hold test scripts and tools for Green Button Connect My Data

[Virtual Machine Setup](#virtual)

[Requirements for Installing the Test Suite](#install)

[Configuration](#configuration)

[Sample Data](#data)

<h1 id="virtual">Virtual Machine</h1> 

[Top](#top)

##Download the VM and VMware Player

- You will need to download and install VMware Player (Windows/Linux) or Fusion (Mac) to run the virtual machine on your PC.

- VM Ware Player for Linux and Windows: [http://www.vmware.com/products/player/overview.html](http://www.vmware.com/products/player/overview.html)

- VM Ware Fusion for Mac OSx: [http://www.vmware.com/products/fusion/overview.html](http://www.vmware.com/products/fusion/overview.html)

- Next, download the virtual machine: http://openespi.s3-website-us-east-1.amazonaws.com/virtualmachines/OpenESPI-Greenbutton-javaVM.zip

##VM Configuration Instructions

1. NOTE: The Virtual Machine is downloaded as a compressed file. On Windows 7, we have noticed that WinZip is required to de-compress the VM folder!
2. When asked if VM was "copied" or "moved", select "copied" 
3. login as bitnami (the default but will say "Authorized User" in menu) with password: password
4. setup github keys
	1. If you have already configured your keys:
		1. from a local VM terminal:
			1. copy your keys to the /home/bitnami/.ssh
			2. **sudo chmod 600 id_rsa** (assumes private key is id_rsa) to set permissions on private key
			3. **ssh –T git@github.com** (to test keys and verify you connect to git securely)
		2. If you have not made keys, Follow procedure defined at [Github](github.com) (start at step Next: Set Up SSH Keys)
		3. Note: You might want to save these keys for use on other computers/platforms if desired
		4. Use Firefox and go to: [https://github.com/](github.com)
		5. login to GitHub using your github.com credentials **{gitUserName : pw}**
		6. navigate to the https://github.com/energyos/OpenESPI repository
		7. On GitHub, fork the master branch of the OpenESPI repository
		8. Configure your local git repository: from a local VM Terminal, do git commands to configure the git workspace
        	1. **git config --global user.name "Firstname Lastname"**
        	2. **git config --global user.email "your_email@youremail.com"**
        	3. **git config --global github.user {gitUserName}**

<h1 id="install">Installation and Initial Test Setup</h1> 

[Top](#top)

## Install Test Harness

- Install Firefox version 31 (jar files are compatible with this version only at this time)
- [Install SOAPUI](http://sourceforge.net/projects/soapui/files/soapui/5.0.0/SoapUI-x64-5.0.0.sh/download)
- [Install project dependencies](https://github.com/energyos/OpenESPI-GreenButtonCMDTest/tree/master/SOAPUI/projectDependencies)
 

###Install Firefox version 31
Note newer versions of FireFox do not have a compatible "selenium" driver yet (as of 12/31/2014):

    wget ftp://ftp.mozilla.org/pub/mozilla.org/firefox/releases/31.0/linux-x86_64/en-US/firefox-31.0.tar.bz2
    tar -xjvf firefox-31.0.tar.bz2
    sudo mv firefox /opt/firefox31.0
    sudo ln -sf /opt/firefox31.0/firefox /usr/bin/firefox

###Install SOAPUI 5.1.3 
- [Download](http://sourceforge.net/projects/soapui/files/soapui/5.1.3/SoapUI-x64-5.1.3.sh/download)
- Install depending on your operating system


###How to integrate selenium with SOAPUI##

1.	Install SoapUI.
1.	Retrieve project dependencies jars and drop them into your SoapUI installation (into %SOAPUI_HOME%\bin\ext).	
1.	Ensure lftp is installed. To install use the follloing command: sudo apt-get install lftp
1.	For the given unit under test use lftp to ensure sftp credentials work and are applied. For example: lftp sftp://user:password@host  -e "dir /; bye"

###Documentation on the WebDriver API 
This link provides documentation of the WebDriver API for running selenium steps from within a programming language. [WebDriver API Pages](http://docs.seleniumhq.org/docs/03_webdriver.jsp#selenium-webdriver-api-commands-and-operations) 

### Install the test suite SOAPUI project
Clone [SOAPUI Project](https://github.com/energyos/OpenESPI-GreenButtonCMDTest.git) into a local directory.

Open project in SOAPUI.

<h1 id="configuration">Configuring SOAPUI Project</h1>


[Top](#top)
## Initial Checkout Regression Test
1. To test the installation, you need to have the OpenESPI Suite running. See [Github development with OpenESPI](http://www.greenbuttondata.org/espi/developmentEnvironment/).

1. Install the development environment and run the default application which will be on localhost:8080.

1. In the test project directory, run the bash script **%Test Project Directory%OpenESPI-GreenButtonCMDTest\SOAPUI\etc\toRegression.sh**. This script will copy default configuration files and populate the MySql database for the regression test.

1. In the SOAPUI project, run the script **GBCMD/Library/GeneralScripts/LoadConfig**. This will load the default test configuration environment that matches the OpenESPI default configuration.

1. Run the Regression Test Suite: **OpenESPIIntegrationTests**. This suite will exercise all tests of functionality of the OpenESPI suite and will validate your installation and configurations.


## Configuration for "testing" out of the box"
After installing the software, and assuming you have the OpenESPI suite running, these instructions will describe how to configure the test suite to run a regression test.

### populate the mysql database
Depending on your test configuration, there are a set of batch scripts that can be used to prepopulate the database:

1. **toRegression.sh** -- this script will populate the "out of the box" test configuration that matches the OpenESPI source tree.
2. **toRegressionSecure.sh** -- this script will populate the database with secure versions of the settings allowing TLS messaging.

There are other versions for specific purposes. Use these scripts to initialize the database ready to test.

Then, with the database populated, run a regression test by:

1. Load the configuration file with the SOAPUI project **GBCMD/Library/GeneralScripts/Test Steps/LoadConfig**
2. Open the test suite OpenESPIIntegrationTests and run this test suite. It should complete in about 5 minutes with no errors.

### the gbcmd.conf file
Depending on the location and nature of your test target, you will need a site-specific configuration for the tests. In the Library folder of the SOAPUI project there is a script that will load parameters from a file ./etc/gbcmd.conf. This file is not in source control and may be tailored from the "sample" version of the file gbcmd.conf.sample :

	FileName="gbcmd.conf"

	// urls
	BaseURL="http://localhost:8080"
	ServiceEndpoint="http://localhost:8080/DataCustodian"
	DataCustodianContext="DataCustodian"
	ThirdPartyContext="ThirdParty"
	linkPrefixForReplace="http://localhost:8080/DataCustodian/"

	// test accounts
	TestManager="grace"
	TestManagerPW="koala"
	TestRetailCustomer="alan"
	TestRetailCustomerPW="koala"

	// resourceIds
	retailCustomerId="1"
	usagePointId="1"
	resourceId="01"
	meterReadingId="1"
	readingTypeId="1"
	intervalBlockId="1"
	electricPowerQualitySummaryId="1"
	electricPowerUsageSummaryId="1"
	subscriptionId="5"
	applicationInformationId="1"
	authorizationId="1"
	bulkId="1"
	localTimeParametersId="1"

	// test files
	TestFile="test_usage_data.xml"
	usagePointUUID1="48C2A019-5598-4E16-B0F9-49E4FF27F5FB"
	usagePointDescription1="Front Electric Meter"
	TestFile2="Gas.xml"
	usagePointUUID2="642EABA-8E42-4D1A-A375-AF54993C007B"
	usagePointDescription2="Gas"

	// access tokens
	upload_access_token="809caf03-612e-4e89-94b1-6f86d83b1ef8"
	data_custodian_access_token="688b026c-665f-4994-9139-6b21b13fbeee"
	third_party_access_token="75dd9c46-becf-48b5-9cb5-9c3233d718d0"
	registration_third_party_access_token="d89bb056-0f02-4d47-9fd2-ec6a19ba8d0c"

	// external program system commands
	mysqlCmdDC="mysql --user=root --password=password"
	mysqlCmdTP="mysql --user=root --password=password"
	opensslCmd="openssl"
	timeoutCmd="timeout"
	DBprepopulateScriptName="prepopulatesql_dc.sql"


### How to change and load the settings
The most common fields to change are:

- BaseURL -the URL of the target system you are running tests against
- mysqlCmd -the shell command to access mysql on your platform (required by some of the scripts)
- TestFile -a path to the test file used in the regression tests

To load these settings into the SOAPUI project use the Library script "LoadConfig".


<h1 id="data">Test Data Files</h1>

[Top](#top)


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
