package advisor.actions.spotify;

import advisor.models.CategoryReleases;
import advisor.models.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ResourcesHelper {

    final private HttpClient client;

    final private User user;

    public ResourcesHelper (User user) {
        this.client = HttpClient.newBuilder().build();
        this.user = user;
    }

    public JsonArray getFeatured() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create((user.getResourcesBaseURI() + "/v1/browse/featured-playlists")))
                .header("Authorization", "Bearer " + user.getAccessToken())
                .GET()
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.print("Error: Error on getFeatures(): " + e.getMessage());
        }

        if (response != null) {
            JsonObject jsonBody = JsonParser.parseString(response.body()).getAsJsonObject();
            System.out.println(jsonBody.toString());
            System.out.println();
            JsonObject playlists = jsonBody.get("playlists").getAsJsonObject();
            return playlists.get("items").getAsJsonArray();
        } else {
            System.out.println("Error: Empty response on getFeatured().");
            return null;
        }
    }

    public JsonArray getNew() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(user.getResourcesBaseURI() + "/v1/browse/new-releases"))
                .header("Authorization", "Bearer " + user.getAccessToken())
                .GET()
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("Error: Error on getNew(): " + e.getMessage());
        }

        if (response != null) {
            JsonObject jsonBody = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject albums = jsonBody.get("albums").getAsJsonObject();
            return albums.get("items").getAsJsonArray();
        } else {
            System.out.println("Error: Empty response on getNew().");
            return null;
        }
    }

    public JsonArray getCategories() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(user.getResourcesBaseURI() + "/v1/browse/categories"))
                .header("Authorization", "Bearer " + user.getAccessToken())
                .GET()
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("Error: Error on getCategories(): " + e.getMessage());
        }

        if (response != null) {
            JsonObject jsonBody = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject categories = jsonBody.get("categories").getAsJsonObject();
            return categories.get("items").getAsJsonArray();
        } else {
            System.out.println("Error: Empty response on getCategories().");
            return null;
        }
    }

    public JsonArray getCategoriesByName() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(user.getResourcesBaseURI() + "/v1/browse/categories"))
                .header("Authorization", "Bearer " + user.getAccessToken())
                .GET()
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("Error: Error on getCategories(): " + e.getMessage());
        }

        if (response != null) {
            JsonObject jsonBody = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject categories = jsonBody.get("categories").getAsJsonObject();
            return categories.get("items").getAsJsonArray();
        } else {
            System.out.println("Error: Empty response on getCategories().");
            return null;
        }
    }

    public JsonArray getPlaylistsByCategory(String categoryName) {
        List<CategoryReleases> categories = user.getCategories();
        if (categories == null) return null;

        CategoryReleases category = null;
        for (CategoryReleases item : categories) {
            if (item.getTitle().equalsIgnoreCase(categoryName)) {
                category = item;
            }
        }
        if (category == null) return null;

        String endpoint = "/v1/browse/categories/" + category.getId() + "/playlists";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(user.getResourcesBaseURI() + endpoint))
                .header("Authorization", "Bearer " + user.getAccessToken())
                .GET()
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("Error: Error on getPlaylistsByCategory(): " + e.getMessage());
        }

        if (response != null) {
            JsonObject jsonBody = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject playlists = jsonBody.get("playlists").getAsJsonObject();
            return playlists.get("items").getAsJsonArray();
        } else {
            System.out.println("Error: Empty response on getPlaylistsByCategory().");
            return null;
        }
    }
}
