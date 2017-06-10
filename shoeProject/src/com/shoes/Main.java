package com.shoes;

import java.util.List;
import java.util.Scanner;


public class Main {
    private static Scanner scan = new Scanner(System.in);
    private static UserContainer uc;

    public static void main(String[] args) {
	// write your code here
        uc = SaveHelper.load();
        if (uc == null) {
            uc = UserContainer.getInstance();
        }
        printGreeting();
    }

    private static void printGreeting() {
        pl("Welcome to the shoe project thing - I'll put the real name here later");
        printMainOptions();
    }

    private static void printMainOptions() {
        pl("Enter the number corresponding to the option you want to choose.");
        pl("0: Add User");
        pl("1: Edit users");
        int choice = scan.nextInt();

        switch(choice) {
            case 0:
                addUser();
                break;
            case 1:
                editUsers();
                break;
            default:
                pl("Please enter one of the numbers listed above");
                printMainOptions();
        }
    }

    private static void addUser() {
        pl("Enter the info for the new User (enter 'exit' to go back to the main menu)");
        pl("Enter this User's name");
        String name = scan.next();
        if (name.equals("exit")) {
            printMainOptions();
            return;
        }
        pl("Enter this User's email");
        String email = scan.next();
        if (email.equals("exit")) {
            printMainOptions();
            return;
        }
        pl("Enter this User's password");
        String password = scan.next();
        if (password.equals("exit")) {
            printMainOptions();
            return;
        }
        uc.addUser(name, email, password);
        SaveHelper.save(uc);
        printMainOptions();
    }

    private static void editUsers() {
        pl("Enter a number corresponding the user you want to edit. (enter 'exit' to go back to the main menu)");
        List<User> userList = uc.getUserList();
        int i = 0;
        for (User u: userList) {
            pl(i + ": " + u.getName());
            i++;
        }
        int choice = scan.nextInt();
        if (choice < userList.size()) {
            editUser(userList.get(choice));
        } else {
            pl("that wasn't a valid number");
            editUsers();
        }
        printMainOptions();
    }

    private static void editUser(User u) {
        pl("User selected the user named: " + u.getName());
        pl("Email: " + u.getEmail());
        pl("Password: " + u.getPassword());
        pl("Sites: " + u.getSites());
        pl("What would you like to edit (enter 'exit' to go back to the main menu)");
        pl("0: name");
        pl("1: email");
        pl("2: password");
        pl("3: edit site");
        pl("4: add site");

        int choice = scan.nextInt();

        switch(choice) {
            case 0:
                editName(u);
                break;
            case 1:
                editEmail(u);
                break;
            case 2:
                editPassword(u);
                break;
            case 3:
                editSites(u);
                break;
            case 4:
                addSite(u);
                break;
            default:
                pl("Please enter one of the numbers listed above");
                editUser(u);
        }
        editUsers();
    }

    private static void editName(User u) {
        pl("Please enter the user's new name (enter 'exit' to go back to the previous menu)");
        String name = scan.next();
        if (name.equals("exit")) {
            editUser(u);
        } else {
            u.setName(name);
            SaveHelper.save(uc);
        }
    }

    private static void editEmail(User u) {
        pl("Please enter the user's new password (enter 'exit' to go back to the previous menu)");
        String email = scan.next();
        if (email.equals("exit")) {
            editUser(u);
        } else {
            u.setEmail(email);
            SaveHelper.save(uc);
        }
    }

    private static void editPassword(User u) {
        pl("Please enter the user's new password (enter 'exit' to go back to the previous menu)");
        String password = scan.next();
        if (password.equals("exit")) {
            editUser(u);
        } else {
            u.setPassword(password);
            SaveHelper.save(uc);
        }
    }

    private static void editSites(User u) {
        pl("Enter a number corresponding the site you want to edit. (enter 'exit' to go back to the main menu)");
        List<Site> siteList = u.getSites();
        int i = 0;
        for (Site s: siteList) {
            pl(i + ": " + s.toString());
            i++;
        }
        int choice = scan.nextInt();
        if (choice < siteList.size()) {
            editSite(siteList.get(choice), u);
        } else {
            pl("that wasn't a valid number");
            editSites(u);
        }
        editUser(u);
    }

    private static void addSite(User u) {
        pl("Enter the info for the new Site (enter 'exit' to go back to the main menu)");
        pl("Enter this Site's url");
        String url = scan.next();
        if (url.equals("exit")) {
            editUser(u);
            return;
        }
        pl("Enter this Site's scan frequency - in seconds");
        int scanFrequency = scan.nextInt();

        pl("Enter the number corresponding to the site's type");
        int i = 0;
        for (SiteEnum se: SiteEnum.values()) {
            pl(i + ": " + se.toString());
            i++;
        }
        SiteEnum siteEnum = SiteEnum.values()[0];
        int choice = scan.nextInt();
        if (choice < SiteEnum.values().length) {
            siteEnum = SiteEnum.values()[choice];
        } else {
            pl("that wasn't a valid number");
            addSite(u);
        }

        u.addSite(new Site(siteEnum, url, scanFrequency));
        SaveHelper.save(uc);
        editUser(u);
    }

    private static void editSite(Site s, User u) {
        pl("Enter a number corresponding the part of the site you want to edit (enter 'exit' to go back to the main menu)");
        pl("What would you like to edit (enter 'exit' to go back to the main menu)");
        pl("0: Site type"); //site enum
        pl("1: url");
        pl("2: scan frequency");

        int choice = scan.nextInt();

        switch(choice) {
            case 0:
                editSiteEnum(s, u);
                break;
            case 1:
                editSiteUrl(s, u);
                break;
            case 2:
                editSiteScanFrequency(s, u);
                break;
            default:
                pl("Please enter one of the numbers listed above");
                editSite(s, u);
        }
        editSites(u);
    }

    private static void editSiteEnum(Site s, User u) {
        pl("Please enter the number corresponding the new site type (enter 'exit' to go back to the previous menu)");
        int i = 0;
        for (SiteEnum se: SiteEnum.values()) {
            pl(i + ": " + se.toString());
            i++;
        }
        int choice = scan.nextInt();
        if (choice < SiteEnum.values().length) {
            s.setSiteEnum(SiteEnum.values()[choice]);
            SaveHelper.save(uc);
        } else {
            pl("that wasn't a valid number");
            editSiteEnum(s, u);
        }
        editSite(s, u);
    }

    private static void editSiteUrl(Site s, User u) {
        pl("Please enter the site's new url (enter 'exit' to go back to the previous menu)");
        String url = scan.next();
        if (url.equals("exit")) {
            editSite(s, u);
        } else {
            s.setUrl(url);
            SaveHelper.save(uc);
        }
        editSite(s, u);
    }

    private static void editSiteScanFrequency(Site s, User u) {
        pl("Please enter the site's new scan frequency - in seconds (enter 'exit' to go back to the previous menu)");
        int scanFrequency = scan.nextInt();
        s.setScanFrequency(scanFrequency);
        editSite(s, u);
        SaveHelper.save(uc);
    }

    private static void pl(String s) {
        System.out.println(s);
    }
}
