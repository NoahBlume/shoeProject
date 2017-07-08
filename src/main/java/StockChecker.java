import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


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
            Element sizeSelectionList = doc.getElementById("size_selection_list");
            System.out.println(sizeSelectionList.outerHtml());
            Elements chosenSize = sizeSelectionList.getElementsByAttributeValue("value", "07.5");
            Elements cs = sizeSelectionList.select("a.grid_size[value='07.5']");
            System.out.println("chosen size: " + cs.get(0));

            System.out.printf("Title: %s%n", title);
            System.out.printf("Body: %s", body);
        } catch (Exception e) {
            System.out.println("jsoup thing failed");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
