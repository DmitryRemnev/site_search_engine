import DB.FieldTableWorker;
import DB.IndexTableWorker;
import DB.LemmaTableWorker;
import DB.PageTableWorker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContentHandler {
    private final ArrayList<Field> fields = new ArrayList<>();

    ContentHandler() {
        ResultSet resultSet = FieldTableWorker.getFields();
        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    fields.add(new Field(resultSet.getString(Constants.COLUMN_NAME), resultSet.getFloat(Constants.COLUMN_WEIGHT)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void toHandle() {
        ResultSet pageResultSet = PageTableWorker.getPageResultSet();

        if (pageResultSet != null) {
            try {
                //int x = 1;

                while (pageResultSet.next()) {
                    if (pageResultSet.getInt(Constants.COLUMN_CODE) == Constants.CODE_OK) {
                        StringBuilder text = new StringBuilder();
                        Map<String, Double> lemmaMap = new HashMap<>();
                        int pageId = pageResultSet.getInt(Constants.COLUMN_ID);

                        for (Field field : fields) {
                            try {
                                addElementsToText(pageResultSet.getString(Constants.COLUMN_CONTENT), field.getName(), text);
                                fillingLemmaMap(text.toString(), field.getWeight(), lemmaMap);

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        for (Map.Entry<String, Double> item : lemmaMap.entrySet()) {

                            ResultSet lemmaResultCount = LemmaTableWorker.getResultLemma(item.getKey());
                            if (lemmaResultCount != null) {
                                lemmaResultCount.last();

                                if (lemmaResultCount.getRow() == 0) {
                                    lemmaInsert(item.getKey());
                                    indexInsert(item.getKey(), pageId, item.getValue());
                                }

                                if (lemmaResultCount.getRow() == 1) {
                                    lemmaUpdate(item.getKey());
                                }
                            }
                        }
                    }
                    //x++;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void addElementsToText(String html, String tagName, StringBuilder text) {
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByTag(tagName);

        for (Element element : elements) {
            text.append(element.text());
        }
    }

    private void fillingLemmaMap(String text, Double weight, Map<String, Double> lemmaMap) {
        Map<String, Double> map = new HashMap<>(Lemmatizer.getLemmaAndRatingMap(text, weight));

        for (Map.Entry<String, Double> item : map.entrySet()) {
            if (lemmaMap.containsKey(item.getKey())) {
                lemmaMap.put(item.getKey(), lemmaMap.get(item.getKey()) + map.get(item.getKey()));
            } else {
                lemmaMap.put(item.getKey(), item.getValue());
            }
        }
    }

    private void lemmaInsert(String lemma) {
        LemmaTableWorker.executeInsert(lemma, Constants.FREQUENCY);
    }

    private void lemmaUpdate(String lemma) {
        LemmaTableWorker.executeUpdate(lemma);
    }

    private void indexInsert(String lemma, int pageId, double rating) {
        ResultSet lemmaResultSet = LemmaTableWorker.getResultLemma(lemma);

        if (lemmaResultSet != null) {
            try {
                while (lemmaResultSet.next()) {
                    int lemmaId = lemmaResultSet.getInt(Constants.COLUMN_ID);
                    IndexTableWorker.executeInsert(pageId, lemmaId, rating);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}