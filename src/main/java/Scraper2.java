import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.List;

//the scraper for size.co.uk
//import org.apache.commons.lang3.StringUtils
//LevenshteinDistance - the number of changes needed to change one String into another
//use to find closest match???
class Scraper2 implements Runnable {


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


    Scraper2(User u, Site s, int index, String proxyString) {
        this.u = u;
        this.s = s;
        this.index = index;

        System.setProperty("webdriver.gecko.driver","drivers/geckodriver/geckodriver.exe");
        m_driver = new FirefoxDriver();
    }

    public void run() {
        checkStock();
    }

    void checkStock() {
        int fails = 0;
        try {
            boolean looking = true;
            while(looking) {
                if (fails > FAIL_LIMIT) {
                    return;
                }
                try {
                    m_driver.get("https://www.size.co.uk/mens/footwear/latest/");
                    List<WebElement> newReleases = m_driver.findElements(By.xpath("//a[@data-e2e='product-listing-name']"));
                    for (WebElement release: newReleases) {
                        String releaseName = release.getText();
                        //int dif = StringUtils.getLevenshteinDistance(s.getShoeName().toLowerCase(), releaseName.toLowerCase(), 20);
                        //if (dif <= 5 && dif >= 0) {
                         //   release.click();
                         //   looking = false;
                         //   break;
                        //}
                    }
                } catch(Exception e) {
                    fails++;
                    System.out.println("failed to check availability");
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
        boolean clickedSize = false;
        while (!addedToCart) {
            if (fails > FAIL_LIMIT) {
                checkStock();
                return;
            }
            try {
                List<WebElement> sizes = m_driver.findElements(By.xpath("//button[@data-e2e='product-size']"));
                for (WebElement size: sizes) {
                    String sizeString = s.getShoeSize();
                    //undo size formatting
                    if (sizeString.charAt(0) == '0') {
                        sizeString = sizeString.substring(1);
                    }
                    if (sizeString.charAt(sizeString.length() - 1) == '0') {
                        sizeString = sizeString.substring(0, sizeString.length() - 1);
                    }
                    if (sizeString.charAt(sizeString.length() - 1) == '.') {
                        sizeString = sizeString.substring(0, sizeString.length() - 1);
                    }
                    if (sizeString.equals(size.getText())) {
                        size.click();
                        clickedSize = true;
                        break;
                    }
                }

                if(clickedSize) {
                    m_driver.findElement(By.id("addToBasket")).click();
                    addedToCart = true;
                } else {
                    m_driver.navigate().refresh();
                }
            } catch (NoSuchElementException | TimeoutException | ElementNotInteractableException e) {
                fails++;
                Thread.sleep(250);
                System.out.println("0 - trying again...");
            }
        }
        alert();
    }

    private void alert() {
        SoundUtils su  = new SoundUtils();
        try {
            for (int i = 0; i < 5; i++) {
                su.tone(1000, 100);
                Thread.sleep(1000);
            }
        } catch (LineUnavailableException | InterruptedException e) {
            System.out.println("Line is unavailable/ thread interrupted");
            e.printStackTrace();
        }
        try {
            //waits 30 minutes
            Thread.sleep(Long.MAX_VALUE );
        } catch (InterruptedException e) {
            //do nothing - just closes the window
        }
        return;
    }

    private class SoundUtils {

        public float SAMPLE_RATE = 8000f;

        public void tone(int hz, int msecs)
                throws LineUnavailableException {
            for (int i = 0; i < 10; i++) {
                tone(hz, msecs, 1.0);
            }
        }

        public void tone(int hz, int msecs, double vol)
                throws LineUnavailableException {
            byte[] buf = new byte[1];
            AudioFormat af =
                    new AudioFormat(
                            SAMPLE_RATE, // sampleRate
                            8,           // sampleSizeInBits
                            1,           // channels
                            true,        // signed
                            false);      // bigEndian
            SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
            sdl.open(af);
            sdl.start();
            for (int i = 0; i < msecs * 8; i++) {
                double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
                buf[0] = (byte) (Math.sin(angle) * 127.0 * vol);
                sdl.write(buf, 0, 1);
            }
            sdl.drain();
            sdl.stop();
            sdl.close();
        }
    }

}