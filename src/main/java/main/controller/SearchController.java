package main.controller;

import main.entities.*;
import main.handlers.PageHandler;
import main.handlers.PageRecursiveHandler;
import main.handlers.SinglePageHandler;
import main.handlers.SiteHandler;
import main.service.StatisticsService;
import main.config.AppConfig;
import main.database.DBConnection;
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
    public Response startFullIndexing() {

        if (isRunning) {
            return new BadResponse(false, "Индексация уже запущена");

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

            return new GoodResponse(true);
        }
    }

    @GetMapping("/api/stopIndexing")
    public Response stopCurrentIndexing() {

        if (!isRunning) {
            return new BadResponse(false, "Индексация не запущена");

        } else {
            for (PageRecursiveHandler pageRecursiveHandler : actionsList) {
                pageRecursiveHandler.isCancel.set(true);
            }

            isRunning = false;

            return new GoodResponse(true);
        }
    }

    /*@PostMapping("/api/indexPage")
    public Response addingOrUpdatingSinglePage(String page) {
        System.out.println(page);
        var singlePageHandler = new SinglePageHandler(sites, page);

        if (singlePageHandler.isUrlPresent()) {
            return new GoodResponse(true);
        } else {
            return new BadResponse(false, "Данная страница находится за пределами сайтов, " +
                    "указанных в конфигурационном файле");
        }
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