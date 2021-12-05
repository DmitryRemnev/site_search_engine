package main.entities.statistics;

public class StatisticsResponse {

    boolean result;
    Statistics statistics;

    public StatisticsResponse() {
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }
}