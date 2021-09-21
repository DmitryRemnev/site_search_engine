import java.util.Set;

public class SearchQueryHandler {
    private Set<String> lemmas;

    public void toHandle(String searchQuery) {
        lemmas = Lemmatizer.getLemmaSet(searchQuery);

        for (String lemma : lemmas) {

        }
    }
}