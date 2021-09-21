import DB.IndexTableWorker;
import DB.LemmaTableWorker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class SearchQueryHandler {
    private Set<String> lemmas;
    private Map<String, Integer> unsortedMap;
    private List<String> sortedList;
    private List<Integer> firstPagesList;
    private List<Integer> nextPagesList;
    private boolean isMatchesFound = true;

    public SearchQueryHandler() {
    }

    public void toHandle(String searchQuery) {
        lemmas = Lemmatizer.getLemmaSet(searchQuery);
        fillingMap();
        sortingList();
        searchingRelevantPages();
        if (isMatchesFound && firstPagesList.size() > 0) {

        } else {
            System.out.println("Совпадений не найдено!");
        }
    }

    private void fillingMap() {
        unsortedMap = new HashMap<>();

        for (String lemma : lemmas) {
            ResultSet lemmaResultSet = LemmaTableWorker.getResultLemma(lemma);

            if (lemmaResultSet != null) {
                try {
                    while (lemmaResultSet.next()) {

                        int frequency = lemmaResultSet.getInt(Constants.COLUMN_FREQUENCY);
                        if (frequency < Constants.MAX_FREQUENCY) {
                            unsortedMap.put(lemma, frequency);
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sortingList() {
        sortedList = unsortedMap.
                entrySet().
                stream().
                sorted(Map.Entry.comparingByValue()).
                map(Map.Entry::getKey).
                collect(Collectors.toList());
    }

    private void searchingRelevantPages() {
        for (String lemma : sortedList) {
            ResultSet lemmaResultSet = IndexTableWorker.getResultLemma(lemma);

            if (lemmaResultSet != null && isMatchesFound) {
                try {
                    if (firstPagesList.isEmpty()) {
                        while (lemmaResultSet.next()) {
                            firstPagesList.add(lemmaResultSet.getInt("id"));
                        }

                    } else {
                        nextPagesList.clear();
                        while (lemmaResultSet.next()) {
                            nextPagesList.add(lemmaResultSet.getInt("id"));
                        }

                        if (!isMatchesFound()) {
                            isMatchesFound = false;
                            break;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isMatchesFound() {
        int i = 0;
        for (Integer id : nextPagesList) {
            if (firstPagesList.contains(id)) {
                i++;
            }
        }

        return i > 0;
    }
}