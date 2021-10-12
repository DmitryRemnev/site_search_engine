package main;

import main.db.DBConnection;
import main.db.PageTableWorker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ForkJoinPool;

@RestController
public class SearchController {
    private boolean isRunning = false;

    @GetMapping("/api/startIndexing")
    public ResponseEntity<String> startFullIndexing() {

        if (isRunning) {
            return ResponseEntity.badRequest().body("Индексация уже запущена");

        } else {
            isRunning = true;

            DBConnection.cleanDatabase();
            new ForkJoinPool().invoke(new SiteRecursiveAction(new Site(Constants.BASE_URL)));
            PageTableWorker.executeMultiInsert();
            ContentHandler contentHandler = new ContentHandler();
            contentHandler.toHandle();

            isRunning = false;

            return ResponseEntity.ok("true");
        }
    }

    @GetMapping("/api/stopIndexing")
    public ResponseEntity<String> stopCurrentIndexing() {

        if (!isRunning) {
            return ResponseEntity.badRequest().body("Индексация не запущена");

        } else {
            SiteRecursiveAction.isCancel = true;

            isRunning = false;
            return ResponseEntity.ok("true");
        }
    }
}