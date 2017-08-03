import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.xml.crypto.Data;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Writer;


/**
 * Created by Mbrune on 6/10/2017.
 */
public class SaveHelper {

    /**
     *
     * WE SHOULD USE JSOUP FOR THE STOCK CHECKING PART (then create selenium instance to buy the shoe)
     *
     */

    public static void save(DataManager dm) {
        try (Writer writer = new FileWriter("saveData.json")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(dm, writer);
            System.out.println("Successfully saved JSON.");
        } catch (java.io.IOException e) {
            System.out.println("Failed to save JSON.");
        }
    }

    public static DataManager load() {

        try (Reader reader = new FileReader("saveData.json")) {

            Gson gson = new Gson();
            // Convert JSON to Java Object
            DataManager dm = gson.fromJson(reader, DataManager.class);
            System.out.println("Successfully loaded JSON.");
            return dm;

        } catch (IOException e) {
            System.out.println("Failed to load JSON.");
        }
        return null;
    }
}
