package com.shoes;

// First option
// import org.openqa.selenium.*;
// Second option
// import org.seleniumhq.selenium.*;

public class Scraper {


    // Class Variables

    // Default to a Firefox web browser.
    public WebDriver m_driver = new FirefoxDriver();
    // Default to Footlocker.
    public string m_site = "http://www.footlocker.com";

    // Will want the webscraper to go somewhere else first to get a reference header and it needs to be randomized.
    // Possibly taking a file of websites in as the references.

    public Scraper() {

    }

    public Scraper(string site) {
        m_site = site;

    }

    public Scraper(string site, Webdriver driver) {
        m_site = site;
        m_driver = driver;
    }

    public Scrape() {
        // Begin scrape
        m_driver.get(m_site);

        // Navigation Example
        //m_driver.findElement(By.name("login")).click();

        // Refresh Example
        m_driver.navigate().refresh();

        // Thread safe exit page not windows
        // End scrape
        m_driver.close();

        // None thread safe
        // m_driver.quit();

    }
}