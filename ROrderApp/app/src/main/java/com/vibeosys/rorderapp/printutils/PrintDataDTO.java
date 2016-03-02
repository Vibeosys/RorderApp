package com.vibeosys.rorderapp.printutils;

/**
 * Created by akshay on 29-02-2016.
 */
public class PrintDataDTO {

    private String mHeader;
    private String mFooter;
    private String mBody;


    public PrintDataDTO() {
    }

    public PrintDataDTO(String mBody) {
        this.mBody = mBody;
    }

    public String getHeader() {
        return mHeader;
    }

    public void setHeader(String mHeader) {
        this.mHeader = mHeader;
    }

    public String getFooter() {
        return mFooter;
    }

    public void setFooter(String mFooter) {
        this.mFooter = mFooter;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String mBody) {
        this.mBody = mBody;
    }
}
