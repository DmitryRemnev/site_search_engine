package main;

import main.db.IndexTableWorker;
import main.db.LemmaTableWorker;
import main.db.PageTableWorker;
import main.entities.Page;
import main.entities.RelevantPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
    private final List<RelevantPage> relevantPagesList = new ArrayList<>();
    private boolean isMatchesFound = true;
    private Double maximumAbsoluteRelevance = 0.0;

    public void toHandle(String searchQuery) {
        lemmas = Lemmatizer.getLemmaSet(searchQuery);
        fillingMap();
        sortingList();
        searchingPages();

        if (isMatchesFound && firstIdList.size() > 0) {
            clearPageList();
            calculateAbsoluteRelevance();
            calculateMaximumAbsoluteRelevance();
            calculateRelativeRelevance();
            sortingPageList();
            createRelevantPagesList();

            for (RelevantPage page : relevantPagesList) {
                System.out.println(page.getUri());
                System.out.println(page.getTitle());
                System.out.println(page.getSnippet());
                System.out.println(page.getRelevance());
            }

        } else {
            System.out.println(Constants.NO_MATCHES_FOUND);
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

    private void searchingPages() {
        for (String lemma : sortedList) {
            ResultSet lemmaResultSet = IndexTableWorker.getResultLemma(lemma);

            if (lemmaResultSet != null && isMatchesFound) {
                try {
                    if (firstIdList.isEmpty()) {
                        while (lemmaResultSet.next()) {
                            createPage(lemmaResultSet, lemma);
                        }

                    } else {
                        nextIdList.clear();
                        while (lemmaResultSet.next()) {
                            int pageId = lemmaResultSet.getInt(Constants.COLUMN_ID);
                            double rating = lemmaResultSet.getDouble(Constants.COLUMN_RATING);

                            updatePage(pageId, lemma, rating);

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

    private void createPage(ResultSet lemmaResultSet, String lemma) {
        try {
            int pageId = lemmaResultSet.getInt(Constants.COLUMN_ID);
            double rating = lemmaResultSet.getDouble(Constants.COLUMN_RATING);

            Page page = new Page(pageId);
            page.setLemmaRatingMap(lemma, rating);
            pagesList.add(page);
            firstIdList.add(pageId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePage(int pageId, String lemma, double rating) {

        if (firstIdList.contains(pageId)) {
            for (Page page : pagesList) {
                if (page.getId() == pageId) {
                    page.setLemmaRatingMap(lemma, rating);
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

    private void clearPageList() {
        for (Page page : pagesList) {
            int id = page.getId();

            if (!firstIdList.contains(id)) {
                pagesList.remove(page);
            }
        }
    }

    private void calculateAbsoluteRelevance() {
        for (Page page : pagesList) {
            Map<String, Double> lemmaRatingMap;
            lemmaRatingMap = page.getLemmaRatingMap();
            Double abs = 0.0;

            for (Map.Entry<String, Double> item : lemmaRatingMap.entrySet()) {
                abs += item.getValue();
            }

            page.setAbsoluteRelevance(abs);
        }
    }

    private void calculateMaximumAbsoluteRelevance() {
        List<Double> list = new ArrayList<>();

        for (Page page : pagesList) {
            list.add(page.getAbsoluteRelevance());
        }

        maximumAbsoluteRelevance = Collections.max(list);
    }

    private void calculateRelativeRelevance() {
        for (Page page : pagesList) {
            double relative = (page.getAbsoluteRelevance() / maximumAbsoluteRelevance);
            page.setRelativeRelevance((Math.round(relative * 100.0) / 100.0));
        }
    }

    private void sortingPageList() {
        pagesList.sort(new PageComparator());
    }

    private void createRelevantPagesList() {
        for (Page page : pagesList) {
            ResultSet pageResultSet = PageTableWorker.getResultPage(page.getId());

            if (pageResultSet != null) {
                try {
                    while (pageResultSet.next()) {
                        String path = pageResultSet.getString(Constants.COLUMN_PATH);

                        String html = pageResultSet.getString(Constants.COLUMN_CONTENT);
                        Document document = Jsoup.parse(html);
                        String title = document.title();

                        Element element = document.tagName(Constants.TAG_B);
                        String snippet = element.text();

                        createRelevantPage(path, title, snippet, page);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createRelevantPage(String path, String title, String snippet, Page page) {
        RelevantPage relevantPage = new RelevantPage();

        relevantPage.setUri(path.replaceFirst(Constants.SLASH, Constants.BASE_URL));
        relevantPage.setTitle(title);
        relevantPage.setSnippet(snippet);
        relevantPage.setRelevance(page.getRelativeRelevance());

        relevantPagesList.add(relevantPage);
    }
}