package main.entities;

public class GoodResponse extends Response {
    boolean result;

    public GoodResponse(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }
}
