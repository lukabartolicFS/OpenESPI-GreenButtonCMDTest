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

	public static quit()
	{
		_driver.quit();
	}

	public static void login(String strEndpoint,String strUserName,String strPassword)
	{
		_driver.get(strEndpoint);
		_driver.findElement(By.id("login")).click();
		_driver.findElement(By.name("j_username")).clear();
		_driver.findElement(By.name("j_username")).sendKeys(strUserName);
		_driver.findElement(By.name("j_password")).clear();
		_driver.findElement(By.name("j_password")).sendKeys(strPassword);
		_driver.findElement(By.name("submit")).click();
	}

	public static void logout()
	{
		_driver.findElement(By.id("logout")).click();
	}

	public static void sendKeysByName(String strLink,String strValue)
	{
		_driver.findElement(By.name(strLink)).sendKeys(strValue);
	}
	
	public static void clickLinkByText(String strLink)
	{
        WebElement link = _driver.findElement(By.linkText(strLink));
        link.click();		
	}

	public static clickByClass(String strLink)
	{
        WebElement radio = _driver.findElement(By.className(strLink));
        radio.click();
	}

	public static clickBypartialLinkText(String strLink)
	{
		_driver.findElement(By.partialLinkText(strLink)).click();	
	}

	public static fillInById(String strName,String strValue)
	{
		_driver.findElement(By.id(strName)).clear();
		_driver.findElement(By.id(strName)).sendKeys(strValue);
	}

	public static fillInByName(String strName,String strValue)
	{
		_driver.findElement(By.name(strName)).clear();
		_driver.findElement(By.name(strName)).sendKeys(strValue);
	}

	public static clickByName(String strName)
	{
		_driver.findElement(By.name(strName)).click();
	}

	public static assertUrlEndsWith(String strValue)
	{
		if(!_driver.getCurrentUrl().endsWith(strValue))
		{
			log.error("Current URL does not contain: '" + strValue + "'");
			_driver.quit();
			assert false;			
		}
	}

	public static assertContains(String strValue)
	{
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
