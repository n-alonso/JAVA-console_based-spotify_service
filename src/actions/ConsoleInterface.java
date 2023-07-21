package advisor.actions;

import advisor.actions.spotify.Adapter;
import advisor.actions.spotify.AuthHelper;
import advisor.actions.spotify.ResourcesHelper;
import advisor.models.*;
import com.google.gson.JsonArray;

import java.util.Arrays;
import java.util.List;

public class ConsoleInterface {

    public static void printMenu() {
        String output = """
                Select one action:
                - auth (Required before any other action)
                - new
                - featured
                - categories
                - playlists <category_name> (Requires categories action to be taken first, <category_name> is case insensitive)
                - anything else to exit
                """;
        System.out.println(output);
    }

    public static void printFeatured(User user, AuthHelper helper, ResourcesHelper resources) throws Exception {
        helper.ensureAccessTokenIsValid(user, helper);

        if (user.getAccessToken() != null) {
            JsonArray featuredElements = resources.getFeatured();
            assert featuredElements != null;
            List<FeaturedReleases> releases = Adapter.convertFeaturedReleases(featuredElements);

            for (FeaturedReleases release : releases) {
                String output = release.getTitle() + "\n"
                        + release.getURI() + "\n";
                System.out.println(output);
            }
        } else {
            System.out.println("Error: Please, provide access for application.");
        }
    }

    public static void printNew(User user, AuthHelper helper, ResourcesHelper resources) throws Exception {
        helper.ensureAccessTokenIsValid(user, helper);

        if (user.getAccessToken() != null) {
            JsonArray albumElements = resources.getNew();
            assert albumElements != null;
            List<NewReleases> releases = Adapter.convertNewReleases(albumElements);

            for (NewReleases release : releases) {
                String output = release.getTitle() + "\n"
                        + Arrays.toString(release.getArtists()) + "\n"
                        + release.getURI() + "\n";
                System.out.println(output);
            }
        } else {
            System.out.println("Error: Please, provide access for application.");
        }
    }

    public static void printCategories(User user, AuthHelper helper, ResourcesHelper resources) throws Exception {
        helper.ensureAccessTokenIsValid(user, helper);

        if (user.getAccessToken() != null) {
            JsonArray categoryElements = resources.getCategories();
            assert categoryElements != null;
            List<CategoryReleases> categories = Adapter.convertCategories(categoryElements);
            user.setCategories(categories);

            for (CategoryReleases category : categories) {
                String output = category.getTitle() + "\n";
                System.out.println(output);
            }
        } else {
            System.out.println("Error: Please, provide access for application.");
        }
    }

    public static void printPlaylists(User user, AuthHelper helper, ResourcesHelper resources, String name) throws Exception {
        helper.ensureAccessTokenIsValid(user, helper);

        if (user.getAccessToken() != null) {
            JsonArray playListElements = resources.getPlaylistsByCategory(name);
            if (playListElements == null) {
                System.out.println("Error: Please, use the categories service first.\n");
                return;
            }

            List<PlaylistReleases> playlists = Adapter.convertPlaylists(playListElements);

            for (PlaylistReleases playlist : playlists) {
                String output = playlist.getTitle() + "\n"
                        + playlist.getURI() + "\n";
                System.out.println(output);
            }
        } else {
            System.out.println("Error: Please, provide access for application.");
        }
    }

    public static void printExit() {
        String output = """
                ---GOODBYE!---""";
        System.out.println(output);
    }

    public static void printAuth(User user, AuthHelper helper) {
        helper.getCode(user, helper);
        if (user.getCode() != null) helper.getAccessToken(user, helper);
    }
}
