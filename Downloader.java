package Scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.TreeMap;

/**
 * Created by Anders Hofmeister Brønden on 23/12/15.
 */
public class Downloader extends ScannerAbstract {
    private String clientID;

    public void setClientID(String clientID) {
        this.clientID = "Client-ID " + clientID;
    }

    private Response query(URL url) {
        System.out.println("Querying");
        try {
            TreeMap headers = new TreeMap<String, String>();
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Authorization", clientID);

            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            InputStream response = getStream(url, headers);
            Response data = getJSON(response);

            return data;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean begin() {
        try {
            Response queryData = query(this.getUrl());
            if(queryData != null) {
                ImgurImage[] data = queryData.getData();

                for(int i = 0; i < data.length; i++) {
                    System.out.println("Download " + data[i]);
                    this.fetchFile(new URL(data[i].getLink()), this.getDestination(), i);
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
