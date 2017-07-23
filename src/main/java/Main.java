import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scan = new Scanner(System.in);
    private static DataManager dm;
    static List<Thread> buying = Collections.synchronizedList(new ArrayList<Thread>());

    public static void main(String[] args) {
        dm = SaveHelper.load();
        if (dm == null) {
            dm = DataManager.getInstance();
        }


//        List<User> ul = dm.getUserList();

//        Scraper scrappyDoo = new Scraper();
//        scrappyDoo.checkStock(ul.get(0), ul.get(0).getSites().get(0));

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
        pl("3: Add Proxy");
        pl("4: Delete Proxy");
        pl("5: Run All Bots");

        String input = scan.next();
        if ("exit".equals(input)) {
            System.exit(0);
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
                case 3:
                    addProxy();
                    break;
                case 4:
                    deleteProxy();
                    break;
                case 5:
                    runAllBots();
                    break;
                default:
                    pl("Please enter one of the numbers listed above");
                    printMainOptions();
                    return;
            }
        } catch (NumberFormatException e) {
            pl("Please enter one of the numbers listed above");
            printMainOptions();
            return;
        }
        printMainOptions();
    }

    private static void addUser() {
        pl("Enter the info for the new User (enter 'exit' to go back to the main menu)");
        pl("Enter this User's first name");
        String firstName = scan.next();
        if (firstName.equals("exit")) {
            return;
        }

        pl("Enter this User's last name");
        String lastName = scan.next();
        if (lastName.equals("exit")) {
            return;
        }
        User user = new User(firstName, lastName);

        pl("Enter this User's email");
        String email = scan.next();
        if (email.equals("exit")) {
            return;
        }
        user.setEmail(email);

        pl("Enter this user's phone number (10 digits with no dashes)");
        String phone = scan.next();
        if (phone.equals("exit")) {
            return;
        }
        user.setPhone(phone);

        pl("Enter the user's credit card number.");
        String ccNumber = scan.next();
        if (ccNumber.equals("exit")) {
            return;
        }
        user.setCcNumber(ccNumber);

        pl("Enter the user's credit card expiration date.(eg 05/20)");
        String expDate = scan.next();
        if (expDate.equals("exit")) {
            return;
        }
        user.setCcExpirationDate(expDate);

        pl("Enter the user's credit card cvc number.");
        String cvc = scan.next();
        if (cvc.equals("exit")) {
            return;
        }
        user.setCvc(cvc);

        pl("Enter the user's street address (eg 123 Main Street)");
        scan.nextLine();
        String street = scan.nextLine();
        if (street.equals("exit")) {
            return;
        }
        user.setStreetAddress(street);

        pl("Enter the user's zipcode");
        String zipCode = scan.next();
        if (zipCode.equals("exit")) {
            return;
        }
        user.setZipCode(zipCode);

        pl("Please enter the user's city");
        scan.nextLine();
        String city = scan.nextLine();
        if (city.equals("exit")) {
            return;
        }
        user.setCity(city);

        pl("Please enter the user's state");
        String state = scan.nextLine();
        if (state.equals("exit")) {
            return;
        }
        user.setState(state);

        dm.addUser(user);
        SaveHelper.save(dm);
        printMainOptions();
    }

    private static void editUsers() {
        pl("Enter a number corresponding the user you want to edit. (enter 'exit' to go back to the main menu)");
        List<User> userList = dm.getUserList();
        int i = 0;
        for (User u: userList) {
            pl(i + ": " + u.getFullName());
            i++;
        }

        String input = scan.next();
        if ("exit".equals(input)) {
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
        pl("2: phone");
        pl("3: credit card info");
        pl("4: address info");
        pl("5: edit site");
        pl("6: add site");
        pl("7: delete site");

        String input = scan.next();
        if ("exit".equals(input)) {
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
                    editPhone(u);
                    break;
                case 3:
                    editCreditCard(u);
                    break;
                case 4:
                    editAddress(u);
                    break;
                case 5:
                    editSites(u);
                    break;
                case 6:
                    addSite(u);
                    break;
                case 7:
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
            return;
        } else {
            u.setFirstName(firstName);
            SaveHelper.save(dm);
        }
        pl("Please enter the user's new last name (enter 'exit' to go back to the previous menu)");
        String lastName = scan.next();
        if (!lastName.equals("exit")) {
            u.setLastName(lastName);
            SaveHelper.save(dm);
        }
    }

    private static void editEmail(User u) {
        pl("Please enter the user's new email (enter 'exit' to go back to the previous menu)");
        String email = scan.next();
        if (!email.equals("exit")) {
            u.setEmail(email);
            SaveHelper.save(dm);
        }
    }

    private static void editPhone(User u) {
        pl("Please enter the user's new phone number (10 digits with no dashes)");
        String phone = scan.next();
        if (!phone.equals("exit")) {
            u.setPhone(phone);
            SaveHelper.save(dm);
        }
    }

    private static void editCreditCard(User u) {
        pl("Please enter the user's new credit card info (enter 'exit' to go back to the previous menu)");
        pl("Please enter the user's new credit card number.");
        String ccNumber = scan.next();
        if (ccNumber.equals("exit")) {
            return;
        } else {
            u.setCcNumber(ccNumber);
            SaveHelper.save(dm);
        }

        pl("Please enter the user's new credit card expiration date.(eg 05/20)");
        String expDate = scan.next();
        if (expDate.equals("exit")) {
            return;
        } else {
            u.setCcExpirationDate(expDate);
            SaveHelper.save(dm);
        }

        pl("Please enter the user's new credit card cvc number.");
        String cvc = scan.next();
        if (cvc.equals("exit")) {
            editUser(u);
        } else {
            u.setCvc(cvc);
            SaveHelper.save(dm);
        }
    }

    private static void editAddress(User u) {
        pl("Please enter the user's new address info (enter 'exit' to go back to the previous menu)");
        pl("Please enter the user's new street address (eg 123 Main Street)");
        scan.nextLine();
        String street = scan.nextLine();
        if (street.equals("exit")) {
            return;
        } else {
            u.setStreetAddress(street);
            SaveHelper.save(dm);
        }

        pl("Please enter the user's new zipcode");
        String zipCode = scan.next();
        if (zipCode.equals("exit")) {
            return;
        } else {
            u.setZipCode(zipCode);
            SaveHelper.save(dm);
        }

        pl("Please enter the user's new city");
        scan.nextLine();
        String city = scan.nextLine();
        if (city.equals("exit")) {
            return;
        } else {
            u.setCity(city);
            SaveHelper.save(dm);
        }

        pl("Please enter the user's new state");
        String state = scan.nextLine();
        if (!state.equals("exit")) {
            u.setState(state);
            SaveHelper.save(dm);
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
            return;
        }
        Site site = new Site(url);

        pl("Enter this Site's scan frequency - in seconds");
        String scanFrequency = scan.next();
        if (scanFrequency.equals("exit")) {
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

/*
        private String shoeSize;
        private String sku;
        private int quantity = 1;
        private int scanFrequency = 10; //seconds
        private String sitePassword;
        private String siteUsername; //or email
*/
        pl("please enter the shoe size you want to buy");
        String size = scan.next();
        if ("exit".equals(size)) {
            return;
        }
        try {
            double choice = Double.parseDouble(size);
            DecimalFormat df = new DecimalFormat("00.0");
            String formattedSize = df.format(choice);
            site.setShoeSize(formattedSize);
        } catch (NumberFormatException e) {
            pl("that wasn't a valid number");
            addSite(u);
            return;
        }

        pl("please enter the number of shoes you want to buy");
        String quantity = scan.next();
        if (quantity.equals("exit")) {
            return;
        }
        site.setQuantity(quantity);

        u.addSite(site);
        SaveHelper.save(dm);
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

        SaveHelper.save(dm);
        editUser(u);
    }

    private static void editSite(Site s, User u) {
        pl("Enter a number corresponding the part of the site you want to edit (enter 'exit' to go back to the main menu)");
        pl("What would you like to edit (enter 'exit' to go back to the main menu)");
        pl("0: Site type"); //site enum
        pl("1: url");
        pl("2: scan frequency");
        pl("3: quantity");
        pl("4: shoe size");

        String input = scan.next();
        if ("exit".equals(input)) {
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
                case 3:
                    editSiteQuantity(s, u);
                    break;
                case 4:
                    editSiteShoeSize(s, u);
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
            return;
        }
        try {
            int choice = Integer.parseInt(input);
            if (choice < SiteEnum.values().length) {
                s.setSiteEnum(SiteEnum.values()[choice]);
                SaveHelper.save(dm);
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
            return;
        } else {
            s.setUrl(url);
            SaveHelper.save(dm);
        }
        editSite(s, u);
    }

    private static void editSiteScanFrequency(Site s, User u) {
        pl("Please enter the site's new scan frequency - in seconds (enter 'exit' to go back to the previous menu)");
        String frequency = scan.next();
        if ("exit".equals(frequency)) {
            return;
        } else {
            s.setScanFrequency(frequency);
            SaveHelper.save(dm);
        }
        editSite(s, u);
    }

    private static void editSiteQuantity(Site s, User u) {
        pl("Please enter the quantity of shoes wanted (enter 'exit' to go back to the previous menu)");
        String quantity = scan.next();
        if ("exit".equals(quantity)) {
            return;
        } else {
            s.setQuantity(quantity);
            SaveHelper.save(dm);
        }
        editSite(s, u);
    }

    private static void editSiteShoeSize(Site s, User u) {
        pl("Please enter the size of shoe you want (enter 'exit' to go back to the previous menu)");
        String size = scan.next();
        if ("exit".equals(size)) {
            return;
        }
        try {
            double choice = Double.parseDouble(size);
            DecimalFormat df = new DecimalFormat("00.0");
            String formattedSize = df.format(choice);
            s.setShoeSize(formattedSize);
            SaveHelper.save(dm);
        } catch (NumberFormatException e) {
            pl("that wasn't a valid number");
            editSiteShoeSize(s, u);
            return;
        }
        editSite(s, u);
    }

    private static void deleteUsers() {
        pl("Please enter the number corresponding to the user you would like to delete.");
        int i = 0;
        for (User u: dm.getUserList()) {
            pl(i + ": " + u.getFullName());
            i++;
        }
        String input = scan.next();
        if ("exit".equals(input)) {
            return;
        }
        try {
            int choice = Integer.parseInt(input);
            if (choice < dm.getUserList().size()) {
                dm.removeUser(choice);
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

        SaveHelper.save(dm);
        printMainOptions();
    }

    private static void addProxy() {
        pl("Enter the new proxy in the form 'ipAddress:port' - e.g. 216.3.128.12:8080 (enter 'exit' to go back to the main menu)");
        String proxy = scan.next();
        if (proxy.equals("exit")) {
            return;
        }

        dm.addProxy(proxy);
        SaveHelper.save(dm);
        printMainOptions();
    }

    private static void deleteProxy() {
        pl("Please enter the number corresponding to the proxy you would like to delete.");
        int i = 0;
        for (String s: dm.getProxyList()) {
            pl(i + ": " + s);
            i++;
        }

        String input = scan.next();
        if ("exit".equals(input)) {
            return;
        }
        try {
            int choice = Integer.parseInt(input);
            if (choice < dm.getProxyList().size()) {
                dm.removeProxy(choice);
            } else {
                pl("that wasn't a valid user number");
                deleteProxy();
                return;
            }
        } catch (NumberFormatException e) {
            pl("that wasn't a valid user number");
            deleteProxy();
            return;
        }

        SaveHelper.save(dm);
        printMainOptions();
    }

    private static void runAllBots() {
        //List<User> ul = dm.getUserList();
       // Scraper scrappyDoo = new Scraper();
        //scrappyDoo.checkStock(ul.get(0), ul.get(0).getSites().get(0));
        List<String> proxyList = dm.getProxyList();
        for (User u: dm.getUserList()) {
            for (Site s: u.getSites()) {
                try {
                    int index = buying.size();
                    Scraper scraper = new Scraper(u, s, index, proxyList.get(index % proxyList.size()));
                    Thread thread = new Thread(scraper);
                    thread.setPriority(Thread.MIN_PRIORITY);
                    thread.start();
                    buying.add(thread);
                    //scrappyDoo.checkStock(u, s);
                } catch (Exception e) {
                    System.out.println("Failed to check user: " + u.getFullName() + " site: " + s.getUrl());
                    System.out.println("please make sure that all info is entered correctly for the user and site");
                }
            }
        }
        printMainOptions();
    }

    private static void pl(String s) {
        System.out.println(s);
    }
}
