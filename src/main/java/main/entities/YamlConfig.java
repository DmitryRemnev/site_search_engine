package main.entities;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class YamlConfig {
    private Spring spring;
    private List<Site> sites;
    private String user_agent;

    public YamlConfig() {
    }

    public Spring getSpring() {
        return spring;
    }

    public void setSpring(Spring spring) {
        this.spring = spring;
    }

    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }
}