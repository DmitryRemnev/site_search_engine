import DB.DBConnection;
import DB.PageTableWorker;

import java.util.concurrent.ForkJoinPool;

public class Main {

    public static void main(String[] args) {
        //long start = System.currentTimeMillis();

        new ForkJoinPool().invoke(new SiteRecursiveAction(new Site(Constants.BASE_URL)));
        PageTableWorker.executeMultiInsert();

        ContentHandler contentHandler = new ContentHandler();
        contentHandler.toHandle();

        DBConnection.closeConnection();
        //System.out.println(System.currentTimeMillis() - start);
    }
}