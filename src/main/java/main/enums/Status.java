package main.enums;

public enum Status {
    INDEXING("INDEXING"),
    INDEXED("INDEXED"),
    FAILED("FAILED");

    private final String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}