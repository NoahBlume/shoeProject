import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


/**
 * Created by Mbrune on 6/10/2017.
 */
public class StockChecker {
    private Purchaser purchaser;

    public StockChecker() {

    }

    public void checkStock(User u, Site s) {
        try {

            String url = s.getUrl();

            Document doc = Jsoup.connect(url).timeout(100*1000).get();
            String title = doc.title();

            //Document doc = Jsoup.parse(htmlString);
            //String title = doc.title();
            String body = doc.body().text();

            System.out.printf("Title: %s%n", title);
            System.out.printf("Body: %s", body);
        } catch (Exception e) {
            System.out.println("jsoup thing failed");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
