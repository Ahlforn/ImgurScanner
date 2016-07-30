package Scanner;

import javax.swing.*;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeMap;
import java.util.concurrent.*;

/**
 * Created by Anders Hofmeister Brønden on 23/12/15.
 */
public class DownloadHandler extends ScannerAbstract implements Runnable {
    private String clientID;
    private int pageLimit;
    private Callback c;
    private Thread t;
    private ExecutorService pool;
    private LinkedBlockingQueue<String> dls;
    private int downloadCount;
    private JProgressBar progress;

    public void setProgress(JProgressBar progress) {
        this.progress = progress;
    }

    public LinkedBlockingQueue<String> getDls() {
        return dls;
    }

    public int getPageLimit() {
        return pageLimit;
    }

    public void setPageLimit(int pageLimit) {
        this.pageLimit = pageLimit;
    }

    public void setClientID(String clientID) {
        this.clientID = "Client-ID " + clientID;
    }

    public DownloadHandler(Callback c) {
        this.c = c;
    }

    private Response query(URL url, GalleryTypes type) {
        System.out.println("Querying");

        TreeMap headers = new TreeMap<String, String>();
        headers.put("Authorization", this.clientID);

        InputStream response = getStream(url, headers);

        return getJSON(response, type);
    }

    public boolean begin() {
        try {
            pool = Executors.newFixedThreadPool(10);
            dls = new LinkedBlockingQueue<String>();
            String title = null;

            Response queryData;

            for(int n = 0; n < pageLimit; n++) {
                queryData = (Subreddit) query(new URL(this.getRawUrl() + "/time/" + n), GalleryTypes.SUBREDDIT);
                title = this.getGalleryID();
System.out.println(new URL(this.getRawUrl() + "/time/" + n));
                if(queryData.getImages().length == 0) {
                    System.out.println(this.getRawUrl(GalleryTypes.GALLERY));
                    queryData = (Gallery) query((new URL(this.getRawUrl(GalleryTypes.GALLERY))), GalleryTypes.GALLERY);
                    try {
                        title = ((ImgurGallery) queryData.getData()).getTitle();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    n = pageLimit;
                }

                File dir = new File(this.getDestination().getPath() + "/" + title);
                if(!dir.isDirectory()) {
                    dir.mkdir();
                }

                if (queryData != null) {
                    ImgurImage[] data = (ImgurImage[]) queryData.getImages();

                    for (int i = 0; i < data.length; i++) {
                        Downloader dl = new Downloader(new URL(data[i].getLink()), dir, this);
                        dls.add(data[i].getId());
                        pool.submit(dl);
                    }
                }
            }

            downloadCount = dls.size();
            pool.shutdown();
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

            System.out.println("Download complete.");

            return true;
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void updateProgress() {
        if(progress != null) {
            progress.setMaximum(downloadCount);
            progress.setValue(downloadCount - dls.size());
            progress.repaint();

            System.out.println(progress.getValue() + "/" + downloadCount);
        }
    }

    public void run() {
        this.begin();
        this.c.callback();
    }

    public void start() {
        if(t == null) {
            t = new Thread(this, "downloadHandler");
            t.start();
        }
    }
}
