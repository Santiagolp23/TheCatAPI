package model;

public class FavoriteCat {
    private String id, image_id;
    public Cat image;

    public Cat getCat() {
        return image;
    }

    public void setCat(Cat cat) {
        this.image = cat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }
}
