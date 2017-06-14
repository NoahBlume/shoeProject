import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mbrune on 6/10/2017.
 */
public class UserContainer implements Serializable {
    private static UserContainer instance = null;
    private List<User> userList = new ArrayList<>();

    private UserContainer() {}

    public static UserContainer getInstance() {
        if (instance == null) {
            instance = new UserContainer();
        }
        return instance;
    }

    public static void setInstance(UserContainer instance) {
        UserContainer.instance = instance;
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
}
