import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class which find all auction data
 */
public class AuctionCalendar {
  private ArrayList<AlcopaAuctionFr> alcopaAuctionFr = new ArrayList<>();

  public String toString() {
    String toReturn = "";
    for(var i : alcopaAuctionFr)
      toReturn += i + "\n";
    return toReturn;
  }

  /**
   * Finds all auction links and store them to the ArrayList auctions
   */
  public void find_showrooms_alcopaauctionfr() {
    try {
      ArrayList<AlcopaAuctionFr> newAuctions = new ArrayList<>();
      Document doc = Jsoup.connect("https://www.alcopa-auction.fr/en/auction-calendar").get();
      Elements elts = doc.select("a[href]");

      for(var i : elts){
        if(i.toString().contains("class"))
          if(i.toString().contains("online-auction") || i.toString().contains("auction-room")){
            String str = i.toString().substring(i.toString().indexOf("\"")+1);
            str = str.substring(0, str.indexOf("\""));
            newAuctions.add(new AlcopaAuctionFr(str));
          }
      }
      if(elts.size() > 0)
        alcopaAuctionFr.clear();
      alcopaAuctionFr = new ArrayList<>(newAuctions);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
