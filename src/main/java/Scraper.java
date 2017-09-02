

import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.TransferQueue;

import static org.openqa.selenium.remote.CapabilityType.BROWSER_NAME;
import static org.openqa.selenium.remote.CapabilityType.PROXY;


class Scraper implements Runnable {
    //footsites: footlocker, eastbay, champs, footaction
    //test links:
        //champs - http://www.champssports.com/product/model:173251/sku:08497006/jordan-retro-4-mens/black/blue/
        //eastbay - http://www.eastbay.com/product/model:98963/sku:24300657/nike-air-force-1-low-mens/all-white/white/
        //footaction - http://www.footaction.com/product/model:274977/sku:78627100/nike-pg-1-mens/paul-george/
        //footlocker - https://www.footlocker.com/product/model:175563/sku:11881026/nike-roshe-one-mens/all-black/black/

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

        FirefoxProfile firefoxProfile = new FirefoxProfile();
        ProfilesIni profile = new ProfilesIni();
        FirefoxProfile myProfile = profile.getProfile("list");
        m_driver = new FirefoxDriver(myProfile);


        firefoxProfile.setPreference("browser.privatebrowsing.autostart", true);
 //        try {
//             String[] proxySplit = proxyString.split(":");
//             String host = proxySplit[0];
//             String port = proxySplit[1];

//             JsonObject json = new JsonObject();
//             json.addProperty("proxyType", "MANUAL");
//             json.addProperty("httpProxy", host);
//             json.addProperty("httpProxyPort", port);
//             json.addProperty("sslProxy", host);
//             json.addProperty("sslProxyPort", port);
//             DesiredCapabilities capabilities = new DesiredCapabilities();
//             capabilities.setCapability(CapabilityType.PROXY, json);

//            DesiredCapabilities capabilities = new DesiredCapabilities();
//            Proxy proxy = new Proxy();
//            proxy.setHttpProxy(proxyString);
//            capabilities.setCapability(PROXY, proxy);

//             m_driver = new FirefoxDriver(capabilities);
//         } catch (Exception e) {
   //          e.printStackTrace();
   //          System.out.println("proxy use failed");
             //m_driver = new FirefoxDriver(firefoxProfile);
  //       }

/*
          m_driver = new FirefoxDriver(firefoxProfile);
 */
//        DesiredCapabilities caps = new DesiredCapabilities();
//        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
//                "drivers/phantomjs-2.1.1-windows/bin/phantomjs.exe");
//        m_driver = new  PhantomJSDriver(caps);
    }

    public void run() {
//        m_driver.get(s.getUrl());
        m_driver.get(s.getShoeName());
        login();
    }

    void login() {
        int fails = 0;
        try {
            //String url = s.getUrl();
            boolean loggedIn = false;
            while(!loggedIn) {
                if (fails > FAIL_LIMIT) {
                    return;
                }
                try {
                    m_driver.findElement(By.xpath("//a[contains(text(), 'Join / Log In')]")).click();
                    Thread.sleep(1000);
                    m_driver.findElement(By.xpath("//input[@data-componentname='emailAddress']")).clear();
                    m_driver.findElement(By.xpath("//input[@data-componentname='emailAddress']")).sendKeys(u.getEmail());
                    m_driver.findElement(By.xpath("//input[@data-componentname='password']")).clear();
                    m_driver.findElement(By.xpath("//input[@data-componentname='password']")).sendKeys(u.getPassword());
                    m_driver.findElement(By.xpath("//input[@type='button'][@value='LOG IN']")).click();
                    loggedIn = true;
                    Thread.sleep(5000);
                } catch(Exception e) {
                    fails++;
                    System.out.println("failed to login.");
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            checkStock();
        } finally {
            m_driver.close();
        }
    }

    private void checkStock() {
        int fails = 0;
        try {
            //String url = s.getUrl();
            boolean inStock = false;
            while(!inStock) {
                if (fails > FAIL_LIMIT) {
                    return;
                }
                try {
                    //m_driver.findElement(By.xpath("//span[contains(@class, 'login-text')]")).sendKeys(Keys.CONTROL, Keys.SHIFT, "P" );
                    Thread.sleep(1000);
                    String size = s.getShoeSize();
                    if (size.charAt(0) == '0') {
                        size = size.substring(1);
                    }
                    if (size.charAt(size.length() - 1) == '0') {
                        size = size.substring(0, size.length() - 1);
                    }
                    if (size.charAt(size.length() - 1) == '.') {
                        size = size.substring(0, size.length() - 1);
                    }
                    m_driver.findElement(By.className("js-selectBox-label")).click();
                    WebElement sizeContainer = m_driver.findElement(By.className("exp-pdp-size-container"));
                    List<WebElement> sizes = sizeContainer.findElements(By.xpath("//li[contains(text(), '" + size + "')]"));
                    for (WebElement foundSize : sizes) {
                        String text = foundSize.getText();
                        if (foundSize.isEnabled() && foundSize.getText().trim().equals(size.trim())) {
                            foundSize.click();
                            inStock = true;
                            break;
                        }
                    }
                    if (!inStock) {
                        m_driver.navigate().refresh();
                    }
                } catch(Exception e) {
                    fails++;
                    System.out.println("failed to login.");
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            startPurchase();
        } finally {
            m_driver.close();
        }
    }

    private void startPurchase() {
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
                login();
                return;
            }
            try {
                m_driver.findElement(By.id("buyingtools-add-to-cart-button")).click();
                addedToCart = true;
                if (!clickedCart) {
                    String startingUrl = m_driver.getCurrentUrl();
                    String endingUrl = m_driver.getCurrentUrl();
                    while (startingUrl.equals(endingUrl)) {
                        try {
                            m_driver.findElement(By.className("notification-button")).click();
                        } catch (NoSuchElementException e) {
                            //sometimes the popup wont be there
                        }
                        if(m_driver.findElements(By.xpath("//span[@class='nsg-glyph--cart']")).size() > 0) {
                            m_driver.findElement(By.xpath("//span[@class='nsg-glyph--cart']")).click();
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
            goToCheckout();
        }
    }

    private void goToCheckout() throws Exception {
        int fails = 0;
        boolean checkingOut = false;
        while (!checkingOut) {
            if (fails > FAIL_LIMIT) {
                login();
                return;
            }
            try {
                String startingUrl = m_driver.getCurrentUrl();
                String endingUrl = m_driver.getCurrentUrl();
                while (startingUrl.equals(endingUrl) || endingUrl.contains("checkout/html/cart")) {
                    if(m_driver.findElements(By.id("ch4_cartCheckoutBtn")).size() > 0) {
                        m_driver.findElement(By.id("ch4_cartCheckoutBtn")).click();
                        endingUrl = m_driver.getCurrentUrl();
                    }
                }
                checkingOut = true;
            } catch (NoSuchElementException | TimeoutException | ElementNotInteractableException e){
                fails++;
                Thread.sleep(250);
                e.printStackTrace();
                System.out.println("0.25 - trying again...");
            }
            checkout();
        }
    }

    private void checkout() throws Exception {
        int fails = 0;
        System.out.println("Made it to checkout");
        Thread.sleep(250);
        boolean confirmedAddress = true;
        while(!confirmedAddress) {
            if (fails > FAIL_LIMIT) {
                enterCVC();
                return;
            }
            try {
                m_driver.findElement(By.className("ui-dialog-titlebar-close")).click();
                m_driver.findElement(By.className("ch4_btnUseThisAddress")).click();
                m_driver.findElement(By.id("shippingSubmit")).click();
                confirmedAddress = true;
            } catch (NoSuchElementException | TimeoutException | ElementNotInteractableException e) {
                fails++;
                Thread.sleep(250);
                System.out.println("0.5 - trying again...");
            }
        }
        enterCVC();
    }

    private void enterCVC() throws Exception {
        int fails = 0;
        boolean cvcEntered = false;
        m_driver.switchTo().frame("billingFormFrame");
        while(!cvcEntered) {
            if (fails > FAIL_LIMIT) {
                login();
                return;
            }
            try {
                try {
                    m_driver.findElement(By.className("js-cvv")).clear();
                    m_driver.findElement(By.className("js-cvv")).sendKeys(u.getCvc());
                    m_driver.switchTo().defaultContent();
                    m_driver.findElement(By.id("ch4_editShipping")).sendKeys("USA");
                    WebElement scroll = m_driver.findElement(By.id("ch4_checkoutHeadingOpen"));
                    scroll.sendKeys(Keys.PAGE_DOWN);
                    m_driver.switchTo().frame("billingFormFrame");
                    m_driver.findElement(By.className("js-confirmCVV")).click();
//                    JavascriptExecutor jse = (JavascriptExecutor) m_driver;
//                    jse.executeScript("window.scrollBy(0,250)", "");
//                    if(m_driver.findElements(By.className("js-useCard")).size() > 0
//                            && m_driver.findElement(By.className("js-useCard")).isEnabled()) {
//                        m_driver.findElement(By.className("js-useCard")).click();
//                    }
                } catch (Exception e) {
                    //buttons may not be present, continue to next step
                }
                try {
                    m_driver.findElement(By.id("billingSubmit")).click();
                    if (m_driver.findElements(By.id("billingSubmit")).size() == 0) {
                        cvcEntered = true;
                    }
                } catch (Exception e) {
                    //button may not be present, wait for previous step
                }
            } catch (ElementNotInteractableException | NoSuchElementException e) {
                fails++;
                cvcEntered = false;
                Thread.sleep(250);
                System.out.println("1 - trying again...");
            }
        }
        placeOrder();
    }

    private void placeOrder() throws Exception {
        int fails = 0;
        boolean orderPlaced = false;
        m_driver.switchTo().defaultContent();
        while(!orderPlaced) {
            if (fails > FAIL_LIMIT) {
                login();
                return;
            }
            if (fails > 0) {
                Thread.sleep(250);
            }
            try {
                m_driver.findElement(By.className("ch4_btnPlaceOrder")).click();
                orderPlaced = true;
                Thread.sleep(1000);
            } catch (ElementNotInteractableException | NoSuchElementException e) {
                fails++;
                orderPlaced = false;
                Thread.sleep(250);
                System.out.println("2 - trying again...");
            }

        }
        //sometimes the button isn't pressed the first time
        //this is a quick temp fix that tries to click the button again
        try {
            m_driver.findElement(By.className("ch4_btnPlaceOrder")).click();
            Thread.sleep(1000);
        } catch (ElementNotInteractableException | NoSuchElementException e) {
            //just continue without crashing, the button may have already been pressed
        }
        logPurchase();
    }


    private void logPurchase() throws Exception {
        try {
            Files.write(Paths.get("attemptedBuys.txt"), (u.getEmail() + " " + s.getShoeName() + "\n").getBytes(), StandardOpenOption.APPEND);
            Thread.sleep(10 * 1000);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        //logging
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