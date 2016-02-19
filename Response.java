package Scanner;

import java.util.Map;

/**
 * Created by anders on 19/02/16.
 */
public class Response {
    ImgurImage[] data;
    boolean success;
    int status;

    public ImgurImage[] getData() {
        return data;
    }

    public void setData(ImgurImage[] data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
