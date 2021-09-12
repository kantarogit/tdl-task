package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WebDriverFactory;

public class BasePage {

    protected WebDriver driver;
    private String pageUrl;

    @FindBy(how = How.CLASS_NAME, using = "menu")
    private WebElement topMenu;

    public BasePage(String pageUrl) {
        driver = WebDriverFactory.getDriver();
        PageFactory.initElements(driver, this);
        driver.manage().window().maximize();
        this.pageUrl = pageUrl;
    }

    public void goTo() {
        driver.navigate().to(pageUrl);
    }

    public static void endSession() {
        WebDriverFactory.closeBrowser();
    }

    public boolean isAt() {
        return new WebDriverWait(driver, 20).until(ExpectedConditions.urlContains(pageUrl));
    }

    public boolean urlContains(String url) {

        //not the best solution, another might be instead of checking the browser URL maybe to wait on some unique element
        //of the given page, this is bad because of all these string manipulation
        url = url.toLowerCase();
        url = url.replace(" & ", "-");
        url = url.replace(", ", "-");
        url = url.replace(" ", "-");
        url = url.replace("/", "-");
        url = url.replace("press", "newsroom");

        return new WebDriverWait(driver, 20).until(ExpectedConditions.urlContains(url));
    }

    protected WebElement getMenu() {
        return this.topMenu;
    }

}