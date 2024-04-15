package com.example.user.shopping;

public class Item {

    int itemID; // 물품 고유 번호
    String itemBarcode; // 물품 바코드
    String itemName; // 물품명
    String itemAddDate; // 물품 등록 날짜
    String itemUpdateDate; // 물품 변경 날짜
    int itemPrice; // 물품 가격
    int itemAmount; // 물품 수량
    String list; // 리스트별

    int foodID; // 식품 고유 번호
    //String itemName; // 물품명
    String foodKind; // 식품별 종류
    String foodRace; // 식품 특성
    String foodSeason; // 계절(제철)

    int foodRival; // 등록된 인원 갯수
    int itemNumber; // 해당 사용자의 등록한 물건 가지 수

    String itemBuyOrYet; // 구입 여부

    int listID; // 카테고리별 고유 번호
    // String list; // 리스트별

    int itemBuyYetPrice; // YET 또는 BUY로 등록된 물품 가격
    int itemBuyYetNumber; // YET 또는 BUY로 등록된 물품 가지수

    int listImage; // 리스트 이미지

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemAddDate() {
        return itemAddDate;
    }

    public void setItemAddDate(String itemAddDate) {
        this.itemAddDate = itemAddDate;
    }

    public String getItemUpdateDate() {
        return itemUpdateDate;
    }

    public void setItemUpdateDate(String itemUpdateDate) {
        this.itemUpdateDate = itemUpdateDate;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public String getFoodKind() {
        return foodKind;
    }

    public void setFoodKind(String foodKind) {
        this.foodKind = foodKind;
    }

    public String getFoodRace() {
        return foodRace;
    }

    public void setFoodRace(String foodRace) {
        this.foodRace = foodRace;
    }

    public String getFoodSeason() {
        return foodSeason;
    }

    public void setFoodSeason(String foodSeason) {
        this.foodSeason = foodSeason;
    }

    public Item(int foodID, String foodKind, String foodRace, String foodSeason,  String itemName, String itemBarcode) {
        this.foodID = foodID;
        this.foodKind = foodKind;
        this.foodRace = foodRace;
        this.foodSeason = foodSeason;
        this.itemName = itemName;
        this.itemBarcode = itemBarcode;
    } // item 생성자

    public Item(int foodID) {
        this.foodID = foodID;
    }

    public Item(String list, int itemNumber) {
        this.list = list;
        this.itemNumber = itemNumber;
    } // 해당 사용자가 등록한 물품 가지 수의 생성자


    public String getItemBuyOrYet() {
        return itemBuyOrYet;
    }

    public void setItemBuyOrYet(String itemBuyOrYet) {
        this.itemBuyOrYet = itemBuyOrYet;
    }

    public Item(int itemID, String itemBarcode, String itemName, String itemAddDate, String itemUpdateDate, String list, int itemPrice, int itemAmount, String itemBuyOrYet) {
        this.itemID = itemID;
        this.itemBarcode = itemBarcode;
        this.itemName = itemName;
        this.itemAddDate = itemAddDate;
        this.itemUpdateDate = itemUpdateDate;
        this.list = list;
        this.itemPrice = itemPrice;
        this.itemAmount = itemAmount;
        this.itemBuyOrYet = itemBuyOrYet;
    } // management, Search 생성자


    public int getFoodRival() {
        return foodRival;
    }

    public void setFoodRival(int foodRival) {
        this.foodRival = foodRival;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public int getItemBuyYetPrice() {
        return itemBuyYetPrice;
    }

    public void setItemBuyYetPrice(int itemBuyYetPrice) {
        this.itemBuyYetPrice = itemBuyYetPrice;
    }

    public int getItemBuyYetNumber() {
        return itemBuyYetNumber;
    }

    public void setItemBuyYetNumber(int itemBuyYetNumber) {
        this.itemBuyYetNumber = itemBuyYetNumber;
    }

    public int getListImage() {
        return listImage;
    }

    public void setListImage(int kindImage) {
        this.listImage = listImage;
    }

    public Item(int listID, String list, int listImage, int itemBuyYetPrice, int itemBuyYetNumber) {
        this.listID = listID;
        this.list = list;
        this.listImage = listImage;
        this.itemBuyYetPrice = itemBuyYetPrice;
        this.itemBuyYetNumber = itemBuyYetNumber;
    } // list 생성자
}
