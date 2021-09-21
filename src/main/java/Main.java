import DB.DBConnection;
import DB.PageTableWorker;

import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Main {

    public static void main(String[] args) {
        //long start = System.currentTimeMillis();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите запрос: ");
        String searchQuery = scanner.nextLine();

        /*new ForkJoinPool().invoke(new SiteRecursiveAction(new Site(Constants.BASE_URL)));
        PageTableWorker.executeMultiInsert();

        ContentHandler contentHandler = new ContentHandler();
        contentHandler.toHandle();*/

        SearchQueryHandler queryHandler = new SearchQueryHandler();
        queryHandler.toHandle(searchQuery);

        DBConnection.closeConnection();
        //System.out.println(System.currentTimeMillis() - start);
    }
}