package Scanner;

import com.google.gson.Gson;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Created by Anders Hofmeister Brønden on 24/12/15.
 */
public abstract class ScannerAbstract implements Scanner {
    private File destination;
    private Map<String, String> requestProperties;
    private String apiGalleryBaseURL;
    private String[] apiUrlPrefixes;
    private String galleryID;

    public String getGalleryID() {
        return galleryID;
    }

    public void setGalleryID(String galleryID) {
        this.galleryID = galleryID;
    }

    public String getApiGalleryBaseURL() {
        return apiGalleryBaseURL;
    }

    public void setApiGalleryBaseURL(String apiGalleryBaseURL) {
        this.apiGalleryBaseURL = apiGalleryBaseURL;
    }

    public ScannerAbstract(String galleryID, File destination) {
        this.galleryID = galleryID;
        this.destination = destination;
        this.requestProperties = null;
        this.apiUrlPrefixes = new String[2];
        this.apiUrlPrefixes[GalleryTypes.GALLERY.ordinal()] = "gallery/";
        this.apiUrlPrefixes[GalleryTypes.SUBREDDIT.ordinal()] = "r/";
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
        try {
            return new URL(getRawUrl());
        }
        catch(MalformedURLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getRawUrl(GalleryTypes type) {
        String output = this.apiGalleryBaseURL + this.apiUrlPrefixes[GalleryTypes.GALLERY.ordinal()];
        if(type == GalleryTypes.SUBREDDIT) output += this.apiUrlPrefixes[type.ordinal()];

        output += this.galleryID;

        return output;
    }

    public String getRawUrl() {
        return getRawUrl(GalleryTypes.SUBREDDIT);
    }

    public InputStream getStream(URL url) {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000);

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
        this.requestProperties = requestProperties;

        return getStream(url);
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

    public Response getJSON(InputStream input, GalleryTypes type) {
        Gson gson = new Gson();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        if(type == GalleryTypes.SUBREDDIT) {
            return (Subreddit) gson.fromJson(reader, Subreddit.class);
        }
        else {
            return (Gallery) gson.fromJson(reader, Gallery.class);
        }
    }

    public Response getJSON(InputStream input) {
        return getJSON(input, GalleryTypes.SUBREDDIT);
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
        if(fetchFile(this.getUrl(), destination) != null) return true;

        return false;
    }
}
