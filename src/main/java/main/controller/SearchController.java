package main.controller;

import main.handlers.PageHandler;
import main.handlers.PageRecursiveHandler;
import main.handlers.SiteHandler;
import main.service.StatisticsService;
import main.config.AppConfig;
import main.database.DBConnection;
import main.entities.Site;
import main.entities.StartResponse;
import main.entities.YamlConfig;
import main.entities.statistics.StatisticsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private StatisticsService statisticsService;

    private final List<PageRecursiveHandler> actionsList;
    private final List<Site> sites;
    private boolean isRunning = false;

    public SearchController() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        YamlConfig config = context.getBean(YamlConfig.class);
        sites = config.getSites();
        actionsList = new ArrayList<>();
    }

    @GetMapping("/api/startIndexing")
    public StartResponse startFullIndexing() {
        var response = new StartResponse();

        if (isRunning) {
            response.setResult(false);
            response.setError("Индексация уже запущена");

            return response;

        } else {
            isRunning = true;

            DBConnection.cleanDatabase();
            for (Site site : sites) {
                var pageRecursiveAction = new PageRecursiveHandler(
                        new PageHandler(site.getUrl(), site.getName(), site.getUrl()));
                actionsList.add(pageRecursiveAction);
                new SiteHandler(pageRecursiveAction, site).start();
            }

            isRunning = false;

            response.setResult(true);

            return response;
        }
    }

    @GetMapping("/api/stopIndexing")
    public StartResponse stopCurrentIndexing() {
        var response = new StartResponse();

        if (!isRunning) {
            //return ResponseEntity.badRequest().body("Индексация не запущена");

            response.setResult(false);
            response.setError("Индексация не запущена");

            return response;

        } else {
            for (PageRecursiveHandler pageRecursiveHandler : actionsList) {
                pageRecursiveHandler.isCancel.set(true);
            }

            isRunning = false;
            //return ResponseEntity.ok("true");

            response.setResult(true);

            return response;
        }
    }

    /*@PostMapping("/api/indexPage")
    public ResponseEntity<String> addingOrUpdatingSinglePage(@RequestParam("indexPage") String indexPage) {
        var singlePageHandler = new SinglePageHandler(sites, page);

        if (singlePageHandler.isUrlPresent()) {
            return ResponseEntity.ok("true");
        } else {
            return ResponseEntity.badRequest().body("Данная страница находится за пределами сайтов, " +
                    "указанных в конфигурационном файле");
        }
        System.out.println(indexPage);
        return ResponseEntity.ok("true");
    }*/

    @GetMapping("/api/statistics")
    public StatisticsResponse getStatistics() {
        return statisticsService.getStatistics();
    }

    @GetMapping("/api/search")
    public ResponseEntity<String> getSearch() {

        System.out.println("SEARCH");
        return ResponseEntity.ok("true");
    }
}