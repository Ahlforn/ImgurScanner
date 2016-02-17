package Scanner;

import com.oracle.javafx.jmx.json.JSONReader;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    private Map query(URL url) {
        System.out.println("Quarrying");
        try {
            TreeMap headers = new TreeMap<String, String>();
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Authorization", clientID);

            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            InputStream response = getStream(url, headers);
            Map<String, Object> data = getJSON(response);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean begin() {
        try {
            Object queryData = query(new URL("https://api.imgur.com/3/topics/aww"));
            if(queryData instanceof Map) {
                TreeMap<String, Object> info = new TreeMap<String, Object>();
                //info.putAll(((Map<String, Object>) querryData).entrySet());
                TreeMap<String, Object> data = (TreeMap<String, Object>) info.get("data");

                for(Map.Entry<String, Object> entry : data.entrySet()) {
                    ImgurImage image = new ImgurImage((TreeMap<String, Object>) entry);
                    System.out.println("Download " + image.getProperty("link"));
                }
            }
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
