package com.maro.basiclogin.entity;

/**
 * Created by Maro on 23/08/2015.
 */
public class Response {
    private int responseCode;
    private String status;
    private String reason;

    public Response() {
    }

    @Override
    public String toString() {
        return "Response{" +
                "responseCode=" + responseCode +
                ", status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
