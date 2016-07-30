package Scanner;

import java.io.File;
import java.net.URL;

/**
 * Created by Anders Hofmeister Brønden on 21-02-2016.
 */
public class Downloader implements Runnable {
    private URL url;
    private File destination;
    private DownloadHandler handler;

    public Downloader(URL url, File destination, DownloadHandler handler) {
        this.url = url;
        this.destination = destination;
        this.handler = handler;
    }

    public void run() {
        System.out.println("Download " + url.toString());
        handler.fetchFile(url, destination);
        handler.getDls().remove();
        handler.updateProgress();
    }
}
