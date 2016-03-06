package Scanner;

/**
 * Created by Anders Hofmeister Brønden on 05-03-2016.
 */
public class Gallery implements Response {
    ImgurGallery data;
    boolean success;
    int status;

    public ImgurGallery getData() {
        return data;
    }

    public void setData(ImgurGallery data) {
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

    public ImgurImage[] getImages() {
        return data.getImages();
    }
}
