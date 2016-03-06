package Scanner;

/**
 * Created by hirom on 05-03-2016.
 */
public interface Response {
    public Object getData();
    public boolean isSuccess();
    public int getStatus();
    public ImgurImage[] getImages();
}
