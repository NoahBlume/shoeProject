import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mbrune on 6/10/2017.
 */
public class DataManager implements Serializable {
    private static DataManager instance = null;
    private List<User> userList = new ArrayList<>();
    private List<String> proxyList = new ArrayList<>();

    private DataManager() {}

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public static void setInstance(DataManager instance) {
        DataManager.instance = instance;
    }

    public void addUser(String firstName, String lastName) {
        User u = new User(firstName, lastName);
        userList.add(u);
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public User removeUser(int userIndex) {
        return userList.remove(userIndex);
    }

    public List<User> getUserList() {
        return userList;
    }

    public void addProxy(String proxy) { proxyList.add(proxy);}

    public String removeProxy(int proxyIndex) { return proxyList.remove(proxyIndex); }

    public List<String> getProxyList() { return proxyList; }
}
