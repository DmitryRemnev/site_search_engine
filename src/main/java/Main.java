import java.util.concurrent.ForkJoinPool;

public class Main {
    public static final String URL = "https://volochek.life/";

    public static void main(String[] args) {
        new ForkJoinPool().invoke(new SiteRecursiveAction(URL, ""));
    }
}