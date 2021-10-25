package main;

import main.db.DBConnection;
import main.db.PageTableWorker;
import main.entities.Site;
import main.entities.YamlConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

@RestController
public class SearchController {
    private SiteRecursiveAction siteRecursiveAction;
    private final List<Site> sites;
    private boolean isRunning = false;

    public SearchController() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        YamlConfig config = context.getBean(YamlConfig.class);
        sites = config.getSites();
    }

    @GetMapping("/api/startIndexing")
    public ResponseEntity<String> startFullIndexing() {

        if (isRunning) {
            return ResponseEntity.badRequest().body("Индексация уже запущена");

        } else {
            isRunning = true;

            DBConnection.cleanDatabase();
            siteRecursiveAction = new SiteRecursiveAction(new SiteHandler(Constants.BASE_URL));
            new ForkJoinPool().invoke(siteRecursiveAction);
            PageTableWorker.executeMultiInsert();
            new ContentHandler().toHandle();

            isRunning = false;

            return ResponseEntity.ok("true");
        }
    }

    @GetMapping("/api/stopIndexing")
    public ResponseEntity<String> stopCurrentIndexing() {

        if (!isRunning) {
            return ResponseEntity.badRequest().body("Индексация не запущена");

        } else {
            siteRecursiveAction.isCancel.set(true);

            isRunning = false;
            return ResponseEntity.ok("true");
        }
    }

    @PostMapping("/api/indexPage")
    public ResponseEntity<String> addingOrUpdatingSinglePage(String url) {
        System.out.println(url);

        for (Site site : sites) {
            System.out.println(site.getName() + " - " + site.getUrl());
        }

        return ResponseEntity.ok("true");
    }

    @GetMapping("/api/statistics")
    public ResponseEntity<String> getStatistics() {

        return ResponseEntity.ok("true");
    }
}