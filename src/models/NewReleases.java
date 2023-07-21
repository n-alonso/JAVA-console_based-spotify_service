package advisor.models;

public class NewReleases extends AbstractReleases {

    private String[] artists;

    public NewReleases(String title, String[] artists, String URI) {
        this.title = title;
        this.URI = URI;
        this.artists = artists;
    }

    public String[] getArtists() {
        return artists;
    }
}
