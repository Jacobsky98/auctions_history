import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class AuctionCalendar {
  private ArrayList<AuctionData> auctions = new ArrayList<>();

  public String toString() {
    String toReturn = "";
    for(var i : auctions)
      toReturn += i + "\n";
    return toReturn;
  }

  public void findAuctions() {
    try {
      ArrayList<AuctionData> newAuctions = new ArrayList<>();
      Document doc = Jsoup.connect("https://www.alcopa-auction.fr/en/auction-calendar").get();
      Elements elts = doc.select("a[href]");

      for(var i : elts){
        if(i.toString().contains("class"))
          if(i.toString().contains("online-auction") || i.toString().contains("auction-room")){
            String str = i.toString().substring(i.toString().indexOf("\"")+1);
            str = str.substring(0, str.indexOf("\""));
            newAuctions.add(new AuctionData(str));
          }
      }
      if(elts.size() > 0)
        auctions.clear();
      auctions = new ArrayList<>(newAuctions);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
