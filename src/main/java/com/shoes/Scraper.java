package com.shoes;

import java.io.Serializable;


import com.thoughtworks.selenium.*;
import org.seleniumhq.selenium.*;

/**
 * Created by Mbrune on 6/10/2017.
 * Edited by dr4g0nbyt3 on 6/13/2017.
 */
public class Scraper implements Serializable {

    public void scrape(SiteEnum se) {
        switch(se) {
            case SITE_1:
                scrape1();
                break;
            case SITE_2:
                scrape2();
                break;
            case SITE_3:
                scrape3();
                break;
            default:
                return;
        }
    }

    private void scrape1() {

    }

    private void scrape2() {

    }

    private void scrape3() {

    }
}
