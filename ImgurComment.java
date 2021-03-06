package Scanner;

/**
 * Created by hirom on 05-03-2016.
 */
public class ImgurComment {
    private int id;
    private String image_id;
    private String comment;
    private String author;
    private int author_id;
    private boolean on_album;
    private String album_cover;
    private int ups;
    private int downs;
    private int datetime;
    private int parent_id;
    private boolean deleted;
    private Object vote;
    private String platform;
    private ImgurComment[] children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public boolean isOn_album() {
        return on_album;
    }

    public void setOn_album(boolean on_album) {
        this.on_album = on_album;
    }

    public String getAlbum_cover() {
        return album_cover;
    }

    public void setAlbum_cover(String album_cover) {
        this.album_cover = album_cover;
    }

    public int getUps() {
        return ups;
    }

    public void setUps(int ups) {
        this.ups = ups;
    }

    public int getDowns() {
        return downs;
    }

    public void setDowns(int downs) {
        this.downs = downs;
    }

    public int getDatetime() {
        return datetime;
    }

    public void setDatetime(int datetime) {
        this.datetime = datetime;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Object getVote() {
        return vote;
    }

    public void setVote(Object vote) {
        this.vote = vote;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public ImgurComment[] getChildren() {
        return children;
    }

    public void setChildren(ImgurComment[] children) {
        this.children = children;
    }
}
