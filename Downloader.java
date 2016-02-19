package Scanner;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.JSONReader;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Anders Hofmeister Brønden on 23/12/15.
 */
public class Downloader extends ScannerAbstract {
    private final String clientID = "Client-ID 1eb2bfc9d5463c4"; //new Base64().encode("Client-ID 1eb2bfc9d5463c4".getBytes());

    private Response query(URL url) {
        System.out.println("Quarrying");
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
            }
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
