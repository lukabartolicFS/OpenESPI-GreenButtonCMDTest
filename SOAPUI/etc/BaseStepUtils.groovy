import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertTrue;


public class BaseStepUtilsA {

	public static WebDriver _driver;
	public static Object log;
	
	public final static String BASE_URL = "http://localhost:8080/";
	public final static String THIRD_PARTY_CONTEXT = "ThirdParty";
	public final static String DATA_CUSTODIAN_CONTEXT = "DataCustodian";
	public final static String DATA_CUSTODIAN_BASE_URL = BASE_URL + DATA_CUSTODIAN_CONTEXT;
	public final static String THIRD_PARTY_BASE_URL = BASE_URL + THIRD_PARTY_CONTEXT;
	
	public static final String USERNAME = "alan";
	public static final String PASSWORD = "koala";
	
	public static final String CUSTODIAN_USERNAME = "grace";
	public static final String CUSTODIAN_PASSWORD = "koala";

	private static boolean _bLogActions = true;

	public static quit()
	{
		_driver.quit();
	}

	private static void LogAction()
	{
	    if(_bLogActions)
        {
        	if(Thread.currentThread().getStackTrace()[8].getLineNumber()!=-1)
        	{
        		log.info("Action from script line[" + Thread.currentThread().getStackTrace()[8].getLineNumber() + "]:" + Thread.currentThread().getStackTrace()[3].getMethodName())
    		}
    		//log.info("Stack:" + Thread.currentThread().getStackTrace());
        }	
	}

	public static void Before()
	{	
		LogAction();
        _driver.get(THIRD_PARTY_BASE_URL + "/j_spring_security_logout");
        _driver.get(DATA_CUSTODIAN_BASE_URL + "/logout.do");
        login(DATA_CUSTODIAN_BASE_URL, "grace", "koala");    
        _driver.get(DATA_CUSTODIAN_BASE_URL + "/custodian/removealltokens");
        _driver.get(DATA_CUSTODIAN_BASE_URL + "/logout.do");	     	
	}

	public static void login(String strEndpoint,String strUserName,String strPassword)
	{
        //navigateTo(context, "/");
        //if (_driver.findElements(By.id("logout")).size() > 0) {
        //    logout();
        //}
        //logout(); 
        LogAction();
		_driver.get(strEndpoint);
		_driver.findElement(By.id("login")).click();
		_driver.findElement(By.name("j_username")).clear();
		_driver.findElement(By.name("j_username")).sendKeys(strUserName);
		_driver.findElement(By.name("j_password")).clear();
		_driver.findElement(By.name("j_password")).sendKeys(strPassword);
		_driver.findElement(By.name("submit")).click();
	}

	public int GetNumElsByTagName(String strTagName)
	{
		LogAction();

		return _driver.findElements(By.tagName(strTagName)).size();
	}

	public static void logout(String strContext)
	{
		LogAction();
	    _driver.get(strContext);
        if (_driver.findElements(By.id("logout")).size() > 0) {
            _driver.findElement(By.id("logout")).click();
        }	

		//_driver.findElement(By.id("logout")).click();
	}

    public static void navigateTo(String context, String path) 
    {
    	LogAction();
        _driver.get(BASE_URL + context + path);
    }

	public static void sendKeysByName(String strLink,String strValue)
	{
		LogAction();
		_driver.findElement(By.name(strLink)).sendKeys(strValue);
	}
	
	public static void clickByXpath(String strLink)
	{
		LogAction();
		_driver.findElement(By.xpath(strLink)).click();
	}

	public static void clickLinkByText(String strLink)
	{
		LogAction();
        _driver.findElement(By.linkText(strLink)).click();	
	}

	public static clickByClass(String strLink)
	{
		LogAction();
        _driver.findElement(By.className(strLink)).click();
	}

	public static clickBypartialLinkText(String strLink)
	{
		LogAction();
		_driver.findElement(By.partialLinkText(strLink)).click();	
	}

	public static fillInById(String strName,String strValue)
	{
		LogAction();
		_driver.findElement(By.id(strName)).clear();
		_driver.findElement(By.id(strName)).sendKeys(strValue);
	}

	public static fillInByName(String strName,String strValue)
	{
		LogAction();
		_driver.findElement(By.name(strName)).clear();
		_driver.findElement(By.name(strName)).sendKeys(strValue);
	}

	public static clickByName(String strName)
	{
		LogAction();
		_driver.findElement(By.name(strName)).click();
	}

	public static assertUrlContains(String strValue)
	{
		LogAction();
		if(!_driver.getCurrentUrl().contains(strValue))
		{
			log.error("Current URL does not contain: '" + strValue + "'");
			_driver.quit();
			assert false;			
		}
	}

	public static assertUrlEndsWith(String strValue)
	{
		LogAction();
		if(!_driver.getCurrentUrl().endsWith(strValue))
		{
			log.error("Current URL does not contain: '" + strValue + "'");
			_driver.quit();
			assert false;			
		}
	}

	public static assertContains(String strValue)
	{
		LogAction();
		//assertTrue("Page source did not contain '" + strValue + "'", _driver.getPageSource().contains(strValue));

		if(!_driver.getPageSource().contains(strValue))
		{
			log.error("Page source did not contain '" + strValue + "'");

			_driver.quit();
			assert false;
		}
	}

	public static assertDoesNotContain(String strValue)
	{
		LogAction();
		//assertTrue("Page source did not contain '" + strValue + "'", _driver.getPageSource().contains(strValue));

		if(_driver.getPageSource().contains(strValue))
		{
			log.error("Page source contains '" + strValue + "'");
			_driver.quit();
			assert false;
		}
	}

	public static logStep(String strStep)
	{
		log.info(strStep);
		//sleep(5000);
	}
}

public static BaseStepUtilsA GetObj(Object theLog)
{
	WebDriver driver = new FirefoxDriver()
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

	BaseStepUtilsA temp = new BaseStepUtilsA();
	temp._driver = driver;
	temp.log = theLog;
	return temp;
}
