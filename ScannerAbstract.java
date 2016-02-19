package Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sun.font.Script;
import sun.net.www.MimeEntry;
import sun.net.www.MimeTable;

import javax.activation.MimeType;
import javax.activation.MimetypesFileTypeMap;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Anders Hofmeister Brønden on 24/12/15.
 */
public abstract class ScannerAbstract implements Scanner {
    private URL url;
    private File destination;
    private ScriptEngine engine;
    Map<String, String> requestProperties;

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
            String extension = getFileExt(mimeType);

            destination = new File(destination.getPath() + "/" + nr + extension);

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

    public String getFileExt(String mimeType) {
        try {
            System.out.println("type: " + mimeType);
            String extension = "";

            MimeTable testTable = MimeTable.getDefaultTable();
            Enumeration e = testTable.elements();
            while (e.hasMoreElements()) {
                MimeEntry entry = (MimeEntry) e.nextElement();
                String contentType = entry.getType();
                String extensionString = entry.getExtensionsAsList();
                String[] extensionArray = extensionString.split(",");
                extensionString = extensionArray[extensionArray.length - 1];
                mimeType = mimeType.replaceAll("/", ".*");
                if (contentType.matches(mimeType)) {
                    extension = extensionString;
                    break;
                }
            }
            return extension;
        }
        catch(Exception e) {
            e.getMessage();

            return null;
        }
    }
}
