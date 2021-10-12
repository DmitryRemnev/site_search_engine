package main.entities;

import java.util.HashMap;
import java.util.Map;

public class Page {
    private final Integer id;
    private final Map<String, Double> lemmaRatingMap;
    private Double absoluteRelevance;
    private Double relativeRelevance;

    public Page(int id) {
        this.id = id;
        lemmaRatingMap = new HashMap<>();
    }

    public void setLemmaRatingMap(String lemma, Double rating) {
        lemmaRatingMap.put(lemma, rating);
    }

    public Integer getId() {
        return id;
    }

    public Map<String, Double> getLemmaRatingMap() {
        return lemmaRatingMap;
    }

    public void setAbsoluteRelevance(Double absoluteRelevance) {
        this.absoluteRelevance = absoluteRelevance;
    }

    public Double getAbsoluteRelevance() {
        return absoluteRelevance;
    }

    public void setRelativeRelevance(Double relativeRelevance) {
        this.relativeRelevance = relativeRelevance;
    }

    public Double getRelativeRelevance() {
        return relativeRelevance;
    }
}