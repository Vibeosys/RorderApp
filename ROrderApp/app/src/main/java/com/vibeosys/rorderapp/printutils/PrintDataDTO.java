package com.vibeosys.rorderapp.printutils;

import android.text.TextUtils;

import com.vibeosys.rorderapp.data.OrderDetailsDTO;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by akshay on 29-02-2016.
 */
public class PrintDataDTO {

    private PrintHeader mHeader;
    private String mFooter;
    private PrintBody mBody;


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

    public String getKotPrint(int maxNoChar,int padding,int margin) {
        HashMap<Integer, OrderDetailsDTO> menusHashMap = this.mBody.getMenus();
        StringBuilder strPrint = new StringBuilder();
        String strLine = "";
        for (int i = 0; i < maxNoChar; i++) {
            strLine = strLine + "_";
        }
        String strMargin="";
        for(int i=0;i<margin;i++)
        {
            strMargin=strMargin+" ";
        }
        String strPadding="";
        for(int i=0;i<padding;i++)
        {
            strPadding=strPadding+" ";
        }
        strPrint.append(strMargin);
        strPrint.append(this.mHeader.getServedBy()+"\n");
        strPrint.append(strMargin);
        String tableNo=this.mHeader.getTableNo();
        strPrint.append(tableNo);
        String time=this.mHeader.getTime();
        strPrint.append(getSpaceString(tableNo.length(),maxNoChar,margin+time.length()));
        strPrint.append(time+"\n");
        strPrint.append(strPadding);
        strPrint.append(strLine);
        strPrint.append(strPadding+"\n");
        strPrint.append(strMargin);
        String strDesc = "Description";
        strPrint.append(strDesc) ;
        strPrint.append(getSpaceString(strDesc.length(),maxNoChar,margin+6));
        strPrint.append("Qty\n");
        strPrint.append(strPadding);
        strPrint.append(strLine);
        strPrint.append(strPadding+"\n");
        Set<Integer> keys = menusHashMap.keySet();
        String menuList = "";
        for (Integer i : keys) {
            OrderDetailsDTO orderMenu = menusHashMap.get(i);
            String menuTitle = strMargin+ orderMenu.getMenuTitle();
            int menuLength = menuTitle.length();
            String quantity=String.valueOf(orderMenu.getOrderQuantity());
            String note=orderMenu.getmNote();
            if(TextUtils.isEmpty(note))
            {
                menuTitle = menuTitle + getSpaceString(menuLength,maxNoChar,margin+quantity.length()) +quantity ;
            }
            else {
                menuTitle = menuTitle + getSpaceString(menuLength,maxNoChar,margin+quantity.length()) +quantity+"\n";
                menuTitle=menuTitle+strMargin+strPadding+note;
            }
            menuList = menuList + menuTitle + strMargin+"\n";
        }
        strPrint.append(menuList);
        strPrint.append(strPadding);
        strPrint.append(strLine);
        strPrint.append(strPadding+"\n");
        strPrint.append(strMargin);
        strPrint.append(getCentreAlign(this.mFooter,maxNoChar,margin)+"\n");
        return strPrint.toString();
    }

    public String getSpaceString(int noChar,int maxChar,int margin)
    {
        int space = maxChar - noChar;
        String strSpace = "";
        for (int j = 0; j < space - margin; j++) {
            strSpace = strSpace + " ";
        }
        return strSpace;
    }

    public String getCentreAlign(String str,int maxChar,int margin)
    {
        String outStr;
        int space=(maxChar/2)-((str.length()+(margin*2))/2);
        String strSpace = "";
        for (int j = 0; j < space; j++) {
            strSpace = strSpace + " ";
        }
        outStr=strSpace+str;
        return outStr;
    }
}
