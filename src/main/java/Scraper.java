

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


class Scraper {


    // Class Variables

    // Default to a Firefox web browser.
    private WebDriver m_driver;
    //private Keyboard keyboard;
    // Default to Footlocker.
    //private String m_site;

    // Will want the webscraper to go somewhere else first to get a reference header and it needs to be randomized.
    // Possibly taking a file of websites in as the references.

    Scraper() {
        //this("http://www.footlocker.com");
        //m_site = "http://www.footlocker.com";
        System.setProperty("webdriver.gecko.driver","drivers/geckodriver/geckodriver.exe");
        m_driver = new FirefoxDriver();
//        try {
//            keyboard = new Keyboard();
//        } catch (Exception e) {
//            System.out.println("failed to instantiate keyboard");
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
    }

//    public Scraper(String site, WebDriver driver) {
//        m_site = site;
//        System.setProperty("webdriver.gecko.driver","drivers/geckodriver/geckodriver.exe");
//        m_driver = new FirefoxDriver();
//    }

    void checkStock(User u, Site s) {
        String url = s.getUrl();
        boolean inStock = false;
        while(!inStock) {
            try {
                m_driver.get(url);
                WebElement selectedSize = m_driver.findElement(By.xpath("//a[contains(text(), '" + s.getShoeSize() + "')]"));
                String sizeClass = selectedSize.getAttribute("class");
                if (sizeClass.contains("in-stock")) {
                    inStock = true;
                    System.out.println("in stock");
                } else {
                    System.out.println("out of stock");
                }

            } catch(Exception e) {
                System.out.println("failed to check stock.");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        scrape(u, s);
    }

    private void scrape(User u, Site s) {
        try {
            addToCart(u, s);
        } catch(Exception e) {
            System.out.println("shit fucked up.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        m_driver.quit();
    }

    private void addToCart(User u, Site s) throws Exception {
        boolean addedToCart = false;
        boolean clickedCart = false;
        while (!addedToCart) {
            try {
                if (!m_driver.findElement(By.id("header_cart_count")).getText().equals("1")) {
                    m_driver.findElement(By.id("current_size_display")).click();
                    By locator = By.cssSelector("a[value='08.5']");
                    WebElement theButton = m_driver.findElement(locator);
                    theButton.sendKeys(Keys.ENTER);
                    m_driver.findElement(By.id("pdp_addtocart_button")).click();
                    addedToCart = true;
                }
                if (!clickedCart) {
                    System.out.println("true");
                    String startingUrl = m_driver.getCurrentUrl();
                    String endingUrl = m_driver.getCurrentUrl();
                    while (startingUrl.equals(endingUrl)) {
                        System.out.println("one");
                        if(m_driver.findElements(By.id("header_cart_button")).size() > 0) {
                            m_driver.findElement(By.id("header_cart_button")).click();
                        }
                        System.out.println("uno");
                        endingUrl = m_driver.getCurrentUrl();
                    }
                    clickedCart = true;
                }
            } catch (NoSuchElementException | TimeoutException | ElementNotInteractableException e){
                Thread.sleep(250);
                System.out.println("0 - trying again...");
            }
            checkout(u, s);
        }
    }

    private void checkout(User u, Site s) throws Exception {
        System.out.println("Made it to checkout");
        Thread.sleep(250);
        boolean quantityCheck = false;
        while(!quantityCheck) {
            try {
                //m_driver.findElement(By.xpath("//id[contains(text(), 'quantity_901136208')]")).click();
                //m_driver.findElement(By.xpath("//id[contains(text(), 'quantity_901136208')]")).sendKeys("1");
                if (!m_driver.findElement(By.className("quantity")).getAttribute("value").equals(toString().valueOf(s.getQuantity()))) {
                    m_driver.findElement(By.name("quantity")).clear();
                    m_driver.findElement(By.name("quantity")).sendKeys(toString().valueOf(s.getQuantity()));
                    m_driver.findElement(By.xpath("//name[contains(text(), 'quantity')]")).click();
                    m_driver.findElement(By.xpath("//a[contains(text(), 'Update')]")).click();
                }

                WebElement checkoutButton = (new WebDriverWait(m_driver, 1))
                        .until(ExpectedConditions.presenceOfElementLocated(By.id("cart_checkout_button")));
                checkoutButton.click();
                quantityCheck = true;
            } catch (NoSuchElementException | TimeoutException | ElementNotInteractableException e) {
                Thread.sleep(250);
                System.out.println("0.5 - trying again...");
            }
        }
        enterAddress(u, s);
    }

    private void enterAddress(User u, Site s) throws Exception {
        boolean statePicked = false;
        boolean infoEntered = false;
        while(!infoEntered) {
            try {
                m_driver.findElement(By.xpath("//label[contains(text(), 'First Name')]")).click();
                if (!m_driver.findElement(By.id("billFirstName")).getAttribute("value")
                        .toLowerCase().contains(u.getFirstName().toLowerCase())) {
                    m_driver.findElement(By.id("billFirstName")).sendKeys(u.getFirstName());
                }

                if (!m_driver.findElement(By.id("billEmailAddress")).getAttribute("value")
                        .toLowerCase().contains(u.getEmail().toLowerCase())) {
                    m_driver.findElement(By.id("billEmailAddress")).sendKeys(u.getEmail());
                }

                if (!m_driver.findElement(By.id("billLastName")).getAttribute("value")
                        .toLowerCase().contains(u.getLastName().toLowerCase())) {
                    m_driver.findElement(By.id("billLastName")).sendKeys(u.getLastName());
                }

                if (!m_driver.findElement(By.id("billAddress1")).getAttribute("value")
                        .toLowerCase().contains(u.getStreetAddress().toLowerCase())) {
                    m_driver.findElement(By.id("billAddress1")).sendKeys(u.getStreetAddress());
                }

                if (!m_driver.findElement(By.id("billPostalCode")).getAttribute("value")
                        .toLowerCase().contains(u.getZipCode().toLowerCase())) {
                    m_driver.findElement(By.id("billPostalCode")).sendKeys(u.getZipCode());
                }

                if (!m_driver.findElement(By.id("billCity")).getAttribute("value")
                        .toLowerCase().contains(u.getCity().toLowerCase())) {
                    m_driver.findElement(By.id("billCity")).sendKeys(u.getCity());
                }

                try {
                    if (!statePicked) {
                        m_driver.findElement(By.xpath("//select[@id='billState']/option[contains(text(), '" + u.getState() + "')]")).click();
                        statePicked = true;
                    }
                } catch (Exception e) {
                    System.out.println("User's state is spelled incorrectly");
                    return;
                }

                if (!m_driver.findElement(By.id("billHomePhone")).getAttribute("value")
                        .toLowerCase().contains(u.getPhone().toLowerCase())) {
                    m_driver.findElement(By.id("billHomePhone")).sendKeys(u.getPhone());
                }

                if (m_driver.findElement(By.id("billFirstName")).getAttribute("value")
                        .toLowerCase().contains(u.getFirstName().toLowerCase())) {
                    infoEntered = true;
                }
            } catch (ElementNotInteractableException | NoSuchElementException e) {
                infoEntered = false;
                Thread.sleep(250);
                System.out.println("1 - trying again...");
            }
        }
        submitAddressInfo(u, s);
    }

    private void submitAddressInfo(User u, Site s) throws Exception {
        boolean billPaneContinueClicked = false;
        boolean processed = false;
        while(!billPaneContinueClicked || !processed) {
            try {
                if (m_driver.findElement(By.id("billPaneContinue")).isDisplayed()) {
                    m_driver.findElement(By.id("billPaneContinue")).click();
                }

                if (m_driver.findElement(By.id("shipMethodPaneContinue")).isDisplayed()) {
                    processed = true;
                } else if (m_driver.findElements(By.id("address_verification_edit_address_button")).size() > 0
                        && m_driver.findElement(By.id("address_verification_edit_address_button")).isDisplayed()) {
                    m_driver.findElement(By.id("address_verification_edit_address_button")).click();
                    enterAddress(u, s);
                    return;
                } else if (m_driver.findElement(By.id("billEmailAddress")).isDisplayed()) {
//                    m_driver.findElement(By.xpath("//label[contains(text(), 'Email')]")).click();
                    for (int i = 0; i < 30; i++) {
                        m_driver.findElement(By.id("billEmailAddress")).sendKeys(Keys.DELETE);
                    }
                    m_driver.findElement(By.id("billEmailAddress")).sendKeys(u.getEmail());
//                    m_driver.findElement(By.id("billEmailAddress")).click();
//                    m_driver.findElement(By.id("billEmailAddress")).sendKeys(" ");
//                    m_driver.findElement(By.id("billPaneContinue")).click();
                }

                billPaneContinueClicked = true;
            } catch (ElementNotInteractableException | NoSuchElementException e) {
                billPaneContinueClicked = false;
                Thread.sleep(250);
                System.out.println("2 - trying again...");
            }
        }
        selectShipping(u, s);
    }

    private void selectShipping(User u, Site s) throws Exception {
        boolean shipMethodContinueClicked = false;
        while(!shipMethodContinueClicked) {
            try {
                if (m_driver.findElement(By.id("billEmailAddress")).isDisplayed()) {
                    m_driver.findElement(By.id("billEmailAddress")).click();
                    m_driver.findElement(By.id("billEmailAddress")).sendKeys(" ");
                    m_driver.findElement(By.id("billPaneContinue")).click();
                }

                if (m_driver.findElement(By.id("shipMethodPaneContinue")).isDisplayed()) {
                    m_driver.findElement(By.id("shipMethodPaneContinue")).click();
                    shipMethodContinueClicked = true;
                }
            } catch (ElementNotInteractableException | NoSuchElementException e) {
                shipMethodContinueClicked = false;
                Thread.sleep(250);
                System.out.println("3 - trying again...");
                e.printStackTrace();
            }
        }
        enterCreditCard(u, s);
    }

    private void enterCreditCard(User u, Site s) throws Exception {
        boolean ccInfoEntered = false;
        while(!ccInfoEntered) {
            try {
                //m_driver.findElement(By.xpath("//span[contains(text(), '3. Promo Code (optional)')]")).click();
                if (!m_driver.findElement(By.id("CardNumber")).getAttribute("value").contains(u.getCcNumber())) {
                    m_driver.findElement(By.id("CardNumber")).sendKeys(u.getCcNumber());
                }

                String[] ccDates = u.getCcExpirationDate().split("/");

                if (!m_driver.findElement(By.id("CardExpireDateMM")).getAttribute("value").contains(ccDates[0])) {
                    m_driver.findElement(By.id("CardExpireDateMM")).sendKeys(ccDates[0]);
                }

                if (!m_driver.findElement(By.id("CardExpireDateYY")).getAttribute("value").contains(ccDates[1])) {
                    m_driver.findElement(By.id("CardExpireDateYY")).sendKeys(ccDates[1]);
                }

                if (!m_driver.findElement(By.id("CardCCV")).getAttribute("value").contains(u.getCvc())) {
                    m_driver.findElement(By.id("CardCCV")).sendKeys(u.getCvc());
                }
                WebElement ccStatus = m_driver.findElement(By.id("CC_statusCheck"));
                if (ccStatus.findElement(By.xpath("//span[@role='alert']")).getText().contains("successfully")) {
                    ccInfoEntered = true;
                }
            } catch (ElementNotInteractableException | NoSuchElementException e) {
                ccInfoEntered = false;
                Thread.sleep(250);
                System.out.println("4 - trying again...");
                e.printStackTrace();
            }
        }
        paymentSubmit(u, s);
    }

    private void paymentSubmit(User u, Site s) throws Exception {
        m_driver.findElement(By.id("payMethodPaneContinue")).click();
        unsubscribe(u, s);
    }

    private void unsubscribe(User u, Site s) throws Exception {
        boolean unsubClicked = false;
        while(!unsubClicked) {
            try {
                m_driver.findElement(By.xpath("//label[@for='orderReviewPaneBillSubscribeEmail']")).click();
                unsubClicked = true;
            } catch (ElementNotInteractableException | NoSuchElementException e) {
                unsubClicked = false;
                Thread.sleep(250);
                System.out.println("5 - trying again...");
            }
        }
        purchase(u, s);
    }

    private void purchase(User u, Site s) {
        if(u != null & s!= null) {
            System.out.println("delete me when you actually put something here");
        }
    }


    // Refresh Example
       // m_driver.navigate().refresh();

        // Thread safe exit page not windows
        // End scrape
        //m_driver.close();

        // None thread safe
        // m_driver.quit();


        //how to wait for element to be on page before doing something
//        WebDriver driver = new FirefoxDriver();
//        driver.get("http://somedomain/url_that_delays_loading");
//        WebElement myDynamicElement = (new WebDriverWait(driver, 10))
//                .until(ExpectedConditions.presenceOfElementLocated(By.id("myDynamicElement")));

}