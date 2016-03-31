package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 31-03-2016.
 */
public class ApplicationErrorDBDTO {

    private int errorId;
    private String source;
    private String method;
    private String description;

    public ApplicationErrorDBDTO(String source, String method, String description) {
        this.source = source;
        this.method = method;
        this.description = description;
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
}
