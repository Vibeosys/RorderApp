package com.vibeosys.rorderapp.printutils;

import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;

import com.vibeosys.rorderapp.data.BillDetailsDTO;
import com.vibeosys.rorderapp.data.OrderDetailsDTO;

import java.util.HashMap;
import java.util.Set;

import com.epson.eposprint.*;
import com.vibeosys.rorderapp.data.RestaurantDTO;
import com.vibeosys.rorderapp.database.DbRepository;

/**
 * Created by akshay on 29-02-2016.
 */
public class PrintDataDTO {

    public static final int BILL_DINE_IN = 1;
    public static final int KOT = 2;
    public static final int BILL_TAKE_AWAY = 3;
    private PrintHeader mHeader;
    private String mFooter;
    private PrintBody mBody;
    private int type;
    private BillDetailsDTO billDetailsDTO;
    DbRepository mDbRepository;
    String mConstantFooter = "Powered by QuickServe(TM)";

    public PrintDataDTO() {
    }

    public PrintDataDTO(PrintBody mBody) {
        this.mBody = mBody;
    }

    public PrintHeader getHeader() {
        return mHeader;
    }

    public void setHeader(PrintHeader mHeader) {
        this.mHeader = mHeader;
    }

    public String getFooter() {
        return mFooter;
    }

    public void setFooter(String mFooter) {
        this.mFooter = mFooter;
    }

    public PrintBody getBody() {
        return mBody;
    }

    public void setBody(PrintBody mBody) {
        this.mBody = mBody;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public BillDetailsDTO getBillDetailsDTO() {
        return billDetailsDTO;
    }

    public void setBillDetailsDTO(BillDetailsDTO billDetailsDTO) {
        this.billDetailsDTO = billDetailsDTO;
    }

    public Builder getPrint(Builder builder, int maxNoChar, int padding, int margin) {
        if (this.type == BILL_DINE_IN)
            return getPrintBill(builder, maxNoChar, margin, padding);
        else if (this.type == BILL_TAKE_AWAY)
            return getPrintTakeBill(builder, maxNoChar, margin, padding);
        else
            return getKotPrint(builder, maxNoChar, padding, margin);
    }

    public String getKotPrint(int maxNoChar, int padding, int margin) {
        HashMap<Integer, OrderDetailsDTO> menusHashMap = this.mBody.getMenus();
        StringBuilder strPrint = new StringBuilder();
        String strLine = "";
        for (int i = 0; i < maxNoChar; i++) {
            strLine = strLine + "_";
        }
        String strMargin = "";
        for (int i = 0; i < margin; i++) {
            strMargin = strMargin + " ";
        }
        String strPadding = "";
        for (int i = 0; i < padding; i++) {
            strPadding = strPadding + " ";
        }
        strPrint.append(strMargin);
        strPrint.append(this.mHeader.getServedBy() + "\n");
        strPrint.append(strMargin);
        String tableNo = this.mHeader.getTableNo();
        strPrint.append(tableNo);
        String time = this.mHeader.getTime();
        strPrint.append(getSpaceString(tableNo.length(), maxNoChar, margin + time.length()));
        strPrint.append(time + "\n");
        strPrint.append(strPadding);
        strPrint.append(strLine);
        strPrint.append(strPadding + "\n");
        strPrint.append(strMargin);
        String strDesc = "Description";
        strPrint.append(strDesc);
        strPrint.append(getSpaceString(strDesc.length(), maxNoChar, margin + 6));
        strPrint.append("Qty\n");
        strPrint.append(strPadding);
        strPrint.append(strLine);
        strPrint.append(strPadding + "\n");
        Set<Integer> keys = menusHashMap.keySet();
        String menuList = "";
        for (Integer i : keys) {
            OrderDetailsDTO orderMenu = menusHashMap.get(i);
            String menuTitle = strMargin + orderMenu.getMenuTitle();
            int menuLength = menuTitle.length();
            String quantity = String.valueOf(orderMenu.getOrderQuantity());
            String note = orderMenu.getmNote();
            if (TextUtils.isEmpty(note)) {
                menuTitle = menuTitle + getSpaceString(menuLength, maxNoChar, margin + quantity.length()) + quantity;
            } else {
                menuTitle = menuTitle + getSpaceString(menuLength, maxNoChar, margin + quantity.length()) + quantity + "\n";
                menuTitle = menuTitle + strMargin + strPadding + note;
            }
            menuList = menuList + menuTitle + strMargin + "\n";
        }
        strPrint.append(menuList);
        strPrint.append(strPadding);
        strPrint.append(strLine);
        strPrint.append(strPadding + "\n");
        strPrint.append(strMargin);
        strPrint.append(getCentreAlign(this.mFooter, maxNoChar, margin) + "\n");
        return strPrint.toString();
    }

    public String getSpaceString(int noChar, int maxChar, int margin) {
        int space = maxChar - noChar;
        String strSpace = "";
        for (int j = 0; j < space - margin; j++) {
            strSpace = strSpace + " ";
        }
        return strSpace;
    }

    public String getCentreAlign(String str, int maxChar, int margin) {//
        String outStr;
        int space = (maxChar / 2) - (str.length() / 2);
        String strSpace = "";
        for (int j = 0; j < space; j++) {
            strSpace = strSpace + " ";
        }
        outStr = strSpace + str;
        return outStr;
    }


    public String getBillPrint(int maxNoChar, int padding, int margin) {
        HashMap<Integer, OrderDetailsDTO> menusHashMap = this.mBody.getMenus();
        StringBuilder strPrint = new StringBuilder();
        String strLine = "";
        for (int i = 0; i < maxNoChar; i++) {
            strLine = strLine + "_";
        }
        String strMargin = "";
        for (int i = 0; i < margin; i++) {
            strMargin = strMargin + " ";
        }
        String strPadding = "";
        for (int i = 0; i < padding; i++) {
            strPadding = strPadding + " ";
        }
        strPrint.append(getCentreAlign(this.mHeader.getRestaurantName(), maxNoChar, margin) + "\n");
        strPrint.append(getCentreAlign(this.mHeader.getAddress(), maxNoChar, margin) + "\n");
        strPrint.append(getCentreAlign(this.mHeader.getPhoneNumber(), maxNoChar, margin) + "\n");
        strPrint.append(strMargin);
        strPrint.append(this.mHeader.getTableNo() + "\n");
        strPrint.append(strMargin);
        strPrint.append(this.mHeader.getNumber() + "\n");
        String time = this.mHeader.getTime();
        strPrint.append(strMargin);
        strPrint.append(time + "\n");
        strPrint.append(strPadding);
        strPrint.append(strLine);
        strPrint.append(strPadding + "\n");
        strPrint.append(strMargin);
        String strDesc = "Description";
        String strQty = "Qty    Amt";
        //strPrint.append(getSpaceString(strAmout.length(),maxNoChar,margin+strAmout.length()));

        String column = strDesc + getSpaceString(strDesc.length() + strQty.length(), maxNoChar, margin) + strQty;
        strPrint.append(column + "\n");
        strPrint.append(strPadding);
        strPrint.append(strLine);
        strPrint.append(strPadding + "\n");
        Set<Integer> keys = menusHashMap.keySet();
        String menuList = "";
        for (Integer i : keys) {
            OrderDetailsDTO orderMenu = menusHashMap.get(i);
            String menuTitle = strMargin + orderMenu.getMenuTitle();
            int menuLength = menuTitle.length();
            String quantity = String.valueOf(orderMenu.getOrderQuantity()) + "\t" + String.valueOf(orderMenu.getOrderPrice());
            String space = getSpaceString(strDesc.length() + quantity.length(), maxNoChar, margin);
            menuTitle = menuTitle + space + quantity;
            //menuTitle = menuTitle + strMargin + strPadding;
            menuList = menuList + menuTitle + strMargin + "\n";
        }
        strPrint.append(menuList);
        strPrint.append(strPadding);
        strPrint.append(strLine);
        strPrint.append(strPadding + "\n");
        strPrint.append(strMargin);
        strPrint.append(rightAlign("Net Amount " + billDetailsDTO.getNetAmount() + "   ", maxNoChar) + "\n");
        strPrint.append(rightAlign("Taxes " + billDetailsDTO.getTotalTax() + "    ", maxNoChar) + "\n");
        //strPrint.append(rightAlign("Discount"+billDetailsDTO.get))
        strPrint.append(strLine + "\n");
        strPrint.append(rightAlign("Total Amount " + billDetailsDTO.getTotalPayableAmt() + "    ", maxNoChar) + "\n\n");
        strPrint.append(getCentreAlign(this.mFooter, maxNoChar, margin) + "\n");
        return strPrint.toString();
    }

    public String rightAlign(String str, int maxChar) {
        String out = "";
        out = out + getSpaceString(str.length(), maxChar, 0);
        return out + str;
    }

    public Builder getPrintBill(Builder builder, int maxChar, int margin, int padding) {

        String strLine = "";
        for (int i = 0; i < maxChar; i++) {
            strLine = strLine + "_";
        }
        String strMargin = "";
        for (int i = 0; i < margin; i++) {
            strMargin = strMargin + " ";
        }
        String strPadding = "";
        for (int i = 0; i < padding; i++) {
            strPadding = strPadding + " ";
        }
        try {
            //  builder.addPageBegin();
            //builder.addTextAlign(builder.ALIGN_CENTER);
            //builder.addTextSize(1, 1);
            //builder.addText(strMargin);
            // Here applying text style

            // builder.addImage(this.mHeader.getBmpIcon(),0,0,256,256,Builder.COLOR_1, Builder.MODE_GRAY16,Builder.HALFTONE_DITHER,1.0);
            builder.addTextSize(1, 2);
            builder.addTextStyle(Builder.PARAM_UNSPECIFIED, Builder.FALSE, Builder.TRUE, Builder.PARAM_UNSPECIFIED);
            builder.addText(getCentreAlign(this.mHeader.getRestaurantName(), maxChar, margin) + "\n");
            // builder.addTextStyle(Builder.PARAM_UNSPECIFIED,Builder.TRUE,Builder.PARAM_UNSPECIFIED,Builder.PARAM_UNSPECIFIED);
            //builder.addText(strMargin);
            // Here removing  text style
            builder.addTextStyle(Builder.PARAM_UNSPECIFIED, Builder.FALSE, Builder.FALSE, Builder.PARAM_UNSPECIFIED);

            builder.addTextSize(1, 1);
            builder.addText(strMargin);
            builder.addText(getCentreAlign(this.mHeader.getAddress(), maxChar, margin) + "\n");
            // builder.addText(strMargin);
            String restaurantPh = "Ph: ".concat(this.mHeader.getPhoneNumber());
            builder.addText(getCentreAlign(restaurantPh, maxChar, margin) + "\n");
            // builder.addText(strMargin);
            builder.addTextStyle(Builder.PARAM_UNSPECIFIED, Builder.FALSE, Builder.TRUE, Builder.PARAM_UNSPECIFIED);
            builder.addText(getCentreAlign("Tax Invoice", maxChar, margin) + "\n");
            builder.addTextStyle(Builder.PARAM_UNSPECIFIED, Builder.FALSE, Builder.FALSE, Builder.PARAM_UNSPECIFIED);
            builder.addText(strPadding);
            builder.addText(strLine + "\n");
            builder.addText(getCentreAlign(this.mHeader.getBillType(), maxChar, margin) + "\n");
            builder.addText(strMargin);
            //builder.addTextAlign(builder.ALIGN_LEFT);
            builder.addText(this.mHeader.getNumber() + "\n");
            builder.addText(strMargin);
            builder.addText(this.mHeader.getTableNo() + "\n");
            builder.addText(strMargin);
            builder.addText(this.mHeader.getTime() + "\n");
            builder.addText(strMargin);
            builder.addText(this.mHeader.getServedBy() + "\n");
            builder.addText(strPadding);
            builder.addText(strLine + "\n");
            String strDesc = "Description";
            String strQty = "Qty    Amount";
            //strPrint.append(getSpaceString(strAmout.length(),maxNoChar,margin+strAmout.length()));
            String column = strDesc + getSpaceString(strDesc.length() + strQty.length() , maxChar, margin) + strQty;

            builder.addText(strMargin);

            builder.addTextStyle(Builder.PARAM_UNSPECIFIED, Builder.FALSE, Builder.TRUE, Builder.PARAM_UNSPECIFIED);
            builder.addText(column + "\n");
            builder.addTextStyle(Builder.PARAM_UNSPECIFIED, Builder.FALSE, Builder.FALSE, Builder.PARAM_UNSPECIFIED);

            builder.addText(strPadding);
            builder.addText(strLine + "\n");
            HashMap<Integer, OrderDetailsDTO> menusHashMap = this.mBody.getMenus();
            Set<Integer> keys = menusHashMap.keySet();
            String menuList = "";
            for (Integer i : keys) {
                OrderDetailsDTO orderMenu = menusHashMap.get(i);
                String menuTitle = orderMenu.getMenuTitle();
                int menuLength = menuTitle.length();
                String spaceQtyAmt = getSpaceString(String.format("%.2f", orderMenu.getOrderPrice()).length(), 11, 0);
                String quantity = String.valueOf(orderMenu.getOrderQuantity()) + spaceQtyAmt + String.format("%.2f", orderMenu.getOrderPrice());
                String space = getSpaceString(menuTitle.length() + quantity.length(), maxChar, margin);
                menuTitle = menuTitle + space + quantity;
                //menuTitle = menuTitle + strMargin + strPadding;
                builder.addText(strMargin);
                builder.addText(menuTitle + "\n");
                menuList = menuList + menuTitle + "\n";
            }
            //builder.addText(menuList);
            builder.addText(strPadding);
            builder.addText(strLine + "\n");
            //String net="Net Amount";
            String netSpace = getSpaceString(String.format("%.2f", billDetailsDTO.getNetAmount()).length(), 11, 0);
            builder.addText(rightAlign("Net Amount" + netSpace + String.format("%.2f", billDetailsDTO.getNetAmount()) + "", maxChar) + "\n");
            String taxSpace = getSpaceString(String.format("%.2f", billDetailsDTO.getTotalTax()).length(), 11, 0);
            builder.addText(rightAlign("Taxes" + taxSpace + String.format("%.2f", billDetailsDTO.getTotalTax()) + "", maxChar) + "\n");
            String discSpace = getSpaceString(String.format("%.2f", billDetailsDTO.getDiscount()).length(), 11, 0);
            builder.addText(rightAlign("Discount" + discSpace + String.format("%.2f", billDetailsDTO.getDiscount()) + "", maxChar) + "\n");
            //strPrint.append(rightAlign("Discount"+billDetailsDTO.get))
            builder.addText(strPadding);
            builder.addText(strLine + "\n");
            String totalSpace = getSpaceString(String.format("%.2f", billDetailsDTO.getTotalPayableAmt()).length(), 11, 0);

            builder.addTextStyle(Builder.PARAM_UNSPECIFIED, Builder.FALSE, Builder.TRUE, Builder.PARAM_UNSPECIFIED);
            builder.addText(rightAlign("Total Amount" + totalSpace + String.format("%.2f", billDetailsDTO.getTotalPayableAmt()) + "", maxChar) + "\n");
            builder.addTextStyle(Builder.PARAM_UNSPECIFIED, Builder.FALSE, Builder.FALSE, Builder.PARAM_UNSPECIFIED);

            builder.addText(strPadding);
            builder.addText(strLine);
/*            builder.addText(""+mFooterPrint);
            builder.addText(getCentreAlign(this.mFooter, maxChar, margin) + "\n");*/
            builder.addTextAlign(Builder.ALIGN_CENTER);
            builder.addText("" + this.mFooter + "\n");
            builder.addText(strLine);
            builder.addText(getCentreAlign(mConstantFooter, maxChar, margin) + "\n");
            String testingDataFor = this.mHeader.getNumber();
            String[] parts = testingDataFor.split(":");
            String part1 = parts[0];
            String part2 = parts[1];
           /* builder.addText(strMargin);
            builder.addText(strMargin);
            builder.addText(strMargin);*/
            builder.addTextAlign(Builder.ALIGN_CENTER);
            builder.addBarcode(part2, Builder.BARCODE_CODE39, Builder.HRI_BELOW, Builder.FONT_A, Builder.PARAM_UNSPECIFIED, 80);
            //   builder.addPageEnd();
            //   builder.addPageEnd();
        } catch (EposException e) {
            e.printStackTrace();
        }

        return builder;
    }

    public Builder getKotPrint(Builder builder, int maxNoChar, int padding, int margin) {
        HashMap<Integer, OrderDetailsDTO> menusHashMap = this.mBody.getMenus();
        StringBuilder strPrint = new StringBuilder();
        String strLine = "";
        for (int i = 0; i < maxNoChar; i++) {
            strLine = strLine + "_";
        }
        String strMargin = "";
        for (int i = 0; i < margin; i++) {
            strMargin = strMargin + " ";
        }
        String strPadding = "";
        for (int i = 0; i < padding; i++) {
            strPadding = strPadding + " ";
        }
        try {
            //builder.addTextSize(1, 1);
            builder.addText(strMargin);
            builder.addText(this.mHeader.getServedBy() + "\n");
            builder.addText(strMargin);
            String tableNo = this.mHeader.getTableNo();
            builder.addText(tableNo);
            String time = this.mHeader.getTime();
            builder.addText(getSpaceString(tableNo.length(), maxNoChar, margin + time.length()));
            builder.addText(time + "\n");
            builder.addText(strPadding);
            builder.addText(strLine);
            //  builder.addText(strPadding + "\n");
            builder.addText(strMargin);
            String strDesc = "Description";
            builder.addText(strDesc);
            builder.addText(getSpaceString(strDesc.length(), maxNoChar, margin + 6));
            builder.addText("Qty" + "\n");
            builder.addText(strPadding);
            builder.addText(strLine);
            builder.addText(strPadding + "\n");
            Set<Integer> keys = menusHashMap.keySet();
            String menuList = "";
            for (Integer i : keys) {
                OrderDetailsDTO orderMenu = menusHashMap.get(i);
                String menuTitle = strMargin + orderMenu.getMenuTitle();
                int menuLength = menuTitle.length();
                String quantity = String.valueOf(orderMenu.getOrderQuantity());
                String note = orderMenu.getmNote();
                if (TextUtils.isEmpty(note)) {
                    menuTitle = menuTitle + getSpaceString(menuLength, maxNoChar, margin + quantity.length()) + quantity;
                } else {
                    menuTitle = menuTitle + getSpaceString(menuLength, maxNoChar, margin + quantity.length()) + quantity + "\n";
                    menuTitle = menuTitle + strMargin + strPadding + note;
                }
                menuList = menuList + menuTitle + strMargin + "\n";
            }
            builder.addText(menuList);
            builder.addText(strPadding);
            builder.addText(strLine);
            builder.addText(strPadding + "\n");
            builder.addText(strMargin);
            builder.addText(getCentreAlign(mConstantFooter, maxNoChar, margin) + "\n");
        } catch (EposException e) {
            e.printStackTrace();
        }

        return builder;
    }

    public Builder getPrintTakeBill(Builder builder, int maxChar, int margin, int padding) {

        String strLine = "";
        for (int i = 0; i < maxChar; i++) {
            strLine = strLine + "_";
        }
        String strMargin = "";
        for (int i = 0; i < margin; i++) {
            strMargin = strMargin + " ";
        }
        String strPadding = "";
        for (int i = 0; i < padding; i++) {
            strPadding = strPadding + " ";
        }
        try {
            //builder.addPageBegin();
            //builder.addTextAlign(builder.ALIGN_CENTER);
            //builder.addTextSize(1, 1);
            //builder.addText(strMargin);

            /*Here Apply text size and font size*/
            builder.addTextSize(1, 2);
            builder.addTextStyle(Builder.PARAM_UNSPECIFIED, Builder.FALSE, Builder.TRUE, Builder.PARAM_UNSPECIFIED);
            builder.addText(getCentreAlign(this.mHeader.getRestaurantName(), maxChar, margin) + "\n");
            builder.addTextStyle(Builder.PARAM_UNSPECIFIED, Builder.FALSE, Builder.FALSE, Builder.PARAM_UNSPECIFIED);
            /*Here removing text size and font size*/
            builder.addTextSize(1, 1);

            builder.addText(strMargin);
            builder.addText(getCentreAlign(this.mHeader.getAddress(), maxChar, margin) + "\n");
            // builder.addText(strMargin);
            String restaurantPh = "Ph: ".concat(this.mHeader.getPhoneNumber());
            builder.addText(getCentreAlign(restaurantPh, maxChar, margin) + "\n");
            // builder.addText(strMargin);
            builder.addTextStyle(Builder.PARAM_UNSPECIFIED, Builder.FALSE, Builder.TRUE, Builder.PARAM_UNSPECIFIED);
            builder.addText(getCentreAlign("Tax Invoice", maxChar, margin) + "\n");
            builder.addTextStyle(Builder.PARAM_UNSPECIFIED, Builder.FALSE, Builder.FALSE, Builder.PARAM_UNSPECIFIED);
            builder.addText(strPadding);
            builder.addText(strLine + "\n");


            builder.addText(getCentreAlign(this.mHeader.getBillType(), maxChar, margin) + "\n");

            builder.addText(strMargin);
            builder.addText(this.mHeader.getCustName() + "\n");
            builder.addText(strMargin);
            if (this.mHeader.getCustAddress().length() >= 41) {
                String testFirst32 = this.mHeader.getCustAddress().substring(0, 41);
                String test = "      ";
                String testSecond32 = this.mHeader.getCustAddress().substring(41);
                String FinalTesting = test.concat(testSecond32);
                builder.addText("" + testFirst32 + "\n");
                builder.addText("" + FinalTesting);
            } else {
                builder.addText("" + this.mHeader.getCustAddress().toString());
            }

            //  builder.addText(this.mHeader.getCustAddress()+"\n");
            builder.addText(strMargin);
            builder.addText(this.mHeader.getPhNo() + "\n");
            builder.addText(strMargin);
            //builder.addTextAlign(builder.ALIGN_LEFT);
            builder.addText(this.mHeader.getNumber() + "\n");
            builder.addText(strMargin);
            builder.addText(this.mHeader.getTableNo() + "\n");
            builder.addText(strMargin);
            builder.addText(this.mHeader.getTime() + "\n");
           /* builder.addText(strMargin);
            builder.addText(this.mHeader.getServedBy() + "\n");*/
            builder.addText(strPadding);
            builder.addText(strLine + "\n");
            String strDesc = "Description";
            String strQty = "Qty    Amount";
            //strPrint.append(getSpaceString(strAmout.length(),maxNoChar,margin+strAmout.length()));
            String column = strDesc + getSpaceString(strDesc.length() + strQty.length() , maxChar, margin) + strQty;
            builder.addText(strMargin);
            builder.addTextStyle(Builder.PARAM_UNSPECIFIED, Builder.FALSE, Builder.TRUE, Builder.PARAM_UNSPECIFIED);
            builder.addText(column + "\n");
            builder.addTextStyle(Builder.PARAM_UNSPECIFIED, Builder.FALSE, Builder.FALSE, Builder.PARAM_UNSPECIFIED);
            builder.addText(strPadding);
            builder.addText(strLine + "\n");
            HashMap<Integer, OrderDetailsDTO> menusHashMap = this.mBody.getMenus();
            Set<Integer> keys = menusHashMap.keySet();
            String menuList = "";
            for (Integer i : keys) {
                OrderDetailsDTO orderMenu = menusHashMap.get(i);
                String menuTitle = orderMenu.getMenuTitle();
                int menuLength = menuTitle.length();
                String spaceQtyAmt = getSpaceString(String.format("%.2f", orderMenu.getOrderPrice()).length(), 11, 0);
                String quantity = String.valueOf(orderMenu.getOrderQuantity()) + spaceQtyAmt + String.format("%.2f", orderMenu.getOrderPrice());
                String space = getSpaceString(menuTitle.length() + quantity.length(), maxChar, margin);
                menuTitle = menuTitle + space + quantity;
                //menuTitle = menuTitle + strMargin + strPadding;
                builder.addText(strMargin);
                builder.addText(menuTitle + "\n");
                menuList = menuList + menuTitle + "\n";
            }
            //builder.addText(menuList);
            builder.addText(strPadding);
            builder.addText(strLine + "\n");
            //String net="Net Amount";
            String netSpace = getSpaceString(String.format("%.2f", billDetailsDTO.getNetAmount()).length(), 11, 0);
            builder.addText(rightAlign("Net Amount" + netSpace + String.format("%.2f", billDetailsDTO.getNetAmount()) + "", maxChar) + "\n");
            String taxSpace = getSpaceString(String.format("%.2f", billDetailsDTO.getTotalTax()).length(), 11, 0);
            builder.addText(rightAlign("Taxes" + taxSpace + String.format("%.2f", billDetailsDTO.getTotalTax()) + "", maxChar) + "\n");
            String discSpace = getSpaceString(String.format("%.2f", billDetailsDTO.getDiscount()).length(), 11, 0);
            builder.addText(rightAlign("Discount" + discSpace + String.format("%.2f", billDetailsDTO.getDiscount()) + "", maxChar) + "\n");
            String deliverySpace = getSpaceString(String.format("%.2f", billDetailsDTO.getDeliveryChr()).length(), 11, 0);
            builder.addText(rightAlign("Delivery Charges" + deliverySpace + String.format("%.2f", billDetailsDTO.getDeliveryChr()) + "", maxChar) + "\n");
            //strPrint.append(rightAlign("Discount"+billDetailsDTO.get))
            builder.addText(strPadding);
            builder.addText(strLine + "\n");
            String totalSpace = getSpaceString(String.format("%.2f", billDetailsDTO.getTotalPayableAmt()).length(), 11, 0);
            builder.addTextStyle(Builder.PARAM_UNSPECIFIED, Builder.FALSE, Builder.TRUE, Builder.PARAM_UNSPECIFIED);
            builder.addText(rightAlign("Total Amount" + totalSpace + String.format("%.2f", billDetailsDTO.getTotalPayableAmt()) + "", maxChar) + "\n");
            builder.addTextStyle(Builder.PARAM_UNSPECIFIED, Builder.FALSE, Builder.FALSE, Builder.PARAM_UNSPECIFIED);
            builder.addText(strPadding);
            builder.addText(strLine);
            builder.addTextAlign(Builder.ALIGN_CENTER);
            builder.addText("" + this.mFooter + "\n");
            builder.addText(strLine);
            String testingDataFor = this.mHeader.getNumber();

            builder.addText(getCentreAlign(mConstantFooter, maxChar, margin) + "\n");
            String[] parts = testingDataFor.split(":");
            String part1 = parts[0];
            String part2 = parts[1];
            builder.addTextAlign(Builder.ALIGN_CENTER);
            builder.addBarcode(part2, Builder.BARCODE_CODE39, Builder.HRI_BELOW, Builder.FONT_A, Builder.PARAM_UNSPECIFIED, 80);
          //  builder.addText(strLine);
          //  builder.addText(getCentreAlign(mConstantFooter, maxChar, margin) + "\n");
            //builder.addPageEnd();
        } catch (EposException e) {
            e.printStackTrace();
        }

        return builder;
    }
}
