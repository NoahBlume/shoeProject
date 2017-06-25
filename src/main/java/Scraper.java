

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.*;

import java.util.concurrent.TimeUnit;

public class Scraper {


    // Class Variables

    // Default to a Firefox web browser.
    public WebDriver m_driver;
    // Default to Footlocker.
    public String m_site = "http://www.footlocker.com";

    // Will want the webscraper to go somewhere else first to get a reference header and it needs to be randomized.
    // Possibly taking a file of websites in as the references.

    public Scraper() {
        //this("http://www.footlocker.com");
        m_site = "http://www.footlocker.com";
        System.setProperty("webdriver.gecko.driver","drivers/geckodriver/geckodriver.exe");
        m_driver = new FirefoxDriver();
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

        try {
            m_driver.findElement(By.id("current_size_display")).click();
            WebElement productInfo = m_driver.findElement(By.id("product_information"));
            WebElement productForm = productInfo.findElement(By.id("product_form"));
            WebElement sizeSelectionContainer = productForm.findElement(By.id("size_selection_container"));
            WebElement sizeSelectionList = sizeSelectionContainer.findElement(By.id("size_selection_list"));
            WebElement yourSize = sizeSelectionList.findElement(By.xpath("//a[@value='07.5']"));
            By locator = By.cssSelector("a[value='07.5']");
            WebElement theButton = m_driver.findElement(locator);
            theButton.sendKeys(Keys.ENTER);
            //theButton.click();
            m_driver.findElement(By.id("pdp_addtocart_button")).click();
            m_driver.findElement(By.id("header_cart_button")).click();
            m_driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            m_driver.findElement(By.id("cart_checkout_button")).click();

            m_driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);

            m_driver.findElement(By.id("billFirstName")).click();
            m_driver.findElement(By.id("billFirstName")).sendKeys("Caleb");
            //m_driver.findElement(By.id("billlName")).sendKeys(u.getLastName());

        } catch(Exception e) {
            System.out.println("shit fucked up.");
            System.out.println(e.getMessage());
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