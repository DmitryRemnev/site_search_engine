package main;

import main.db.DBConnection;
import main.db.PageTableWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ForkJoinPool;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

        /*Scanner scanner = new Scanner(System.in);
        System.out.println("Введите запрос: ");
        String searchQuery = scanner.nextLine();

        new ForkJoinPool().invoke(new PageRecursiveAction(new PageHandler(Constants.BASE_URL)));
        PageTableWorker.executeMultiInsert();

        ContentHandler contentHandler = new ContentHandler();
        contentHandler.toHandle();

        SearchQueryHandler queryHandler = new SearchQueryHandler();
        queryHandler.toHandle(searchQuery);*/

        DBConnection.closeConnection();
    }
}