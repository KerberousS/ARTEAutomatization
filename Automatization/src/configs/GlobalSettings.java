package configs;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import io.github.bonigarcia.wdm.ChromeDriverManager;

public class GlobalSettings {
    //Execute calendar for date specific values
    public LocalDateTime calendar = LocalDateTime.now();
    public String Day = DateTimeFormatter.ofPattern("yyyyMMdd").format(calendar);
    public String Time = DateTimeFormatter.ofPattern("HHmmss").format(calendar);
    public String DayAndTime = Day + "-" + Time;

	//Public variables
	public WebDriver driver;
	public WebDriverWait wait;
	
	//Global Strings for Test Use
        //Site Strings
        public String StronaGlowna = "https://artegence.com/";

        //Folders Strings
        public String TestOutputFolder = "test-output//";
        public String ScreenShotsFolder = "Screenshots//";
        public String FormatScreenshot = ".png";

	 @BeforeClass
		public void Settings()throws  Exception{
		
		//Setting Chrome to use proxy and accept certs
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		
		//Starting ChromeDriver, Merging Options and Capabilities into one, adding wait.
		ChromeDriverManager.getInstance().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--incognito");
         options.addArguments("--kiosk");
		options.merge(capabilities);
		driver = new ChromeDriver(options);
		wait = new WebDriverWait(driver, 10);
		DOMConfigurator.configure("log4j.xml");
		
		//Setting exception timeouts for page load, scripts and every find element, maximize window
		driver.manage().timeouts().pageLoadTimeout(10,SECONDS);
		driver.manage().timeouts().setScriptTimeout(5,SECONDS);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	 }

	public WebDriver getDriver() {
		return driver;
	}
	public WebDriverWait getDriverWait() {
		return wait;
	}
	public LocalDateTime calendar() { return calendar; }
}
