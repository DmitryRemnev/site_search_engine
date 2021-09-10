import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lemmatizer {
    public static final String RUSSIAN_LETTERS = "[А-Яа-я]";
    public static final String WORD_UNION = "СОЮЗ";
    public static final String WORD_PREPOSITION = "ПРЕДЛ";
    public static final String WORD_INTERJECTION = "МЕЖД";
    public static final String WORD_DEMONSTRATIVE = "указат";
    public static final String WORD_PREDICATIVE = "ПРЕДК";

    public static LuceneMorphology luceneMorph;

    static Map<String, Double> getLemmaMap(String text, Double weight) {
        Map<String, Double> lemmaMap = new HashMap<>();

        try {
            luceneMorph = new RussianLuceneMorphology();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String word : getWords(text)) {
            List<String> wordBaseForms = luceneMorph.getNormalForms(word);
            addToLemmaList(wordBaseForms, lemmaMap, weight);
        }

        return lemmaMap;
    }

    static List<String> getWords(String text) {
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
                    if (isAddToWords(word.toLowerCase())) {
                        list.add(word.toLowerCase());
                    }
                    isNewString = true;
                }
            }
        }

        return list;
    }

    private static boolean isAddToWords(String word) {
        try {
            luceneMorph = new RussianLuceneMorphology();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> wordInfo = luceneMorph.getMorphInfo(word);
        boolean isAdd = true;
        for (String info : wordInfo) {
            if (info.contains(WORD_UNION) ||
                    info.contains(WORD_PREPOSITION) ||
                    info.contains(WORD_INTERJECTION) ||
                    info.contains(WORD_DEMONSTRATIVE) ||
                    info.contains(WORD_PREDICATIVE)) {

                isAdd = false;
                break;
            }
        }

        return isAdd;
    }

    private static void addToLemmaList(List<String> wordBaseForms, Map<String, Double> cleanList, Double weight) {
        for (String word : wordBaseForms) {
            if (cleanList.containsKey(word)) {
                cleanList.put(word, cleanList.get(word) + weight);
            } else {
                cleanList.put(word, weight);
            }
        }
    }
}