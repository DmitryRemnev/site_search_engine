package main;

import main.db.DBConnection;
import main.db.PageTableWorker;
import main.entities.Site;
import main.entities.YamlConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@RestController
public class SearchController {
    private SiteRecursiveAction siteRecursiveAction;
    private List<Site> sites;
    private boolean isRunning = false;

    public SearchController() {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);

        Yaml yaml = new Yaml(new Constructor(YamlConfig.class), representer);

        InputStream inputStream = this
                .getClass()
                .getClassLoader()
                .getResourceAsStream("config/application.yaml");

        YamlConfig config = yaml.load(inputStream);
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

        return ResponseEntity.ok("true");
    }
}