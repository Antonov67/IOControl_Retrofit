package com.example.iocontrol_retrofit;

public class Data {
    private String check;
    private String value;
    private String date;
    private String dateUnix;
    private String message;
    private String requestTime;

    public String getCheck() {
        return check;
    }

    public String getValue() {
        return value;
    }

    public String getDate() {
        return date;
    }

    public String getDateUnix() {
        return dateUnix;
    }

    public String getMessage() {
        return message;
    }

    public String getRequestTime() {
        return requestTime;
    }

    @Override
    public String toString() {
        return "Data{" +
                "check='" + check + '\'' +
                ", value='" + value + '\'' +
                ", date='" + date + '\'' +
                ", dateUnix='" + dateUnix + '\'' +
                ", message='" + message + '\'' +
                ", requestTime='" + requestTime + '\'' +
                '}';
    }
}
