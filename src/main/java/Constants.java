public class Constants {
    public static final String BASE_URL = "http://www.playback.ru/";

    public static final String DB_NAME = "search_engine";
    public static final String DB_USER = "root";
    public static final String DB_PASS = "Tgb10Ujm73";
    public static final String LOCAL_HOST = "jdbc:mysql://localhost:3306/";
    public static final String USER = "?user=";
    public static final String PASSWORD = "&password=";
    public static final String TIME_ZONE = "&serverTimezone=UTC";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS page";
    public static final String CREATE_TABLE = "CREATE TABLE page(";
    public static final String ID_INT = "id INT NOT NULL AUTO_INCREMENT, ";
    public static final String PATH_TEXT = "path TEXT NOT NULL, ";
    public static final String CODE_INT = "code INT NOT NULL, ";
    public static final String CONTENT_MEDIUMTEXT = "content MEDIUMTEXT NOT NULL, ";
    public static final String PRIMARY_KEY = "PRIMARY KEY(id))";
    public static final String SQL_QUERY = "INSERT INTO page(path, code, content) VALUES (?, ?, ?)";

    public static final String REG_TYPES_FILES = ".*\\.(jpg|docx|doc|pdf|png|zip)";
    public static final int THREAD_SLEEP = 500;

    public static final String AGENT = "SearchBot";
    public static final String REFERRER = "http://www.google.com";
    public static final String CSS_QUERY = "a[href]";
    public static final String ATTRIBUTE_KEY = "abs:href";
    public static final String SLASH = "/";
}