package main.entities;

public class RelevantPage {
    String uri;
    String title;
    String snippet;
    Double relevance;

    public RelevantPage() {
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public void setRelevance(Double relevance) {
        this.relevance = relevance;
    }

    public String getUri() {
        return uri;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }

    public Double getRelevance() {
        return relevance;
    }
}