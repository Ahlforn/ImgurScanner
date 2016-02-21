package Scanner;

import com.google.gson.Gson;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.mime.MimeTypeException;
import sun.net.www.MimeEntry;
import sun.net.www.MimeTable;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by Anders Hofmeister Brønden on 24/12/15.
 */
public abstract class ScannerAbstract implements Scanner {
    private URL url;
    private File destination;
    private Map<String, String> requestProperties;

    public ScannerAbstract(URL url, File destination) {
        this.url = url;
        this.destination = destination;
    }

    public ScannerAbstract() {
        this(null, null);
    }

    public File getDestination() {
        return destination;
    }

    public void setDestination(File destination) {
        this.destination = destination;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public InputStream getStream() {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            if(requestProperties != null) {
                for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
                    System.out.println(entry.getKey() + "/" + entry.getValue());
                    con.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            InputStream in = new BufferedInputStream(con.getInputStream());
            return in;

        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public InputStream getStream(URL url, Map<String, String> requestProperties) {
        this.url = url;
        this.requestProperties = requestProperties;

        return getStream();
    }

    public InputStream getStream(URL url) {
        this.url = url;
        this.requestProperties = null;

        return getStream();
    }

    public byte[] processStream(InputStream input) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = input.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            input.close();

            return out.toByteArray();
        }
        catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Response getJSON(InputStream input) {
        Gson gson = new Gson();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        Response data = gson.fromJson(reader, Response.class);

        return data;
    }

    public File fetchFile(URL url, File destination, int nr) {
        try {
            InputStream in = getStream(url, null);
            String mimeType = URLConnection.guessContentTypeFromStream(in);

            destination = new File(destination.getPath() + url.getFile());

            FileOutputStream fos = new FileOutputStream(destination.toString());
            fos.write(processStream(in));
            fos.close();

            return destination;
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public File fetchFile(URL url, File destination) {
        return fetchFile(url, destination, 0);
    }

    public boolean begin() {
        if(fetchFile(url, destination) != null) return true;

        return false;
    }
}
