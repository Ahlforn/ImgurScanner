package Scanner;

import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by anders on 25/12/15.
 */
public class ImgurImage {
    private Map data;

    public ImgurImage(TreeMap<String, Object> data) {
        this.data = data;
    }

    public Object getProperty(String property) {
        for(Object p : data.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) p;

            if(entry.getKey().equals(property)) {
                return entry.getValue();
            }
        };

        return null;
    }
}
