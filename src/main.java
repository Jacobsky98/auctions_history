//package com.codetriage.scraper;

public class main {
    public static void main(String [] args){
        System.out.println("test");
        AuctionCalendar calendar = new AuctionCalendar();
        calendar.find_showrooms_alcopaauctionfr();
        AlcopaAuctionFr alp = new AlcopaAuctionFr("https://www.alcopa-auction.fr/en/auction-room/rennes/2031");
        alp.store_vehicles_data("https://www.alcopa-auction.fr/en/voiture-used/peugeot/407-1-6-hdi-16v-110ch-fap-blue-lion-signature-206067");
        System.out.println(calendar);

    }
}
