import java.util.ArrayList;
import java.util.Calendar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;

public class AuctionData {
  private Calendar calendarBegin = Calendar.getInstance();
  private Calendar calendarEnd = Calendar.getInstance();
  private String[] monthsArray = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
  private String url = "";
  private String city = "";
  private String auctionType = "cars";
  private int auctionNr = 0;
  private int passengerVehicles = 0;
  private int utilityVehicles = 0;
  private int brokenVehicles = 0;
  private int gearMotorcycles = 0;
  private int mopedMotorcycles = 0;


  public AuctionData(String url) {
    this.url = url;
    if(url.indexOf("?site=internet") > 0)
      auctionNr = Integer.parseInt(url.substring(url.lastIndexOf("/")+1, url.indexOf("?site=internet")-1));
    else{
      auctionNr = Integer.parseInt(url.substring(url.lastIndexOf("/")+1));
    init();}
  }



  public final void init() {
    try{
      System.out.println(url);
      Document doc = Jsoup.connect("https://www.alcopa-auction.fr" + url).get();
      Elements elTime = doc.getElementsByClass("fa fa-calendar");

      calendarBegin.set(Calendar.SECOND, 0);
      calendarBegin.set(Calendar.MILLISECOND, 0);
      calendarEnd.set(Calendar.SECOND, 0);
      calendarEnd.set(Calendar.MILLISECOND, 0);
      var time = elTime.get(0).nextSibling().nextSibling().toString();
      int hourBegin, minuteBegin, hourEnd, minuteEnd;
      int year = 1970, month = 0, day = 1;
      hourBegin = Integer.parseInt(time.substring(time.indexOf(">")+1, time.indexOf(":")));
      minuteBegin = Integer.parseInt(time.substring(time.indexOf(":")+1, time.indexOf("</")));
      time = elTime.get(0).nextSibling().nextSibling().nextSibling().nextSibling().toString();
      hourEnd = Integer.parseInt(time.substring(time.indexOf(">")+1, time.indexOf(":")));
      minuteEnd = Integer.parseInt(time.substring(time.indexOf(":")+1, time.indexOf("</")));
      Elements elDate = doc.getElementsByClass("fa fa-car text-prim mr-1");
      if(elDate.isEmpty()) {
        elDate = doc.getElementsByClass("fa fa-motorcycle text-alcyellow mr-1");
        auctionType = "motorcycles";
      }
      String date = elDate.get(0).nextSibling().toString();
      for(int i = 0; i < monthsArray.length; i++) {
        if (date.indexOf(monthsArray[i]) > 0) {
          day = Integer.parseInt(date.substring(date.indexOf("of ")+3, date.indexOf(monthsArray[i])-1));
          month = i;
          year = Integer.parseInt(date.substring(date.indexOf(monthsArray[i])+monthsArray[i].length()+1, date.indexOf(" at ")));
          city = date.substring(date.indexOf(" at ") + " at ".length()+1);
          city = date.substring(0, date.indexOf(" "));
          break;
        }
      }
      calendarBegin.set(year, month, day, hourBegin, minuteBegin);
      calendarEnd.set(year, month, day, hourEnd, minuteEnd);
      getVehiclesData();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ArithmeticException e) {
      e.printStackTrace();
    }

  }

  public final void getVehiclesData() {
    url = "/en/auction-room/nancy/2274";
    ArrayList<String> vehiclesType = new ArrayList<>();
    try{
      Document doc = Jsoup.connect("https://www.alcopa-auction.fr" + url).get();
      Elements el = doc.getElementsByClass("nav nav-tabs mb-2").select("a[href]");
      for(var i : el){
        String str = i.toString().substring(i.toString().indexOf("\"")+1);
        str = str.substring(0, str.indexOf("\""));
        vehiclesType.add(str);
      }

    } catch (IOException e) {
      e.printStackTrace();
    } catch (ArithmeticException e) {
      e.printStackTrace();
    }
  }

  public String getUrl() { return url; }
  public String getLiveUrl() { return "http://live.alcopa-auction.fr/" + auctionNr; }
  public int getPassengerVehicles() { return passengerVehicles; }
  public int getUtilityVehicles() { return utilityVehicles; }
  public int getBrokenVehicles() { return brokenVehicles; }
  public int getAllVehicles() { return passengerVehicles+utilityVehicles+brokenVehicles;}

  public String toString() {
    String toReturn = "";
    toReturn += "City: " + city;
    toReturn += "\tType: " + auctionType;
    toReturn += "\tnr: " + auctionNr + "\n";
    toReturn += "Begin: " + calendarBegin.getTime() + "\n";
    toReturn += "End: " + calendarEnd.getTime() + "\n";
    if(auctionType.equals("cars")) {
      toReturn += "Passenger vehicles: " + passengerVehicles + "\n";
      toReturn += "Utility vehicles: " + utilityVehicles + "\n";
    }else{
      toReturn += "Gears: " + gearMotorcycles + "\n";
      toReturn += "Mopeds: " + mopedMotorcycles + "\n";
    }
    toReturn += "Not allowed to traffic: " + brokenVehicles + "\n";
    return toReturn;
  }
}
