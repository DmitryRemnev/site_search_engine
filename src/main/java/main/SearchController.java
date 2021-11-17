package main;

import main.db.DBConnection;
import main.entities.Site;
import main.entities.YamlConfig;
import main.entities.statistics.Statistics;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchController {
    private final List<PageRecursiveAction> actionsList;
    private final List<Site> sites;
    private boolean isRunning = false;

    public SearchController() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        YamlConfig config = context.getBean(YamlConfig.class);
        sites = config.getSites();
        actionsList = new ArrayList<>();
    }

    @GetMapping("/api/startIndexing")
    public ResponseEntity<String> startFullIndexing() {

        if (isRunning) {
            return ResponseEntity.badRequest().body("Индексация уже запущена");

        } else {
            isRunning = true;

            DBConnection.cleanDatabase();
            for (Site site : sites) {
                var pageRecursiveAction = new PageRecursiveAction(
                        new PageHandler(site.getUrl(), site.getName(), site.getUrl()));
                actionsList.add(pageRecursiveAction);
                new SiteHandler(pageRecursiveAction, site).start();
            }

            isRunning = false;

            return ResponseEntity.ok("true");
        }
    }

    @GetMapping("/api/stopIndexing")
    public ResponseEntity<String> stopCurrentIndexing() {
        System.out.println("STOP");

        if (!isRunning) {
            return ResponseEntity.badRequest().body("Индексация не запущена");

        } else {
            for (PageRecursiveAction pageRecursiveAction : actionsList) {
                pageRecursiveAction.isCancel.set(true);
            }

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
    public ResponseEntity<Statistics> getStatistics() {

        System.out.println("STATISTICS");
        return new ResponseEntity<>(StatisticsHandler.getStatistics(), HttpStatus.OK);
    }

    @GetMapping("/api/search")
    public ResponseEntity<String> getSearch() {

        System.out.println("SEARCH");
        return ResponseEntity.ok("true");
    }
}