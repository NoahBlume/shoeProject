package com.shoes;

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

    public void addUser(String name, String email, String password) {
        User u = new User(name, email, password);
        userList.add(u);
    }

    public List<User> getUserList() {
        return userList;
    }
}
