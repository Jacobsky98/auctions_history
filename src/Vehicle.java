import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;

public class Vehicle {
  private static String basePath;
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

  private String showRoomUrl;
  private String technicalInspectionURL;
  private String historicVehicleURL;

  public Vehicle(String showRoomUrl) {
    this.showRoomUrl = showRoomUrl;
    this.basePath = String.join(File.separator, "I:", "perfectvin");
  }

  private void save_to_file(String imageURL){
    save_to_file(imageURL, "");
  }

  private void save_to_file(String imageURL, String additionalInfo){
    String path = String.join(File.separator,  basePath, make, model, vin, "");

    if (Files.notExists(Paths.get(path))) {
      File file = new File(path);
      boolean createdPath = file.mkdirs();
      if(createdPath)
        System.out.println("Directory created");
    }
    try {
      String fileName = imageURL.substring(imageURL.lastIndexOf("/") + 1);
      fileName = String.join(additionalInfo, fileName.substring(0, fileName.lastIndexOf(".")), fileName.substring(fileName.lastIndexOf(".")));
      path = String.join(File.separator, path, fileName);
      path = path.replace(File.separator+File.separator, File.separator);
      if(Files.notExists(Paths.get(path))) {
        //open the stream from URL
        URL urlImage = new URL(imageURL);
        InputStream in = urlImage.openStream();

        byte[] buffer = new byte[4096];
        OutputStream os = new FileOutputStream(path);

        //write bytes to the output stream
        int n = -1;
        while ((n = in.read(buffer)) != -1) {
          os.write(buffer, 0, n);
        }
        os.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void save_all_images_to_files(){
    for(String i : images){
      save_to_file(i);
    }
    for(String i : imagesDamaged){
      save_to_file(i);
    }
  }

  public void save_all_other_documents(){
    save_to_file(technicalInspectionURL, "hist");
    save_to_file(historicVehicleURL, "tech");
  }

  public void addOption(String optionName){
    options.add(optionName.toLowerCase());
  }

  public void addComment(String comment){
    comments.add(comment.toLowerCase());
  }
  public void addImage(String imageUrl){
    images.add(imageUrl);
  }

  public void addImageDamaged(String imageUrl){
    imagesDamaged.add(imageUrl);
  }

  public void setMake(String make) {
    this.make = make.toLowerCase();
  }

  public void setModel(String model) {
    this.model = model.toLowerCase();
  }

  public void setVersion(String version) {
    this.version = version.toLowerCase();
  }

  public void setWarranty(boolean warranty) {
    this.warranty = warranty;
  }

  public void setRegistration(String registration) {
    this.registration = registration.toUpperCase();
  }

  public void setFuelType(String fuelType) {
    this.fuelType = fuelType.toLowerCase();
  }

  public void setFirstProduced(Calendar firstProduced) {
    this.firstProduced = firstProduced;
  }

  public void setMileage(int mileage) {
    this.mileage = mileage;
  }

  public void setVin(String vin) {
    this.vin = vin.toUpperCase();
  }

  public void setColor(String color) {
    this.color = color.toLowerCase();
  }

  public void setRecoverableVAT(boolean recoverableVAT) {
    this.recoverableVAT = recoverableVAT;
  }

  public void setType(String type) {
    this.type = type.toLowerCase();
  }

  public void setStorageLocation(String storageLocation) {
    this.storageLocation = storageLocation.toLowerCase();
  }

  public void setCo2(int co2) {
    this.co2 = co2;
  }

  public void setManualGearbox(boolean manualGearbox) {
    this.manualGearbox = manualGearbox;
  }

  public void setLot(int lot) {
    this.lot = lot;
  }

  public void setTechnicalInspectionURL(String technicalInspectionURL) {
    this.technicalInspectionURL = technicalInspectionURL;
  }

  public void setHistoricVehicleURL(String historicVehicleURL) {
    this.historicVehicleURL = historicVehicleURL;
  }

  public String getColor() {
    return color;
  }

  public String getMake() {
    return make;
  }

  public String getModel() {
    return model;
  }

  public String getVersion() {
    return version;
  }

  public boolean isWarranty() {
    return warranty;
  }

  public String getRegistration() {
    return registration;
  }

  public String getFuelType() {
    return fuelType;
  }

  public Calendar getFirstProduced() {
    return firstProduced;
  }

  public int getMileage() {
    return mileage;
  }

  public String getVin() {
    return vin;
  }

  public boolean isRecoverableVAT() {
    return recoverableVAT;
  }

  public String getType() {
    return type;
  }

  public String getStorageLocation() {
    return storageLocation;
  }

  public int getCo2() {
    return co2;
  }

  public boolean isManualGearbox() {
    return manualGearbox;
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

  public String getShowRoomUrl() { return showRoomUrl; }
  public int getLot() { return lot; }
  private String yesOrNo(boolean tf) { if(tf) return "Yes"; return "No";}

}
