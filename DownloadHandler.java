package Scanner;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Anders Hofmeister Brønden on 23/12/15.
 */
public class DownloadHandler extends ScannerAbstract {
    private String clientID;
    private int pageLimit;

    public int getPageLimit() {
        return pageLimit;
    }

    public void setPageLimit(int pageLimit) {
        this.pageLimit = pageLimit;
    }

    public void setClientID(String clientID) {
        // Authorization: Client-ID YOUR_CLIENT_ID
        this.clientID = "Client-ID " + clientID;
    }

    private Response query(URL url) {
        System.out.println("Querying");

        TreeMap headers = new TreeMap<String, String>();
        headers.put("Authorization", this.clientID);

        InputStream response = getStream(url, headers);
        Response data = getJSON(response);

        return data;
    }

    public boolean begin() {
        try {
            //ArrayList<Downloader> downloaders = new ArrayList<Downloader>();
            ExecutorService pool = Executors.newFixedThreadPool(10);
            for(int n = 0; n < pageLimit; n++) {
                Response queryData = query(new URL(this.getRawUrl() + "/" + n));
                if (queryData != null) {
                    ImgurImage[] data = queryData.getData();

                    for (int i = 0; i < data.length; i++) {
                        Downloader dl = new Downloader(new URL(data[i].getLink()), this.getDestination(), this);
                        pool.submit(dl);
                    }
                }
            }

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
}
