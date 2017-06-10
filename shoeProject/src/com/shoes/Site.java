package com.shoes;

import java.io.Serializable;

/**
 * Created by Mbrune on 6/10/2017.
 */
public class Site implements Serializable{
    private SiteEnum siteEnum;
    String url;
    private Purchaser scraper = new Purchaser();
    private int scanFrequency = 10; //seconds

    public Site(SiteEnum siteEnum, String url, int scanFrequency) {
        this.siteEnum = siteEnum;
        this.url = url;
        this.scanFrequency = scanFrequency;
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

    public void setSiteEnum(SiteEnum siteEnum) {
        this.siteEnum = siteEnum;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setScanFrequency(int scanFrequency) {
        this.scanFrequency = scanFrequency;
    }

    @Override
    public String toString() {
        return url;
    }
}
