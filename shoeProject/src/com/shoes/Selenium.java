package com.shoes;

import org.seleniumhq.selenium.*;


public class Scraper {

    // Default to a Firefox web browser.
    public WebDriver m_driver = new FirefoxDriver();

    // Default to Footlocker.
    public string m_site = "http://www.footlocker.com";

    // Will want the webscraper to go somewhere else first to get a reference header and it needs to be randomized.
    // Possibly taking a file of websites in as the references.

    public Scraper() {

    }

    public Scraper(stringn site) {
        m_site = site;

    }

    public Scraper(stringn site, Webdriver driver) {
        m_site = site;
        m_driver = driver;
    }

    public Scrape() {
        driver.get("http://www.google.com");
    }
}