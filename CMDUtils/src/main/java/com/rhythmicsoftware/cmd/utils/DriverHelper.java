package com.rhythmicsoftware.cmd.utils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


public class DriverHelper {

	public WebDriver _driver;
	
	public Logger log;
	
	public String _strBaseUrl = "";
	public String _strDataCustodianContext = "";
	public String _strThirdPartyContext = "";
	
	public final String CUSTODIAN_USERNAME = "grace";
	public final String CUSTODIAN_PASSWORD = "koala";

	public boolean _bLogActions = false;
	public int _iSleepBetweenStepsMs = 0;

	private int _iActionLineNum = 0;
	private String _strActionMethodName = "";

	public DriverHelper(String strBaseURL,String strDataCustodianContext,String strThirdPartyContext,Logger theLog)
	{
		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		_driver = driver;
		log =  theLog;
		_strBaseUrl = strBaseURL;
		_strDataCustodianContext = strDataCustodianContext;
		_strThirdPartyContext = strThirdPartyContext;
	}

	public void quit()
	{
		_driver.quit();
	}
	
	public Set<Cookie> getSessionCookie()
	{
		return _driver.manage().getCookies();
	}

	private void CaptureAction() throws Exception
	{
 		//log.info("Stack:" + Thread.currentThread().getStackTrace());
		_iActionLineNum = 0;
		_strActionMethodName = "";

		for(int i=0;i<Thread.currentThread().getStackTrace().length;i++)
		{
			String strTrace = Thread.currentThread().getStackTrace()[i].toString();

			//log.info("Trace[:" + i + "]:" + strTrace);

			if((strTrace.contains("(Script")) && (strTrace.contains(".groovy:")))
			{
				_iActionLineNum = Thread.currentThread().getStackTrace()[i].getLineNumber();
			}
  			if(strTrace.contains("BaseStepUtils.groovy"))
			{
				_strActionMethodName = Thread.currentThread().getStackTrace()[i].getMethodName();
			}  			
		}

	    if(_bLogActions)
        {
   			log.info("Action from script line[" + _iActionLineNum + "]:" + _strActionMethodName);
   		}	

   		if(_iSleepBetweenStepsMs>0)
   		{
   			Thread.sleep(_iSleepBetweenStepsMs);
   		}

   		if(_driver.getPageSource().contains("Application Error"))
		{
			log.error("Page source contains 'Application Error'");
 			throw new Exception(); 
		}	

  		if(_driver.getPageSource().contains("JPA transaction"))
		{
			log.error("Page source contains 'JPA transaction'");
 			throw new Exception(); 
		}

   		if(_driver.getPageSource().contains("unexpected error"))
		{
			log.error("Page source contains 'unexpected error'");
 			throw new Exception(); 
		}

		if(_driver.getPageSource().contains("HTTP Status 404"))
		{
			log.error("Page source contains 'HTTP Status 404'");
 			throw new Exception(); 
		}	

	}

	public void Before() throws Exception
	{	
		CaptureAction();
        _driver.get(_strBaseUrl + "/" + _strThirdPartyContext + "/j_spring_security_logout");
        _driver.get(_strBaseUrl + "/" + _strDataCustodianContext + "/logout.do");
        login(_strBaseUrl + "/" + _strDataCustodianContext, CUSTODIAN_USERNAME, CUSTODIAN_PASSWORD);    
    //    _driver.get(_strBaseUrl + "/" + _strDataCustodianContext + "/custodian/removealltokens");
        _driver.get(_strBaseUrl + "/" + _strDataCustodianContext + "/logout.do");	     	
	}

	public void login(String strEndpoint,String strUserName,String strPassword) throws Exception
	{
        //navigateTo(context, "/");
        //if (_driver.findElements(By.id("logout")).size() > 0) {
        //    logout();
        //}
        //logout(); 
        CaptureAction();
		_driver.get(strEndpoint);
		_driver.findElement(By.id("login")).click();
		_driver.findElement(By.name("j_username")).clear();
		_driver.findElement(By.name("j_username")).sendKeys(strUserName);
		_driver.findElement(By.name("j_password")).clear();
		_driver.findElement(By.name("j_password")).sendKeys(strPassword);
		_driver.findElement(By.name("submit")).click();
	}

	public void submitLoginForm(String strUserName,String strPassword) throws Exception
	{
	    CaptureAction();	
		_driver.findElement(By.name("j_username")).clear();
		_driver.findElement(By.name("j_username")).sendKeys(strUserName);
		_driver.findElement(By.name("j_password")).clear();
		_driver.findElement(By.name("j_password")).sendKeys(strPassword);
		_driver.findElement(By.name("submit")).click();
	}

	public int GetNumElsByTagName(String strTagName) throws Exception
	{
		CaptureAction();

		return _driver.findElements(By.tagName(strTagName)).size();
	}

	public void logout(String strContext) throws Exception
	{
		CaptureAction();
	    _driver.get(strContext);
        if (_driver.findElements(By.id("logout")).size() > 0) {
            _driver.findElement(By.id("logout")).click();
        }	

		//_driver.findElement(By.id("logout")).click();
	}

	public WebElement findElement(By by) throws Exception
	{
		CaptureAction();
		return _driver.findElement(by);
	}

    public void navigateTo(String strPath) throws Exception 
    {
    	CaptureAction();
    	log.info("navto:" + strPath);
        _driver.get(strPath);
    }

	public List<WebElement> FindElementsByXpath(String strXpath) throws Exception
	{
		CaptureAction();
		return _driver.findElements(By.xpath(strXpath));
	}

    public void get(String path) throws Exception 
    {
    	CaptureAction();
        _driver.get(path);
    }	


    public void AssertStringNotEmpty(String strValue) throws Exception
    {
    	CaptureAction();

    	if(strValue.length()==0){
    		log.error("String not empty:'" + strValue + "'");
 			throw new Exception();   		
    	}
    }


	public void assertUrlContains(String strValue) throws Exception
	{
		CaptureAction();
		if(!_driver.getCurrentUrl().contains(strValue))
		{
			log.error("Current URL does not contain: '" + strValue + "'");
 			throw new Exception(); 	
		}
	}

	public void assertUrlEndsWith(String strValue) throws Exception
	{
		CaptureAction();
		if(!_driver.getCurrentUrl().endsWith(strValue))
		{
			log.error("Current URL does not contain: '" + strValue + "'");
 			throw new Exception(); 		
		}
	}

	public void assertContains(String strValue) throws Exception
	{
		CaptureAction();
		//assertTrue("Page source did not contain '" + strValue + "'", _driver.getPageSource().contains(strValue));

		if(!_driver.getPageSource().contains(strValue))
		{
			log.error("Page source did not contain '" + strValue + "'");
 			throw new Exception(); 
		}
	}

	public void assertDoesNotContain(String strValue) throws Exception
	{
		CaptureAction();
		//assertTrue("Page source did not contain '" + strValue + "'", _driver.getPageSource().contains(strValue));

		if(_driver.getPageSource().contains(strValue))
		{
			log.error("Page source contains '" + strValue + "'");
 			throw new Exception(); 
		}
	}

	public void assertXpathExists(String strSearch, String string) throws Exception
	{
		CaptureAction();

		try{
//			 XMLAssert.assertXpathExists(strSearch, _driver.getPageSource());
			 assertXpathExists(strSearch, _driver.getPageSource());
		}
		catch(Exception e)
		{
			log.error("XPath did not exist:'" + strSearch + "'");
			throw new Exception(); 		
		}
	}

	public void logStep(String strStep)
	{
		log.info("//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
		log.info(strStep);
		log.info("//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
		//Thread.sleep(5000);
	}

	public String getErrorString()
	{
		String strResult;
		if(_iActionLineNum!=0)
		{
			strResult = "Failed performing: Action from script line[" + _iActionLineNum + "]:" + _strActionMethodName;
		}
		else
		{
			strResult = "Unknown error";
		}
		_iActionLineNum = 0;
		_strActionMethodName = "";
		return strResult;
	}


}
