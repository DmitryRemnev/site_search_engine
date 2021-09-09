import DB.PageTableWorker;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentHandler {
    public static final String RUSSIAN_LETTERS = "[А-Яа-я]";
    public static LuceneMorphology luceneMorph;

    public static void toHandle() {
        ResultSet resultSet = PageTableWorker.getContent();

        if (resultSet != null) {
            try {
                boolean flag = true;
                while (resultSet.next()) {
                    if (flag) {
                        String html = resultSet.getString("content");
                        Document document = Jsoup.parse(html);
                        Elements titles = document.getElementsByTag("title");

                        StringBuilder text = new StringBuilder();
                        for (Element element : titles) {
                            text.append(element.text());
                        }
                        Elements bodies = document.getElementsByTag("body");
                        for (Element element : bodies) {
                            text.append(element.text());
                        }

                        long start = System.currentTimeMillis();
                        Map<String, Integer> cleanList = getCleanList(getList(text.toString()));
                        System.out.println(System.currentTimeMillis() - start);

                        int i = 1;
                        for (String key : cleanList.keySet()) {
                            String value = cleanList.get(key).toString();
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

    private static List<String> getList(String text) {
        List<String> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        boolean isNewString = true;
        char c;

        for (int i = 0; i < text.length(); i++) {

            c = text.charAt(i);

            if (Character.toString(c).matches(RUSSIAN_LETTERS)) {
                if (isNewString) {
                    builder = new StringBuilder();
                    builder.append(c);
                    isNewString = false;

                } else {
                    builder.append(c);
                }

            } else {
                if (builder.length() > 0 && !isNewString) {
                    String word = builder.toString();
                    if (isAddToList(word.toLowerCase())) {
                        list.add(word.toLowerCase());
                    }
                    isNewString = true;
                }
            }
        }

        return list;
    }

    private static boolean isAddToList(String word) {
        try {
            luceneMorph = new RussianLuceneMorphology();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> wordInfo = luceneMorph.getMorphInfo(word);
        boolean isAdd = true;
        for (String info : wordInfo) {
            if (info.contains("СОЮЗ") ||
                    info.contains("ПРЕДЛ") ||
                    info.contains("МЕЖД") ||
                    info.contains("указат") ||
                    info.contains("ПРЕДК")) {

                isAdd = false;
                break;
            }
        }

        return isAdd;
    }

    private static Map<String, Integer> getCleanList(List<String> list) {
        Map<String, Integer> cleanList = new HashMap<>();

        try {
            luceneMorph = new RussianLuceneMorphology();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String word : list) {
            List<String> wordBaseForms = luceneMorph.getNormalForms(word);
            addToCleanLeast(wordBaseForms, cleanList);
        }

        return cleanList;
    }

    private static void addToCleanLeast(List<String> wordBaseForms, Map<String, Integer> cleanList) {
        for (String word : wordBaseForms) {
            if (cleanList.containsKey(word)) {
                cleanList.put(word, cleanList.get(word) + 1);
            } else {
                cleanList.put(word, 1);
            }
        }
    }
}