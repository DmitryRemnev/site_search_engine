public class Constants {
    public static final String BASE_URL = "https://et-cetera.ru/mobile/";

    public static final String REG_TYPES_FILES = ".*\\.(jpg|docx|doc|pdf|png|zip)";
    public static final int THREAD_SLEEP = 500;

    public static final String AGENT = "SearchBot";
    public static final String REFERRER = "http://www.google.com";
    public static final String CSS_QUERY = "a[href]";
    public static final String ATTRIBUTE_KEY = "abs:href";
    public static final String SLASH = "/";

    public static final int CODE_OK = 200;
    public static final int FREQUENCY = 1;
    public static final int LEMMA_ABSENT = 0;
    public static final int LEMMA_PRESENT = 1;
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_ID = "id";

    public static final String RUSSIAN_LETTERS = "[А-Яа-я]";
    public static final String WORD_UNION = "СОЮЗ";
    public static final String WORD_PREPOSITION = "ПРЕДЛ";
    public static final String WORD_INTERJECTION = "МЕЖД";
    public static final String WORD_DEMONSTRATIVE = "указат";
    public static final String WORD_PREDICATIVE = "ПРЕДК";

    public static final String QUERY_SELECT_FIELD = "SELECT * FROM _field;";
    public static final String QUERY_SELECT_PAGE = "SELECT * FROM _page;";

    public static final String COLUMN_FREQUENCY = "frequency";
    public static final int MAX_FREQUENCY = 20;
}