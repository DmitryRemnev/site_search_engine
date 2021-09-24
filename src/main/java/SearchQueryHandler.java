import DB.IndexTableWorker;
import DB.LemmaTableWorker;
import Entities.Page;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class SearchQueryHandler {
    private Set<String> lemmas;
    private Map<String, Integer> unsortedMap;
    private List<String> sortedList;
    private final List<Integer> firstIdList = new ArrayList<>();
    private final List<Integer> nextIdList = new ArrayList<>();
    private final List<Page> pagesList = new ArrayList<>();
    private boolean isMatchesFound = true;

    public void toHandle(String searchQuery) {
        lemmas = Lemmatizer.getLemmaSet(searchQuery);
        fillingMap();
        sortingList();
        searchingRelevantPages();
        if (isMatchesFound && firstIdList.size() > 0) {
            for (Integer id : firstIdList) {
                for (Page page : pagesList) {
                    if (Objects.equals(page.getId(), id)) {
                        System.out.println(page.getId());
                        Map<String, Double> lemmaRatingMap;
                        lemmaRatingMap = page.getLemmaRatingMap();
                        for (Map.Entry<String, Double> item : lemmaRatingMap.entrySet()) {
                            System.out.println(item.getKey() + " - " + item.getValue());
                        }
                    }
                }
            }
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
                    if (firstIdList.isEmpty()) {
                        while (lemmaResultSet.next()) {
                            int pageId = lemmaResultSet.getInt(Constants.COLUMN_ID);
                            double rating = lemmaResultSet.getDouble(Constants.COLUMN_RATING);

                            Page page = new Page(pageId);
                            page.setLemmaRatingMap(lemma, rating);
                            pagesList.add(page);
                            firstIdList.add(pageId);
                        }

                    } else {
                        nextIdList.clear();
                        while (lemmaResultSet.next()) {
                            int pageId = lemmaResultSet.getInt(Constants.COLUMN_ID);
                            double rating = lemmaResultSet.getDouble(Constants.COLUMN_RATING);

                            if (firstIdList.contains(pageId)) {
                                for (Page page : pagesList) {
                                    if (page.getId() == pageId) {
                                        page.setLemmaRatingMap(lemma, rating);
                                    }
                                }
                            }

                            nextIdList.add(pageId);
                        }

                        if (!isMatchesFound()) {
                            isMatchesFound = false;
                            break;
                        }

                        clearFirstList();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isMatchesFound() {
        int i = 0;
        for (Integer id : nextIdList) {
            if (firstIdList.contains(id)) {
                i++;
            }
        }

        return i > 0;
    }

    private void clearFirstList() {
        firstIdList.removeIf(id -> !nextIdList.contains(id));
    }
}