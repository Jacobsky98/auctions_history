//package com.codetriage.scraper;

public class main {
    public static void main(String [] args){
        System.out.println("test");
        AuctionCalendar calendar = new AuctionCalendar();
        calendar.findAuctions();
        System.out.println(calendar);
    }
}
