import java.sql.SQLException;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static final String URL = "https://skillbox.ru/";

    public static void main(String[] args) throws SQLException {
        new ForkJoinPool().invoke(new SiteRecursiveAction(URL, ""));

        //DBConnection.executeMultiInsert();
    }
}