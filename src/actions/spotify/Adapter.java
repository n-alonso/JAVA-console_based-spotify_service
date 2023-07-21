package advisor.actions.spotify;

import advisor.models.CategoryReleases;
import advisor.models.FeaturedReleases;
import advisor.models.NewReleases;
import advisor.models.PlaylistReleases;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Adapter {

    public static List<NewReleases> convertNewReleases(JsonArray albumElements) {
        List<NewReleases> temp = new ArrayList<>();

        System.out.println("albums: " + albumElements.toString());

        for (JsonElement element : albumElements) {
            JsonObject item = element.getAsJsonObject();
            String title = item.get("name").getAsString();
            JsonArray artistElements = item.get("artists").getAsJsonArray();
            String[] artists = convertArtists(artistElements);
            String URI = item.get("uri").getAsString();
            NewReleases release = new NewReleases(title, artists, URI);
            temp.add(release);
        }

        return temp;
    }

    public static List<FeaturedReleases> convertFeaturedReleases(JsonArray featuredElements) {
        List<FeaturedReleases> temp = new ArrayList<>();

        for (JsonElement element : featuredElements) {
            JsonObject featured = element.getAsJsonObject();
            String name = featured.get("name").getAsString();
            String uri = featured.get("uri").getAsString();
            FeaturedReleases release = new FeaturedReleases(name, uri);
            temp.add(release);
        }

        return temp;
    }

    public static List<CategoryReleases> convertCategories(JsonArray categoryElements) {
        List<CategoryReleases> temp = new ArrayList<>();

        for (JsonElement element : categoryElements) {
            JsonObject category = element.getAsJsonObject();
            String name = category.get("name").getAsString();
            String uri = category.get("href").getAsString();
            String id = category.get("id").getAsString();
            CategoryReleases release = new CategoryReleases(name, uri, id);
            temp.add(release);
        }

        return temp;
    }

    public static String[] convertArtists(JsonArray artistElements) {
        List<String> temp = new ArrayList<>();

        for (JsonElement element : artistElements) {
            JsonObject artist = element.getAsJsonObject();
            temp.add(artist.get("name").getAsString());
        }

        return temp.toArray(new String[0]);
    }

    public static List<PlaylistReleases> convertPlaylists(JsonArray playlistElements) {
        List<PlaylistReleases> temp = new ArrayList<>();

        for (JsonElement element : playlistElements) {
            if (element.isJsonObject()) {
                JsonObject playlist = element.getAsJsonObject();
                String name = playlist.get("name").getAsString();
                String uri = playlist.get("uri").getAsString();
                PlaylistReleases release = new PlaylistReleases(name, uri);
                temp.add(release);
            }
        }

        return temp;
    }
}
