package main.constants;

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
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_LAST_ERROR = "last_error";
    public static final String COLUMN_STATUS_TIME = "status_time";

    public static final String RUSSIAN_LETTERS = "[А-Яа-я]";
    public static final String WORD_UNION = "СОЮЗ";
    public static final String WORD_PREPOSITION = "ПРЕДЛ";
    public static final String WORD_INTERJECTION = "МЕЖД";
    public static final String WORD_DEMONSTRATIVE = "указат";
    public static final String WORD_PREDICATIVE = "ПРЕДК";

    public static final String QUERY_SELECT_FIELD = "SELECT * FROM _field;";

    public static final String COLUMN_FREQUENCY = "frequency";
    public static final int MAX_FREQUENCY = 20;

    public static final String NO_MATCHES_FOUND = "Совпадений не найдено!";
    public static final String TAG_B = "b";

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String QUERY_SITES_COUNT = "SELECT count(*) AS sitesCount FROM _site";
    public static final String QUERY_PAGES_COUNT = "SELECT count(*) AS pagesCount FROM _page";
    public static final String QUERY_LEMMAS_COUNT = "SELECT count(*) AS lemmasCount FROM _lemma";
    public static final String QUERY_STATUS = "SELECT status FROM _site";
    public static final String COLUMN_LABEL_SITES = "sitesCount";
    public static final String COLUMN_LABEL_PAGES = "pagesCount";
    public static final String COLUMN_LABEL_LEMMAS = "lemmasCount";
    public static final String QUERY_SITES_ALL = "SELECT * FROM _site";
    public static final String QUERY_PAGES_COUNT_DETAILED = "SELECT count(*) AS pagesCountDetailed" +
            " FROM _page WHERE site_id = ?";
    public static final String QUERY_LEMMAS_COUNT_DETAILED = "SELECT count(*) AS lemmasCountDetailed" +
            " FROM _lemma WHERE site_id = ?";
    public static final String COLUMN_PAGES_COUNT = "pagesCountDetailed";
    public static final String COLUMN_LEMMAS_COUNT = "lemmasCountDetailed";
}