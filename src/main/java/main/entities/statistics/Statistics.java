package main.entities.statistics;

import java.util.List;

public class Statistics {
    private Total total;
    private List<SiteDetailed> detailed;

    public Statistics() {
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }

    public List<SiteDetailed> getDetailed() {
        return detailed;
    }

    public void setDetailed(List<SiteDetailed> detailed) {
        this.detailed = detailed;
    }
}