import java.io.Serializable;

/**
 * Created by Mbrune on 6/10/2017.
 */
public class Site implements Serializable{
    private SiteEnum siteEnum;
    private String url;
    private String shoeSize;
    private int quantity = 1;
    private int scanFrequency = 6; //stock checks per minute - default 6



    public Site(String url) {
        this.url = url;
    }

    public SiteEnum getSiteEnum() {
        return siteEnum;
    }

    public String getUrl() {
        return url;
    }

    public int getScanFrequency() {
        return scanFrequency;
    }

    public String getShoeSize() {
        return shoeSize;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setSiteEnum(SiteEnum siteEnum) {
        this.siteEnum = siteEnum;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setScanFrequency(String scanFrequency) {
        try {
            this.scanFrequency = Integer.parseInt(scanFrequency);
        } catch (NumberFormatException e) {
            System.out.println("you didn't enter a valid number for the scan frequency");
            System.out.println("its value was not updated and is still: " + scanFrequency);
        }
    }

    public void setShoeSize(String shoeSize) {
        this.shoeSize = shoeSize;
    }

    public void setQuantity(String quantity) {
        try {
            this.quantity = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            System.out.println("you didn't enter a valid number for the quantity");
            System.out.println("its value was not updated and is still: " + quantity);
        }
    }

    @Override
    public String toString() { return url; }
}
