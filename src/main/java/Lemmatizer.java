import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lemmatizer {
    public static LuceneMorphology luceneMorph;

    static Map<String, Double> getLemmaAndRatingMap(String text, Double weight) {
        Map<String, Double> lemmaAndRatingMap = new HashMap<>();

        try {
            luceneMorph = new RussianLuceneMorphology();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String word : getWords(text)) {
            List<String> wordBaseForms = luceneMorph.getNormalForms(word);
            addToLemmaAndRatingMap(wordBaseForms, lemmaAndRatingMap, weight);
        }

        return lemmaAndRatingMap;
    }

    static List<String> getWords(String text) {
        List<String> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        boolean isNewString = true;
        char c;

        for (int i = 0; i < text.length(); i++) {

            c = text.charAt(i);

            if (Character.toString(c).matches(Constants.RUSSIAN_LETTERS)) {
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
            if (info.contains(Constants.WORD_UNION) ||
                    info.contains(Constants.WORD_PREPOSITION) ||
                    info.contains(Constants.WORD_INTERJECTION) ||
                    info.contains(Constants.WORD_DEMONSTRATIVE) ||
                    info.contains(Constants.WORD_PREDICATIVE)) {

                isAdd = false;
                break;
            }
        }

        return isAdd;
    }

    private static void addToLemmaAndRatingMap(List<String> wordBaseForms, Map<String, Double> lemmaAndRatingMap, Double weight) {
        for (String word : wordBaseForms) {
            if (lemmaAndRatingMap.containsKey(word)) {
                lemmaAndRatingMap.put(word, lemmaAndRatingMap.get(word) + weight);
            } else {
                lemmaAndRatingMap.put(word, weight);
            }
        }
    }
}