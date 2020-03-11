import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class Vehicle {
  private String make;
  private String model;
  private String version;
  private boolean warranty = false;
  private String registration;
  private String fuelType;
  private Calendar firstProduced;
  private int mileage = 0;
  private String vin;
  private String color;
  private boolean recoverableVAT = false;
  private String type;
  private String storageLocation;
  private int co2 = 0;
  private boolean manualGearbox = true;
  private int lot = 0;

  private ArrayList<String> options = new ArrayList<>();
  private ArrayList<String> comments = new ArrayList<>();
  private ArrayList<String> images = new ArrayList<>();
  private ArrayList<String> imagesDamaged = new ArrayList<>();

  private String auctionURL;
  private String technicalInspectionURL;
  private String historicVehicleURL;

  public Vehicle(String auctionURL) {
    this.auctionURL = auctionURL;
    init();
  }

  public final void init() {
    try {
      Document doc = Jsoup.connect(auctionURL).get();
      captureDesciption(doc);
      captureImages(doc);
    } catch (IOException e){
      e.printStackTrace();
    }
  }

  private void captureDesciption(Document doc) {
    try{
      // get lot
      Node eltsLot = doc.getElementsByClass("h4 mb-3").first().childNode(0);
      lot = Integer.parseInt(eltsLot.toString().substring(eltsLot.toString().indexOf("Lot ")+4, eltsLot.toString().lastIndexOf(" ")));

      Elements eltsDesc = doc.getElementsByClass("pl-2 pr-5 py-1 text-nowrap");
      Elements eltsData = doc.getElementsByClass("px-1 py-1");

      Node desc = eltsDesc.first();
      Node data = eltsData.first();
      for(int i = 0; i < eltsDesc.size(); i++){
        String descName = desc.childNode(0).toString();
        String dataDesc = data.childNode(0).toString();
        switch (descName){
          case "Make": make = dataDesc; break;
          case "Model": model = dataDesc; break;
          case "Finish": version = dataDesc; break;
          case "Warranty":
            if(dataDesc.equals("YES"))
              warranty = true;
            else warranty = false;
            break;
          case "Registration": registration = dataDesc; break;
          case "Fuel type": fuelType = dataDesc; break;
          case "First produced":
            firstProduced = Calendar.getInstance();
            firstProduced.set(Calendar.HOUR_OF_DAY, 0);
            firstProduced.set(Calendar.MINUTE, 0);
            firstProduced.set(Calendar.SECOND, 0);
            firstProduced.set(Calendar.MILLISECOND, 0);
            firstProduced.set(Integer.parseInt(dataDesc.substring(dataDesc.lastIndexOf("/")+1)), Integer.parseInt(dataDesc.substring(dataDesc.indexOf("/")+1, dataDesc.lastIndexOf("/")))-1, Integer.parseInt(dataDesc.substring(0, dataDesc.indexOf("/"))));
            break;
          case "Mileage": mileage = Integer.parseInt(dataDesc.substring(0, dataDesc.indexOf("KM")-1).replace(" ", "")); break;
          case "Serial number": vin = dataDesc; break;
          case "Colour": color = dataDesc; break;
          case "Recoverable VAT":
            if(dataDesc.equals("YES"))
              recoverableVAT = true;
            else recoverableVAT = false;
            break;
          case "Type": type = dataDesc; break;
          case "Storage location": storageLocation = dataDesc; break;
          case "CO2": co2 = Integer.parseInt(dataDesc); break;
          case "Gearbox":
            if(dataDesc.equals("YES"))
              manualGearbox = true;
            else manualGearbox = false;
            break;
          default: break;
        }
        desc = eltsDesc.get(i);
        data = eltsData.get(i);
      }

    } catch (ArithmeticException e) {
      e.printStackTrace();
    } catch (StringIndexOutOfBoundsException e) {
      e.printStackTrace();
    }
  }

  private void captureImages(Document doc) {
    try{
      Elements elts = doc.select("a[data-gallery]");
      for(int i = 1; i < elts.size(); i++){
        images.add(elts.get(i).attr("href"));
      }
      // trzeba dodać sciąanie linków do jpeg uszkodzonych, &quot;http:\/\/archives-photos.alcopa-auction.fr\/photos\/AT\/AT590RT\/damages\/1582701045.jpeg&quot;},
      int sdadascascasc =214;



    }  catch (ArithmeticException e) {
      e.printStackTrace();
    } catch (StringIndexOutOfBoundsException e) {
      e.printStackTrace();
    }
  }

  public String toString() {
    String toReturn = "";
    toReturn += "Make: " + make + "\n";
    toReturn += "Model: " + model + "\n";
    toReturn += "Version: " + version + "\n";
    toReturn += "Lot: " + lot + "\n";
    toReturn += "Warranty: " + yesOrNo(warranty) + "\n";
    toReturn += "Registration: " + registration + "\n";
    toReturn += "Fuel:" + fuelType + "\n";
    toReturn += "First produced: " + firstProduced + "\n";
    toReturn += "Mileage: " + mileage + "\n";
    toReturn += "VIN: " + vin + "\n";
    toReturn += "Color: " + color + "\n";
    toReturn += "Recoverable VAT: " + yesOrNo(recoverableVAT) + "\n";
    toReturn += "Type: " + type + "\n";
    toReturn += "Storage location: " + storageLocation + "\n";
    toReturn += "CO2: " + co2 + "\n";
    toReturn += "Manual gearbox: " + yesOrNo(manualGearbox) + "\n";
    return toReturn;
  }

  public String getAuctionURL() { return auctionURL; }
  public int getLot() { return lot; }
  private String yesOrNo(boolean tf) { if(tf) return "Yes"; return "No";}

}
