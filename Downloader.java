package Scanner;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeMap;

/**
 * Created by Anders Hofmeister Brønden on 23/12/15.
 */
public class Downloader extends ScannerAbstract {
    private String clientID;

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
            Response queryData = query(this.getUrl());
            if(queryData != null) {
                ImgurImage[] data = queryData.getData();

                for(int i = 0; i < data.length; i++) {
                    System.out.println("Download " + data[i] + ", type: " + data[i].getType());
                    this.fetchFile(new URL(data[i].getLink()), this.getDestination(), i);
                    System.out.println("---");
                }

                return true;
            }
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
