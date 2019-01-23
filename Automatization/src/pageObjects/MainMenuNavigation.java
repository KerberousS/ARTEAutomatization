package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MainMenuNavigation {
	
    final WebDriver driver;
 
    @FindBy(xpath="//A[@class='button no-margin']")
    public WebElement loginButton;
 
    public MainMenuNavigation(WebDriver driver){

        this.driver = driver;

        //This initElements method will create all WebElements

        PageFactory.initElements(driver, this);

    }
}
