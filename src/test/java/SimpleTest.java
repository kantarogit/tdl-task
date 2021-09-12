import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matchers;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pageObjects.BasePage;
import pageObjects.LandingPage;
import utils.ContactFormField;
import utils.ErrorMessageColors;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class SimpleTest {

    private LandingPage landingPage;

    @BeforeClass
    public void setup() {
        landingPage = new LandingPage();
        landingPage.goTo();

        assertThat(landingPage.isAt(), Matchers.is(true));
    }

    @AfterClass
    public void teardown() {

        BasePage.endSession();
    }

    @Test(description = "navigates to all sub menus and checks changing browser url")
    public void shouldAccessAllSubMenu() {

        List<WebElement> menuItems = landingPage.getAllSubMenuPerMenu();

        menuItems.forEach(menuItem -> landingPage.iterateThroughEachSubMenu(menuItem));
    }

    @Test
    public void cannotSubmitEmptyMessageForm() {

        landingPage.contactUs()
                .withEmail("random@email.com")
                .withFirstName("Filip")
                .withLastName("Kantaro")
                .withMessage(StringUtils.EMPTY)
                .submitContactForm();

        assertThat(landingPage.getErrorMessage(ContactFormField.MESSAGE), Matchers.is("This field is required"));
        //fails on purpose, expecting pure RED :)
        assertThat(landingPage.getFieldColor(ContactFormField.MESSAGE), Matchers.is(ErrorMessageColors.RED));
    }
}
