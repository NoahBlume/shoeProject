package com.shoes;

import java.io.Serializable;

/**
 * Created by Mbrune on 6/10/2017.
 */
public class Purchaser implements Serializable {

    public static void purchase(SiteEnum se) {
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

    private static void scrape1() {

    }

    private static void scrape2() {

    }

    private static void scrape3() {

    }
}
