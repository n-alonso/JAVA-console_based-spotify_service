package advisor.models;

public class CategoryReleases extends AbstractReleases {

    private String id;

    public CategoryReleases(String title, String URI, String id) {
        this.title = title;
        this.URI = URI;
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
