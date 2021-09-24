package Entities;

import java.util.HashMap;
import java.util.Map;

public class Page {
    private final Integer id;
    private final Map<String, Double> lemmaRatingMap;
    private Double abs;
    private Double rel;

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
}