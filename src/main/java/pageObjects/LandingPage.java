package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import utils.ContactFormField;

import java.util.ArrayList;
import java.util.List;

public class LandingPage extends BasePage {

    //this might be (and should be) put in the base page (all of the pages share this button)
    @FindBy(how = How.CLASS_NAME, using = "contact-us-btn")
    private WebElement contactUs;

    @FindBy(how = How.ID, using = "firstName")
    private WebElement firstName;
    @FindBy(how = How.ID, using = "lastName")
    private WebElement lastName;
    @FindBy(how = How.ID, using = "email")
    private WebElement email;
    @FindBy(how = How.ID, using = "message")
    private WebElement message;
    @FindBy(how = How.CLASS_NAME, using = "submit-button")
    private WebElement submitButton;

    private static final String PAGE_URL = "https://www.testdevlab.com/";

    public LandingPage() {
        super(PAGE_URL);
    }

    public List<WebElement> getAllSubMenuPerMenu() {

        List<WebElement> menuElements = getMenu()
                .findElement(By.className("sections"))
                .findElements(By.xpath("child::div"));

        return menuElements;
    }

    public void iterateThroughEachSubMenu(WebElement menuItem) {

        menuItem.click();
        List<WebElement> subMenuItems = menuItem.findElements(By.className("menu-item"));

        subMenuItems.forEach(subMenu -> {
            String menuName = subMenu.getText();

            System.out.println(menuName);

            // Blog link opens new tab so that is why this differs from the other cases
            if (subMenu.getText().equals("Blog")) {
                subMenu.click();
                List<String> tabs = new ArrayList<>(this.driver.getWindowHandles());
                this.driver.switchTo().window(tabs.get(1));
                this.urlContains(menuName);
                this.driver.close();
                this.driver.switchTo().window(tabs.get(0));
            } else {
                subMenu.click();
                this.urlContains(menuName);
                menuItem.click();
            }
        });
    }

    public ContactForm contactUs() {
        contactUs.click();
        return new ContactForm();
    }

    public String getErrorMessage(ContactFormField contactFormField) {
        switch (contactFormField) {
            case MESSAGE:
                return message
                        .findElement(By.xpath("parent::*")) // get parrent
                        .findElement(By.xpath("parent::*")) // get parrent once again to get the message div in which
                        .findElement(By.className("form-field-error-msg")) // this error is living under
                        .getText();
            case FIRST_NAME:
                return "Not implemented";
            default:
                return "";
        }
    }

    public String getFieldColor(ContactFormField contactFormField) {
        switch (contactFormField) {
            case MESSAGE:
                return message
                        .findElement(By.xpath("parent::*")) // same logic as in the getErrorMessage() method
                        .findElement(By.xpath("parent::*"))
                        .findElement(By.className("form-field-error-msg"))
                        .getCssValue("color");
            case FIRST_NAME:
                return "Not implemented";
            default:
                return "";
        }
    }

    // fluent syntax for contact form filling
    // it can be very helpful in data driven cases in which you can easily omit some of the mandatory fields
    // basically a building block when building internal DSL
    public class ContactForm {

        private ContactForm() {
        }

        public ContactForm withFirstName(String fname) {
            firstName.sendKeys(fname);
            return this;
        }

        public ContactForm withLastName(String lname) {
            lastName.sendKeys(lname);
            return this;
        }

        public ContactForm withEmail(String mail) {
            email.sendKeys(mail);
            return this;
        }

        public ContactForm withMessage(String messageText) {
            message.sendKeys(messageText);
            return this;
        }

        public void submitContactForm() {
            submitButton.click();
        }
    }
}
