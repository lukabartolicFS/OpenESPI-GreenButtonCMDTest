import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertTrue;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;

public class DriverHelper {

	public WebDriver _driver;
	public Object log;
	
	public String _strBaseUrl = "";
	public String _strDataCustodianContext = "";
	public String _strThirdPartyContext = "";
	
	public final String CUSTODIAN_USERNAME = "grace";
	public final String CUSTODIAN_PASSWORD = "koala";

	public boolean _bLogActions = false;
	public int _iSleepBetweenStepsMs = 0;

	private int _iActionLineNum = 0;
	private String _strActionMethodName = "";

	public DriverHelper(WebDriver driver,String strBaseURL,String strDataCustodianContext,String strThirdPartyContext,Object theLog)
	{
		_driver = driver;
		log = theLog;
		_strBaseUrl = strBaseURL;
		_strDataCustodianContext = strDataCustodianContext;
		_strThirdPartyContext = strThirdPartyContext;
	}

	public void quit()
	{
		_driver.quit();
	}

	private void CaptureAction()
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
   			sleep(_iSleepBetweenStepsMs);
   		}

   		if(_driver.getPageSource().contains("Application Error"))
		{
			log.error("Page source contains 'Application Error'");
 			Exception e;
			throw e;  
		}	

  		if(_driver.getPageSource().contains("JPA transaction"))
		{
			log.error("Page source contains 'JPA transaction'");
 			Exception e;
			throw e;  
		}

   		if(_driver.getPageSource().contains("unexpected error"))
		{
			log.error("Page source contains 'unexpected error'");
 			Exception e;
			throw e;  
		}

	}

	public void Before()
	{	
		CaptureAction();
        _driver.get(_strBaseUrl + "/" + _strThirdPartyContext + "/j_spring_security_logout");
        _driver.get(_strBaseUrl + "/" + _strDataCustodianContext + "/logout.do");
        login(_strBaseUrl + "/" + _strDataCustodianContext, CUSTODIAN_USERNAME, CUSTODIAN_PASSWORD);    
        _driver.get(_strBaseUrl + "/" + _strDataCustodianContext + "/custodian/removealltokens");
        _driver.get(_strBaseUrl + "/" + _strDataCustodianContext + "/logout.do");	     	
	}

	public void login(String strEndpoint,String strUserName,String strPassword)
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

	public void submitLoginForm(String strUserName,String strPassword)
	{
	    CaptureAction();	
		_driver.findElement(By.name("j_username")).clear();
		_driver.findElement(By.name("j_username")).sendKeys(strUserName);
		_driver.findElement(By.name("j_password")).clear();
		_driver.findElement(By.name("j_password")).sendKeys(strPassword);
		_driver.findElement(By.name("submit")).click();
	}

	public int GetNumElsByTagName(String strTagName)
	{
		CaptureAction();

		return _driver.findElements(By.tagName(strTagName)).size();
	}

	public void logout(String strContext)
	{
		CaptureAction();
	    _driver.get(strContext);
        if (_driver.findElements(By.id("logout")).size() > 0) {
            _driver.findElement(By.id("logout")).click();
        }	

		//_driver.findElement(By.id("logout")).click();
	}

	public WebElement findElement(By by)
	{
		CaptureAction();
		return _driver.findElement(by);
	}

    public void navigateTo(String strPath) 
    {
    	CaptureAction();
    	log.info("navto:" + strPath);
        _driver.get(strPath);
    }

	public List<WebElement> FindElementsByXpath(String strXpath)
	{
		CaptureAction();
		return _driver.findElements(By.xpath(strXpath));
	}

    public void get(String path) 
    {
    	CaptureAction();
        _driver.get(path);
    }	


    public void AssertStringNotEmpty(String strValue)
    {
    	CaptureAction();

    	if(strValue.length()==0){
    		log.error("String not empty:'" + strValue + "'");
 			Exception e;
			throw e;   		
    	}
    }


	public assertUrlContains(String strValue)
	{
		CaptureAction();
		if(!_driver.getCurrentUrl().contains(strValue))
		{
			log.error("Current URL does not contain: '" + strValue + "'");
 			Exception e;
			throw e;  		
		}
	}

	public assertUrlEndsWith(String strValue)
	{
		CaptureAction();
		if(!_driver.getCurrentUrl().endsWith(strValue))
		{
			log.error("Current URL does not contain: '" + strValue + "'");
 			Exception e;
			throw e;  		
		}
	}

	public assertContains(String strValue)
	{
		CaptureAction();
		//assertTrue("Page source did not contain '" + strValue + "'", _driver.getPageSource().contains(strValue));

		if(!_driver.getPageSource().contains(strValue))
		{
			log.error("Page source did not contain '" + strValue + "'");
 			Exception e;
			throw e;  
		}
	}

	public assertDoesNotContain(String strValue)
	{
		CaptureAction();
		//assertTrue("Page source did not contain '" + strValue + "'", _driver.getPageSource().contains(strValue));

		if(_driver.getPageSource().contains(strValue))
		{
			log.error("Page source contains '" + strValue + "'");
 			Exception e;
			throw e;  
		}
	}

	public assertXpathExists(String strSearch)
	{
		CaptureAction();

		try{
			assertXpathExists(strSearch, _driver.getPageSource());
		}
		catch(Exception e)
		{
			log.error("XPath did not exist:'" + strSearch + "'");
			throw e;			
		}
	}

	public logStep(String strStep)
	{
		log.info("//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
		log.info(strStep);
		log.info("//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
		//sleep(5000);
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

public static DriverHelper GetObj(String strBaseURL,String strDataCustodianContext,String strThirdPartyContext,Object theLog)
{
	WebDriver driver = new FirefoxDriver()
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

	DriverHelper temp = new DriverHelper(driver,strBaseURL,strDataCustodianContext,strThirdPartyContext,theLog);

	return temp;
}
