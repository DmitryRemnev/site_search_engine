import DB.PageTableWorker;

import java.util.concurrent.ForkJoinPool;

public class Main {

    public static void main(String[] args) {
        Site root = new Site(Constants.BASE_URL);

        new ForkJoinPool().invoke(new SiteRecursiveAction(root));

        PageTableWorker.executeMultiInsert();

        ContentHandler.toHandle();
    }
}