package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 31-03-2016.
 */
public class ApplicationErrorDBDTO {

    private int errorId;
    private String source;
    private String method;
    private String description;
    private String errorDate;
    private String errorTime;

    public ApplicationErrorDBDTO(String source, String method, String description, String date, String time) {
        this.source = source;
        this.method = method;
        this.description = description;
        this.errorDate = date;
        this.errorTime = time;
    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(String errorDate) {
        this.errorDate = errorDate;
    }

    public String getTime() {
        return errorTime;
    }

    public void setTime(String time) {
        this.errorTime = time;
    }
}
