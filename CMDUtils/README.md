OpenESPI-GreenButtonCMDTest CMDUtils
===========================
This project will hold test scripts and tools for Green Button Connect My Data

## Build

Build the CMDUtils jar using Maven:

mvn package

## Setup

Copy the file 

target/CMDUtils-1.0-SNAPSHOT.jar 

to the SoapUI extension folder
<SOAPUI installfolder>/bin/ext/CMDUtils-1.0-SNAPSHOT.jar 

##	Documentation on the CMDUtils 

CMDUtils provides a helper class DriverHelper which encapsultes functionality required to run the SoapUI OpenESPIRegression tests that utilize the selenium/firefox drivers for web GUI testing.

This class simplifies reuse of selenium IDE created and exported java test code.

## Using eclipse to debug

Add the following Ecslipse variable:

SOAPUI_HOME=/Applications/SoapUI-5.0.0.app/Contents/java/app (for example on OS X - this should be SoapUI install folder )

Add external jars (lib,ext and bin)

Add the following Debug Envonmnet vars:

SOAPUI_HOME=${SOAPUI_HOME}
SOAPUI_CLASSPATH=${SOAPUI_HOME}/bin/soapui-5.0.0.jar:$SOAPUI_HOME/lib/*

Using the Debug configuration editor use the collowing settings:

Main class: com.eviware.soapui.SoapUI

debug VM arguments:

-Xms128m -Xmx1024m -Dsoapui.properties=soapui.properties -Dsoapui.home=${SOAPUI_HOME}/bin -splash:soapui-splash.png -Dsoapui.ext.libraries=${SOAPUI_HOME}/bin/ext -Dsoapui.ext.listeners=${SOAPUI_HOME}/bin/listeners -Dsoapui.ext.actions=${SOAPUI_HOME}/bin/actions -Djava.library.path=${SOAPUI_HOME}/bin -Dwsi.dir=${SOAPUI_HOME}/wsi-test-tools -cp 'java -cp ${SOAPUI_HOME}/bin/soapui-5.0.0.jar:${SOAPUI_HOME}/lib/* com.eviware.soapui.tools.JfxrtLocator':${SOAPUI_HOME}/bin/soapui-5.0.0.jar:${SOAPUI_HOME}/lib/*


