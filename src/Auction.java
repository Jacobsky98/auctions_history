import org.jsoup.nodes.Document;
import java.util.ArrayList;
import java.util.Calendar;

public abstract class Auction {
  protected Calendar calendarBegin;// = Calendar.getInstance();
  protected Calendar calendarEnd;
  protected String showRoomUrl = "";
  protected String city = "";
  protected String auctionType = "cars";
  protected int auctionNr = 0;
  protected int passengerVehiclesQuantity = 0;
  protected ArrayList<Vehicle> vehicles;


  abstract void captureVehicleDescription(Document doc, Vehicle vehicle);
  abstract void captureVehicleImages(Document doc, Vehicle vehicle);


  public Calendar getCalendarBegin() {
    return calendarBegin;
  }

  public Calendar getCalendarEnd() {
    return calendarEnd;
  }

  public String getShowRoomUrl() {
    return showRoomUrl;
  }

  public String getCity() {
    return city;
  }

  public String getAuctionType() {
    return auctionType;
  }

  public int getAuctionNr() {
    return auctionNr;
  }

  public int getPassengerVehiclesQuantity() {
    return passengerVehiclesQuantity;
  }
}
