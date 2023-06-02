package steps;

import com.github.javafaker.Faker;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import pages.SmartBearOrdersPage;
import utils.Driver;
import utils.DropdownHandler;
import utils.Waiter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmartBearSteps {

    WebDriver driver;
    SmartBearOrdersPage smartBearOrdersPage;
    Faker faker;
    String customerName;
    String street;
    String city;
    String state;
    String zipCode;
    String paymentType;
    String cardNo;
    String expDate;
    int numberOfOrders = 8;

    @Before
    public void setup(){
        driver = Driver.getDriver();
        faker = new Faker();
        customerName = faker.name().fullName();
        street = faker.address().streetAddress();
        city = faker.address().city();
        state = faker.address().state();
        zipCode = faker.address().zipCode().substring(0, 5);
        paymentType = "Visa";
        cardNo = faker.business().creditCardNumber().replace("-", "");
        expDate = "12/27";
        smartBearOrdersPage = new SmartBearOrdersPage();
    }

    @When("user clicks on {string} link")
    public void userClicksOnButton(String linkText) {
        WebElement linkToBeClicked = null;
        switch (linkText){
            case "Check All":
                linkToBeClicked = smartBearOrdersPage.checkAllLink;
                break;
            case "Uncheck All":
                linkToBeClicked = smartBearOrdersPage.uncheckAllLink;
                break;
            case "Process":
                linkToBeClicked = smartBearOrdersPage.processButton;
                break;
            default:
                throw new NotFoundException("Link text is not properly defined in the feature file!!!");
        }
        linkToBeClicked.click();
    }

    @Then("all rows should be checked")
    public void allRowsShouldBeChecked() {
        for (WebElement element : smartBearOrdersPage.checkboxes) {
            Assert.assertTrue(element.isSelected());
        }
    }

    @Then("all rows should be unchecked")
    public void allRowsShouldBeUnchecked() {
        for (WebElement element : smartBearOrdersPage.checkboxes){
            Assert.assertFalse(element.isSelected());
        }
    }

    @When("user clicks on {string} menu item")
    public void userClicksMenuItem(String menuItemText) {
        smartBearOrdersPage.clickOnMenuItem(smartBearOrdersPage.webOrdersMenuItems, menuItemText);
    }

    @And("user selects {string} as product")
    public void userSelectsAsProduct(String productName) {
        DropdownHandler.selectOptionByVisibleText(smartBearOrdersPage.productDropdown, productName);
    }

    @And("user enters {int} as quantity")
    public void userEntersAsQuantity(int quantity) {
        smartBearOrdersPage.quantityInputBox.sendKeys("" + quantity);
    }

    @And("user enters all address information")
    public void userEntersAllAddressInformation() {
        smartBearOrdersPage.customerNameInputBox.sendKeys(customerName);
        smartBearOrdersPage.streetInputBox.sendKeys(street);
        smartBearOrdersPage.cityInputBox.sendKeys(city);
        smartBearOrdersPage.stateInputBox.sendKeys(state);
        smartBearOrdersPage.zipInputBox.sendKeys(zipCode);

    }

    @And("user enters all payment information")
    public void userEntersAllPaymentInformation() {
        smartBearOrdersPage.selectPaymentType(paymentType);
        smartBearOrdersPage.cardNumberInputBox.sendKeys(cardNo);
        smartBearOrdersPage.cardExpirationDateInputBox.sendKeys(expDate);
    }

    @Then("user should see their order displayed in the List of All Orders table")
    public void userShouldSeeTheirOrderDisplayedInTheTable() {
        int newTotalOfOrders = smartBearOrdersPage.checkboxes.size();
        Assert.assertTrue((numberOfOrders+1) == newTotalOfOrders);
        System.out.println(numberOfOrders + "================");
        System.out.println(newTotalOfOrders + "================");
    }

    @And("validate all information entered displayed correct with the order")
    public void validateAllInformationEnteredDisplayedCorrectWithTheOrder() {
        Assert.assertEquals(customerName, smartBearOrdersPage.orderInfo.get(1).getText());
        Assert.assertEquals(street, smartBearOrdersPage.orderInfo.get(5).getText());
        Assert.assertEquals(city, smartBearOrdersPage.orderInfo.get(6).getText());
        Assert.assertEquals(state, smartBearOrdersPage.orderInfo.get(7).getText());
        Assert.assertEquals(zipCode, smartBearOrdersPage.orderInfo.get(8).getText());
        Assert.assertEquals(paymentType, smartBearOrdersPage.orderInfo.get(9).getText());
        Assert.assertEquals(cardNo, smartBearOrdersPage.orderInfo.get(10).getText());
        Assert.assertEquals(expDate, smartBearOrdersPage.orderInfo.get(11).getText());
    }
}
