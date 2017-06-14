import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scan = new Scanner(System.in);
    private static UserContainer uc;

    public static void main(String[] args) {
	    // write your code here
        // writing my code here
        Scraper scrappyDoo = new Scraper();
        scrappyDoo.scrape();

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
        pl("1: Edit Users");
        pl("2: Delete Users");

        String input = scan.next();
        if ("exit".equals(input)) {
            return;
        }
        try {
            int choice = Integer.parseInt(input);
            switch(choice) {
                case 0:
                    addUser();
                    break;
                case 1:
                    editUsers();
                    break;
                case 2:
                    deleteUsers();
                    break;
                default:
                    pl("Please enter one of the numbers listed above");
                    printMainOptions();
            }
        } catch (NumberFormatException e) {
            pl("Please enter one of the numbers listed above");
            printMainOptions();
        }

    }

    private static void addUser() {
        pl("Enter the info for the new User (enter 'exit' to go back to the main menu)");
        pl("Enter this User's first name");
        String firstName = scan.next();
        if (firstName.equals("exit")) {
            printMainOptions();
            return;
        }

        pl("Enter this USer's last name");
        String lastName = scan.next();
        if (lastName.equals("exit")) {
            printMainOptions();
            return;
        }
        User user = new User(firstName, lastName);

        pl("Enter this User's email");
        String email = scan.next();
        if (email.equals("exit")) {
            printMainOptions();
            return;
        }
        user.setEmail(email);

        uc.addUser(user);
        SaveHelper.save(uc);
        printMainOptions();
    }

    private static void editUsers() {
        pl("Enter a number corresponding the user you want to edit. (enter 'exit' to go back to the main menu)");
        List<User> userList = uc.getUserList();
        int i = 0;
        for (User u: userList) {
            pl(i + ": " + u.getFullName());
            i++;
        }

        String input = scan.next();
        if ("exit".equals(input)) {
            printMainOptions();
            return;
        }
        try {
            int choice = Integer.parseInt(input);
            if (choice < userList.size()) {
                editUser(userList.get(choice));
            } else {
                pl("that wasn't a valid number");
                editUsers();
                return;
            }
        } catch (NumberFormatException e) {
            pl("that wasn't a valid number");
            editUsers();
            return;
        }

        printMainOptions();
    }

    private static void editUser(User u) {
        pl("User selected the user named: " + u.getFullName());
        pl("Email: " + u.getEmail());
        pl("Sites: " + u.getSites());
        pl("What would you like to edit (enter 'exit' to go back to the main menu)");
        pl("0: name");
        pl("1: email");
        pl("2: credit card number");
        pl("3: edit site");
        pl("4: add site");
        pl("5: delete site");

        String input = scan.next();
        if ("exit".equals(input)) {
            editUsers();
            return;
        }
        try {
            int choice = Integer.parseInt(input);
            switch(choice) {
                case 0:
                    editName(u);
                    break;
                case 1:
                    editEmail(u);
                    break;
                case 2:
                    editCcNumber(u);
                    break;
                case 3:
                    editSites(u);
                    break;
                case 4:
                    addSite(u);
                    break;
                case 5:
                    deleteSites(u);
                    break;
                default:
                    pl("Please enter one of the numbers listed above");
                    editUser(u);
                    return;
            }
        } catch (NumberFormatException e) {
            pl("that wasn't a valid number");
            editUser(u);
            return;
        }

        editUsers();
    }

    private static void editName(User u) {
        pl("Please enter the user's new first name (enter 'exit' to go back to the previous menu)");
        String firstName = scan.next();
        if (firstName.equals("exit")) {
            editUser(u);
            return;
        } else {
            u.setFirstName(firstName);
            SaveHelper.save(uc);
        }
        pl("Please enter the user's new last name (enter 'exit' to go back to the previous menu)");
        String lastName = scan.next();
        if (lastName.equals("exit")) {
            editUser(u);
        } else {
            u.setFirstName(lastName);
            SaveHelper.save(uc);
        }
    }

    private static void editEmail(User u) {
        pl("Please enter the user's new email (enter 'exit' to go back to the previous menu)");
        String email = scan.next();
        if (email.equals("exit")) {
            editUser(u);
        } else {
            u.setEmail(email);
            SaveHelper.save(uc);
        }
    }

    private static void editCcNumber(User u) {
        pl("Please enter the user's new credit card number (enter 'exit' to go back to the previous menu)");
        String ccNumber = scan.next();
        if (ccNumber.equals("exit")) {
            editUser(u);
        } else {
            u.setCcNumber(ccNumber);
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

        String input = scan.next();
        if ("exit".equals(input)) {
            editUser(u);
            return;
        }
        try {
            int choice = Integer.parseInt(input);
            if (choice < siteList.size()) {
                editSite(siteList.get(choice), u);
            } else {
                pl("that wasn't a valid number");
                editSites(u);
                return;
            }
        } catch (NumberFormatException e) {
            pl("that wasn't a valid number");
            editSites(u);
            return;
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
        Site site = new Site(url);

        pl("Enter this Site's scan frequency - in seconds");
        String scanFrequency = scan.next();
        if (scanFrequency.equals("exit")) {
            editUser(u);
            return;
        }
        site.setScanFrequency(scanFrequency);

        pl("Enter the number corresponding to the site's type");
        int i = 0;
        for (SiteEnum se: SiteEnum.values()) {
            pl(i + ": " + se.toString());
            i++;
        }

        String input = scan.next();
        if ("exit".equals(input)) {
            editUser(u);
            return;
        }
        try {
            int choice = Integer.parseInt(input);
            if (choice < SiteEnum.values().length) {
                SiteEnum siteEnum = SiteEnum.values()[choice];
                site.setSiteEnum(siteEnum);
            } else {
                pl("that wasn't a valid number");
                addSite(u);
                return;
            }
        } catch (NumberFormatException e) {
            pl("that wasn't a valid number");
            addSite(u);
            return;
        }

        u.addSite(site);
        SaveHelper.save(uc);
        editUser(u);
    }

    private static void deleteSites(User u) {
        pl("Please enter the number corresponding to the site you would like to delete.");
        int i = 0;
        for (Site s: u.getSites()) {
            pl(i + ": " + s.toString());
            i++;
        }
        String input = scan.next();
        if ("exit".equals(input)) {
            editUser(u);
            return;
        }
        try {
            int choice = Integer.parseInt(input);
            if (choice < u.getSites().size()) {
                u.removeSite(choice);
            } else {
                pl("that wasn't a valid user number");
                deleteSites(u);
                return;
            }
        } catch (NumberFormatException e) {
            pl("that wasn't a valid user number");
            deleteSites(u);
            return;
        }

        SaveHelper.save(uc);
        editUser(u);
    }

    private static void editSite(Site s, User u) {
        pl("Enter a number corresponding the part of the site you want to edit (enter 'exit' to go back to the main menu)");
        pl("What would you like to edit (enter 'exit' to go back to the main menu)");
        pl("0: Site type"); //site enum
        pl("1: url");
        pl("2: scan frequency");

        String input = scan.next();
        if ("exit".equals(input)) {
            editSites(u);
            return;
        }
        try {
            int choice = Integer.parseInt(input);
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
                    return;
            }
        } catch (NumberFormatException e) {
            pl("that wasn't a valid number");
            editSite(s, u);
            return;
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

        String input = scan.next();
        if ("exit".equals(input)) {
            editSite(s, u);
            return;
        }
        try {
            int choice = Integer.parseInt(input);
            if (choice < SiteEnum.values().length) {
                s.setSiteEnum(SiteEnum.values()[choice]);
                SaveHelper.save(uc);
            } else {
                pl("that wasn't a valid number");
                editSiteEnum(s, u);
                return;
            }
        } catch (NumberFormatException e) {
            pl("that wasn't a valid number");
            editSiteEnum(s, u);
            return;
        }

        editSite(s, u);
    }

    private static void editSiteUrl(Site s, User u) {
        pl("Please enter the site's new url (enter 'exit' to go back to the previous menu)");
        String url = scan.next();
        if (url.equals("exit")) {
            editSite(s, u);
            return;
        } else {
            s.setUrl(url);
            SaveHelper.save(uc);
        }
        editSite(s, u);
    }

    private static void editSiteScanFrequency(Site s, User u) {
        pl("Please enter the site's new scan frequency - in seconds (enter 'exit' to go back to the previous menu)");
        String input = scan.next();
        if ("exit".equals(input)) {
            editSite(s, u);
            return;
        }
        try {
            int choice = Integer.parseInt(input);
            if (choice < SiteEnum.values().length) {
                SiteEnum siteEnum = SiteEnum.values()[choice];
                s.setSiteEnum(siteEnum);
            } else {
                pl("that wasn't a valid number");
                editSiteScanFrequency(s, u);
                return;
            }
        } catch (NumberFormatException e) {
            pl("that wasn't a valid number");
            editSiteScanFrequency(s, u);
            return;
        }

        SaveHelper.save(uc);
        editSite(s, u);
    }

    private static void deleteUsers() {
        pl("Please enter the number corresponding to the user you would like to delete.");
        int i = 0;
        for (User u: uc.getUserList()) {
            pl(i + ": " + u.getFullName());
            i++;
        }
        String input = scan.next();
        if ("exit".equals(input)) {
            printMainOptions();
            return;
        }
        try {
            int choice = Integer.parseInt(input);
            if (choice < uc.getUserList().size()) {
                uc.removeUser(choice);
            } else {
                pl("that wasn't a valid user number");
                deleteUsers();
                return;
            }
        } catch (NumberFormatException e) {
            pl("that wasn't a valid user number");
            deleteUsers();
            return;
        }

        SaveHelper.save(uc);
        printMainOptions();
    }

    private static void pl(String s) {
        System.out.println(s);
    }
}
