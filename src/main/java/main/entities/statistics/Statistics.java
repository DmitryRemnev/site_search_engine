package main.entities.statistics;

public class Statistics {
    private Total total;
    private Detailed detailed;

    public Statistics() {
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }

    public Detailed getDetailed() {
        return detailed;
    }

    public void setDetailed(Detailed detailed) {
        this.detailed = detailed;
    }
}