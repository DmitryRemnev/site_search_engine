package main;

import main.entities.Site;

import java.util.List;

public class SinglePageHandler {
    List<Site> sites;
    String page;

    public SinglePageHandler(List<Site> sites, String page) {
        this.sites = sites;
        this.page = page;
    }

    public boolean isUrlPresent() {
        for (Site site : sites) {
            String baseUrl = site.getUrl();
            if (page.contains(baseUrl)) {
                return true;
            }
        }

        return false;
    }
}