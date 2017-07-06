

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;
import java.util.List;

public class Scraper {


    // Class Variables

    // Default to a Firefox web browser.
    public WebDriver m_driver;
    public Keyboard keyboard;
    // Default to Footlocker.
    public String m_site = "http://www.footlocker.com";

    // Will want the webscraper to go somewhere else first to get a reference header and it needs to be randomized.
    // Possibly taking a file of websites in as the references.

    public Scraper() {
        //this("http://www.footlocker.com");
        m_site = "http://www.footlocker.com";
        System.setProperty("webdriver.gecko.driver","drivers/geckodriver/geckodriver.exe");
        m_driver = new FirefoxDriver();
        try {
            keyboard = new Keyboard();
        } catch (Exception e) {
            System.out.println("failed to instantiate keyboard");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public Scraper(String site) {
       // this(site, new FirefoxDriver());

    }

    public Scraper(String site, WebDriver driver) {
        m_site = site;
        System.setProperty("webdriver.gecko.driver","drivers/geckodriver/geckodriver.exe");
        m_driver = new FirefoxDriver();
    }

    public void scrape(User u, Site s) {
        // Begin scrape
        String url = s.getUrl();
        m_driver.get(url);
        m_driver.manage().window().maximize();

        try {
            m_driver.findElement(By.id("current_size_display")).click();
            WebElement productInfo = m_driver.findElement(By.id("product_information"));
            WebElement productForm = productInfo.findElement(By.id("product_form"));
            WebElement sizeSelectionContainer = productForm.findElement(By.id("size_selection_container"));
            WebElement sizeSelectionList = sizeSelectionContainer.findElement(By.id("size_selection_list"));
            WebElement yourSize = sizeSelectionList.findElement(By.xpath("//a[@value='07.5']"));
            By locator = By.cssSelector("a[value='08.5']");
            WebElement theButton = m_driver.findElement(locator);
            theButton.sendKeys(Keys.ENTER);
            Thread.sleep(1000);
            m_driver.findElement(By.id("pdp_addtocart_button")).click();
            Thread.sleep(3000);
            m_driver.findElement(By.id("header_cart_button")).click();
            Thread.sleep(1000);
            m_driver.findElement(By.id("cart_checkout_button")).click();
            Thread.sleep(8000);

            m_driver.findElement(By.xpath("//label[contains(text(), 'First Name')]")).click();
            //keyboard.type("Caleb");
            m_driver.findElement(By.id("billFirstName")).sendKeys(u.getFirstName());
            //keyboard.type('\t');

           // m_driver.findElement(By.xpath("//label[contains(text(), 'Last Name')]")).click();
            m_driver.findElement(By.id("billLastName")).sendKeys(u.getLastName());
            //keyboard.type('\t');

            //.findElement(By.xpath("//label[contains(text(), 'Street')]")).click();
            m_driver.findElement(By.id("billAddress1")).sendKeys(u.getStreetAddress());
            //keyboard.type('\t');

            //m_driver.findElement(By.xpath("//label[contains(text(), 'Zip Code')]")).click();
            m_driver.findElement(By.id("billPostalCode")).sendKeys(u.getZipCode());
           // keyboard.type('\t');

            //m_driver.findElement(By.xpath("//label[contains(text(), 'City')]")).click();
            m_driver.findElement(By.id("billCity")).sendKeys(u.getCity());
           // keyboard.type('\t');

            //m_driver.findElement(By.xpath("//label[contains(text(), 'State')]")).click();
            m_driver.findElement(By.id("billState")).sendKeys(u.getState());
            //keyboard.type('\t');

            //m_driver.findElement(By.xpath("//label[contains(text(), 'Phone')]")).click();
            m_driver.findElement(By.id("billHomePhone")).sendKeys(u.getPhone());
            //keyboard.type('\t');

            //m_driver.findElement(By.xpath("//label[contains(text(), 'Email')]")).click();
            m_driver.findElement(By.id("billEmailAddress")).sendKeys(u.getEmail());

            Thread.sleep(2000);
            m_driver.findElement(By.id("billPaneContinue")).click();
            //inventoryCheck_loading
            List<WebElement> loadingScreen = m_driver.findElements(By.id("inventoryCheck_loading"));
            while(loadingScreen.size() > 0) {
                System.out.println("processing...");
                Thread.sleep(1000);
                loadingScreen = m_driver.findElements(By.id("inventoryCheck_loading"));
            }

            Thread.sleep(1000);
            System.out.println("probably about to fuck up");
            m_driver.findElement(By.id("shipMethodPaneContinue")).click();
            System.out.println("got here");
            //m_driver.findElement(By.xpath("//span[contains(@class, 'newCC_currCC')]")).click();
            m_driver.findElement(By.xpath("//span[contains(text(), '3. Promo Code (optional)')]")).click();
            for (int i = 0; i < 20; i++) {
                keyboard.type('\t');
            }
            System.out.println("clicked it?");
            m_driver.findElement(By.id("CardNumber")).sendKeys(u.getCcNumber());
            String[] ccDates = u.getCcExpirationDate().split("/");
            m_driver.findElement(By.id("CardExpireDateMM")).sendKeys(ccDates[0]);
            m_driver.findElement(By.id("CardExpireDateYY")).sendKeys(ccDates[1]);

            m_driver.findElement(By.id("CardCCV")).sendKeys(u.getCvc());

            Thread.sleep(1000);
            m_driver.findElement(By.id("payMethodPaneContinue")).click();

            Thread.sleep(3000);
            m_driver.findElement(By.xpath("//label[@for='orderReviewPaneBillSubscribeEmail']")).click();

            //m_driver.findElement(By.id("orderReviewPaneBillSubscribeEmail")).sendKeys(" ");
        } catch(Exception e) {
            System.out.println("shit fucked up.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        // Refresh Example
       // m_driver.navigate().refresh();

        // Thread safe exit page not windows
        // End scrape
        //m_driver.close();

        // None thread safe
        // m_driver.quit();

    }
}