import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mbrune on 6/10/2017.
 */
public class User implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String ccNumber;
    private String ccExpirationDate;
    private String cvc;
    private String zipCode;
    private String streetAddress;
    private String city;
    private String state;
    private String phone;
    private List<Site> sites;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;

        sites = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return  email;
    }

    public List<Site> getSites() {
        return sites;
    }

    public String getCcNumber() {
        return ccNumber;
    }

    public String getCcExpirationDate() {
        return ccExpirationDate;
    }

    public String getCvc() {
        return cvc;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPhone() {
        return phone;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    public void setCcExpirationDate(String ccExpirationDate) {
        this.ccExpirationDate = ccExpirationDate;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void addSite(Site site) {
        sites.add(site);
    }

    public Site removeSite(int siteIndex) {
        return sites.remove(siteIndex);
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return getFullName();
    }
}
