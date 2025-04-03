package com.ssdts;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AppTest {
    private WebDriver driver;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.get("https://www.saucedemo.com/");
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    //Helper Methods
    private void login() {
        WebElement username = driver.findElement(By.name("user-name")); //find an input element using the name attribute
        username.sendKeys("standard_user"); //type into the input
        WebElement password = driver.findElement(By.name("password")); //find an input element using the name attribute
        password.sendKeys("secret_sauce"); //type into the input
        WebElement loginButton = driver.findElement(By.name("login-button")); //find a button element using this name attribute
        loginButton.click(); //click the button
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500)); //wait for elements to appear before throwing a timeout
    }

    private void addOneToCart() {
        WebElement firstButton = driver.findElement(By.name("add-to-cart-sauce-labs-backpack")); //get the button of the first item in the inventory
        firstButton.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500)); //wait for elements to appear before throwing a timeout
        WebElement cartLink = driver.findElement(By.className("shopping_cart_link")); //find an element using this name attribute
        cartLink.click(); //click the button
    }

    private void checkOutCart() {
        WebElement checkoutButton = driver.findElement(By.name("checkout")); //find an element using this name attribute
        checkoutButton.click(); //click the button
    }

    private void enterInformation (){
        WebElement firstName = driver.findElement(By.name("firstName"));
        firstName.sendKeys("John");
        WebElement lastName = driver.findElement(By.name("lastName"));
        lastName.sendKeys("Alexander");
        WebElement postalCode = driver.findElement(By.name("postalCode"));
        postalCode.sendKeys("K2V2B5");
        WebElement continueButton = driver.findElement(By.name("continue")); //find an element using this name attribute
        continueButton.click(); //click the button
    }

    private void testURL(String URL) {
        driver.get(URL);
    }

    private void waitToLoad(Long time) {
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(time));
    }

    //Functional Tests
    @Test
    public void testURLLoaded() {
        //land on login page and check if login button is displayed
        WebElement loginButton = driver.findElement(By.name("login-button"));
        Assert.assertTrue(loginButton.isDisplayed());
    }

    @Test
    public void testValidLogin() {
        //enter login details
        WebElement username = driver.findElement(By.name("user-name"));
        username.sendKeys("standard_user");
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys("secret_sauce");
        WebElement loginButton = driver.findElement(By.name("login-button"));
        loginButton.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        //assert inventory page is displayed
        Assert.assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl());
    }

    @Test
    public void testInValidLogin() {
        //enter invalid login details
        WebElement username = driver.findElement(By.name("user-name"));
        username.sendKeys("");
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys("");
        WebElement loginButton = driver.findElement(By.name("login-button"));
        loginButton.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        WebElement errorButton = driver.findElement(By.className("error-button"));
        //assert error button page is displayed
        Assert.assertTrue(errorButton.isDisplayed());
    }

    @Test
    public void testAddAllToCart() {
        //log in
        login();
        Assert.assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl());
        //get all the add to cart button for the items, click on them, and count each click
        List<WebElement> allAddButtons = driver.findElements(By.cssSelector(".btn.btn_primary.btn_small.btn_inventory"));
        int counter = 0;
        for (WebElement eachButton : allAddButtons) {
            eachButton.click();
            counter++;
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        //get the shopping cart badge
        WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
        String number = cartBadge.getText();
        //assert the number of items that were added to cart
        Assert.assertEquals(Integer.toString(counter), number);
    }

    @Test
    public void testRemoveOneFromCart() {
        //log in
        login();
        Assert.assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl());
        //get all the add to cart button for the items, click on them, and count each click
        List<WebElement> allAddButtons = driver.findElements(By.cssSelector(".btn.btn_primary.btn_small.btn_inventory"));
        int counter = 0;
        for (WebElement eachButton : allAddButtons) {
            eachButton.click();
            counter++;
        }
        //remove on item from the cart and update the count
        WebElement removeButton = driver.findElement(By.name("remove-sauce-labs-backpack"));
        removeButton.click();
        int updatedCounter = counter - 1;
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
        String number = cartBadge.getText();
        //assert the number of items that are left in the cart
        Assert.assertEquals(Integer.toString(updatedCounter), number);
    }

    @Test
    public void testRemoveAllFromCart() {
        //log in
        login();
        Assert.assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl());
        //get all the add to cart button for the items, click on them, and count each click
        List<WebElement> allAddButtons = driver.findElements(By.cssSelector(".btn.btn_primary.btn_small.btn_inventory"));
        for (WebElement eachButton : allAddButtons) {
            eachButton.click();
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        //remove all the items in the cart
        List<WebElement> allRemoveButtons = driver.findElements(By.cssSelector(".btn.btn_secondary.btn_small.btn_inventory"));
        for (WebElement eachButton : allRemoveButtons) {
            eachButton.click();
        }
        //wait for 10 seconds and assert shopping cart badge is invisible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean invisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("shopping_cart_badge")));
        Assert.assertTrue("Shopping cart counter badge should be invisible", invisible);
    }

    @Test
    public void testSortByAToZ() {
        //log in
        login();
        Assert.assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl());
        //click on the sort button and select A to Z
        WebElement sort = driver.findElement(By.className("product_sort_container"));
        Select dropdown = new Select(sort);
        dropdown.selectByValue("az");
        //get all the items displayed as a list
        List<WebElement> allItemNames = driver.findElements(By.className("inventory_item_name"));
        List<String> itemNames = new ArrayList<>();
        for (WebElement itemName : allItemNames) {
            itemNames.add(itemName.getText());
        }
        System.out.println("Sorted item names via the app's sort function: " + itemNames);
        //make a copy of them and sort them using the collections.sort method
        List<String> sortedItemNames = new ArrayList<>(itemNames);
        Collections.sort(sortedItemNames);
        System.out.println("Sorted item names via Collections method : " + sortedItemNames);
        //assert both lists are same
        Assert.assertEquals("List of items is not sorted from A to Z", sortedItemNames, itemNames);
    }

    @Test
    public void testSortByZToA() {
        //log in
        login();
        Assert.assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl());
        //click on the sort button and select Z to A
        WebElement sort = driver.findElement(By.className("product_sort_container"));
        Select dropdown = new Select(sort);
        dropdown.selectByValue("za");
        //get all the items displayed as a list
        List<WebElement> allItemNames = driver.findElements(By.className("inventory_item_name"));
        List<String> itemNames = new ArrayList<>();
        for (WebElement itemName : allItemNames) {
            itemNames.add(itemName.getText());
        }
        System.out.println("Reversed sorted item names via the app's sort function: " + itemNames);
        //make a copy of them and reverse-sort them using the collections.sort method
        List<String> sortedItemNames = new ArrayList<>(itemNames);
        Collections.sort(sortedItemNames, Collections.reverseOrder());
        System.out.println("Reversed sorted item names via Collections method : " + sortedItemNames);
        //assert both lists are same
        Assert.assertEquals("List of items is not sorted from Z to A", sortedItemNames, itemNames);
    }

    @Test
    public void testSortLowToHigh() {
        //log in
        login();
        Assert.assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl());
        //click on the sort button and select low to high
        WebElement sort = driver.findElement(By.className("product_sort_container"));
        Select dropdown = new Select(sort);
        dropdown.selectByValue("lohi");
        //get all the items by price displayed as a list
        List<WebElement> allItemNames = driver.findElements(By.className("inventory_item_price"));
        List<Double> itemNames = new ArrayList<>();
        for (WebElement itemName : allItemNames) {
            String numberAsText = itemName.getText().replace("$", "");
            itemNames.add(Double.parseDouble(numberAsText));
        }
        System.out.println("Sorted item names via the app's sort function: " + itemNames);
        //make a copy of them and sort them using the collections.sort method
        List<Double> sortedItemNames = new ArrayList<>(itemNames);
        Collections.sort(sortedItemNames);
        System.out.println("Sorted item names via Collections method : " + sortedItemNames);
        //assert both lists are same
        Assert.assertEquals("List of items is not sorted from low to high", sortedItemNames, itemNames);
    }

    @Test
    public void testSortHighToLow() {
        //log in
        login();
        Assert.assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl());
        //click on the sort button and select high to low
        WebElement sort = driver.findElement(By.className("product_sort_container"));
        Select dropdown = new Select(sort);
        dropdown.selectByValue("hilo");
        //get all the items by price displayed as a list
        List<WebElement> allItemNames = driver.findElements(By.className("inventory_item_price"));
        List<Double> itemNames = new ArrayList<>();
        for (WebElement itemName : allItemNames) {
            String numberAsText = itemName.getText().replace("$", "");
            itemNames.add(Double.parseDouble(numberAsText));
        }
        System.out.println("Sorted item names via the app's sort function: " + itemNames);
        //make a copy of them and reverse-sort them using the collections.sort method
        List<Double> sortedItemNames = new ArrayList<>(itemNames);
        Collections.sort(sortedItemNames, Collections.reverseOrder());
        System.out.println("Sorted item names via Collections method : " + sortedItemNames);
        //assert both lists are same
        Assert.assertEquals("List of items is not sorted from high to low", sortedItemNames, itemNames);
    }

    //End-to-End Tests
    @Test
    public void testMakePurchase() {
        //log in
        login();
        Assert.assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl());
        //add to cart
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        addOneToCart();
        Assert.assertEquals("https://www.saucedemo.com/cart.html", driver.getCurrentUrl());
        waitToLoad(1000L);
        //checkout
        checkOutCart();
        waitToLoad(1000L);

        enterInformation();
        waitToLoad(1000L);
        //assert the URL that is loaded is the URL expected to be loaded when checkout-step-two page loads
        Assert.assertEquals("https://www.saucedemo.com/checkout-step-two.html", driver.getCurrentUrl());

        //place order and finish
        waitToLoad(1000L);

        WebElement finishButton = driver.findElement(By.name("finish"));
        finishButton.click();
        //assert the URL that is loaded is the URL expected to be loaded when checkout-complete page loads
        Assert.assertEquals("https://www.saucedemo.com/checkout-complete.html", driver.getCurrentUrl());
    }

    //UI/UX Tests
    @Test
    public void testButtonDisplayed() {
        //test button on login page, if displays login
        WebElement loginButton = driver.findElement(By.name("login-button"));
        if (loginButton.isDisplayed()) {
            System.out.println("Login button is displayed");
            Assert.assertTrue(loginButton.isDisplayed());
            login();
        } else {
            System.out.println("Login button is not displayed");
        }

        waitToLoad(1000L);

        //test buttons on inventory page, if they display, add to cart
        List<WebElement> inventoryButtons = driver.findElements(By.tagName("button"));
        for (WebElement eachItemButton : inventoryButtons) {
            if (eachItemButton.isDisplayed()) {
                System.out.println("Button on the inventory page is displayed");
                Assert.assertTrue(eachItemButton.isDisplayed());
            }
        }
        addOneToCart();

        waitToLoad(1000L);

        //test buttons on cart page, if they display, check out
        List<WebElement> cartButtons = driver.findElements(By.tagName("button"));
        for (WebElement eachItemButton : cartButtons) {
            if (eachItemButton.isDisplayed()) {
                System.out.println("Button on the cart page is displayed");
                Assert.assertTrue(eachItemButton.isDisplayed());
            }
        }
        checkOutCart();

        waitToLoad(1000L);

        //test button on checkout step one page, if they display, enter information
        WebElement continueButton = driver.findElement(By.name("continue"));
        WebElement cancelButton = driver.findElement(By.name("cancel"));

        if (continueButton.isDisplayed() && cancelButton.isDisplayed()) {
            System.out.println("Continue, cancel, and menu buttons are displayed");
            Assert.assertTrue(continueButton.isDisplayed());
            Assert.assertTrue(cancelButton.isDisplayed());
            enterInformation();
        }

        waitToLoad(1000L);

        //test buttons on checkout step two page, if they display, click finish
        List<WebElement> checkoutTwoButtons = driver.findElements(By.tagName("button"));
        for (WebElement eachCheckoutTwoButton : checkoutTwoButtons) {
            if (eachCheckoutTwoButton.isDisplayed()) {
                System.out.println("Finish, cancel, and menu buttons are displayed");
                Assert.assertTrue(eachCheckoutTwoButton.isDisplayed());
            }
        }
        WebElement finishButton = driver.findElement(By.name("finish"));
        finishButton.click();

        waitToLoad(1000L);

        //test buttons on checkout complete page, if they display, click back to home
        List<WebElement> checkoutCompleteButtons = driver.findElements(By.tagName("button"));
        for (WebElement eachCheckoutCompleteButton : checkoutCompleteButtons) {
            if (eachCheckoutCompleteButton.isDisplayed()) {
                System.out.println("Back to home and menu buttons are displayed");
                Assert.assertTrue(eachCheckoutCompleteButton.isDisplayed());
            }
        }
        tearDown();
    }

    @Test
    public void testDirectURLAccess() {
        testURL("https://www.saucedemo.com/inventory.html");
        WebElement errorButtonOne = driver.findElement(By.className("error-button"));
        Assert.assertTrue(errorButtonOne.isDisplayed());

        waitToLoad(1000L);

        testURL("https://www.saucedemo.com/cart.html");
        WebElement errorButtonTwo = driver.findElement(By.className("error-button"));
        Assert.assertTrue(errorButtonTwo.isDisplayed());

        waitToLoad(1000L);

        testURL("https://www.saucedemo.com/checkout-step-one.html");
        WebElement errorButtonThree = driver.findElement(By.className("error-button"));
        Assert.assertTrue(errorButtonThree.isDisplayed());

        waitToLoad(1000L);

        testURL("https://www.saucedemo.com/checkout-step-two.html");
        WebElement errorButtonFour = driver.findElement(By.className("error-button"));
        Assert.assertTrue(errorButtonFour.isDisplayed());

        waitToLoad(1000L);

        testURL("https://www.saucedemo.com/checkout-complete.html");
        WebElement errorButtonFive = driver.findElement(By.className("error-button"));
        Assert.assertTrue(errorButtonFive.isDisplayed());

    }

    @Test
    public void testSecureConnection(){
        String URL = driver.getCurrentUrl();
        if (URL.startsWith("https://")) {
            System.out.println("The connection is secure");
            Assert.assertTrue(URL.startsWith("https://"));
        }
    }
}