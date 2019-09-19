package com.kwe.kweplus.modal;

public class ReturnMessage {

    private String status;

    private String message;

    public static String STATUS_ERROR="400";
    public static String STATUS_OK="200";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ReturnMessage() {
    }

    @Override
    public String toString() {
        return "ReturnMessage{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
