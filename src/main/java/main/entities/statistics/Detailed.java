package main.entities.statistics;

import java.util.ArrayList;
import java.util.List;

public class Detailed {
    private List<SiteDetailed> listDetailed;

    public Detailed() {
        listDetailed = new ArrayList<>();
    }

    public List<SiteDetailed> getListDetailed() {
        return listDetailed;
    }

    public void setListDetailed(List<SiteDetailed> listDetailed) {
        this.listDetailed = listDetailed;
    }
}