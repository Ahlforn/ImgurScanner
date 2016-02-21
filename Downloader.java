package Scanner;

import java.io.File;
import java.net.URL;

/**
 * Created by hirom on 21-02-2016.
 */
public class Downloader implements Runnable {
    private URL url;
    private File destination;
    private ScannerAbstract handler;

    public Downloader(URL url, File destination, ScannerAbstract handler) {
        this.url = url;
        this.destination = destination;
        this.handler = handler;
    }

    public void run() {
        System.out.println("Download " + url.toString());
        handler.fetchFile(url, destination);
    }
}
