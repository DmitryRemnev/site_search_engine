import DB.FieldTableWorker;
import DB.PageTableWorker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContentHandler {
    public static final int CODE_OK = 200;
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_CONTENT = "content";
    private static final ArrayList<Field> FIELDS = new ArrayList<>();

    ContentHandler() {
        ResultSet resultSet = FieldTableWorker.getFields();
        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    FIELDS.add(new Field(resultSet.getString(COLUMN_NAME), resultSet.getFloat(COLUMN_WEIGHT)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void toHandle() {
        ResultSet resultSet = PageTableWorker.getPageResultSet();

        if (resultSet != null) {
            try {
                boolean flag = true;

                while (resultSet.next()) {
                    if (resultSet.getInt(COLUMN_CODE) == CODE_OK && flag) {
                        StringBuilder text = new StringBuilder();
                        Map<String, Double> lemmaMap = new HashMap<>();

                        for (Field field : FIELDS) {
                            try {
                                Document document = Jsoup.parse(resultSet.getString(COLUMN_CONTENT));
                                Elements elements = document.getElementsByTag(field.getName());
                                for (Element element : elements) {
                                    text.append(element.text());
                                }

                                Map<String, Double> map = new HashMap<>(Lemmatizer.getLemmaMap(text.toString(), field.getWeight()));
                                for (String key : map.keySet()) {
                                    if (lemmaMap.containsKey(key)) {
                                        lemmaMap.put(key, lemmaMap.get(key) + map.get(key));
                                    } else {
                                        lemmaMap.put(key, map.get(key));
                                    }
                                }

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        DecimalFormat df = new DecimalFormat("#.#");
                        int i = 1;
                        for (String key : lemmaMap.keySet()) {
                            String value = df.format(lemmaMap.get(key));
                            System.out.println(i + ") " + key + " - " + value);
                            i++;
                        }

                        flag = false;
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}