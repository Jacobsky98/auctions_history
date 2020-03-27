import java.util.ArrayList;
import java.util.Calendar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import java.io.IOException;

public class AlcopaAuctionFr extends Auction{
  private String[] monthsArray = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
  private int utilityVehiclesQuantity = 0;
  private int brokenVehiclesQuantity = 0;
  private int gearMotorcyclesQuantity = 0;
  private int mopedMotorcyclesQuantity = 0;
  private Calendar calendarBegin = Calendar.getInstance();
  private Calendar calendarEnd = Calendar.getInstance();


  public AlcopaAuctionFr(String showRoomUrl) {
    this.showRoomUrl = showRoomUrl;
    if(showRoomUrl.indexOf("?site=internet") > 0)
      auctionNr = Integer.parseInt(showRoomUrl.substring(showRoomUrl.lastIndexOf("/")+1, showRoomUrl.indexOf("?site=internet")-1));
    else{
      auctionNr = Integer.parseInt(showRoomUrl.substring(showRoomUrl.lastIndexOf("/")+1));
//      init();
    }
  }

  public final void init() {
    try{
      Document doc = Jsoup.connect("https://www.alcopa-auction.fr" + showRoomUrl).get();
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
      String date = "";
      if(elDate.size() > 0)
        date = elDate.get(0).nextSibling().toString();
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
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  final void captureVehicleDescription(Document doc, Vehicle vehicle) {
    try{
      // get lot
      Node eltsLot = doc.getElementsByClass("h4 mb-3").first().childNode(0);
      vehicle.setLot(Integer.parseInt(eltsLot.toString().substring(eltsLot.toString().indexOf("Lot ")+4, eltsLot.toString().lastIndexOf(" "))));

      Elements eltsDesc = doc.getElementsByClass("pl-2 pr-5 py-1 text-nowrap");
      Elements eltsData = doc.getElementsByClass("px-1 py-1");

      Node desc = eltsDesc.first();
      Node data = eltsData.first();
      for(int i = 0; i < eltsDesc.size(); i++){
        String descName = desc.childNode(0).toString();
        String dataDesc = data.childNode(0).toString();
        switch (descName){
          case "Make": vehicle.setMake(dataDesc); break;
          case "Model": vehicle.setModel(dataDesc); break;
          case "Finish": vehicle.setVersion(dataDesc); break;
          case "Warranty":
            if(dataDesc.equals("YES"))
              vehicle.setWarranty(true);
            else vehicle.setWarranty(false);
            break;
          case "Registration": vehicle.setRegistration(dataDesc); break;
          case "Fuel type": vehicle.setFuelType(dataDesc); break;
          case "First produced":
            Calendar firstProduced = Calendar.getInstance();
            firstProduced.set(Calendar.HOUR_OF_DAY, 0);
            firstProduced.set(Calendar.MINUTE, 0);
            firstProduced.set(Calendar.SECOND, 0);
            firstProduced.set(Calendar.MILLISECOND, 0);
            firstProduced.set(Integer.parseInt(dataDesc.substring(dataDesc.lastIndexOf("/")+1)), Integer.parseInt(dataDesc.substring(dataDesc.indexOf("/")+1, dataDesc.lastIndexOf("/")))-1, Integer.parseInt(dataDesc.substring(0, dataDesc.indexOf("/"))));
            vehicle.setFirstProduced(firstProduced);
            break;
          case "Mileage": vehicle.setMileage(Integer.parseInt(dataDesc.substring(0, dataDesc.indexOf("KM")-1).replace(" ", ""))); break;
          case "Serial number": vehicle.setVin(dataDesc); break;
          case "Colour": vehicle.setColor(dataDesc); break;
          case "Recoverable VAT":
            if(dataDesc.equals("YES"))
              vehicle.setRecoverableVAT(true);
            else vehicle.setRecoverableVAT(false);
            break;
          case "Type": vehicle.setType(dataDesc); break;
          case "Storage location": vehicle.setStorageLocation(dataDesc); break;
          case "CO2": vehicle.setCo2(Integer.parseInt(dataDesc)); break;
          case "Gearbox":
            if(dataDesc.equals("YES"))
              vehicle.setManualGearbox(true);
            else vehicle.setManualGearbox(false);
            break;
          default: break;
        }
        desc = eltsDesc.get(i);
        data = eltsData.get(i);
      }

      Elements inspections = doc.getElementsByClass("btn btn-primary no-print mb-2"); // historic and techincal inspenction
      String element = inspections.first().attr("href");
      vehicle.setTechnicalInspectionURL(element);
      element = inspections.first().nextElementSibling().attr("href");
      vehicle.setHistoricVehicleURL(element);

      Element options = doc.getElementsByClass("w-100 table table-sm table-striped").get(0);
      Elements rows = options.select("tr");

      for (int i = 0; i < rows.size(); i++) { //first row is the col names so skip it.
        Element row = rows.get(i);
        Elements cols = row.select("td");
        for(int j = 0; j < cols.size(); j++){
          System.out.println(String.valueOf(cols.get(j)));
          vehicle.addOption(String.valueOf(cols.get(j)));
        }

      }

      Elements comments = doc.getElementsByClass("mb-4");
      String comments_str = "";
      for(int i = 0; i < comments.size(); i++){
        boolean foundComments = false;
        for(int j = 0; j < comments.get(i).childNodeSize(); j++){
          Node child = comments.get(i).childNode(0).parentNode().childNode(j);
          if(foundComments){
            comments_str += child.toString();
          }
          if(child.toString().indexOf("<h3 class=\"mb-3\">Comments</h3>") > 0){
            foundComments = true;
            i = comments.size();
          }
        }

      }

      System.out.println(comments_str);



    } catch (ArithmeticException e) {
      e.printStackTrace();
    } catch (StringIndexOutOfBoundsException e) {
      e.printStackTrace();
    }
  }



  @Override
  final void captureVehicleImages(Document doc, Vehicle vehicle) {
    try{
      Elements elts = doc.select("a[data-gallery]");
      for(int i = 1; i < elts.size(); i++){
        vehicle.addImage(elts.get(i).attr("href"));
      }
      // trzeba dodać sciąanie linków do jpeg uszkodzonych, &quot;http:\/\/archives-photos.alcopa-auction.fr\/photos\/AT\/AT590RT\/damages\/1582701045.jpeg&quot;},
      String damaged_str = doc.getElementsByClass("js-damage-proges damage-proges").attr("data-source");
      while(damaged_str.indexOf("\"vehiculephoto_url\":\"") > 0){
        damaged_str = damaged_str.substring(damaged_str.indexOf("\"vehiculephoto_url\":\"")+"\"vehiculephoto_url\":\"".length());
        String image_damaged = damaged_str.substring(0, damaged_str.indexOf("\"}"));
        image_damaged = image_damaged.replace("\\", "");
        vehicle.addImageDamaged(image_damaged);
      }
    }  catch (ArithmeticException e) {
      e.printStackTrace();
    } catch (StringIndexOutOfBoundsException e) {
      e.printStackTrace();
    }
  }

  public void store_vehicles_data(String url) {
    try {
      Vehicle vehicle = new Vehicle(url);
      Document doc = Jsoup.connect(url).get();
      captureVehicleDescription(doc, vehicle);
      captureVehicleImages(doc, vehicle);
//      vehicle.save_all_images_to_files();
      vehicle.save_all_other_documents();
      vehicles.add(vehicle);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }







  public final void getVehiclesData() {
    ArrayList<String> vehiclesType = new ArrayList<>();
    try{
      Document doc = Jsoup.connect("https://www.alcopa-auction.fr" + showRoomUrl).get();
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

// it might not work if it is an internet auction
  public String getAuctionUrl() {
    return "http://live.alcopa-auction.fr/" + auctionNr;
  }

  public int getUtilityVehiclesQuantity() {
    return utilityVehiclesQuantity;
  }

  public int getBrokenVehiclesQuantity() {
    return brokenVehiclesQuantity;
  }

  public int getAllVehiclesQuantity() {
    return passengerVehiclesQuantity+utilityVehiclesQuantity+brokenVehiclesQuantity;
  }

  public String toString() {
    String toReturn = "";
    toReturn += "City: " + city;
    toReturn += "\tType: " + auctionType;
    toReturn += "\tnr: " + auctionNr + "\n";
    toReturn += "Begin: " + calendarBegin.getTime() + "\n";
    toReturn += "End: " + calendarEnd.getTime() + "\n";
    if(auctionType.equals("cars")) {
      toReturn += "Passenger vehicles: " + passengerVehiclesQuantity + "\n";
      toReturn += "Utility vehicles: " + utilityVehiclesQuantity + "\n";
    }else{
      toReturn += "Gears: " + gearMotorcyclesQuantity + "\n";
      toReturn += "Mopeds: " + mopedMotorcyclesQuantity + "\n";
    }
    toReturn += "Not allowed to traffic: " + brokenVehiclesQuantity + "\n";
    return toReturn;
  }



}
