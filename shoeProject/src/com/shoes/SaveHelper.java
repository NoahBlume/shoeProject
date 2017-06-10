package com.shoes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Mbrune on 6/10/2017.
 */
public class SaveHelper {

    public static void save(UserContainer uc) {
        try {
            // Write to disk with FileOutputStream
            FileOutputStream f_out = new FileOutputStream("myUserContainer.data");

            // Write object with ObjectOutputStream
            ObjectOutputStream obj_out = new ObjectOutputStream(f_out);

            // Write object out to disk
            obj_out.writeObject(uc);
            System.out.println("Successfully saved.");
        } catch (java.io.IOException e) {
            System.out.println("Failed to save.");
        }
    }

    public static UserContainer load() {
        try {
            // Read from disk using FileInputStream
            FileInputStream f_in = new FileInputStream("myUserContainer.data");

            // Read object using ObjectInputStream
            ObjectInputStream obj_in = new ObjectInputStream (f_in);

            // Read an object
            Object obj = obj_in.readObject();

            if (obj instanceof UserContainer)
            {
                // Cast object to a UserContainer
                UserContainer loadedUC = (UserContainer) obj;

                System.out.println("Successfully loaded.");
                UserContainer.setInstance(loadedUC);
                return loadedUC;
            } else {
                System.out.println("Falied to load.");
            }
        } catch(java.io.IOException | ClassNotFoundException e) {
            System.out.println("Falied to load.");
        }
        return null;
    }
}
