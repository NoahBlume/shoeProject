

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

import static org.openqa.selenium.remote.CapabilityType.PROXY;


class Scraper implements Runnable {


    // Class Variables

    // Default to a Firefox web browser.
    private WebDriver m_driver;
    private User u;
    private Site s;
    private int index = 0;
    private final int FAIL_LIMIT = 25;
    //private Keyboard keyboard;
    // Default to Footlocker.
    //private String m_site;

    // Will want the webscraper to go somewhere else first to get a reference header and it needs to be randomized.
    // Possibly taking a file of websites in as the references.


    Scraper(User u, Site s, int index, String proxyString) {
        this.u = u;
        this.s = s;
        this.index = index;

        System.setProperty("webdriver.gecko.driver","drivers/geckodriver/geckodriver.exe");
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(proxyString);
            capabilities.setCapability(PROXY, proxy);

            m_driver = new FirefoxDriver(capabilities);
        } catch (Exception e) {
            m_driver = new FirefoxDriver();
        }

//        m_driver = new FirefoxDriver();
//        DesiredCapabilities caps = new DesiredCapabilities();
//        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
//                "drivers/phantomjs-2.1.1-windows/bin/phantomjs.exe");
//        m_driver = new  PhantomJSDriver(caps);
    }

    public void run() {
        checkStock();
    }

    void checkStock() {
        int fails = 0;
        try {
            String url = s.getUrl();
            boolean inStock = false;
            while(!inStock) {
                if (fails > FAIL_LIMIT) {
                    return;
                }
                try {
                    m_driver.get(url);
                    WebElement selectedSize = m_driver.findElement(By.xpath("//a[contains(text(), '" + s.getShoeSize() + "')]"));
                    String sizeClass = selectedSize.getAttribute("class");
                    if (sizeClass.contains("in-stock")) {
                        inStock = true;
                        System.out.println("in stock");
                    } else {
                        Thread.sleep(10000);
                        System.out.println("out of stock");
                    }

                } catch(Exception e) {
                    fails++;
                    System.out.println("failed to check stock.");
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            scrape();
        } finally {
            m_driver.close();
        }
    }

    private void scrape() {
        try {
            Main.buying.get(index).setPriority(Thread.MAX_PRIORITY);
            addToCart();
        } catch(Exception e) {
            System.out.println("shit fucked up.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        m_driver.quit();
    }

    private void addToCart() throws Exception {
        int fails = 0;
        boolean addedToCart = false;
        boolean clickedCart = false;
        while (!addedToCart) {
            if (fails > FAIL_LIMIT) {
                checkStock();
                return;
            }
            try {
                if (!m_driver.findElement(By.id("header_cart_count")).getText().equals("1")) {
                    m_driver.findElement(By.id("current_size_display")).click();
                    By locator = By.cssSelector("a[value='" + s.getShoeSize() + "']");
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
                        if(m_driver.findElements(By.id("header_cart_button")).size() > 0) {
                            m_driver.findElement(By.id("header_cart_button")).click();
                        }
                        endingUrl = m_driver.getCurrentUrl();
                    }
                    clickedCart = true;
                }
            } catch (NoSuchElementException | TimeoutException | ElementNotInteractableException e){
                fails++;
                Thread.sleep(250);
                System.out.println("0 - trying again...");
            }
            checkout();
        }
    }

    private void checkout() throws Exception {
        int fails = 0;
        System.out.println("Made it to checkout");
        Thread.sleep(250);
        boolean quantityCheck = false;
        while(!quantityCheck) {
            if (fails > FAIL_LIMIT) {
                checkStock();
                return;
            }
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
                fails++;
                Thread.sleep(250);
                System.out.println("0.5 - trying again...");
            }
        }
        enterAddress();
    }

    private void enterAddress() throws Exception {
        int fails = 0;
        boolean statePicked = false;
        boolean infoEntered = false;
        while(!infoEntered) {
            if (fails > FAIL_LIMIT) {
                checkStock();
                return;
            }
            try {
                m_driver.findElement(By.xpath("//label[contains(text(), 'First Name')]")).click();
                if (!m_driver.findElement(By.id("billFirstName")).getAttribute("value")
                        .toLowerCase().contains(u.getFirstName().toLowerCase())) {
                    m_driver.findElement(By.id("billFirstName")).sendKeys(u.getFirstName());
                }

                if (!m_driver.findElement(By.id("billEmailAddress")).getAttribute("value")
                        .toLowerCase().contains(u.getEmail().toLowerCase())) {
                    ((JavascriptExecutor) m_driver).executeScript("window.focus();");
                    m_driver.findElement(By.id("billEmailAddress")).clear();
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
                fails++;
                infoEntered = false;
                Thread.sleep(250);
                System.out.println("1 - trying again...");
            }
        }
        submitAddressInfo();
    }

    private void submitAddressInfo() throws Exception {
        int fails = 0;
        boolean billPaneContinueClicked = false;
        boolean processed = false;
        WebElement billEmailAddress = m_driver.findElement(By.id("billEmailAddress"));
        while(!billPaneContinueClicked || !processed) {
            if (fails > FAIL_LIMIT) {
                checkStock();
                return;
            }
            if (fails > 0) {
                Thread.sleep(250);
            }
            try {
                if (m_driver.findElement(By.id("billPaneContinue")).isDisplayed()) {
                    m_driver.findElement(By.id("billPaneContinue")).click();
                }

                if (m_driver.findElement(By.id("shipMethodPaneContinue")).isDisplayed()) {
                    processed = true;
                } else if (m_driver.findElements(By.id("address_verification_edit_address_button")).size() > 0
                        && m_driver.findElement(By.id("address_verification_edit_address_button")).isDisplayed()) {
                    m_driver.findElement(By.id("address_verification_edit_address_button")).click();
                    enterAddress();
                    return;
                } else if (billEmailAddress.isDisplayed()) {
                    ((JavascriptExecutor) m_driver).executeScript("window.focus();");
                    billEmailAddress.clear();
                    m_driver.findElement(By.id("billEmailAddress")).sendKeys(u.getEmail());
                }

                billPaneContinueClicked = true;
            } catch (ElementNotInteractableException | NoSuchElementException e) {
                fails++;
                billPaneContinueClicked = false;
                Thread.sleep(250);
                System.out.println("2 - trying again...");
            }
        }
        selectShipping();
    }

    private void selectShipping() throws Exception {
        int fails = 0;
        boolean shipMethodContinueClicked = false;
        while(!shipMethodContinueClicked) {
            if (fails > FAIL_LIMIT) {
                checkStock();
                return;
            }
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
                fails++;
                shipMethodContinueClicked = false;
                Thread.sleep(250);
                System.out.println("3 - trying again...");
                e.printStackTrace();
            }
        }
        enterCreditCard();
    }

    private void enterCreditCard() throws Exception {
        int fails = 0;
        boolean ccInfoEntered = false;
        while(!ccInfoEntered) {
            if (fails > FAIL_LIMIT) {
                checkStock();
                return;
            }
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
                fails++;
                ccInfoEntered = false;
                Thread.sleep(250);
                System.out.println("4 - trying again...");
                e.printStackTrace();
            }
        }
        paymentSubmit();
    }

    private void paymentSubmit() throws Exception {
        int fails = 0;
        boolean submitted = false;
        while(!submitted) {
            if (fails > FAIL_LIMIT) {
                checkStock();
                return;
            }
            try {
                m_driver.findElement(By.id("payMethodPaneContinue")).click();
                submitted = true;
            } catch (ElementNotInteractableException | NoSuchElementException e) {
                fails++;
                submitted = false;
                Thread.sleep(250);
                System.out.println("5 - trying again...");
            }
        }
        unsubscribe();
    }

    private void unsubscribe() throws Exception {
        int fails = 0;
        boolean unsubClicked = false;
        while(!unsubClicked) {
            if (fails > FAIL_LIMIT) {
                checkStock();
                return;
            }
            try {
                m_driver.findElement(By.xpath("//label[@for='orderReviewPaneBillSubscribeEmail']")).click();
                unsubClicked = true;
            } catch (ElementNotInteractableException | NoSuchElementException e) {
                fails++;
                unsubClicked = false;
                Thread.sleep(250);
                System.out.println("5 - trying again...");
            }
        }
        purchase(u, s);
    }

    private void purchase(User u, Site s) throws Exception {
        int fails = 0;
        boolean bought = false;
        while(!bought) {
            if (fails > FAIL_LIMIT) {
                checkStock();
                return;
            }
            try {
                m_driver.findElement(By.id("orderSubmit")).click();
                Thread.sleep(5000);
                bought = true;
                Main.buying.get(index).setPriority(Thread.MIN_PRIORITY);
            } catch (ElementNotInteractableException | NoSuchElementException e) {
                fails++;
                bought = false;
                Thread.sleep(250);
                System.out.println("6 - trying again...");
            }
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