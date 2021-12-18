package main.entities;

public class BadResponse extends Response {
    boolean result;
    String error;

    public BadResponse(boolean result, String error) {
        this.result = result;
        this.error = error;
    }

    public boolean isResult() {
        return result;
    }

    public String getError() {
        return error;
    }
}